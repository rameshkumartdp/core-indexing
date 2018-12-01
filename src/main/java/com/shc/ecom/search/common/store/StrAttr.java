package com.shc.ecom.search.common.store;

public class StrAttr {
	private Sites sites;

	public Sites getSites() {
		if(sites==null) {
			sites = new Sites();
		}
		return sites;
	}

	public void setSites(Sites sites) {
		this.sites = sites;
	}

	@Override
	public String toString() {
		return "StrAttr [sites = " + sites + "]";
	}
}