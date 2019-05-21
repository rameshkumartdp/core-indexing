package com.adk.aktway.search.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rames on 20-05-2019.
 */
public class AccessoryDetailEntries {
    private String type;
    private String description;
    private List<Measurements> measurements = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Measurements> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurements> measurements) {
        this.measurements = measurements;
    }
}
