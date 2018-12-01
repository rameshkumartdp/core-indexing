/**
 *
 */
package com.shc.ecom.search.common.vo.pricegrid;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author djohn0
 */
public class PriceRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5365537783790858809L;

    @JsonProperty("store-id")
    private String storeId;

    @JsonProperty("price-identifier")
    private List<PriceIdentifier> priceIdentifier;


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public List<PriceIdentifier> getPriceIdentifier() {
        return priceIdentifier;
    }

    public void setPriceIdentifier(List<PriceIdentifier> priceIdentifier) {
        this.priceIdentifier = priceIdentifier;
    }

}
