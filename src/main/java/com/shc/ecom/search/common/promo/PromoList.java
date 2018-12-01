package com.shc.ecom.search.common.promo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pchauha
 */
public class PromoList implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2499022688409304813L;

    private List<Misc> misc;
    private List<Regular> regular;
    private List<SywMax> sywMax;
    private List<SywMbr> sywMbr;
    private List<SywRdmpt> sywRdmpt;
    private List<Financing> financing;
    private List<InstSavings> instSavings;

    public List<Financing> getFinancing() {
        if (financing == null) {
            financing = new ArrayList<>();
        }
        return financing;
    }

    public void setFinancing(List<Financing> financing) {
        this.financing = financing;
    }

    public List<InstSavings> getInstSavings() {

        if (instSavings == null) {
            instSavings = new ArrayList<>();
        }
        return instSavings;
    }

    public void setInstSavings(List<InstSavings> instSavings) {
        this.instSavings = instSavings;
    }

    public List<Misc> getMisc() {
        if (misc == null) {
            misc = new ArrayList<>();
        }
        return misc;
    }

    public void setMisc(List<Misc> misc) {
        this.misc = misc;
    }

    public List<Regular> getRegular() {
        if (regular == null) {
            regular = new ArrayList<>();
        }
        return regular;
    }

    public void setRegular(List<Regular> regular) {
        this.regular = regular;
    }

    public List<SywMax> getSywMax() {
        if (sywMax == null) {
            sywMax = new ArrayList<>();
        }
        return sywMax;
    }

    public void setSywMax(List<SywMax> sywMax) {
        this.sywMax = sywMax;
    }

    public List<SywMbr> getSywMbr() {
        if (sywMbr == null) {
            sywMbr = new ArrayList<>();
        }
        return sywMbr;
    }

    public void setSywMbr(List<SywMbr> sywMbr) {
        this.sywMbr = sywMbr;
    }

    public List<SywRdmpt> getSywRdmpt() {
        if (sywRdmpt == null) {
            sywRdmpt = new ArrayList<>();
        }
        return sywRdmpt;
    }

    public void setSywRdmpt(List<SywRdmpt> sywRdmpt) {
        this.sywRdmpt = sywRdmpt;
    }

}
