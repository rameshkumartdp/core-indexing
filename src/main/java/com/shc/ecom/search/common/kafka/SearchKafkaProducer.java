package com.shc.ecom.search.common.kafka;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;
import com.google.gson.JsonObject;
import com.shc.common.index.rules.Decision;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.indexer.ProcessManager;
import com.shc.ecom.search.jmx.Metrics;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author rgopala
 */

@Component
public class SearchKafkaProducer implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2079949760803189351L;

	private static final int KAFKA_THREAD_POOL_SIZE = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.KAFKA_THREAD_POOL_COUNT));

    private static final String KAFKA_ZOOKEEPER = PropertiesLoader.getProperty(GlobalConstants.ZOOKEEPER_KAFKA_LIST);

    public static final String INDEX_FAIL_TOPIC_NAME = PropertiesLoader.getProperty(GlobalConstants.INDEX_FAIL_TOPIC_NAME);

    public static final boolean INDEX_FAIL_FLAG = Boolean.parseBoolean(PropertiesLoader.getProperty(GlobalConstants.ENABLE_KAFKA_INDEX_FAIL));

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchKafkaProducer.class.getName());

    private static ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.KAFKA_THREAD_POOL_COUNT))));

    private int messageQueueSize = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.KAFKA_MESSAGE_QUEUE_SIZE));

    private BlockingQueue<KafkaMessageRecord> messageQueue = new ArrayBlockingQueue<>(messageQueueSize);

    private AtomicInteger completedThreadsCount = new AtomicInteger(0);

    private AtomicBoolean brokersUp = new AtomicBoolean();

    @Autowired
    private MetricManager metricManager;

    @Inject
    private ApplicationContext ctx;

    @PostConstruct
    private void init() {

        areKafkaBrokersUp();

        List<KafkaProducerThread> kafkaProducerThreadList = Lists.newArrayList();
        for (int i = 0; i < KAFKA_THREAD_POOL_SIZE; i++) {
            KafkaProducerThread kafkaProducerThread = (KafkaProducerThread) ctx.getBean("kafkaProducerThread");
            kafkaProducerThread.configure("KafkaProducer-" + i, messageQueue);
            kafkaProducerThreadList.add(kafkaProducerThread);
        }

        for (KafkaProducerThread kafkaProducer : kafkaProducerThreadList) {
            ListenableFuture<String> future = listeningExecutorService.submit(kafkaProducer);
            Futures.addCallback(future, callback(kafkaProducerThreadList, listeningExecutorService));
        }

    }

    private FutureCallback<String> callback(final List<KafkaProducerThread> kafkaProducerThreadList,
                                            final ListeningExecutorService listeningExecutorService) {

        return new FutureCallback<String>() {
            @Override
            public void onSuccess(String callable) {
                completedThreadsCount.getAndIncrement();
                LOGGER.info(callable + " complete. Spawn threads: " + kafkaProducerThreadList.size()
                        + ", completed threads count: " + completedThreadsCount);
                if (completedThreadsCount.get() == kafkaProducerThreadList.size() && ProcessManager.closeKafkaThread.get()) {
                    shutdown(listeningExecutorService);
                    ProcessManager.kafkaPushIndexFailComplete.set(true);
                }

            }

            private void shutdown(final ListeningExecutorService listeningExecutorService) {
                listeningExecutorService.shutdown();
                try {
                    LOGGER.info("Waiting " + 1 + " " + TimeUnit.MINUTES.name() + " for kafka push to complete.");
                    listeningExecutorService.awaitTermination(1, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    LOGGER.error("Interrupted while terminating kafka producer executor service. ", e);
                }
                listeningExecutorService.shutdownNow();
            }

            @Override
            public void onFailure(Throwable tw) {
                LOGGER.error("Kafka thread failure ", tw);
            }

        };

    }

    public void addToKafka(KafkaMessageRecord kafkaMessageRecord){
        try{
            messageQueue.add(kafkaMessageRecord);
        } catch (IllegalStateException ex){
            metricManager.incrementCounter(Metrics.KAFKA_PUSH_INDEX_FAIL_EXCEPTIONS_COUNTER);
            LOGGER.error("Queue is full. Message not pushed to Kafka. Message Prefix: " + ((kafkaMessageRecord.getMessage().length() > 40)?
                    kafkaMessageRecord.getMessage().substring(0,40):kafkaMessageRecord.getMessage()) + ex);
        }
    }

    public void addToKafka(Decision decision, String indexFailTopicName) {
        if (isPushToKafkaValid()) {
            KafkaMessageRecord kafkaMessageRecord = new KafkaMessageRecord(decision, INDEX_FAIL_TOPIC_NAME);
            addToKafka(kafkaMessageRecord);
        }
    }

    public void addToKafka(JsonObject metricsJsonObject, String topicName) {
        if (isPushToKafkaValid()) {
            KafkaMessageRecord kafkaMessageRecord = new KafkaMessageRecord(metricsJsonObject, topicName);
            addToKafka(kafkaMessageRecord);
        }
    }

    private boolean isPushToKafkaValid() {
        if (INDEX_FAIL_FLAG && brokersUp.get()) {
            return true;
        }
        return false;
    }

    private void areKafkaBrokersUp() {

        List<String> ids = null;

        try {
            ZooKeeper zk = new ZooKeeper(KAFKA_ZOOKEEPER, 100000, null);
            ids = zk.getChildren("/brokers/ids", false);
            zk.close();
        } catch (IOException | KeeperException | InterruptedException e) {
            brokersUp.set(Boolean.FALSE);
            LOGGER.error("IO Exception occurred while connecting to kafka zookeeper", e);
        }

        if (CollectionUtils.isEmpty(ids)) {
            LOGGER.info("All brokers are down");
            brokersUp.set(Boolean.FALSE);
        } else {
            brokersUp.set(Boolean.TRUE);
        }

    }

    public void addToKafka(String message, String topic) {
        if (isPushToKafkaValid() && StringUtils.isNotEmpty(message)) {
            KafkaMessageRecord kafkaMessageRecord = new KafkaMessageRecord(message, topic);
            addToKafka(kafkaMessageRecord);
        }
    }
}
