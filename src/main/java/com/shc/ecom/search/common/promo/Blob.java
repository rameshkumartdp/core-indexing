package com.shc.ecom.search.common.promo;

import java.io.Serializable;

/**
 * @author pchauha
 */
public class Blob implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1962642519518365792L;

    private PromoRel promorel;

    public PromoRel getPromorel() {
        if (promorel == null) {
            return new PromoRel();
        }
        return promorel;
    }

    public void setPromorel(PromoRel promorel) {
        this.promorel = promorel;
    }

}
