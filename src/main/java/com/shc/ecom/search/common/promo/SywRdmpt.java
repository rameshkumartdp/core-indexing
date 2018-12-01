package com.shc.ecom.search.common.promo;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Shop Your Way Redemption type promotion
 *
 * @author vsingh8
 */
public class SywRdmpt implements TypePromo<SywRdmpt>, Serializable {

    private static final long serialVersionUID = -3273338087980788059L;

    private String promoId;
    private String promoType;
    private String grpName;
    private String freePromoFlag;
    private String endDt;
    private String startDt;

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

    @Override
    public SywRdmpt get(SywRdmpt promo) {
        this.promoId = promo.promoId;
        this.promoType = promo.promoType;
        this.grpName = promo.grpName;
        this.freePromoFlag = promo.freePromoFlag;
        this.endDt = promo.endDt;
        this.startDt = promo.startDt;
        return this;
    }

}
