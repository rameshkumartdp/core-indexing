package com.shc.ecom.search.common.localad;

import java.io.Serializable;

public class LocalAdPageDomainObject implements Serializable {
    private String _id;
    private String _bucket;
    private Blob _blob;

    public String getId() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_bucket() {
        return _bucket;
    }

    public void set_bucket(String _bucket) {
        this._bucket = _bucket;
    }

    public Blob get_blob() {
        return _blob;
    }

    public void set_blob(Blob _blob) {
        this._blob = _blob;
    }

}
