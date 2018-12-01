package com.shc.ecom.search.common.pricinggrid;


import com.fasterxml.jackson.annotation.*;
import com.shc.ecom.search.common.price.bundle.MemberPrice;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class AtcPrice implements Serializable {

private final static long serialVersionUID = 2884294414050079704L;
@JsonProperty("guest-price")
@Valid
private GuestPrice guestPrice;
@JsonProperty("member-price")
@Valid
private MemberPrice memberPrice;
@JsonProperty("currency-code")
private String currencyCode;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
*
* @return
* The guestPrice
*/
@JsonProperty("guest-price")
public GuestPrice getGuestPrice() {
return guestPrice;
}

/**
*
* @param guestPrice
* The guest-price
*/
@JsonProperty("guest-price")
public void setGuestPrice(GuestPrice guestPrice) {
this.guestPrice = guestPrice;
}

/**
*
* @return
* The memberPrice
*/
@JsonProperty("member-price")
public MemberPrice getMemberPrice() {
return memberPrice;
}

/**
*
* @param memberPrice
* The member-price
*/
@JsonProperty("member-price")
public void setMemberPrice(MemberPrice memberPrice) {
this.memberPrice = memberPrice;
}

/**
*
* @return
* The currencyCode
*/
@JsonProperty("currency-code")
public String getCurrencyCode() {
return currencyCode;
}

/**
*
* @param currencyCode
* The currency-code
*/
@JsonProperty("currency-code")
public void setCurrencyCode(String currencyCode) {
this.currencyCode = currencyCode;
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