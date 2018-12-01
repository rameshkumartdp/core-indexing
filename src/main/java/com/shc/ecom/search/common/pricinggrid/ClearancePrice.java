package com.shc.ecom.search.common.pricinggrid;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class ClearancePrice implements Serializable {


        private final static long serialVersionUID = 3027827372716667810L;
@JsonProperty("min")
private String min;
@JsonProperty("max")
private String max;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
 *
* @return
* The min
*/
@JsonProperty("min")
public String getMin() {
return min;
}

/**
 *
* @param min
* The min
*/
@JsonProperty("min")
public void setMin(String min) {
this.min = min;
}

/**
 *
* @return
* The max
*/
@JsonProperty("max")
public String getMax() {
return max;
}

/**
 *
* @param max
* The max
*/
@JsonProperty("max")
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
