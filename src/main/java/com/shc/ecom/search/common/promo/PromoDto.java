package com.shc.ecom.search.common.promo;

import java.io.Serializable;

/**
 * @author pchauha
 */
public class PromoDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2010248475859035362L;

    private Blob _blob;

    public Blob get_blob() {
        if (_blob == null) {
            return new Blob();
        }
        return _blob;
    }

    public void set_blob(Blob _blob) {
        this._blob = _blob;
    }

}
