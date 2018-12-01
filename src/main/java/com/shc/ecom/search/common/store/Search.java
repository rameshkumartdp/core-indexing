package com.shc.ecom.search.common.store;

import java.util.ArrayList;
import java.util.List;

public class Search {
	private List<String> siteId;

	private String zipCd;

	private String state;

	private String strType;

	private String strStatus;

	private String district;

	private String rgn;

	public List<String> getSiteId() {
		if(siteId==null) {
			siteId = new ArrayList<>();
		}
		return siteId;
	}

	public void setSiteId(List<String> siteId) {
		this.siteId = siteId;
	}

	public String getZipCd() {
		return zipCd;
	}

	public void setZipCd(String zipCd) {
		this.zipCd = zipCd;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStrType() {
		return strType;
	}

	public void setStrType(String strType) {
		this.strType = strType;
	}

	public String getStrStatus() {
		return strStatus;
	}

	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getRgn() {
		return rgn;
	}

	public void setRgn(String rgn) {
		this.rgn = rgn;
	}

	@Override
	public String toString() {
		return "Search [siteId = " + siteId + ", zipCd = " + zipCd + ", state = " + state + ", strType = " + strType
				+ ", strStatus = " + strStatus + ", district = " + district + ", rgn = " + rgn + "]";
	}
}