package com.shc.ecom.search.common.seller;

public class CumulatShipcharge {
    private String min;

    private String charge;

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    @Override
    public String toString() {
        return "ClassPojo [min = " + min + ", charge = " + charge + "]";
    }
}