package com.shc.ecom.search.common.localad;

import java.io.Serializable;
import java.util.List;

public class Search implements Serializable {
    private List<String> activityId;
    private List<String> pageId;

    public List<String> getActivityId() {
        return activityId;
    }

    public void setActivityId(List<String> activityId) {
        this.activityId = activityId;
    }

    public List<String> getPageId() {
        return pageId;
    }

    public void setPageId(List<String> pageId) {
        this.pageId = pageId;
    }
}
