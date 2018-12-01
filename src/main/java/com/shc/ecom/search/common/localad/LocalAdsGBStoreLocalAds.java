package com.shc.ecom.search.common.localad;

import java.io.Serializable;
import java.util.List;

public class LocalAdsGBStoreLocalAds implements Serializable {
    List<LocalAdsGBActivity> localAds;

    public List<LocalAdsGBActivity> getLocalAds() {
        return localAds;
    }

    public void setLocalAds(List<LocalAdsGBActivity> localAds) {
        this.localAds = localAds;
    }
}
