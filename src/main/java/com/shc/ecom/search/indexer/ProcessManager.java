package com.shc.ecom.search.indexer;

import java.net.BindException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.common.monitoring.http.MonitoringServer;
import com.shc.ecom.search.util.ProcessUtil;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.shc.ecom.search.common.exception.SearchCommonException;
import com.shc.ecom.search.common.kafka.KafkaProducerInitializer;
import com.shc.ecom.search.common.kafka.KafkaProducerUtility;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.messages.MessageProcessor;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.docbuilder.Archive;
import com.shc.ecom.search.docindexer.DocumentIndexer;
import com.shc.ecom.search.jmx.Statistics;
import com.shc.ecom.search.ssinconsumer.SsinConsumer;
import com.shc.ecom.search.ssinproducer.SsinProducer;
import com.shc.ecom.search.util.JsonUtil;

@Component
public class ProcessManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessManager.class.getName());

    private static final Logger DEBUGLOGGER = LoggerFactory.getLogger("debuglog");

    public static final String INDEXING_METRICS_TOPIC_NAME = PropertiesLoader.getProperty(GlobalConstants.INDEXING_METRICS_TOPIC_NAME);

    /**
     * Since the bucket export and extraction are both "non-blocking" processes, they report back to manager through setting of atomic flag.
     * Conventionally we try to avoid this type of setting if processes can wait for the return value and proceed.
     */
    public static volatile AtomicBoolean bucketExportComplete = new AtomicBoolean();
    public static volatile AtomicBoolean extractionComplete = new AtomicBoolean();
    public static volatile AtomicBoolean kafkaPushIndexFailComplete = new AtomicBoolean();
    public static volatile AtomicBoolean closeKafkaThread = new AtomicBoolean();

    @Autowired
    protected MetricManager metricManager;

    @Autowired
    private SsinProducer ssinProducer;

    @Autowired
    private SsinConsumer ssinConsumer;

    @Autowired
    private DocumentIndexer documentIndexer;

    @Autowired
    private KafkaProducerUtility kafkaProducerUtility;

    @Autowired
    private MessageProcessor messageProcessor;

    @Autowired
    private ProcessUtil processUtil;

    private Integer translateQueueSize = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.TRANSLATE_QUEUE_SIZE));

    private Integer documentQueueSize = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.TRANSLATE_QUEUE_SIZE));

    private Integer monitoringServerPort = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.MONITORING_SERVER_PORT));
    private Boolean enableMonitoring = Boolean.parseBoolean(PropertiesLoader.getProperty(GlobalConstants.ENABLE_MONITORING));

    public ProcessManager() {
        LOGGER.info("Configuring process manager...");
    }

    public void handle(Arguments args) {
        String message = getMessage(args);
        BlockingQueue<String> translateQueue = new ArrayBlockingQueue<String>(translateQueueSize);
        BlockingQueue<Archive> docQueue = new ArrayBlockingQueue<Archive>(documentQueueSize);
        log("BEGIN", message);
        logApplicationProperties();

        try {
            if (enableMonitoring) {
                MonitoringServer.startServer(metricManager, monitoringServerPort);
            }
            ssinProducer.process(translateQueue, args);
            ssinConsumer.process(translateQueue, docQueue, args);
            documentIndexer.process(docQueue, args);

            // once indexing completes, push the metrics to kafka
            String kafkaMessage = getMetricsAndRunConfig(args).toString();
            kafkaProducerUtility.sendToKafka(kafkaMessage, INDEXING_METRICS_TOPIC_NAME);
//        } catch (BindException be) {
//            LOGGER.error("Unable to start monitoring server on this host. ", be);
        } catch (Exception e) {
            LOGGER.error("Application error! Exiting: " + message, e);
            processUtil.exitWithError();
        } finally {

            LOGGER.info(JsonUtil.convertToEntityObject(metricManager.collectMetrics().toString(), Statistics.class).toString());
            ProcessManager.closeKafkaThread.set(true);
            try {
                LOGGER.info("Closing kafka producer.");
                KafkaProducerInitializer.INSTANCE.getKafkaProducer().flush();
                KafkaProducerInitializer.INSTANCE.getKafkaProducer().close();
                LOGGER.info("Closed kafka producer.");
            } catch (KafkaException e) {
                LOGGER.error("Unable to close kafka producer ", e);
            }

            try {
//                if (enableMonitoring) {
//                    MonitoringServer.stopServer();
//                }
            } catch (Exception e) {
                LOGGER.error("Could not stop monitoring server: " + message, e);
            }
        }
        log("END", message);

    }

    private String getMessage(Arguments args) {
        return args.toString();
    }

    private void log(String code, String message) {
        LOGGER.info(code + " " + message);
        DEBUGLOGGER.info(code + " " + message);
    }

    private void logApplicationProperties() {
        LOGGER.info("Application properties begin.");
        LOGGER.info("BASE properties");
        printProperties(PropertiesLoader.getBaseProperties());
        LOGGER.info("OVERRIDE properties");
        printProperties(PropertiesLoader.getOverrideProperties());
        LOGGER.info("Application properties end.");
    }

    private void printProperties(Properties props) {
        Enumeration<?> e = props.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            LOGGER.info(" |" + key + "=" + props.getProperty(key));
        }
    }

    private JsonObject getMetricsAndRunConfig(Arguments args){

        ContextMessage contextMessage= new ContextMessage();
        try {
            contextMessage = messageProcessor.getContextMessage(args);
        } catch(SearchCommonException se){
            LOGGER.error("Unable to start get run Configuration information ", se);
        }
        JsonObject metrics = metricManager.collectMetrics();
        JsonObject runConfig = new JsonObject();

        runConfig.addProperty("storeName",contextMessage.getStoreName() );
        runConfig.addProperty("currentServer",contextMessage.getCurrentServer());
        runConfig.addProperty("timeStamp",contextMessage.getTimestamp());
        metrics.add("runConfig",runConfig);
        return metrics;
    }


}
