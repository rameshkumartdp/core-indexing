package com.shc.ecom.search.common.kafka;

import java.io.Serializable;

import com.shc.common.index.rules.Decision;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;

/**
 * Created by tgulati on 10/28/16.
 */
public class KafkaMessageRecord implements Serializable {

    private static final long serialVersionUID = -8434032089727659132L;

    private Decision decision;

    private String message = StringUtils.EMPTY;

    private String topic = StringUtils.EMPTY;

    private JsonObject metrics;

    public KafkaMessageRecord(Decision decision, String topic) {
        this.decision = decision;
        this.topic = topic;
    }

    public KafkaMessageRecord(JsonObject metricsJsonObject, String topicName) {
        this.metrics = metricsJsonObject;
        this.topic = topicName;
    }

    public KafkaMessageRecord(String message, String topic) {
        this.message = message;
        this.topic = topic;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public JsonObject getMetrics() {
        return metrics;
    }

    public void setMetrics(JsonObject metrics) {
        this.metrics = metrics;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
