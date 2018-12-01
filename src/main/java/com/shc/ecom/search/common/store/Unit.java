package com.shc.ecom.search.common.store;

import java.util.ArrayList;
import java.util.List;

public class Unit {
	private StrHrs strHrs;

	private String strClsReason;

	private String ecircStrType;

	private String mgrName;

	private String countyNum;

	private String closeDt;

	private String nickNm;

	private Meta meta;

	private String id;

	private String cutoffTm;

	private String openDt;

	private String ldCuttOffTmn;

	private String longitude;

	private String prepTm;

	private String district;

	private String daySavTmfl;

	private Offset offset;

	private StrAddr strAddr;

	private String geoCde;

	private String stgTmzn;

	private List<String> siteId;

	private String strType2;

	private String strType1;

	private String isIcce;

	private String daySavTmzn;

	private SrchDet srchDet;

	private String latitude;

	private String strStatus;

	private StrAttr strAttr;

	private String nm;

	private String rgn;

	public StrHrs getStrHrs() {
		if (strHrs == null) {
			strHrs = new StrHrs();
		}
		return strHrs;
	}

	public void setStrHrs(StrHrs strHrs) {
		this.strHrs = strHrs;
	}

	public String getStrClsReason() {
		return strClsReason;
	}

	public void setStrClsReason(String strClsReason) {
		this.strClsReason = strClsReason;
	}

	public String getEcircStrType() {
		return ecircStrType;
	}

	public void setEcircStrType(String ecircStrType) {
		this.ecircStrType = ecircStrType;
	}

	public String getMgrName() {
		return mgrName;
	}

	public void setMgrName(String mgrName) {
		this.mgrName = mgrName;
	}

	public String getCountyNum() {
		return countyNum;
	}

	public void setCountyNum(String countyNum) {
		this.countyNum = countyNum;
	}

	public String getCloseDt() {
		return closeDt;
	}

	public void setCloseDt(String closeDt) {
		this.closeDt = closeDt;
	}

	public String getNickNm() {
		return nickNm;
	}

	public void setNickNm(String nickNm) {
		this.nickNm = nickNm;
	}

	public Meta getMeta() {
		if (meta == null) {
			meta = new Meta();
		}
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCutoffTm() {
		return cutoffTm;
	}

	public void setCutoffTm(String cutoffTm) {
		this.cutoffTm = cutoffTm;
	}

	public String getOpenDt() {
		return openDt;
	}

	public void setOpenDt(String openDt) {
		this.openDt = openDt;
	}

	public String getLdCuttOffTmn() {
		return ldCuttOffTmn;
	}

	public void setLdCuttOffTmn(String ldCuttOffTmn) {
		this.ldCuttOffTmn = ldCuttOffTmn;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getPrepTm() {
		return prepTm;
	}

	public void setPrepTm(String prepTm) {
		this.prepTm = prepTm;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getDaySavTmfl() {
		return daySavTmfl;
	}

	public void setDaySavTmfl(String daySavTmfl) {
		this.daySavTmfl = daySavTmfl;
	}

	public Offset getOffset() {
		if (offset == null) {
			offset = new Offset();
		}
		return offset;
	}

	public void setOffset(Offset offset) {
		this.offset = offset;
	}

	public StrAddr getStrAddr() {
		if (strAddr == null) {
			strAddr = new StrAddr();
		}
		return strAddr;
	}

	public void setStrAddr(StrAddr strAddr) {
		this.strAddr = strAddr;
	}

	public String getGeoCde() {
		return geoCde;
	}

	public void setGeoCde(String geoCde) {
		this.geoCde = geoCde;
	}

	public String getStgTmzn() {
		return stgTmzn;
	}

	public void setStgTmzn(String stgTmzn) {
		this.stgTmzn = stgTmzn;
	}

	public List<String> getSiteId() {
		if(siteId==null) {
			siteId = new ArrayList<>();
		}
		return siteId;
	}

	public void setSiteId(List<String> siteId) {
		this.siteId = siteId;
	}

	public String getStrType2() {
		return strType2;
	}

	public void setStrType2(String strType2) {
		this.strType2 = strType2;
	}

	public String getStrType1() {
		return strType1;
	}

	public void setStrType1(String strType1) {
		this.strType1 = strType1;
	}

	public String getIsIcce() {
		return isIcce;
	}

	public void setIsIcce(String isIcce) {
		this.isIcce = isIcce;
	}

	public String getDaySavTmzn() {
		return daySavTmzn;
	}

	public void setDaySavTmzn(String daySavTmzn) {
		this.daySavTmzn = daySavTmzn;
	}

	public SrchDet getSrchDet() {
		if (srchDet == null) {
			srchDet = new SrchDet();
		}
		return srchDet;
	}

	public void setSrchDet(SrchDet srchDet) {
		this.srchDet = srchDet;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getStrStatus() {
		return strStatus;
	}

	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

	public StrAttr getStrAttr() {
		if (strAttr == null) {
			strAttr = new StrAttr();
		}
		return strAttr;
	}

	public void setStrAttr(StrAttr strAttr) {
		this.strAttr = strAttr;
	}

	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}

	public String getRgn() {
		return rgn;
	}

	public void setRgn(String rgn) {
		this.rgn = rgn;
	}

	@Override
	public String toString() {
		return "Unit [strHrs = " + strHrs + ", strClsReason = " + strClsReason + ", ecircStrType = " + ecircStrType
				+ ", mgrName = " + mgrName + ", countyNum = " + countyNum + ", closeDt = " + closeDt + ", nickNm = "
				+ nickNm + ", meta = " + meta + ", id = " + id + ", cutoffTm = " + cutoffTm + ", openDt = " + openDt
				+ ", ldCuttOffTmn = " + ldCuttOffTmn + ", longitude = " + longitude + ", prepTm = " + prepTm
				+ ", district = " + district + ", daySavTmfl = " + daySavTmfl + ", offset = " + offset + ", strAddr = "
				+ strAddr + ", geoCde = " + geoCde + ", stgTmzn = " + stgTmzn + ", siteId = " + siteId + ", strType2 = "
				+ strType2 + ", strType1 = " + strType1 + ", isIcce = " + isIcce + ", daySavTmzn = " + daySavTmzn
				+ ", srchDet = " + srchDet + ", latitude = " + latitude + ", strStatus = " + strStatus + ", strAttr = "
				+ strAttr + ", nm = " + nm + ", rgn = " + rgn + "]";
	}
}