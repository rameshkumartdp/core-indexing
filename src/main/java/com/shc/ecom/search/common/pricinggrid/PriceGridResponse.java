package com.shc.ecom.search.common.pricinggrid;

import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class PriceGridResponse implements Serializable {

    private final static long serialVersionUID = 5064114051191010697L;
@JsonProperty("price-response")
@Valid
private PriceResponse priceResponse;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
 *
* @return
* The priceResponse
*/
@JsonProperty("price-response")
public PriceResponse getPriceResponse() {
return priceResponse;
}

/**
 *
* @param priceResponse
* The price-response
*/
@JsonProperty("price-response")
public void setPriceResponse(PriceResponse priceResponse) {
this.priceResponse = priceResponse;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}