package com.shc.ecom.search.common.store;

public class State {
	private String nm;

	private String cd;

	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	@Override
	public String toString() {
		return "ClassPojo [nm = " + nm + ", cd = " + cd + "]";
	}
}