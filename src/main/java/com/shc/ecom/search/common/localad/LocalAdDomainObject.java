package com.shc.ecom.search.common.localad;

import java.io.Serializable;


public class LocalAdDomainObject implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5845335439163240930L;
	private String _id;
    private String _bucket;
    private Search _search;
    private LocalAdsGBCollectionBlob _blob;

    public String get_id() {
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

    public Search get_search() {
        return _search;
    }

    public void set_search(Search _search) {
        this._search = _search;
    }

    public LocalAdsGBCollectionBlob get_blob() {
        return _blob;
    }

    public void set_blob(LocalAdsGBCollectionBlob _blob) {
        this._blob = _blob;
    }
}
