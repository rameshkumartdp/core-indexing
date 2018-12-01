package com.shc.ecom.search.common.seller;

import java.io.Serializable;

public class Programs implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4548347826852203399L;

    private Fbm fbm;

    public Fbm getFbm() {
        if (fbm == null) {
            return new Fbm();
        }
        return fbm;
    }

    public void setFbm(Fbm fbm) {
        this.fbm = fbm;
    }

}
