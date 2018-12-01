package com.shc.ecom.search.common.seller;

public class ShipMethods{
    private String premium;

    private String ground;

    private String expedited;

    public String getPremium (){
        return premium;
    }

    public void setPremium (String premium){
        this.premium = premium;
    }

    public String getGround (){
        return ground;
    }

    public void setGround (String ground){
        this.ground = ground;
    }

    public String getExpedited (){
        return expedited;
    }

    public void setExpedited (String expedited){
        this.expedited = expedited;
    }

    @Override
    public String toString(){
        return "ClassPojo [premium = "+premium+", ground = "+ground+", expedited = "+expedited+"]";
    }
}