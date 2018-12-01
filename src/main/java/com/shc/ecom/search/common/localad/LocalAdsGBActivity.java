package com.shc.ecom.search.common.localad;

import java.io.Serializable;
import java.util.List;

public class LocalAdsGBActivity implements Serializable {
    String activityId;
    List<LocalAdsGBPage> pages;
    String endDate;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public List<LocalAdsGBPage> getPages() {
        return pages;
    }

    public void setPages(List<LocalAdsGBPage> pages) {
        this.pages = pages;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
