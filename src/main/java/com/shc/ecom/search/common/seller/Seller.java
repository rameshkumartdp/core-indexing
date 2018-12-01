package com.shc.ecom.search.common.seller;

import java.io.Serializable;

public class Seller implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -708966375929632487L;

    private Programs programs;
    private String sellerTier;

    private StoreFront storeFront;

    public StoreFront getStoreFront() {
        if (storeFront == null) {
            return new StoreFront();
        }
        return storeFront;
    }

    public void setStoreFront(StoreFront storeFront) {
        this.storeFront = storeFront;
    }

    public Programs getPrograms() {
        if (programs == null) {
            return new Programs();
        }
        return programs;
    }

    public void setPrograms(Programs programs) {
        this.programs = programs;
    }

    public String getSellerTier() {
        return sellerTier;
    }

    public void setSellerTier(String sellerTier) {
        this.sellerTier = sellerTier;
    }
}
