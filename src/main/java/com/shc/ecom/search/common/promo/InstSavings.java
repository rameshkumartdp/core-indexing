package com.shc.ecom.search.common.promo;

/**
 * Instant Saving type of promotion
 *
 * @author vsingh8
 */
public class InstSavings implements TypePromo<InstSavings> {

    private String promoType;
    private String minPurchaseVal;
    private String endDt;
    private String grpName;
    private String title;
    private String discAmt;
    private String freePromoFlag;
    private String discType;
    private String startDt;
    private String rank;
    private String promoCode;
    private String details;
    private String promoId;
    private String thresholdCond;
    private String status;

    public String getPromoType() {
        return promoType;
    }

    public void setPromoType(String promoType) {
        this.promoType = promoType;
    }

    public String getMinPurchaseVal() {
        return minPurchaseVal;
    }

    public void setMinPurchaseVal(String minPurchaseVal) {
        this.minPurchaseVal = minPurchaseVal;
    }

    @Override
    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscAmt() {
        return discAmt;
    }

    public void setDiscAmt(String discAmt) {
        this.discAmt = discAmt;
    }

    public String getFreePromoFlag() {
        return freePromoFlag;
    }

    public void setFreePromoFlag(String freePromoFlag) {
        this.freePromoFlag = freePromoFlag;
    }

    public String getDiscType() {
        return discType;
    }

    public void setDiscType(String discType) {
        this.discType = discType;
    }

    @Override
    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPromoId() {
        return promoId;
    }

    public void setPromoId(String promoId) {
        this.promoId = promoId;
    }

    public String getThresholdCond() {
        return thresholdCond;
    }

    public void setThresholdCond(String thresholdCond) {
        this.thresholdCond = thresholdCond;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public InstSavings get(InstSavings promo) {
        this.promoType = promo.promoType;
        this.minPurchaseVal = promo.minPurchaseVal;
        this.endDt = promo.endDt;
        this.grpName = promo.grpName;
        this.title = promo.title;
        this.discAmt = promo.discAmt;
        this.freePromoFlag = promo.freePromoFlag;
        this.discType = promo.discType;
        this.startDt = promo.startDt;
        this.rank = promo.rank;
        this.promoCode = promo.promoCode;
        this.details = promo.details;
        this.promoId = promo.promoId;
        this.thresholdCond = promo.thresholdCond;
        this.status = promo.status;
        return this;
    }
}
