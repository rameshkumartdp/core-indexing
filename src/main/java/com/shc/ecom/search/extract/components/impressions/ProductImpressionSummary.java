package com.shc.ecom.search.extract.components.impressions;

import com.shc.search.MongoAbstractEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product_impression_summary")
public class ProductImpressionSummary extends MongoAbstractEntity {

    private String pid;
    private long runNumber;
    private int impressionCount;

    public ProductImpressionSummary() {
    }

    public ProductImpressionSummary(long runNumber, String pid, int impressionCount) {
        this.runNumber = runNumber;
        this.pid = pid;
        this.impressionCount = impressionCount;
    }

    public ProductImpressionSummary(String pid) {
        this.pid = pid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getImpressionCount() {
        return impressionCount;
    }

    public void setImpressionCount(int impressionCount) {
        this.impressionCount = impressionCount;
    }

    public long getRunNumber() {
        return runNumber;
    }

    public void setRunNumber(long runNumber) {
        this.runNumber = runNumber;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
