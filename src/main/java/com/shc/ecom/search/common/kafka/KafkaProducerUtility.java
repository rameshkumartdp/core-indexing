package com.shc.ecom.search.common.kafka;

import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.jmx.Metrics;


@Component
public class KafkaProducerUtility {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerThread.class.getName());
    private static final boolean POST_TO_KAFKA = StringUtils.equalsIgnoreCase(PropertiesLoader.getProperty(GlobalConstants.ENABLE_POST_JSON_FILES_TO_KAFKA), "true");
    private static final int MAX_RETRY_TIMES = 10;

    @Autowired
    private MetricManager metricManager;

    public static final String INDEX_FAIL_TOPIC_NAME = PropertiesLoader.getProperty(GlobalConstants.INDEX_FAIL_TOPIC_NAME);

    public static final String INDEXING_METRICS_TOPIC_NAME = PropertiesLoader.getProperty(GlobalConstants.INDEXING_METRICS_TOPIC_NAME);
    public static final String JSON_FILES_TOPIC_NAME = PropertiesLoader.getProperty(GlobalConstants.JSON_FILES_TOPIC_NAME);

    public void sendToKafka(String message, String topic) {
        Producer<String, String> producer = KafkaProducerInitializer.INSTANCE.getKafkaProducer();
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, message);
        producer.send(producerRecord, (metadata, e) -> {
            if (topic.equals(INDEX_FAIL_TOPIC_NAME)) {
                metricManager.incrementCounter((e != null) ?
                        Metrics.KAFKA_PUSH_INDEX_FAIL_EXCEPTIONS_COUNTER : Metrics.KAFKA_PUSH_INDEX_FAIL_COUNTER);
            } else if (topic.equals(INDEXING_METRICS_TOPIC_NAME)) {
                metricManager.incrementCounter((e != null) ?
                        Metrics.KAFKA_PUSH_INDEXING_METRICS_EXCEPTIONS_COUNTER : Metrics.KAFKA_PUSH_INDEXING_METRICS_COUNTER);
            } else {
                LOGGER.error("Incorrect topic passed: ", topic);
            }
        });
    }


    public void sendJsonToKafkaTimed(String key, String value) {
        if(POST_TO_KAFKA) {
            metricManager.timed(Metrics.TIMER_POST_JSON_TO_KAFKA, () -> sendJsonToKafkaRetry(0, key, value));
        }
    }

    public void sendJsonToKafkaRetry(int retry , String key, String value) {
        KafkaProducerInitializer.INSTANCE.getKafkaProducer().send(new ProducerRecord<>(JSON_FILES_TOPIC_NAME, key, value), getCallback(++retry, key, value));
    }

    private Callback getCallback(int retry, String key, String value) {
        return (metadata, e) -> {
            metricManager.incrementCounter((e != null) ? Metrics.KAFKA_PUSH_JSON_FAIL_EXCEPTIONS_COUNTER : Metrics.KAFKA_PUSH_JSON_FAIL_COUNTER);
            if (retry < MAX_RETRY_TIMES && e!= null) {
                sendJsonToKafkaRetry(retry, key, value);
            }
        };
    }

}
