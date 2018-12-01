package com.shc.ecom.search.common.pricinggrid;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class PriceMatchDetail implements Serializable {

    private final static long serialVersionUID = -8397078661072819306L;
@JsonProperty("name")
private String name;
@JsonProperty("price")
private String price;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
 *
* @return
* The name
*/
@JsonProperty("name")
public String getName() {
return name;
}

/**
 *
* @param name
* The name
*/
@JsonProperty("name")
public void setName(String name) {
this.name = name;
}

/**
 *
* @return
* The price
*/
@JsonProperty("price")
public String getPrice() {
return price;
}

/**
 *
* @param price
* The price
*/
@JsonProperty("price")
public void setPrice(String price) {
this.price = price;
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