package com.shc.ecom.search.extract.extracts;

import com.shc.ecom.search.common.constants.Stores;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PromoExtract implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5624360225818339558L;

    private Map<Stores, StorePromoExtract> storePromoExtracts;

    public Map<Stores, StorePromoExtract> getStorePromoExtracts() {
        if (storePromoExtracts == null) {
            storePromoExtracts = new HashMap<Stores, StorePromoExtract>();
        }
        return storePromoExtracts;
    }

    public void setStorePromoExtracts(Map<Stores, StorePromoExtract> storePromoExtracts) {
        this.storePromoExtracts = storePromoExtracts;
    }
}
