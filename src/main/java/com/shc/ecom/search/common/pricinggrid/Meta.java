package com.shc.ecom.search.common.pricinggrid;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Meta implements Serializable {

    private final static long serialVersionUID = -8197258125570804313L;
@JsonProperty("price-type")
private String priceType;
@JsonProperty("creation-time")
private String creationTime;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
 *
* @return
* The priceType
*/
@JsonProperty("price-type")
public String getPriceType() {
return priceType;
}

/**
 *
* @param priceType
* The price-type
*/
@JsonProperty("price-type")
public void setPriceType(String priceType) {
this.priceType = priceType;
}

/**
 *
* @return
* The creationTime
*/
@JsonProperty("creation-time")
public String getCreationTime() {
return creationTime;
}

/**
 *
* @param creationTime
* The creation-time
*/
@JsonProperty("creation-time")
public void setCreationTime(String creationTime) {
this.creationTime = creationTime;
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