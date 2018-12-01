package com.shc.ecom.search.common.messages;

import com.shc.ecom.search.ssinproducer.Duration;

import java.io.Serializable;

/**
 * @author rgopala
 *         Jul 17, 2015 search-extract-common
 */
public class ContextMessage implements Serializable {

    private static final long serialVersionUID = 7268012249567457139L;

    private String messageId;

    private String pid;

    private String offerId;

    private String bucketId;

    private String storeName;

    private String runMode;

    private String timestamp;

    private String currentServer;

    private String key;

    private String jsonDocument;

    private Duration duration;

    private String indexCommercial;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    /**
     * @return the runMode
     */
    public String getRunMode() {
        return runMode;
    }

    /**
     * @param runMode the runMode to set
     */
    public void setRunMode(String runMode) {
        this.runMode = runMode;
    }

    public String getCurrentServer() {
        return currentServer;
    }

    public void setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJsonDocument() {
        return jsonDocument;
    }

    public void setJsonDocument(String jsonDocument) {
        this.jsonDocument = jsonDocument;
    }

    public Duration getDuration() {
        if (duration == null) {
            duration = new Duration();
        }
        return duration;
    }

    public String getIndexCommercial() {
        return indexCommercial;
    }

    public void setIndexCommercial(String indexCommercial) {
        this.indexCommercial = indexCommercial;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Context [").append("messageId=").append(messageId)
                .append(", pid=").append(pid).append(", offerId=")
                .append(offerId).append(", storeName=").append(storeName)
                .append(", runMode=").append(runMode).append(", timestamp=")
                .append(timestamp).append(", bucketId=").append(bucketId)
                .append(", currentServer=").append(currentServer).append(" ")
                .append(", indexCommercial=").append(indexCommercial)
                .append(getDuration().toString()).append("]");
        return builder.toString();
    }


}
