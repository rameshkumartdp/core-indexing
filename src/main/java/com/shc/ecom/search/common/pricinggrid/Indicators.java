package com.shc.ecom.search.common.pricinggrid;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Indicators implements Serializable {

	private final static long serialVersionUID = 2764056124420959279L;
@JsonProperty("exceptional-value")
private String exceptionalValue;
@JsonProperty("upp-indicator")
private String uppIndicator;
@JsonProperty("hotbuy-indicator")
private String hotbuyIndicator;
@JsonProperty("deal-setting")
private String dealSetting;
@JsonProperty("pricematch-eligible")
private String pricematchEligible;
@JsonProperty("price-matched")
private String priceMatched;
@JsonProperty("clearanceprice-indicator")
private String clearancepriceIndicator;
@JsonProperty("onlineonly-indicator")
private String onlineonlyIndicator;
@JsonProperty("zipcode-required")
private String zipcodeRequired;
@JsonProperty("saleprice-indicator")
private String salepriceIndicator;
@JsonProperty("sywmemberprice-indicator")
private String sywmemberpriceIndicator;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
 *
* @return
* The exceptionalValue
*/
@JsonProperty("exceptional-value")
public String getExceptionalValue() {
return exceptionalValue;
}

/**
 *
* @param exceptionalValue
* The exceptional-value
*/
@JsonProperty("exceptional-value")
public void setExceptionalValue(String exceptionalValue) {
this.exceptionalValue = exceptionalValue;
}

/**
 *
* @return
* The uppIndicator
*/
@JsonProperty("upp-indicator")
public String getUppIndicator() {
return uppIndicator;
}

/**
 *
* @param uppIndicator
* The upp-indicator
*/
@JsonProperty("upp-indicator")
public void setUppIndicator(String uppIndicator) {
this.uppIndicator = uppIndicator;
}

/**
 *
* @return
* The hotbuyIndicator
*/
@JsonProperty("hotbuy-indicator")
public String getHotbuyIndicator() {
return hotbuyIndicator;
}

/**
 *
* @param hotbuyIndicator
* The hotbuy-indicator
*/
@JsonProperty("hotbuy-indicator")
public void setHotbuyIndicator(String hotbuyIndicator) {
this.hotbuyIndicator = hotbuyIndicator;
}

/**
 *
* @return
* The dealSetting
*/
@JsonProperty("deal-setting")
public String getDealSetting() {
return dealSetting;
}

/**
 *
* @param dealSetting
* The deal-setting
*/
@JsonProperty("deal-setting")
public void setDealSetting(String dealSetting) {
this.dealSetting = dealSetting;
}

/**
 *
* @return
* The pricematchEligible
*/
@JsonProperty("pricematch-eligible")
public String getPricematchEligible() {
return pricematchEligible;
}

/**
 *
* @param pricematchEligible
* The pricematch-eligible
*/
@JsonProperty("pricematch-eligible")
public void setPricematchEligible(String pricematchEligible) {
this.pricematchEligible = pricematchEligible;
}

/**
 *
* @return
* The priceMatched
*/
@JsonProperty("price-matched")
public String getPriceMatched() {
return priceMatched;
}

/**
 *
* @param priceMatched
* The price-matched
*/
@JsonProperty("price-matched")
public void setPriceMatched(String priceMatched) {
this.priceMatched = priceMatched;
}

/**
 *
* @return
* The clearancepriceIndicator
*/
@JsonProperty("clearanceprice-indicator")
public String getClearancepriceIndicator() {
return clearancepriceIndicator;
}

/**
 *
* @param clearancepriceIndicator
* The clearanceprice-indicator
*/
@JsonProperty("clearanceprice-indicator")
public void setClearancepriceIndicator(String clearancepriceIndicator) {
this.clearancepriceIndicator = clearancepriceIndicator;
}

/**
 *
* @return
* The onlineonlyIndicator
*/
@JsonProperty("onlineonly-indicator")
public String getOnlineonlyIndicator() {
return onlineonlyIndicator;
}

/**
 *
* @param onlineonlyIndicator
* The onlineonly-indicator
*/
@JsonProperty("onlineonly-indicator")
public void setOnlineonlyIndicator(String onlineonlyIndicator) {
this.onlineonlyIndicator = onlineonlyIndicator;
}

/**
 *
* @return
* The zipcodeRequired
*/
@JsonProperty("zipcode-required")
public String getZipcodeRequired() {
return zipcodeRequired;
}

/**
 *
* @param zipcodeRequired
* The zipcode-required
*/
@JsonProperty("zipcode-required")
public void setZipcodeRequired(String zipcodeRequired) {
this.zipcodeRequired = zipcodeRequired;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

/**
 * @return the salepriceIndicator
 */
public String getSalepriceIndicator() {
	return salepriceIndicator;
}

/**
 * @param salepriceIndicator the salepriceIndicator to set
 */
public void setSalepriceIndicator(String salepriceIndicator) {
	this.salepriceIndicator = salepriceIndicator;
}

/**
 * @return the sywmemberpriceIndicator
 */
public String getSywmemberpriceIndicator() {
	return sywmemberpriceIndicator;
}

/**
 * @param sywmemberpriceIndicator the sywmemberpriceIndicator to set
 */
public void setSywmemberpriceIndicator(String sywmemberpriceIndicator) {
	this.sywmemberpriceIndicator = sywmemberpriceIndicator;
}

}