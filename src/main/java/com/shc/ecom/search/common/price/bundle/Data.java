package com.shc.ecom.search.common.price.bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rgopala
 */
public class Data {
    private OldPrice oldPrice;

    private String status;

    private String priceType;

    private Savings savings;

    private List<Indicators> indicators;

    private MemberPrice memberPrice;

    private DisplayPrice displayPrice;

    private String memberStatus;

    private String storeId;

    public OldPrice getOldPrice() {
        if (oldPrice == null) {
            oldPrice = new OldPrice();
        }
        return oldPrice;
    }

    public void setOldPrice(OldPrice oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public Savings getSavings() {
        if (savings == null) {
            savings = new Savings();
        }
        return savings;
    }

    public void setSavings(Savings savings) {
        this.savings = savings;
    }

    public List<Indicators> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<Indicators> indicators) {
        if (indicators == null) {
            indicators = new ArrayList<>();
        }
        this.indicators = indicators;
    }

    public MemberPrice getMemberPrice() {
        if (memberPrice == null) {
            memberPrice = new MemberPrice();
        }
        return memberPrice;
    }

    public void setMemberPrice(MemberPrice memberPrice) {
        this.memberPrice = memberPrice;
    }

    public DisplayPrice getDisplayPrice() {
        if (displayPrice == null) {
            displayPrice = new DisplayPrice();
        }
        return displayPrice;
    }

    public void setDisplayPrice(DisplayPrice displayPrice) {
        this.displayPrice = displayPrice;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "ClassPojo [oldPrice = " + oldPrice + ", status = " + status + ", priceType = " + priceType + ", savings = " + savings + ", indicators = " + indicators + ", memberPrice = " + memberPrice + ", displayPrice = " + displayPrice + ", memberStatus = " + memberStatus + ", storeId = " + storeId + "]";
    }
}
