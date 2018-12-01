package com.shc.ecom.search.extract.components.impressions;

import com.shc.search.MongoAbstractEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product_impression_runs")
public class ProductImpressionRuns extends MongoAbstractEntity {
    private String jobName;
    private long runNumber;

    public ProductImpressionRuns() {
    }

    public ProductImpressionRuns(String jobName, int runNumber) {
        this.jobName = jobName;
        this.runNumber = runNumber;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
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
