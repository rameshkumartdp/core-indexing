package com.shc.ecom.search.common.store;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author rgopala
 *
 */
public class Store {
	private String bucket;

	private Blob blob;

	private String id;

	private String tmstmp;

	private Search search;

	@JsonProperty("_bucket")
	public String getBucket() {
		return bucket;
	}

	@JsonProperty("_bucket")
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	@JsonProperty("_blob")
	public Blob getBlob() {
		if(blob==null) {
			blob = new Blob();
		}
		return blob;
	}

	@JsonProperty("_blob")
	public void setBlob(Blob blob) {
		this.blob = blob;
	}

	@JsonProperty("_id")
	public String getId() {
		return id;
	}

	@JsonProperty("_id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("_tmpstmp")
	public String getTmstmp() {
		return tmstmp;
	}

	@JsonProperty("_tmpstmp")
	public void setTmstmp(String tmstmp) {
		this.tmstmp = tmstmp;
	}

	@JsonProperty("_search")
	public Search getSearch() {
		if(search==null) {
			search = new Search();
		}
		return search;
	}

	@JsonProperty("_search")
	public void setSearch(Search search) {
		this.search = search;
	}

	@Override
	public String toString() {
		return "Store [_bucket = " + bucket + ", _blob = " + blob + ", _id = " + id + ", _tmstmp = " + tmstmp
				+ ", _search = " + search + "]";
	}
}
