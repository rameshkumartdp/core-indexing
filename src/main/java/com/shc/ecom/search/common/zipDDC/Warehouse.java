package com.shc.ecom.search.common.zipDDC;

import java.io.Serializable;
import java.util.List;

/**
 * VO used by ZipDDCMapping
 * Created by hdargah on 11/2/2016.
 */
public class Warehouse implements Serializable {

    private static final long serialVersionUID = -8576894624489706464L;

    String dcUnit;
    String dcUnitType;
    List<String> zipList;

    public String getDcUnit() {
        return dcUnit;
    }

    public void setDcUnit(String dcUnit) {
        this.dcUnit = dcUnit;
    }

    public String getDcUnitType() {
        return dcUnitType;
    }

    public void setDcUnitType(String dcUnitType) {
        this.dcUnitType = dcUnitType;
    }

    public List<String> getZipList() {
        return zipList;
    }

    public void setZipList(List<String> zipList) {
        this.zipList = zipList;
    }
}
