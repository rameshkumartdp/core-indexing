package com.shc.ecom.search.common.seller;

public class ShippingRates{
    private String shipMode;

    private ShippingCharges shippingCharges;

    private ShipMethods shipMethods;

    public String getShipMode (){
        return shipMode;
    }

    public void setShipMode (String shipMode){
        this.shipMode = shipMode;
    }

    public ShippingCharges getShippingCharges (){
    	if(shippingCharges==null) {
    		shippingCharges = new ShippingCharges();
    	}
        return shippingCharges;
    }

    public void setShippingCharges (ShippingCharges shippingCharges){
        this.shippingCharges = shippingCharges;
    }

    public ShipMethods getShipMethods (){
    	if(shipMethods==null) {
    		shipMethods = new ShipMethods();
    	}
        return shipMethods;
    }

    public void setShipMethods (ShipMethods shipMethods){
        this.shipMethods = shipMethods;
    }

    @Override
    public String toString(){
        return "ShippingRates [shipMode = "+shipMode+", shippingCharges = "+shippingCharges+", shipMethods = "+shipMethods+"]";
    }
}