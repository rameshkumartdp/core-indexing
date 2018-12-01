package com.shc.ecom.search.common.seller;

import java.util.ArrayList;
import java.util.List;

public class Mailable{
    private CumulatShipcharge cumulatShipcharge;

    private String maxLength;

    private List<WeightRange> weightRange;

    public CumulatShipcharge getCumulatShipcharge (){
        return cumulatShipcharge;
    }

    public void setCumulatShipcharge (CumulatShipcharge cumulatShipcharge){
        this.cumulatShipcharge = cumulatShipcharge;
    }

    public String getMaxLength (){
        return maxLength;
    }

    public void setMaxLength (String maxLength){
        this.maxLength = maxLength;
    }

    public List<WeightRange> getWeightRange (){
    	if(weightRange == null) {
    		weightRange = new ArrayList<>();
    	}
        return weightRange;
    }

    public void setWeightRange (List<WeightRange> weightRange){
        this.weightRange = weightRange;
    }

    @Override
    public String toString(){
        return "ClassPojo [cumulatShipcharge = "+cumulatShipcharge+", maxLength = "+maxLength+", weightRange = "+weightRange+"]";
    }
}
