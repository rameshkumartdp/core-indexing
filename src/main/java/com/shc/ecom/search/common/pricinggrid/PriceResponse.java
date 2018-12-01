package com.shc.ecom.search.common.pricinggrid;

import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class PriceResponse implements Serializable {

    private final static long serialVersionUID = -5535089022312446242L;
@JsonProperty("status-message")
private String statusMessage;
@JsonProperty("site")
private String site;
@JsonProperty("storeunit-number")
private String storeunitNumber;
@JsonProperty("zipCode")
private String zipCode;
@JsonProperty("item-response")
@Valid
private List<ItemResponse> itemResponse = null;
@JsonProperty("product-response")
@Valid
private ProductResponse productResponse;
@JsonProperty("fx-rates-info")
@Valid
private FxRatesInfo fxRatesInfo;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
 *
* @return
* The statusMessage
*/
@JsonProperty("status-message")
public String getStatusMessage() {
return statusMessage;
}

/**
 *
* @param statusMessage
* The status-message
*/
@JsonProperty("status-message")
public void setStatusMessage(String statusMessage) {
this.statusMessage = statusMessage;
}

/**
 *
* @return
* The site
*/
@JsonProperty("site")
public String getSite() {
return site;
}

/**
 *
* @param site
* The site
*/
@JsonProperty("site")
public void setSite(String site) {
this.site = site;
}

/**
 *
* @return
* The storeunitNumber
*/
@JsonProperty("storeunit-number")
public String getStoreunitNumber() {
return storeunitNumber;
}

/**
 *
* @param storeunitNumber
* The storeunit-number
*/
@JsonProperty("storeunit-number")
public void setStoreunitNumber(String storeunitNumber) {
this.storeunitNumber = storeunitNumber;
}

/**
 *
* @return
* The zipCode
*/
@JsonProperty("zipCode")
public String getZipCode() {
return zipCode;
}

/**
 *
* @param zipCode
* The zipCode
*/
@JsonProperty("zipCode")
public void setZipCode(String zipCode) {
this.zipCode = zipCode;
}

/**
 *
* @return
* The itemResponse
*/
@JsonProperty("item-response")
public List<ItemResponse> getItemResponse() {
return itemResponse;
}

/**
 *
* @param itemResponse
* The item-response
*/
@JsonProperty("item-response")
public void setItemResponse(List<ItemResponse> itemResponse) {
this.itemResponse = itemResponse;
}

/**
 *
* @return
* The productResponse
*/
@JsonProperty("product-response")
public ProductResponse getProductResponse() {
return productResponse;
}

/**
 *
* @param productResponse
* The product-response
*/
@JsonProperty("product-response")
public void setProductResponse(ProductResponse productResponse) {
this.productResponse = productResponse;
}

/**
 *
* @return
* The fxRatesInfo
*/
@JsonProperty("fx-rates-info")
public FxRatesInfo getFxRatesInfo() {
return fxRatesInfo;
}

/**
 *
* @param fxRatesInfo
* The fx-rates-info
*/
@JsonProperty("fx-rates-info")
public void setFxRatesInfo(FxRatesInfo fxRatesInfo) {
this.fxRatesInfo = fxRatesInfo;
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