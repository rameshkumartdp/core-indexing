package com.shc.ecom.search.common.pricinggrid;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SellPrice implements Serializable {

	private final static long serialVersionUID = -1807798313417077403L;
@JsonProperty("price")
private String price;
@JsonProperty("price-type")
private String priceType;
@JsonProperty("min")
private String min;
@JsonProperty("max")
private String max;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
 * @return the min
 */
public String getMin() {
	return min;
}

/**
 * @param min the min to set
 */
public void setMin(String min) {
	this.min = min;
}

/**
 * @return the max
 */
public String getMax() {
	return max;
}

/**
 * @param max the max to set
 */
public void setMax(String max) {
	this.max = max;
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
