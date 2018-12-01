package com.shc.ecom.search.common.promo;

import com.shc.ecom.search.extract.components.promo.PromoSywMbr;
import com.shc.ecom.search.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ShopYourMember promotion type
 *
 * @author vsingh8
 */
public class SywMbr implements TypePromo<SywMbr>, Serializable {

    private static final long serialVersionUID = 3798081655627877972L;

    private String promoId;
    private String promoType;
    private String grpName;
    private String freePromoFlag;
    private String thresholdCond;
    private String startDt;
    private String endDt;
    private String shipInfo;
    private String minPurchaseVal;
    private String discType;
    private String discAmt;

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

    public String getFreePromoFlag() {
        if (freePromoFlag == null) {
            freePromoFlag = StringUtils.EMPTY;
        }
        return freePromoFlag;
    }

    public void setFreePromoFlag(String freePromoFlag) {
        this.freePromoFlag = freePromoFlag;
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

    @Override
    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    @Override
    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public String getShipInfo() {
        if (shipInfo == null) {
            shipInfo = StringUtils.EMPTY;
        }
        return shipInfo;
    }

    public void setShipInfo(String shipInfo) {
        this.shipInfo = shipInfo;
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

    @Override
    public SywMbr get(SywMbr promo) {
        this.promoId = promo.promoId;
        this.promoType = promo.promoType;
        this.grpName = promo.grpName;
        this.freePromoFlag = promo.freePromoFlag;
        this.thresholdCond = promo.thresholdCond;
        this.startDt = promo.startDt;
        this.endDt = promo.endDt;
        this.shipInfo = promo.shipInfo;
        this.minPurchaseVal = promo.minPurchaseVal;
        this.discType = promo.discType;
        this.discAmt = promo.discAmt;
        return this;
    }

    /**
     * Returns list of PromoSywMbr from list of syswMbrList promotion type
     *
     * @param syswMbrList
     * @return list of PromoSywMbr
     */
    public List<PromoSywMbr> getPromoSywMbrList(List<SywMbr> syswMbrList) {

        List<PromoSywMbr> promoSywMbr = new ArrayList<>();
        for (SywMbr sywMbr : syswMbrList) {
            PromoSywMbr promo = new PromoSywMbr();
            promo.setPromoType(sywMbr.getPromoType());
            promo.setPromoId(sywMbr.getPromoId());
            promo.setGrpName(sywMbr.getGrpName());
            promo.setShipInfo(sywMbr.getShipInfo());
            promo.setMinPurchaseVal(sywMbr.getMinPurchaseVal());
            promo.setDiscType(sywMbr.getDiscType());
            promo.setDiscAmt(sywMbr.getDiscAmt());
            promo.setThresholdCond(sywMbr.getThresholdCond());
            promo.setStartDt(DateUtil.getLocalDate(sywMbr.getStartDt()));
            promo.setEndDt(DateUtil.getLocalDate(sywMbr.getEndDt()));
            promoSywMbr.add(promo);
        }
        return promoSywMbr;
    }
}
