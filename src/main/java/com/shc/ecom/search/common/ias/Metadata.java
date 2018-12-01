package com.shc.ecom.search.common.ias;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rgopala
 */
public class Metadata {
    private String timestamp;

    private List<String> profile = new ArrayList<>();

    private String found;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getProfile() {
        return profile;
    }

    public void setProfile(List<String> profile) {
        this.profile = profile;
    }

    public String getFound() {
        return found;
    }

    public void setFound(String found) {
        this.found = found;
    }

    @Override
    public String toString() {
        return "ClassPojo [timestamp = " + timestamp + ", profile = " + profile + ", found = " + found + "]";
    }
}