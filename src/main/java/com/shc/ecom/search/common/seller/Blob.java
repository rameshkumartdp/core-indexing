package com.shc.ecom.search.common.seller;

import java.io.Serializable;

/**
 * @author pchauha
 */
public class Blob implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3317539062105613816L;

    private Seller seller;

    public Seller getSeller() {
        if (seller == null) {
            return new Seller();
        }
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

}
