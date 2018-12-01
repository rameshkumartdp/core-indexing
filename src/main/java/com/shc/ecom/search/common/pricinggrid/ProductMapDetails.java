package com.shc.ecom.search.common.pricinggrid;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductMapDetails {

@JsonProperty("violation")
private String violation;
@JsonProperty("syw-violation")
private String sywViolation;
@JsonProperty("sywcc-violation")
private String sywccViolation;
@JsonProperty("setting")
private String setting;
@JsonProperty("map-price")
private MapPrice mapPrice;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The violation
*/
@JsonProperty("violation")
public String getViolation() {
return violation;
}

/**
* 
* @param violation
* The violation
*/
@JsonProperty("violation")
public void setViolation(String violation) {
this.violation = violation;
}

/**
* 
* @return
* The sywViolation
*/
@JsonProperty("syw-violation")
public String getSywViolation() {
return sywViolation;
}

/**
* 
* @param sywViolation
* The syw-violation
*/
@JsonProperty("syw-violation")
public void setSywViolation(String sywViolation) {
this.sywViolation = sywViolation;
}

/**
* 
* @return
* The sywccViolation
*/
@JsonProperty("sywcc-violation")
public String getSywccViolation() {
return sywccViolation;
}

/**
* 
* @param sywccViolation
* The sywcc-violation
*/
@JsonProperty("sywcc-violation")
public void setSywccViolation(String sywccViolation) {
this.sywccViolation = sywccViolation;
}

/**
* 
* @return
* The setting
*/
@JsonProperty("setting")
public String getSetting() {
return setting;
}

/**
* 
* @param setting
* The setting
*/
@JsonProperty("setting")
public void setSetting(String setting) {
this.setting = setting;
}

/**
* 
* @return
* The mapPrice
*/
@JsonProperty("map-price")
public MapPrice getMapPrice() {
return mapPrice;
}

/**
* 
* @param mapPrice
* The map-price
*/
@JsonProperty("map-price")
public void setMapPrice(MapPrice mapPrice) {
this.mapPrice = mapPrice;
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
