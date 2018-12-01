package com.shc.ecom.search.common.tm;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hseth on 6/16/2016.
 */
public class TopicModelingDTO implements Serializable {
    private static final long serialVersionUID = -2794732336111961397L;
    private Map<String, Float> topicDistribution;

    public Map<String, Float> getTopicDistribution() {
        if (topicDistribution == null) {
            return new LinkedHashMap<String, Float>();
        }
        return topicDistribution;
    }

    public void setTopicDistribution(Map<String, Float> topicDistribution) {
        this.topicDistribution = topicDistribution;
    }


}
