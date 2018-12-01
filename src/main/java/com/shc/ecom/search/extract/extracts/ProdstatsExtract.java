package com.shc.ecom.search.extract.extracts;

import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.extract.components.prodstats.ProdStats;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rgopala
 */
public class ProdstatsExtract implements Serializable {

    private static final long serialVersionUID = -8556859217712721394L;

    private Map<Sites, ProdStats> sitesProdStatsMap = new HashMap<>();

    public Map<Sites, ProdStats> getSitesProdStatsMap() {
        return sitesProdStatsMap;
    }

    public void setSitesProdStatsMap(Map<Sites, ProdStats> sitesProdStatsMap) {
        this.sitesProdStatsMap = sitesProdStatsMap;
    }
}
