package com.shc.ecom.search.common.seller;

public class ShippingCharges{
    private WeightBasedShippingCharges weightBasedShippingCharges;

    private NonMailable nonMailable;

    private Mailable mailable;
    
    private OrderAmountBasedShippingCharges orderAmountBasedShippingCharges;

    public WeightBasedShippingCharges getWeightBasedShippingCharges (){
    	if(weightBasedShippingCharges==null) {
    		weightBasedShippingCharges = new WeightBasedShippingCharges();
    	}
        return weightBasedShippingCharges;
    }

    public void setWeightBasedShippingCharges (WeightBasedShippingCharges weightBasedShippingCharges){
        this.weightBasedShippingCharges = weightBasedShippingCharges;
    }

    public NonMailable getNonMailable (){
    	if(nonMailable==null) {
    		nonMailable = new NonMailable();
    	}
        return nonMailable;
    }

    public void setNonMailable (NonMailable nonMailable){
        this.nonMailable = nonMailable;
    }

    public Mailable getMailable (){
    	if(mailable==null) {
    		mailable = new Mailable();
    	}
        return mailable;
    }

    public void setMailable (Mailable mailable){
        this.mailable = mailable;
    }
    
    public OrderAmountBasedShippingCharges getOrderAmountBasedShippingCharges () {
    	if(orderAmountBasedShippingCharges==null) {
    		orderAmountBasedShippingCharges = new OrderAmountBasedShippingCharges();
    	}
        return orderAmountBasedShippingCharges;
    }

    public void setOrderAmountBasedShippingCharges (OrderAmountBasedShippingCharges orderAmountBasedShippingCharges) {
        this.orderAmountBasedShippingCharges = orderAmountBasedShippingCharges;
    }

    @Override
    public String toString(){
    	return "ShippingCharges [weightBasedShippingCharges = "+weightBasedShippingCharges+", nonMailable = "+nonMailable+", orderAmountBasedShippingCharges = "+orderAmountBasedShippingCharges+", mailable = "+mailable+"]";
    }
}
