package com.shc.ecom.search.common.store;

public class Offset {
	private String std;

	private String opnTm;

	private String daySav;

	public String getStd() {
		return std;
	}

	public void setStd(String std) {
		this.std = std;
	}

	public String getOpnTm() {
		return opnTm;
	}

	public void setOpnTm(String opnTm) {
		this.opnTm = opnTm;
	}

	public String getDaySav() {
		return daySav;
	}

	public void setDaySav(String daySav) {
		this.daySav = daySav;
	}

	@Override
	public String toString() {
		return "Offset [std = " + std + ", opnTm = " + opnTm + ", daySav = " + daySav + "]";
	}
}