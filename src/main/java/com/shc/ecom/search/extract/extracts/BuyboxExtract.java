/**
 *
 */
package com.shc.ecom.search.extract.extracts;

import com.google.common.collect.Maps;
import com.shc.ecom.search.common.constants.Sites;

import java.io.Serializable;
import java.util.*;

/**
 * @author rgopala
 */
public class BuyboxExtract implements Serializable {


    private static final long serialVersionUID = -2229875433906008317L;

    private Map<Sites, Integer> sitesOfferRankMap = new HashMap<>();

    private Map<Sites, Boolean> sitesMultipleItemConditionsMap = Maps.newHashMap();
    
    private Map<Sites, List<String>> groupedOfferIds = new HashMap<>();

    private Set<String> kmartOrSearsOfferIds = new HashSet<>();

    public Map<Sites, Integer> getSitesOfferRankMap() {
        return sitesOfferRankMap;
    }

    public void setSitesOfferRankMap(Map<Sites, Integer> sitesOfferRankMap) {
        this.sitesOfferRankMap = sitesOfferRankMap;
    }

    public Map<Sites, Boolean> getSitesMultipleItemConditionsMap() {
        return sitesMultipleItemConditionsMap;
    }

    public void setSitesMultipleItemConditionsMap(Map<Sites, Boolean> sitesMultipleItemConditionsMap) {
        this.sitesMultipleItemConditionsMap = sitesMultipleItemConditionsMap;
    }

	public Map<Sites, List<String>> getGroupedOfferIds() {
		return groupedOfferIds;
	}

	public void setGroupedOfferIds(Map<Sites, List<String>> groupedOfferIds) {
		this.groupedOfferIds = groupedOfferIds;
	}

	public Set<String> getKmartOrSearsOfferIds() {
        return kmartOrSearsOfferIds;
	}

	public void setKmartOrSearsOfferIds(Set<String> kmartOrSearsOfferIds) {
		this.kmartOrSearsOfferIds = kmartOrSearsOfferIds;
	}

}
