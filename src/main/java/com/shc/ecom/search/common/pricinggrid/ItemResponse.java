package com.shc.ecom.search.common.pricinggrid;

import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class ItemResponse implements Serializable {

    private final static long serialVersionUID = -1942981312776880860L;
@JsonProperty("status-code")
private String statusCode;
@JsonProperty("offer")
private String offer;
@JsonProperty("regular-price")
private String regularPrice;
@JsonProperty("promo-price")
private String promoPrice;
@JsonProperty("promo-enddate")
private String promoEnddate;
@JsonProperty("clearance-price")
private String clearancePrice;
@JsonProperty("sell-price")
@Valid
private SellPrice sellPrice;
@JsonProperty("ndd-price")
private String nddPrice;
@JsonProperty("cc-price")
private String ccPrice;
@JsonProperty("syw-price")
private String sywPrice;
@JsonProperty("sywcc-price")
private String sywccPrice;
@JsonProperty("was-price")
private String wasPrice;
@JsonProperty("meta")
@Valid
private Meta meta;
@JsonProperty("indicators")
@Valid
private Indicators indicators;
@JsonProperty("map-details")
@Valid
private MapDetails mapDetails;
@JsonProperty("price-match-details")
@Valid
private List<PriceMatchDetail> priceMatchDetails = null;
@JsonProperty("deal-display")
@Valid
private DealDisplay dealDisplay;
@JsonProperty("atc-price")
@Valid
private AtcPrice atcPrice;
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
* The offer
*/
@JsonProperty("offer")
public String getOffer() {
return offer;
}

/**
 *
* @param offer
* The offer
*/
@JsonProperty("offer")
public void setOffer(String offer) {
this.offer = offer;
}

/**
 *
* @return
* The regularPrice
*/
@JsonProperty("regular-price")
public String getRegularPrice() {
return regularPrice;
}

/**
 *
* @param regularPrice
* The regular-price
*/
@JsonProperty("regular-price")
public void setRegularPrice(String regularPrice) {
this.regularPrice = regularPrice;
}

/**
 *
* @return
* The promoPrice
*/
@JsonProperty("promo-price")
public String getPromoPrice() {
return promoPrice;
}

/**
 *
* @param promoPrice
* The promo-price
*/
@JsonProperty("promo-price")
public void setPromoPrice(String promoPrice) {
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
public String getClearancePrice() {
return clearancePrice;
}

/**
 *
* @param clearancePrice
* The clearance-price
*/
@JsonProperty("clearance-price")
public void setClearancePrice(String clearancePrice) {
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
public String getNddPrice() {
return nddPrice;
}

/**
 *
* @param nddPrice
* The ndd-price
*/
@JsonProperty("ndd-price")
public void setNddPrice(String nddPrice) {
this.nddPrice = nddPrice;
}

/**
 *
* @return
* The ccPrice
*/
@JsonProperty("cc-price")
public String getCcPrice() {
return ccPrice;
}

/**
 *
* @param ccPrice
* The cc-price
*/
@JsonProperty("cc-price")
public void setCcPrice(String ccPrice) {
this.ccPrice = ccPrice;
}

/**
 *
* @return
* The sywPrice
*/
@JsonProperty("syw-price")
public String getSywPrice() {
return sywPrice;
}

/**
 *
* @param sywPrice
* The syw-price
*/
@JsonProperty("syw-price")
public void setSywPrice(String sywPrice) {
this.sywPrice = sywPrice;
}

/**
 *
* @return
* The sywccPrice
*/
@JsonProperty("sywcc-price")
public String getSywccPrice() {
return sywccPrice;
}

/**
 *
* @param sywccPrice
* The sywcc-price
*/
@JsonProperty("sywcc-price")
public void setSywccPrice(String sywccPrice) {
this.sywccPrice = sywccPrice;
}

/**
 *
* @return
* The wasPrice
*/
@JsonProperty("was-price")
public String getWasPrice() {
return wasPrice;
}

/**
 *
* @param wasPrice
* The was-price
*/
@JsonProperty("was-price")
public void setWasPrice(String wasPrice) {
this.wasPrice = wasPrice;
}

/**
 *
* @return
* The meta
*/
@JsonProperty("meta")
public Meta getMeta() {
return meta;
}

/**
 *
* @param meta
* The meta
*/
@JsonProperty("meta")
public void setMeta(Meta meta) {
this.meta = meta;
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
public MapDetails getMapDetails() {
return mapDetails;
}

/**
 *
* @param mapDetails
* The map-details
*/
@JsonProperty("map-details")
public void setMapDetails(MapDetails mapDetails) {
this.mapDetails = mapDetails;
}

/**
 *
* @return
* The priceMatchDetails
*/
@JsonProperty("price-match-details")
public List<PriceMatchDetail> getPriceMatchDetails() {
return priceMatchDetails;
}

/**
 *
* @param priceMatchDetails
* The price-match-details
*/
@JsonProperty("price-match-details")
public void setPriceMatchDetails(List<PriceMatchDetail> priceMatchDetails) {
this.priceMatchDetails = priceMatchDetails;
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

/**
 *
* @return
* The atcPrice
*/
@JsonProperty("atc-price")
public AtcPrice getAtcPrice() {
return atcPrice;
}

/**
 *
* @param atcPrice
* The atc-price
*/
@JsonProperty("atc-price")
public void setAtcPrice(AtcPrice atcPrice) {
this.atcPrice = atcPrice;
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