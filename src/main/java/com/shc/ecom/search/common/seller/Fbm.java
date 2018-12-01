package com.shc.ecom.search.common.seller;

import java.io.Serializable;

public class Fbm implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3593185269485045063L;

    private String goferL3Seller;

    private String sywMaxEligibility;

    private String openOrderThreshold;


    private String returnPolicy;

    private boolean trustedSeller;

    private String ediRouteCode;

    private String serviceHours;

    private ShippingRates shippingRates;

    public String getAggregatorId() {
        return aggregatorId;
    }

    public void setAggregatorId(String aggregatorId) {
        this.aggregatorId = aggregatorId;
    }

    private String dunsNumber;

    private String aggregatorId;


    public String getGoferL3Seller (){
        return goferL3Seller;
    }

    public void setGoferL3Seller (String goferL3Seller){
        this.goferL3Seller = goferL3Seller;
    }

    public String getSywMaxEligibility (){
        return sywMaxEligibility;
    }

    public void setSywMaxEligibility (String sywMaxEligibility){
        this.sywMaxEligibility = sywMaxEligibility;
    }

    public String getOpenOrderThreshold (){
        return openOrderThreshold;
    }

    public void setOpenOrderThreshold (String openOrderThreshold){
        this.openOrderThreshold = openOrderThreshold;
    }

    public String getReturnPolicy (){
        return returnPolicy;
    }

    public void setReturnPolicy (String returnPolicy){
        this.returnPolicy = returnPolicy;
    }

    public boolean getTrustedSeller (){
        return trustedSeller;
    }

    public void setTrustedSeller (boolean trustedSeller){
        this.trustedSeller = trustedSeller;
    }

    public String getEdiRouteCode (){
        return ediRouteCode;
    }

    public void setEdiRouteCode (String ediRouteCode){
        this.ediRouteCode = ediRouteCode;
    }

    public String getServiceHours (){
        return serviceHours;
    }

    public void setServiceHours (String serviceHours){
        this.serviceHours = serviceHours;
    }

    public ShippingRates getShippingRates (){
    	if(shippingRates==null) {
    		shippingRates = new ShippingRates();
    	}
        return shippingRates;
    }

    public void setShippingRates (ShippingRates shippingRates){
        this.shippingRates = shippingRates;
    }

    public String getDunsNumber (){
        return dunsNumber;
    }

    public void setDunsNumber (String dunsNumber){
        this.dunsNumber = dunsNumber;
    }


    @Override
    public String toString(){
        return "Fbm [goferL3Seller = "+goferL3Seller+", sywMaxEligibility = "+sywMaxEligibility+", openOrderThreshold = "+openOrderThreshold+", returnPolicy = "+returnPolicy+", trustedSeller = "+trustedSeller+", ediRouteCode = "+ediRouteCode+", serviceHours = "+serviceHours+", shippingRates = "+shippingRates+", dunsNumber = "+dunsNumber+"]";
    }

}
