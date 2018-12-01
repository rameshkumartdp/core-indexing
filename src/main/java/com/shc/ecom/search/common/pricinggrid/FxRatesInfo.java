package com.shc.ecom.search.common.pricinggrid;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class FxRatesInfo implements Serializable {

    private final static long serialVersionUID = 4097180703415250612L;
@JsonProperty("country-code")
private String countryCode;
@JsonProperty("currency-code")
private String currencyCode;
@JsonProperty("currency-multiplier")
private String currencyMultiplier;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
 *
* @return
* The countryCode
*/
@JsonProperty("country-code")
public String getCountryCode() {
return countryCode;
}

/**
 *
* @param countryCode
* The country-code
*/
@JsonProperty("country-code")
public void setCountryCode(String countryCode) {
this.countryCode = countryCode;
}

/**
 *
* @return
* The currencyCode
*/
@JsonProperty("currency-code")
public String getCurrencyCode() {
return currencyCode;
}

/**
 *
* @param currencyCode
* The currency-code
*/
@JsonProperty("currency-code")
public void setCurrencyCode(String currencyCode) {
this.currencyCode = currencyCode;
}

/**
 *
* @return
* The currencyMultiplier
*/
@JsonProperty("currency-multiplier")
public String getCurrencyMultiplier() {
return currencyMultiplier;
}

/**
 *
* @param currencyMultiplier
* The currency-multiplier
*/
@JsonProperty("currency-multiplier")
public void setCurrencyMultiplier(String currencyMultiplier) {
this.currencyMultiplier = currencyMultiplier;
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