package com.shc.ecom.search.common.pricinggrid;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class DealDisplay implements Serializable {

    private final static long serialVersionUID = -2295343809035666993L;
@JsonProperty("indicator")
private String indicator;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
 *
* @return
* The indicator
*/
@JsonProperty("indicator")
public String getIndicator() {
return indicator;
}

/**
 *
* @param indicator
* The indicator
*/
@JsonProperty("indicator")
public void setIndicator(String indicator) {
this.indicator = indicator;
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
