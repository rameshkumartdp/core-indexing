package com.shc.ecom.search.docindexer;

import com.codahale.metrics.Timer;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.rtc.util.JsonUtil;
import com.shc.ecom.search.cache.memcache.MemcacheManagerUtil;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.kafka.KafkaProducerInitializer;
import com.shc.ecom.search.common.kafka.KafkaProducerUtility;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.docbuilder.Archive;
import com.shc.ecom.search.docbuilder.SearchDoc;
import com.shc.ecom.search.indexer.ProcessManager;
import com.shc.ecom.search.indexer.SolrServerInitializer;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.persistence.SearchFileWriter;
import com.shc.ecom.search.transformations.GroupByTransformations;
import com.shc.ecom.search.util.SolrUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.shc.ecom.search.docindexer.DocumentIndexer.GROUPBY_PATH;
import static com.shc.ecom.search.docindexer.DocumentIndexer.INVERTED_INDEX_DIR;
import static com.shc.ecom.search.docindexer.DocumentIndexer.JSON_PATH;

/**
 * @author rgopala
 */

@Component
@Scope("prototype")
public class IndexerThread implements Callable<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexerThread.class.getName());

    private static final int MAX_RETRY_TIMES = 10;

    private static final int WAIT_TIME = 500;

    private static final boolean INDEX_TO_SOLR = StringUtils.equalsIgnoreCase(PropertiesLoader.getProperty(GlobalConstants.INDEXTOSOLR), "true");
    private static final boolean WRITE_JSON = StringUtils.equalsIgnoreCase(PropertiesLoader.getProperty(GlobalConstants.ENABLE_JSON_FILES), "true");
    private static final boolean ENABLE_GROUPBY = StringUtils.equalsIgnoreCase(PropertiesLoader.getProperty(GlobalConstants.ENABLE_GROUPBY_FILES), "true");
    @Autowired
    protected MetricManager metricManager;
    private String threadId;
    private BlockingQueue<Archive> docQueue;
    private String jsonPath;
    private String jsonFilePrefix;

    private String groupByPath;
    private String groupByFilePrefix;
    @Autowired
    private SearchFileWriter searchFileWriter;

    @Autowired
    private GroupByTransformations groupByTransformations;

    @Autowired
    private KafkaProducerUtility kafkaProducerUtility;

    @Autowired
    private MemcacheManagerUtil memcacheManagerUtil;

    @Autowired
    private ContextMessage context;


    @PostConstruct
    public void init() {
        //Intializes values of this bean
    }

    public void configure(String threadId, BlockingQueue<Archive> docQueue, ContextMessage context) {
        this.threadId = threadId;
        this.docQueue = docQueue;
        this.context = context;
        StringBuilder jsonPathBuilder = new StringBuilder().append(JSON_PATH)
                .append(context.getStoreName().toLowerCase())
                .append("/")
                .append(context.getRunMode().toLowerCase())
                .append("/")
                .append(context.getCurrentServer())
                .append("/")
                .append(this.threadId)
                .append("/");
        jsonPath = jsonPathBuilder.toString();
        jsonFilePrefix = PropertiesLoader.getProperty(GlobalConstants.JSONFILEPREFIX);
        StringBuilder groupByPathBuilder = new StringBuilder().append(GROUPBY_PATH)
                .append(context.getStoreName().toLowerCase())
                .append("/")
                .append(context.getRunMode().toLowerCase())
                .append("/")
                .append(context.getCurrentServer())
                .append("/")
                .append(this.threadId)
                .append("/");
        groupByPath=groupByPathBuilder.toString();
        groupByFilePrefix=PropertiesLoader.getProperty(GlobalConstants.GROUPBYFILEPREFIX);

    }


    @Override
    public String call() throws Exception {

        int fileSentinel = 0;
        BufferedWriter invertedIndexWriter = null;
        if (WRITE_JSON) {
            Path invertedIndexFile = Paths.get(JSON_PATH + INVERTED_INDEX_DIR + Thread.currentThread().getName());
            Files.createFile(invertedIndexFile);
            invertedIndexWriter = Files.newBufferedWriter(invertedIndexFile, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        }
        while (!docQueue.isEmpty() || ProcessManager.extractionComplete.get() == false) {
            Archive archive = docQueue.poll(5, TimeUnit.SECONDS);
            if (archive == null) {
                continue;
            }
            List<SearchDoc> searchDocs = archive.getSearchDocuments();
            if (CollectionUtils.isEmpty(searchDocs)) {
                continue;
            }

            fileSentinel++;
            persist(searchDocs, fileSentinel, invertedIndexWriter);
        }

        if (invertedIndexWriter != null) {
            invertedIndexWriter.close();
        }
        LOGGER.info("Doc Queue Size: " + docQueue.size() + " Extraction complete? " + ProcessManager.extractionComplete.get());
        return threadId;
    }

    private void persist(List<SearchDoc> searchDocList, int fileSentinel, BufferedWriter invertedIndexWriter) throws IOException {
        List<String> jsonDocList = new ArrayList<>();
        Map<String, String> jsonFileMap = new HashMap<>();
        if (WRITE_JSON) {
            for (SearchDoc sd : searchDocList) {
                try {
                    String jsonDoc = JsonUtil.fromDomainObjectGen(sd);
                    jsonDocList.add(jsonDoc);
                    jsonFileMap.put(sd.getId(), jsonDoc);
                    invertedIndexWriter.write(sd.getId() + ":" + InetAddress.getLocalHost().getHostName() + ":" + jsonPath + jsonFilePrefix +
                            Thread.currentThread().getName() + "-" + fileSentinel + SearchFileWriter.FILE_TYPE);
                    invertedIndexWriter.newLine();
                } catch (IOException e) {
                    LOGGER.error("Unable to convert Search Document to JSON Document " + sd.getPartnumber());
                    metricManager.incrementCounter(Metrics.JSONFILEWRITE_EXCEPTIONS_COUNTER);
                }
            }
        }
        boolean isCommercialRun = StringUtils.equalsIgnoreCase(context.getIndexCommercial(), "true");
        boolean isCommercialStore = StringUtils.equalsIgnoreCase(context.getStoreName(), Stores.COMMERCIAL.getStoreName());
        int docSize = searchDocList.size();
        writeJson(fileSentinel, jsonDocList);
        if(ENABLE_GROUPBY){
            if(isCommercialStore) {
                if(!isCommercialRun) {
                    writeGroupBy(fileSentinel,jsonDocList);
                }
            } else {
                writeGroupBy(fileSentinel,jsonDocList);
            }
        }
        sendToKafka(jsonFileMap);
        sendToMemcache(jsonFileMap);
        if (INDEX_TO_SOLR) {
            if(isCommercialStore) {
                if(isCommercialRun) {
                    sendToSolr(searchDocList);
                }
            } else {
                sendToSolr(searchDocList);
            }
        }

        metricManager.incrementCounter(Metrics.INDEXED_ID_COUNTER, docSize);
        jsonDocList.clear();
        searchDocList.clear();
    }

    private void writeJson(int fileSentinel, List<String> jsonDocList) {
        if (WRITE_JSON) {
            searchFileWriter.writer(jsonDocList, jsonPath, jsonFilePrefix + Thread.currentThread().getName(), String.valueOf(fileSentinel),false);
        }
    }

    private void sendToKafka(Map<String, String> jsonFileMap) {
        LOGGER.info("Sending " + jsonFileMap.size() + " documents to Kafka");
        jsonFileMap.forEach(kafkaProducerUtility::sendJsonToKafkaTimed);
    }

    private void sendToMemcache(Map<String, String> jsonFileMap) {
        LOGGER.info("Sending " + jsonFileMap.size() + " documents to Memcache");
        jsonFileMap.forEach(memcacheManagerUtil::sendJsonToMemcacheTimed);
    }

    private void writeGroupBy(int fileSentinel, List<String> jsonDocList) throws IOException{
        if (ENABLE_GROUPBY) {
            LOGGER.info("Writing " + jsonDocList.size() + " documents to groupby dump");
            boolean writeAsSepLines = true;
            List<String> gbJsonDocs = groupByTransformations.groupByTransform(jsonDocList);
            searchFileWriter.writer(gbJsonDocs, groupByPath, groupByFilePrefix + Thread.currentThread().getName(), String.valueOf(fileSentinel),writeAsSepLines);
        }

    }


    private void sendToSolr(List<SearchDoc> searchDocList) {
        LOGGER.info("Sending " + searchDocList.size() + " documents to Solr Cloud");
        for (int retryCount = 0; retryCount != MAX_RETRY_TIMES; ++retryCount) {
            Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_INDEX_TO_SOLR);
            try {

                List<SolrInputDocument> solrInputDocuments = SolrUtil.convertToSolrInputDocuments(searchDocList);
                UpdateResponse updateResponse = SolrServerInitializer.INSTANCE.getSolrClient().add(solrInputDocuments);
                if (updateResponse.getStatus() == 0) {
                    LOGGER.info("Elapsed time in posting {} documents to Solr: {}", searchDocList.size(), updateResponse.getElapsedTime());
                    break;
                } else {
                    LOGGER.info("Failed to post documents to Solr. Response status: {}", searchDocList.size(), updateResponse.getStatus());
                    waitForRetry(retryCount);
                }
            } catch (Exception e) {
                LOGGER.error("Exception in " + Thread.currentThread().getName() + ". Unable to post to Solr.", e);
                metricManager.incrementCounter(Metrics.INDEXING_EXCEPTIONS_COUNTER);
                waitForRetry(retryCount);
            } finally {
                metricManager.stopTiming(timerContext);
            }
        }
        searchDocList.clear();
    }

    private void waitForRetry(int retryCount) {
        if (retryCount < MAX_RETRY_TIMES) {
            try {
                LOGGER.info("Going to retry after " + WAIT_TIME * (retryCount + 1) + " milli seconds");
                Thread.sleep(WAIT_TIME * (retryCount + 1));
            } catch (InterruptedException e) {
                LOGGER.info("Exception occurred in waiting to retry to push to solr");
            }
        } else {
            LOGGER.error("GIVING UP . Cannot add this list of documents after trying " + MAX_RETRY_TIMES + " times");
        }
    }

}
