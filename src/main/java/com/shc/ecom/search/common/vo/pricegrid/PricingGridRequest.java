/**
 *
 */
package com.shc.ecom.search.common.vo.pricegrid;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author djohn0
 */
public class PricingGridRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 9050654519811474104L;

    @JsonProperty("price-request")
    private PriceRequest priceRequest;

    public PriceRequest getPriceRequest() {
        return priceRequest;
    }

    public void setPriceRequest(PriceRequest priceRequest) {
        this.priceRequest = priceRequest;
    }


}
