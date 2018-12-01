package com.shc.ecom.search.common.store;

import java.util.ArrayList;
import java.util.List;

public class StrAddr {
	private List<String> phNo;

	private String addr1;

	private String zipCd;

	private State state;

	private String country;

	private String city;

	public List<String> getPhNo() {
		if(phNo==null) {
			phNo = new ArrayList<>();
		}
		return phNo;
	}

	public void setPhNo(List<String> phNo) {
		this.phNo = phNo;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getZipCd() {
		return zipCd;
	}

	public void setZipCd(String zipCd) {
		this.zipCd = zipCd;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "StrAddr [phNo = " + phNo + ", addr1 = " + addr1 + ", zipCd = " + zipCd + ", state = " + state
				+ ", country = " + country + ", city = " + city + "]";
	}
}