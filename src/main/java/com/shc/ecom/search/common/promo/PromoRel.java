package com.shc.ecom.search.common.promo;

import java.io.Serializable;

/**
 * @author pchauha
 */
public class PromoRel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8845464229867192894L;

    private PromoList promoList;

    public PromoList getPromoList() {
        if (promoList == null) {
            return new PromoList();
        }
        return promoList;
    }

    public void setPromoList(PromoList promoList) {
        this.promoList = promoList;
    }

}
