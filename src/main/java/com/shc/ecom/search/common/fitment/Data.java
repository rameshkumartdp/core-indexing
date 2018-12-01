package com.shc.ecom.search.common.fitment;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<String> fitment;

    private String partType;

    private String partTypeName;

    private String brand;

    private String part;

    public List<String> getFitment() {
        if (fitment == null) {
            fitment = new ArrayList<>();
        }
        return fitment;
    }

    public void setFitment(List<String> fitment) {
        this.fitment = fitment;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public String getPartTypeName() {
        return partTypeName;
    }

    public void setPartTypeName(String partTypeName) {
        this.partTypeName = partTypeName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    @Override
    public String toString() {
        return "ClassPojo [fitment = " + fitment + ", partType = " + partType + ", partTypeName = " + partTypeName + ", brand = " + brand + ", part = " + part + "]";
    }
}
