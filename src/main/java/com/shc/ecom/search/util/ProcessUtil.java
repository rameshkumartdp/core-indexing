package com.shc.ecom.search.util;

import com.shc.common.misc.util.MiscUtil;
import com.shc.ecom.search.common.kafka.KafkaProducerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessUtil {

    public static final String MESSAGE_FAILURE = "{\"message\": \"failure\"}";
    public static final String MESSAGE_SUCCESS = "{\"message\": \"success\"}";
    public static final String EOL = "EOL";

    @Autowired
    private KafkaProducerUtility kafkaProducerUtility;


    public void exitWithError() {
        kafkaProducerUtility.sendJsonToKafkaTimed(EOL, MESSAGE_FAILURE);
        MiscUtil.exitWithError();
    }

    public void exitWithSuccess() {
        kafkaProducerUtility.sendJsonToKafkaTimed(EOL, MESSAGE_SUCCESS);
        MiscUtil.exitWithSuccess();
    }
}
