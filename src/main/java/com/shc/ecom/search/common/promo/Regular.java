package com.shc.ecom.search.common.promo;

import com.shc.ecom.search.extract.components.promo.PromoRegular;
import com.shc.ecom.search.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Regular type of promotion
 *
 * @author vsingh8
 */
public class Regular implements TypePromo<Regular>, Serializable {

    private static final long serialVersionUID = -296120905012123372L;
    private String promoId;
    private String promoType;
    private String grpName;
    private boolean freePromoFlag;
    private String startDt;
    private String endDt;
    private String shipInfo;
    private String minPurchaseVal;
    private String discType;
    private String discAmt;
    private String thresholdCond;

    /**
     * @param regular
     * @return Regular
     */
    public Regular get(Regular regular) {
        this.promoId = regular.promoId;
        this.promoType = regular.promoType;
        this.grpName = regular.grpName;
        this.freePromoFlag = regular.freePromoFlag;
        this.startDt = regular.startDt;
        this.endDt = regular.endDt;
        this.shipInfo = regular.shipInfo;
        this.minPurchaseVal = regular.minPurchaseVal;
        this.discType = regular.discType;
        this.discAmt = regular.discAmt;
        this.thresholdCond = regular.thresholdCond;
        return this;
    }

    public String getShipInfo() {
        return shipInfo;
    }

    public void setShipInfo(String shipInfo) {
        this.shipInfo = shipInfo;
    }

    @Override
    public String getPromoId() {
        if (promoId == null) {
            promoId = StringUtils.EMPTY;
        }
        return promoId;
    }

    public void setPromoId(String promoId) {
        this.promoId = promoId;
    }

    public String getPromoType() {
        if (promoType == null) {
            promoType = StringUtils.EMPTY;
        }
        return promoType;
    }

    public void setPromoType(String promoType) {
        this.promoType = promoType;
    }

    public String getGrpName() {
        if (grpName == null) {
            grpName = StringUtils.EMPTY;
        }
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public boolean getFreePromoFlag() {
        return freePromoFlag;
    }

    public void setFreePromoFlag(boolean freePromoFlag) {
        this.freePromoFlag = freePromoFlag;
    }

    @Override
    public String getStartDt() {
        if (startDt == null) {
            startDt = StringUtils.EMPTY;
        }

        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    @Override
    public String getEndDt() {
        if (endDt == null) {
            endDt = StringUtils.EMPTY;
        }
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public String getMinPurchaseVal() {
        if (minPurchaseVal == null) {
            minPurchaseVal = StringUtils.EMPTY;
        }

        return minPurchaseVal;
    }

    public void setMinPurchaseVal(String minPurchaseVal) {
        this.minPurchaseVal = minPurchaseVal;
    }

    public String getDiscType() {
        if (discType == null) {
            discType = StringUtils.EMPTY;
        }

        return discType;
    }

    public void setDiscType(String discType) {
        this.discType = discType;
    }

    public String getDiscAmt() {
        if (discAmt == null) {
            discAmt = StringUtils.EMPTY;
        }

        return discAmt;
    }

    public void setDiscAmt(String discAmt) {
        this.discAmt = discAmt;
    }

    public String getThresholdCond() {
        if (thresholdCond == null) {
            thresholdCond = StringUtils.EMPTY;
        }

        return thresholdCond;
    }

    public void setThresholdCond(String thresholdCond) {
        this.thresholdCond = thresholdCond;
    }

    /**
     * Return list of PromoRegular promotions from list of Regular type promotions
     *
     * @param regularPromoList list of Regular type promotion
     * @return list of PromoRegular
     */
    public List<PromoRegular> getPromoRegularList(List<Regular> regularPromoList) {
        List<PromoRegular> promoRegulars = new ArrayList<>();
        for (Regular regular : regularPromoList) {
            PromoRegular promo = new PromoRegular();
            promo.setPromoId(regular.getPromoId());
            promo.setDiscAmt(regular.getDiscAmt());
            promo.setDiscType(regular.getDiscType());
            promo.setEndDt(DateUtil.getLocalDate(regular.getEndDt()));
            promo.setFreePromoFlag(regular.getFreePromoFlag());
            promo.setGrpName(regular.getGrpName());
            promo.setMinPurchaseVal(regular.getMinPurchaseVal());
            promo.setPromoId(regular.getPromoId());
            promo.setPromoType(regular.getPromoType());
            promo.setShipInfo(regular.getShipInfo());
            promo.setStartDt(DateUtil.getLocalDate(regular.getStartDt()));
            promo.setThresholdCond(regular.getThresholdCond());
            promoRegulars.add(promo);
        }
        return promoRegulars;
    }
}
