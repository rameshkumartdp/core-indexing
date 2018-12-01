package com.shc.ecom.search.common.pricinggrid;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class MapDetails implements Serializable {

    private final static long serialVersionUID = -461005137602766315L;
@JsonProperty("violation")
private String violation;
@JsonProperty("syw-violation")
private String sywViolation;
@JsonProperty("sywcc-violation")
private String sywccViolation;
@JsonProperty("setting")
private String setting;
@JsonProperty("map-price")
private String mapPrice;
@JsonProperty("threshold-type")
private String thresholdType;
@JsonProperty("threshold-value")
private String thresholdValue;
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
public String getMapPrice() {
return mapPrice;
}

/**
 *
* @param mapPrice
* The map-price
*/
@JsonProperty("map-price")
public void setMapPrice(String mapPrice) {
this.mapPrice = mapPrice;
}

/**
 *
* @return
* The thresholdType
*/
@JsonProperty("threshold-type")
public String getThresholdType() {
return thresholdType;
}

/**
 *
* @param thresholdType
* The threshold-type
*/
@JsonProperty("threshold-type")
public void setThresholdType(String thresholdType) {
this.thresholdType = thresholdType;
}

/**
 *
* @return
* The thresholdValue
*/
@JsonProperty("threshold-value")
public String getThresholdValue() {
return thresholdValue;
}

/**
 *
* @param thresholdValue
* The threshold-value
*/
@JsonProperty("threshold-value")
public void setThresholdValue(String thresholdValue) {
this.thresholdValue = thresholdValue;
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
