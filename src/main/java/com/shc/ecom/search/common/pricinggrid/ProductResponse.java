package com.shc.ecom.search.common.pricinggrid;

import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class ProductResponse implements Serializable {

    private final static long serialVersionUID = 8401596780377423629L;
@JsonProperty("status-code")
private String statusCode;
@JsonProperty("ssin")
private String ssin;
@JsonProperty("regular-price")
@Valid
private RegularPrice regularPrice;
@JsonProperty("promo-price")
@Valid
private PromoPrice promoPrice;
@JsonProperty("promo-enddate")
private String promoEnddate;
@JsonProperty("clearance-price")
@Valid
private ClearancePrice clearancePrice;
@JsonProperty("sell-price")
@Valid
private SellPrice sellPrice;
@JsonProperty("ndd-price")
@Valid
private NddPrice nddPrice;
@JsonProperty("final-price")
@Valid
private FinalPrice finalPrice;
@JsonProperty("cc-price")
@Valid
private CcPrice ccPrice;
@JsonProperty("syw-price")
@Valid
private SywPrice sywPrice;
@JsonProperty("sywcc-price")
@Valid
private SywccPrice sywccPrice;
@JsonProperty("was-price")
@Valid
private WasPrice wasPrice;
@JsonProperty("indicators")
@Valid
private Indicators indicators;
@JsonProperty("map-details")
@Valid
private ProductMapDetails productMapDetails;
@JsonProperty("deal-display")
@Valid
private DealDisplay dealDisplay;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
 *
* @return
* The statusCode
*/
@JsonProperty("status-code")
public String getStatusCode() {
return statusCode;
}

/**
 *
* @param statusCode
* The status-code
*/
@JsonProperty("status-code")
public void setStatusCode(String statusCode) {
this.statusCode = statusCode;
}

/**
 *
* @return
* The ssin
*/
@JsonProperty("ssin")
public String getSsin() {
return ssin;
}

/**
 *
* @param ssin
* The ssin
*/
@JsonProperty("ssin")
public void setSsin(String ssin) {
this.ssin = ssin;
}

/**
 *
* @return
* The regularPrice
*/
@JsonProperty("regular-price")
public RegularPrice getRegularPrice() {
return regularPrice;
}

/**
 *
* @param regularPrice
* The regular-price
*/
@JsonProperty("regular-price")
public void setRegularPrice(RegularPrice regularPrice) {
this.regularPrice = regularPrice;
}

/**
 *
* @return
* The promoPrice
*/
@JsonProperty("promo-price")
public PromoPrice getPromoPrice() {
return promoPrice;
}

/**
 *
* @param promoPrice
* The promo-price
*/
@JsonProperty("promo-price")
public void setPromoPrice(PromoPrice promoPrice) {
this.promoPrice = promoPrice;
}

/**
 *
* @return
* The promoEnddate
*/
@JsonProperty("promo-enddate")
public String getPromoEnddate() {
return promoEnddate;
}

/**
 *
* @param promoEnddate
* The promo-enddate
*/
@JsonProperty("promo-enddate")
public void setPromoEnddate(String promoEnddate) {
this.promoEnddate = promoEnddate;
}

/**
 *
* @return
* The clearancePrice
*/
@JsonProperty("clearance-price")
public ClearancePrice getClearancePrice() {
return clearancePrice;
}

/**
 *
* @param clearancePrice
* The clearance-price
*/
@JsonProperty("clearance-price")
public void setClearancePrice(ClearancePrice clearancePrice) {
this.clearancePrice = clearancePrice;
}

/**
 *
* @return
* The sellPrice
*/
@JsonProperty("sell-price")
public SellPrice getSellPrice() {
return sellPrice;
}

/**
 *
* @param sellPrice
* The sell-price
*/
@JsonProperty("sell-price")
public void setSellPrice(SellPrice sellPrice) {
this.sellPrice = sellPrice;
}

/**
 *
* @return
* The nddPrice
*/
@JsonProperty("ndd-price")
public NddPrice getNddPrice() {
return nddPrice;
}

/**
 *
* @param nddPrice
* The ndd-price
*/
@JsonProperty("ndd-price")
public void setNddPrice(NddPrice nddPrice) {
this.nddPrice = nddPrice;
}

/**
 *
* @return
* The finalPrice
*/
@JsonProperty("final-price")
public FinalPrice getFinalPrice() {
return finalPrice;
}

/**
 *
* @param finalPrice
* The final-price
*/
@JsonProperty("final-price")
public void setFinalPrice(FinalPrice finalPrice) {
this.finalPrice = finalPrice;
}

/**
 *
* @return
* The ccPrice
*/
@JsonProperty("cc-price")
public CcPrice getCcPrice() {
return ccPrice;
}

/**
 *
* @param ccPrice
* The cc-price
*/
@JsonProperty("cc-price")
public void setCcPrice(CcPrice ccPrice) {
this.ccPrice = ccPrice;
}

/**
 *
* @return
* The sywPrice
*/
@JsonProperty("syw-price")
public SywPrice getSywPrice() {
return sywPrice;
}

/**
 *
* @param sywPrice
* The syw-price
*/
@JsonProperty("syw-price")
public void setSywPrice(SywPrice sywPrice) {
this.sywPrice = sywPrice;
}

/**
 *
* @return
* The sywccPrice
*/
@JsonProperty("sywcc-price")
public SywccPrice getSywccPrice() {
return sywccPrice;
}

/**
 *
* @param sywccPrice
* The sywcc-price
*/
@JsonProperty("sywcc-price")
public void setSywccPrice(SywccPrice sywccPrice) {
this.sywccPrice = sywccPrice;
}

/**
 *
* @return
* The wasPrice
*/
@JsonProperty("was-price")
public WasPrice getWasPrice() {
return wasPrice;
}

/**
 *
* @param wasPrice
* The was-price
*/
@JsonProperty("was-price")
public void setWasPrice(WasPrice wasPrice) {
this.wasPrice = wasPrice;
}

/**
 *
* @return
* The indicators
*/
@JsonProperty("indicators")
public Indicators getIndicators() {
return indicators;
}

/**
 *
* @param indicators
* The indicators
*/
@JsonProperty("indicators")
public void setIndicators(Indicators indicators) {
this.indicators = indicators;
}

/**
 *
* @return
* The mapDetails
*/
@JsonProperty("map-details")
public ProductMapDetails getMapDetails() {
return productMapDetails;
}

/**
 *
* @param mapDetails
* The map-details
*/
@JsonProperty("map-details")
public void setMapDetails(ProductMapDetails mapDetails) {
this.productMapDetails = mapDetails;
}

/**
 *
* @return
* The dealDisplay
*/
@JsonProperty("deal-display")
public DealDisplay getDealDisplay() {
return dealDisplay;
}

/**
 *
* @param dealDisplay
* The deal-display
*/
@JsonProperty("deal-display")
public void setDealDisplay(DealDisplay dealDisplay) {
this.dealDisplay = dealDisplay;
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