package com.shc.ecom.search.common.uas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UasDto implements Serializable {

    private static final long serialVersionUID = 5008069502856528624L;

    private Map<String, Map<String, Boolean>> itemMap;

    public Map<String, Map<String, Boolean>> getItemMap() {
        if (itemMap == null) {
            itemMap = new HashMap<>();
        }
        return itemMap;
    }

    public void setItemMap(Map<String, Map<String, Boolean>> itemMap) {
        this.itemMap = itemMap;
    }

}
