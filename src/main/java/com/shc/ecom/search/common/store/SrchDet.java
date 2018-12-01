package com.shc.ecom.search.common.store;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SrchDet {
	private List<String> phNo;

	private String addr;

	public List<String> getPhNo() {
		if(phNo==null) {
			phNo = new ArrayList<>();
		}
		return phNo;
	}

	public void setPhNo(List<String> phNo) {
		this.phNo = phNo;
	}


	@JsonProperty("Addr")
	public String getAddr() {
		return addr;
	}

	@JsonProperty("Addr")
	public void setAddr(String addr) {
		this.addr = addr;
	}

	@Override
	public String toString() {
		return "SrchDet [phNo = " + phNo + ", Addr = " + addr + "]";
	}
}
