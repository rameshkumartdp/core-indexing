package com.shc.ecom.search.extract.components.promo;

import org.joda.time.LocalDate;

public class PromoSywMbr {

    private String promoId;

    private String promoType;

    private String grpName;

    private String shipInfo;

    private String minPurchaseVal;

    private String discType;

    private String discAmt;

    private String thresholdCond;

    private LocalDate startDt;

    private LocalDate endDt;

    public String getPromoId() {
        return promoId;
    }

    public void setPromoId(String promoId) {
        this.promoId = promoId;
    }

    public String getPromoType() {
        return promoType;
    }

    public void setPromoType(String promoType) {
        this.promoType = promoType;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getShipInfo() {
        return shipInfo;
    }

    public void setShipInfo(String shipInfo) {
        this.shipInfo = shipInfo;
    }

    public String getMinPurchaseVal() {
        return minPurchaseVal;
    }

    public void setMinPurchaseVal(String minPurchaseVal) {
        this.minPurchaseVal = minPurchaseVal;
    }

    public String getDiscType() {
        return discType;
    }

    public void setDiscType(String discType) {
        this.discType = discType;
    }

    public String getDiscAmt() {
        return discAmt;
    }

    public void setDiscAmt(String discAmt) {
        this.discAmt = discAmt;
    }

    public String getThresholdCond() {
        return thresholdCond;
    }

    public void setThresholdCond(String thresholdCond) {
        this.thresholdCond = thresholdCond;
    }

    public LocalDate getStartDt() {
        return startDt;
    }

    public void setStartDt(LocalDate startDt) {
        this.startDt = startDt;
    }

    public LocalDate getEndDt() {
        return endDt;
    }

    public void setEndDt(LocalDate endDt) {
        this.endDt = endDt;
    }
}
