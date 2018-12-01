package com.shc.ecom.search.common.seller;

import java.io.Serializable;

/**
 * @author pchauha
 */
public class SellerDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -708966375929632487L;

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