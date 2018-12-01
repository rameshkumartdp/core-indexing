package com.shc.ecom.search.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shc.common.index.rules.Decision;
import com.shc.common.index.rules.ValidationResults;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.kafka.SearchKafkaProducer;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.jmx.Metrics;

/**
 * Created by vsonthy on 3/29/17.
 */
public class DecisionUtil implements Serializable {

    // Logging for debugging. Will be removed
    private static final Logger LOGGER = LoggerFactory.getLogger(DecisionUtil.class.getName());

    private static final long serialVersionUID = 1961143442691050952L;

    private static final Logger DEBUGLOGGER = LoggerFactory.getLogger("debuglog");

    public static final String INDEX_FAIL_TOPIC_NAME = PropertiesLoader.getProperty(GlobalConstants.INDEX_FAIL_TOPIC_NAME);

    public static Decision generateDecision(WorkingDocument wd, ContextMessage context, String offerId, ValidationResults results){
        Decision decision = new Decision();
        ValidationResults validationResults =  new ValidationResults();
        decision.setId(wd.getExtracts().getSsin());
        decision.setIdType("SSIN");
        decision.setOfferId(offerId);
        decision.setStore(context.getStoreName());
        decision.setSites(getSitesList(Stores.getStore(context.getStoreName()).getEligibleSites(wd, context)));
        decision.setOpsUuid(context.getTimestamp());
        decision.setRejected(true);
        decision.setValidationResults(validationResults);
        decision.addValidationResults(wd.getDecision().getValidationResults());
        decision.addValidationResults(results);
        return decision;
    }

    public static String getMessage(Decision decision, MetricManager metricManager){
        String message = "";
        try {
            message = com.shc.ecom.rtc.util.JsonUtil.fromDomainObjectGen(decision);
        } catch (IOException e) {
            DEBUGLOGGER.info("Push to kafka failed as decision object couldn't be converted to a json");
            DEBUGLOGGER.info(e.getMessage());
            metricManager.incrementCounter(Metrics.KAFKA_PUSH_INDEX_FAIL_EXCEPTIONS_COUNTER);
        }
        return message;
    }

    public static void incrementCounterAddToKafka(MetricManager metricManager, SearchKafkaProducer kafkaProducer, Decision decision, String rejectedRule){
        metricManager.incrementCounter( "Counter-" + rejectedRule);
        // Logging for debugging. Will be removed
        if(StringUtils.isEmpty(rejectedRule)){
            LOGGER.info("Rejected rule empty while updating counter SSIN:" + decision.getId() + " Store:" + decision.getStore());
        }
        String offerMessage = DecisionUtil.getMessage(decision, metricManager);
        kafkaProducer.addToKafka(offerMessage, INDEX_FAIL_TOPIC_NAME);
    }

    public static ValidationResults constructValidationResults(boolean valid, String ruleName){
        ValidationResults validationResults = new ValidationResults();
        validationResults.getDataMap().put(ruleName,valid);
        validationResults.setRejectedRule(valid ? "":ruleName);
        validationResults.setPassed(valid);
        return validationResults;
    }
    
    public static List<String> getSitesList(List<Sites> sites) {
    	List<String> siteNames = new ArrayList<>();
    	for(Sites site: sites){
    		siteNames.add(site.getSiteName());
    	}
    	return siteNames;
    }
}
