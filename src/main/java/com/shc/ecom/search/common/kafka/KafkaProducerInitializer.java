package com.shc.ecom.search.common.kafka;

import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.search.config.GlobalConstants;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;


public enum KafkaProducerInitializer {

    INSTANCE;

    private Producer<String, String> producer;

    KafkaProducerInitializer() {

        String bootstrapServerList = PropertiesLoader.getProperty(GlobalConstants.BOOTSTRAP_SERVER_LIST);

        String acks = PropertiesLoader.getProperty(GlobalConstants.ACKS);

        int retries = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.RETRIES));

        String keySerializer = PropertiesLoader.getProperty(GlobalConstants.KEY_SERIALIZER);

        int maxBlockMs = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.MAX_BLOCK_MS));

        int requestTimeoutMs = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.REQUEST_TIMEOUT_MS));

        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServerList);
        props.put("acks", acks);
        props.put("retries", retries);
        props.put("key.serializer", keySerializer);
        props.put("value.serializer", keySerializer);
        props.put("request.timeout.ms", requestTimeoutMs);
        props.put("max.block.ms", maxBlockMs);
        producer = new KafkaProducer<>(props);
    }

    public Producer<String, String> getKafkaProducer() {
        return producer;
    }

}
