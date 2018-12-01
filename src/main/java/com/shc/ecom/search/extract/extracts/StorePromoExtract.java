package com.shc.ecom.search.extract.extracts;

import com.shc.ecom.search.extract.components.promo.PromoRegular;
import com.shc.ecom.search.extract.components.promo.PromoSywMbr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StorePromoExtract {

    private Set<String> promoIds;

    private List<String> regularPromos = new ArrayList<>();

    private boolean freeShipping;
    private boolean freeDelivery;
    private boolean isCompanion;
    private boolean isBuyOneGetOne;
    private String delayInMonths;
    private List<PromoRegular> promoRegulars;
    private List<PromoSywMbr> promoSywMbr;

    public boolean isBuyOneGetOne() {
        return isBuyOneGetOne;
    }

    public void setBuyOneGetOne(boolean isBuyOneGetOne) {
        this.isBuyOneGetOne = isBuyOneGetOne;
    }

    public boolean isCompanion() {
        return isCompanion;
    }

    public void setCompanion(boolean isCompanion) {
        this.isCompanion = isCompanion;
    }

    public boolean isFreeDelivery() {
        return freeDelivery;
    }

    public void setFreeDelivery(boolean freeDelivery) {
        this.freeDelivery = freeDelivery;
    }

    public Set<String> getPromoIds() {
        if (promoIds == null) {
            promoIds = new HashSet<>();
        }
        return promoIds;
    }

    public void setPromoIds(Set<String> promoIds) {
        this.promoIds = promoIds;
    }

    public List<String> getRegularPromos() {
        if (regularPromos == null) {
            regularPromos = new ArrayList<>();
        }
        return regularPromos;
    }

    public void setRegularPromos(List<String> regularPromos) {
        this.regularPromos = regularPromos;
    }

    public boolean isFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public String getDelayInMonths() {
        return delayInMonths;
    }

    public void setDelayInMonths(String delayInMonths) {
        this.delayInMonths = delayInMonths;
    }

    public List<PromoRegular> getPromoRegulars() {
        if (promoRegulars == null) {
            promoRegulars = new ArrayList<>();
        }
        return promoRegulars;
    }

    public void setPromoRegulars(List<PromoRegular> promoRegulars) {
        this.promoRegulars = promoRegulars;
    }

    public List<PromoSywMbr> getPromoSywMbr() {
        if (promoSywMbr == null) {
            promoSywMbr = new ArrayList<>();
        }

        return promoSywMbr;
    }

    public void setPromoSywMbr(List<PromoSywMbr> promoSywMbr) {
        this.promoSywMbr = promoSywMbr;
    }
}
