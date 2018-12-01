package com.shc.ecom.search.common.uas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vsonthy on 3/28/17.
 */
public class UasDtoByOffer implements Serializable {

    private static final long serialVersionUID = -7233828843719436014L;

    private String offerId;
    private Map<String,Boolean> offerItemMap;
    private boolean isViewOnlyOffer;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public Map<String, Boolean> getOfferItemMap() {
        if(offerItemMap == null){
            return new HashMap<>();
        }
        return offerItemMap;
    }

    public void setOfferItemMap(Map<String, Boolean> offerItemMap) {
        this.offerItemMap = offerItemMap;
    }

    public boolean isViewOnlyOffer() {
        return isViewOnlyOffer;
    }

    public void setViewOnlyOffer(boolean viewOnlyOffer) {
        isViewOnlyOffer = viewOnlyOffer;
    }
}
