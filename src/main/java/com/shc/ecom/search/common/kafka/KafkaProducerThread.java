package com.shc.ecom.search.common.kafka;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import com.shc.ecom.search.indexer.ProcessManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by tgulati on 10/12/16.
 */
@Component
@Scope("prototype")
@Qualifier("kafkaProducerThread")
public class KafkaProducerThread implements Callable<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerThread.class.getName());

    private static final int POLL_TIMEOUT_SECONDS = 5;

    private String threadId = StringUtils.EMPTY;

    private BlockingQueue<KafkaMessageRecord> messageQueue;

    @Autowired
    private KafkaProducerUtility kafkaProducerUtility;

    @Override
    public String call() throws Exception {

        LOGGER.info("Started Kafka Producer Thread: " + threadId);

        while (!ProcessManager.closeKafkaThread.get()) {
            KafkaMessageRecord kafkaMessageRecord = messageQueue.poll(POLL_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (kafkaMessageRecord != null) {
                String message = kafkaMessageRecord.getMessage();
                if (!StringUtils.isEmpty(message)) {
                    kafkaProducerUtility.sendToKafka(message, kafkaMessageRecord.getTopic());
                }
            }
        }

        LOGGER.info("Message queue Size: " + messageQueue.size() + " push to broker finish. ");
        return threadId;
    }

    public void configure(String threadId, BlockingQueue<KafkaMessageRecord> messageQueue) {
        this.threadId = threadId;
        this.messageQueue = messageQueue;
    }
}
