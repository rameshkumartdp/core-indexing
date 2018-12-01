package com.shc.ecom.search.extract.components.tm;

import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.querymorph.DocumentExtender;
import com.shc.topicmodel.SearchTopicInferencer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class TopicModelingComponent implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1641068004855899399L;
	private static final Logger LOGGER = Logger.getLogger(TopicModelingComponent.class.getName());
    private static float thresholdProbability;
    private static int replicationFactor;
    private static int numberOfTopics;
    private static SearchTopicInferencer searchTopicInferencer;

    public TopicModelingComponent() {
        setThresholdProbability(Float.parseFloat(PropertiesLoader.getProperty(GlobalConstants.THRESHOLD_POBABILITY)));
        setReplicationFactor(Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.REPLICATION_FACTOR_FOR_TM)));
        setNumberOfTopics(Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.NUMBER_OF_TOPICS)));
        setSearchTopicInferencer();
        LOGGER.info("Finished loading the topic modelling files");
    }

    public static SearchTopicInferencer getSearchTopicInferencer() {
        return searchTopicInferencer;
    }

    public static void setSearchTopicInferencer() {
        String filePathForInferencer = PropertiesLoader.getProperty(GlobalConstants.FILE_PATH_FOR_INFERENCER);
        String filePathForInstances = PropertiesLoader.getProperty(GlobalConstants.FILE_PATH_FOR_INSTANCES);

        DocumentExtender documentExtenderd = new DocumentExtender();
        LOGGER.info("Begining loading inferencer and instances files ...");
        try {
            TopicModelingComponent.searchTopicInferencer = new SearchTopicInferencer(filePathForInferencer, filePathForInstances, documentExtenderd);
        } catch (Exception e) {
            LOGGER.error("Error initializing Topic Modeling inferencer and instances files", e);
        }
    }

    public String getInference(String text) {
        if (StringUtils.isNotBlank(text)) {
            String extQuery = searchTopicInferencer.getTextExtender().extendText(text).trim();
            return searchTopicInferencer.getSearchTermTopicDistribution(extQuery, numberOfTopics);
        }
        return StringUtils.EMPTY;
    }

    public float getThresholdProbability() {
        return thresholdProbability;
    }

    public static void setThresholdProbability(float thresholdProbability) {
        TopicModelingComponent.thresholdProbability = thresholdProbability;
    }

    public int getReplicationFactor() {
        return replicationFactor;
    }

    public static void setReplicationFactor(int replicationFactor) {
        TopicModelingComponent.replicationFactor = replicationFactor;
    }

    public int getNumberOfTopics() {
        return numberOfTopics;
    }

    public static void setNumberOfTopics(int numberOfTopics) {
        TopicModelingComponent.numberOfTopics = numberOfTopics;
    }

}
