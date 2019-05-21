package com.adk.aktway.search.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rames on 21-05-2019.
 */
public class AccessoryDetail {
    private List<AccessoryDetailEntries>accessoryDetailEntries = new ArrayList<>();

    public List<AccessoryDetailEntries> getAccessoryDetailEntries() {
        return accessoryDetailEntries;
    }

    public void setAccessoryDetailEntries(List<AccessoryDetailEntries> accessoryDetailEntries) {
        this.accessoryDetailEntries = accessoryDetailEntries;
    }
}
