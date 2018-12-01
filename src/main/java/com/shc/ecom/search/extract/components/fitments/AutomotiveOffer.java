package com.shc.ecom.search.extract.components.fitments;

import com.shc.ecom.search.common.fitment.FitmentDTO;

public class AutomotiveOffer {

    private String offerId;

    private String uid;

    private String brandCodeId;

    private String mfrPartno;

    private FitmentDTO fitmentDTO;

    public AutomotiveOffer offerId(String offerId) {
        this.offerId = offerId;
        return this;
    }

    public AutomotiveOffer uid(String uid) {
        this.uid = uid;
        return this;
    }

    public AutomotiveOffer brandCodeId(String brandCodeId) {
        this.brandCodeId = brandCodeId;
        return this;
    }

    public AutomotiveOffer mfrPartNo(String mfrPartNo) {
        this.mfrPartno = mfrPartNo;
        return this;
    }

    public AutomotiveOffer fitmentDTO(FitmentDTO fitmentDTO) {
        this.fitmentDTO = fitmentDTO;
        return this;
    }


    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBrandCodeId() {
        return brandCodeId;
    }

    public void setBrandCodeId(String brandCodeId) {
        this.brandCodeId = brandCodeId;
    }

    public String getMfrPartno() {
        return mfrPartno;
    }

    public void setMfrPartno(String mfrPartno) {
        this.mfrPartno = mfrPartno;
    }

    public FitmentDTO getFitmentDTO() {
        if (fitmentDTO == null) {
            fitmentDTO = new FitmentDTO();
        }
        return fitmentDTO;
    }

    public void setFitmentDTO(FitmentDTO fitmentDTO) {
        this.fitmentDTO = fitmentDTO;
    }

}
