package com.shc.ecom.search.common.promo;

import java.util.ArrayList;
import java.util.List;

/**
 * Financing type of promotion
 *
 * @author vsingh8
 */
public class Financing implements TypePromo<Financing> {

    private String promoId;
    private String grpName;
    private String title;
    private String desc;
    private String minPurchaseAmt;
    private String status;
    private String endDt;
    private String startDt;
    private String delayInMonths;
    private String promoType;

    public String getPromoId() {
        return promoId;
    }

    public void setPromoId(String promoId) {
        this.promoId = promoId;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMinPurchaseAmt() {
        return minPurchaseAmt;
    }

    public void setMinPurchaseAmt(String minPurchaseAmt) {
        this.minPurchaseAmt = minPurchaseAmt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    @Override
    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getDelayInMonths() {
        return delayInMonths;
    }

    public void setDelayInMonths(String delayInMonths) {
        this.delayInMonths = delayInMonths;
    }

    public String getPromoType() {
        return promoType;
    }

    public void setPromoType(String promoType) {
        this.promoType = promoType;
    }

    @Override
    public String toString() {
        return "ClassPojo [" + "promoId = " + promoId + ", grpName = " + grpName + ", title = " + title + ", desc = " + desc
                + ", minPurchaseAmt = " + minPurchaseAmt + ", status = " + status + ", endDt = " + endDt + ", startDt = " + startDt
                + ", delayInMonths = " + delayInMonths + ", promoType = " + promoType + "]";
    }

    @Override
    public Financing get(Financing promo) {
        this.promoId = promo.promoId;
        this.grpName = promo.grpName;
        this.title = promo.title;
        this.desc = promo.desc;
        this.minPurchaseAmt = promo.minPurchaseAmt;
        this.status = promo.status;
        this.endDt = promo.endDt;
        this.startDt = promo.startDt;
        this.delayInMonths = promo.delayInMonths;
        this.promoType = promo.promoType;
        return this;
    }

    /**
     * Get list of delay in months from list of financing promotions.
     *
     * @param financingList
     * @return
     */
    public List<String> getDelayInMonthsList(List<Financing> financingList) {
        List<String> delayInMonthsSet = new ArrayList<>();
        for (Financing financing : financingList) {
            delayInMonthsSet.add(financing.getDelayInMonths());
        }
        return delayInMonthsSet;
    }
}
