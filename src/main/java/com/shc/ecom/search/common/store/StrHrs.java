package com.shc.ecom.search.common.store;

import com.shc.ecom.gb.doc.unit.OpenCloseTime;

public class StrHrs {
	private OpenCloseTime mon;
	private OpenCloseTime tue;
	private OpenCloseTime wed;
	private OpenCloseTime thu;
	private OpenCloseTime fri;
	private OpenCloseTime sat;
	private OpenCloseTime sun;
	private OpenCloseTime wkdy;

	public OpenCloseTime getMon() {
		if(mon==null) {
			mon = new OpenCloseTime();
		}
		return mon;
	}

	public void setMon(OpenCloseTime mon) {
		this.mon = mon;
	}

	public OpenCloseTime getTue() {
		if(tue==null) {
			tue = new OpenCloseTime();
		}
		return tue;
	}

	public void setTue(OpenCloseTime tue) {
		this.tue = tue;
	}

	public OpenCloseTime getWed() {
		if(wed==null) {
			wed = new OpenCloseTime();
		}
		return wed;
	}

	public void setWed(OpenCloseTime wed) {
		this.wed = wed;
	}

	public OpenCloseTime getThu() {
		if(thu==null) {
			thu = new OpenCloseTime();
		}
		return thu;
	}

	public void setThu(OpenCloseTime thu) {
		this.thu = thu;
	}

	public OpenCloseTime getFri() {
		if(fri==null) {
			fri = new OpenCloseTime();
		}
		return fri;
	}

	public void setFri(OpenCloseTime fri) {
		this.fri = fri;
	}

	public OpenCloseTime getSat() {
		if(sat==null) {
			sat = new OpenCloseTime();
		}
		return sat;
	}

	public void setSat(OpenCloseTime sat) {
		this.sat = sat;
	}

	public OpenCloseTime getSun() {
		if(sun==null) {
			sun = new OpenCloseTime();
		}
		return sun;
	}

	public void setSun(OpenCloseTime sun) {
		this.sun = sun;
	}

	public OpenCloseTime getWkdy() {
		if(wkdy==null) {
			wkdy = new OpenCloseTime();
		}
		return wkdy;
	}

	public void setWkdy(OpenCloseTime wkdy) {
		this.wkdy = wkdy;
	}

	@Override
	public String toString() {
		return "StrHrs [thu = " + thu + ", sat = " + sat + ", wed = " + wed + ", mon = " + mon + ", wkdy = " + wkdy
				+ ", tue = " + tue + ", sun = " + sun + ", fri = " + fri + "]";
	}
}
