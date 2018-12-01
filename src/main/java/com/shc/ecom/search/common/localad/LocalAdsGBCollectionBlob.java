package com.shc.ecom.search.common.localad;

import java.io.Serializable;

public class LocalAdsGBCollectionBlob implements Serializable {
    LocalAdsGBStoreLocalAds storelocalads;

    public LocalAdsGBStoreLocalAds getStorelocalads() {
        return storelocalads;
    }

    public void setStorelocalads(LocalAdsGBStoreLocalAds storelocalads) {
        this.storelocalads = storelocalads;
    }
}
