package com.shc.ecom.search.common.localad;

import org.eclipse.jetty.util.ConcurrentHashSet;

public class LocalAd {
    private ConcurrentHashSet<String> localAdIds;

    public ConcurrentHashSet<String> getLocalAdIds() {
        return localAdIds;
    }

    public void setLocalAdIds(ConcurrentHashSet<String> localAdIds) {
        this.localAdIds = localAdIds;
    }
}
