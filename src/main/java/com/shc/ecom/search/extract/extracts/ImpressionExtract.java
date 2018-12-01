package com.shc.ecom.search.extract.extracts;

import java.io.Serializable;

/**
 * @author rgopala
 */
public class ImpressionExtract implements Serializable {

    private static final long serialVersionUID = -1211912058157979049L;

    private long impressions;

    public long getImpressions() {
        return impressions;
    }

    public void setImpressions(long impressions) {
        this.impressions = impressions;
    }

}
