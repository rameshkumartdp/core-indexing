package com.shc.ecom.search.common.pas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pchauha
 */
public class Products implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8011356027966342667L;

    private String productId;
    private int storeCount;
    private List<StoreList> storeList;
    private List<String> zoneList;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(int storeCount) {
        this.storeCount = storeCount;
    }

    public List<StoreList> getStoreList() {
        if (storeList == null) {
            storeList = new ArrayList<>();
        }
        return storeList;
    }

    public void setStoreList(List<StoreList> storeList) {
        this.storeList = storeList;
    }

    public List<String> getZoneList() {
        if (zoneList == null) {
            zoneList = new ArrayList<>();
        }
        return zoneList;
    }

    public void setZoneList(List<String> zoneList) {
        this.zoneList = zoneList;
    }

}
