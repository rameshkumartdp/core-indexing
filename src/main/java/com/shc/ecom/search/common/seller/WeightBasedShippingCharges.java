package com.shc.ecom.search.common.seller;

public class WeightBasedShippingCharges{
    private NonMailable nonMailable;

    private Mailable mailable;

    public NonMailable getNonMailable (){
        return nonMailable;
    }

    public void setNonMailable (NonMailable nonMailable){
        this.nonMailable = nonMailable;
    }

    public Mailable getMailable (){
        return mailable;
    }

    public void setMailable (Mailable mailable){
        this.mailable = mailable;
    }

    @Override
    public String toString(){
        return "ClassPojo [nonMailable = "+nonMailable+", mailable = "+mailable+"]";
    }
}