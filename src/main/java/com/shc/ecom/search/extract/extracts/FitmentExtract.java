package com.shc.ecom.search.extract.extracts;

import com.shc.ecom.search.extract.components.fitments.AutomotiveOffer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rgopala
 */
public class FitmentExtract implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 5915497817179130469L;
    private List<AutomotiveOffer> automotiveOffers;

    public List<AutomotiveOffer> getAutomotiveOffers() {
        if (automotiveOffers == null) {
            automotiveOffers = new ArrayList<>();
        }
        return automotiveOffers;
    }

    public void setAutomotiveOffers(List<AutomotiveOffer> automotiveOffers) {
        this.automotiveOffers = automotiveOffers;
    }


}
