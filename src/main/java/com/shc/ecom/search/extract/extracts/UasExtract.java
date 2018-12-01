package com.shc.ecom.search.extract.extracts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UasExtract implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1380328235193852316L;

    private Map<String, Map<String, Boolean>> itemAvailabilityMap = new HashMap<>();

    private List<String> availableItems = new ArrayList<>();

    public List<String> getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(List<String> availableItems) {
        this.availableItems = availableItems;
    }

    public Map<String, Map<String, Boolean>> getItemAvailabilityMap() {
        return itemAvailabilityMap;
    }

    public void setItemAvailabilityMap(Map<String, Map<String, Boolean>> itemAvailabilityMap) {
        this.itemAvailabilityMap = itemAvailabilityMap;
    }

}
