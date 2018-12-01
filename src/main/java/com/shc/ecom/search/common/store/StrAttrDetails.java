package com.shc.ecom.search.common.store;

import java.io.Serializable;
import java.util.List;

public class StrAttrDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5575074575190177934L;
	private boolean carryIn;
	private boolean cityLmt;
	private boolean consoldn;	
	private boolean curbSidePkup;
	private boolean extWrhs;	
	private boolean giftReg;
	private String hasVan;
	private boolean isPharm;	
	private String localDelv;	
	private boolean multiPickUp;	
	private boolean payInStr;	
	private boolean pharmLd;	
	private boolean pickUp;	
	private Boolean mdoPickUp;	
	private boolean receiveWrhs;	
	private boolean resPay;
	private boolean reserveIt;	
	private boolean sfsElig;
	private boolean shipElig;	
	private boolean stsElig;
	private boolean auto;	
	private boolean localAd;
	private boolean stsXfmt;
	private String xfmtDDC;
	private String xrefStr;
	private boolean meetExpertElig;
	private List<String> meetExpertHier;
	
	/**
	 * @return the xfmtDDC
	 */
	public String getXfmtDDC() {
		return xfmtDDC;
	}
	/**
	 * @param xfmtDDC the xfmtDDC to set
	 */
	public void setXfmtDDC(String xfmtDDC) {
		this.xfmtDDC = xfmtDDC;
	}
	/**
	 * @return the xrefStr
	 */
	public String getXrefStr() {
		return xrefStr;
	}
	/**
	 * @param xrefStr the xrefStr to set
	 */
	public void setXrefStr(String xrefStr) {
		this.xrefStr = xrefStr;
	}
	/**
	 * @return the carryIn
	 */
	public boolean isCarryIn() {
		return carryIn;
	}
	/**
	 * @param carryIn the carryIn to set
	 */
	public void setCarryIn(boolean carryIn) {
		this.carryIn = carryIn;
	}
	/**
	 * @return the cityLmt
	 */
	public boolean isCityLmt() {
		return cityLmt;
	}
	/**
	 * @param cityLmt the cityLmt to set
	 */
	public void setCityLmt(boolean cityLmt) {
		this.cityLmt = cityLmt;
	}
	/**
	 * @return the consoldn
	 */
	public boolean isConsoldn() {
		return consoldn;
	}
	/**
	 * @param consoldn the consoldn to set
	 */
	public void setConsoldn(boolean consoldn) {
		this.consoldn = consoldn;
	}
	/**
	 * @return the curbSidePkup
	 */
	public boolean isCurbSidePkup() {
		return curbSidePkup;
	}
	/**
	 * @param curbSidePkup the curbSidePkup to set
	 */
	public void setCurbSidePkup(boolean curbSidePkup) {
		this.curbSidePkup = curbSidePkup;
	}
	/**
	 * @return the extWrhs
	 */
	public boolean isExtWrhs() {
		return extWrhs;
	}
	/**
	 * @param extWrhs the extWrhs to set
	 */
	public void setExtWrhs(boolean extWrhs) {
		this.extWrhs = extWrhs;
	}
	/**
	 * @return the giftReg
	 */
	public boolean isGiftReg() {
		return giftReg;
	}
	/**
	 * @param giftReg the giftReg to set
	 */
	public void setGiftReg(boolean giftReg) {
		this.giftReg = giftReg;
	}
	/**
	 * @return the hasVan
	 */
	public String getHasVan() {
		return hasVan;
	}
	/**
	 * @param hasVan the hasVan to set
	 */
	public void setHasVan(String hasVan) {
		this.hasVan = hasVan;
	}
	/**
	 * @return the isPharm
	 */
	public boolean isPharm() {
		return isPharm;
	}
	/**
	 * @param isPharm the isPharm to set
	 */
	public void setPharm(boolean isPharm) {
		this.isPharm = isPharm;
	}
	/**
	 * @return the localDelv
	 */
	public String getLocalDelv() {
		return localDelv;
	}
	/**
	 * @param localDelv the localDelv to set
	 */
	public void setLocalDelv(String localDelv) {
		this.localDelv = localDelv;
	}
	/**
	 * @return the multiPickUp
	 */
	public boolean isMultiPickUp() {
		return multiPickUp;
	}
	/**
	 * @param multiPickUp the multiPickUp to set
	 */
	public void setMultiPickUp(boolean multiPickUp) {
		this.multiPickUp = multiPickUp;
	}
	/**
	 * @return the payInStr
	 */
	public boolean isPayInStr() {
		return payInStr;
	}
	/**
	 * @param payInStr the payInStr to set
	 */
	public void setPayInStr(boolean payInStr) {
		this.payInStr = payInStr;
	}
	/**
	 * @return the pharmLd
	 */
	public boolean isPharmLd() {
		return pharmLd;
	}
	/**
	 * @param pharmLd the pharmLd to set
	 */
	public void setPharmLd(boolean pharmLd) {
		this.pharmLd = pharmLd;
	}
	/**
	 * @return the pickUp
	 */
	public boolean isPickUp() {
		return pickUp;
	}
	/**
	 * @param pickUp the pickUp to set
	 */
	public void setPickUp(boolean pickUp) {
		this.pickUp = pickUp;
	}
	/**
	 * @return the receiveWrhs
	 */
	public boolean isReceiveWrhs() {
		return receiveWrhs;
	}
	/**
	 * @param receiveWrhs the receiveWrhs to set
	 */
	public void setReceiveWrhs(boolean receiveWrhs) {
		this.receiveWrhs = receiveWrhs;
	}
	/**
	 * @return the resPay
	 */
	public boolean isResPay() {
		return resPay;
	}
	/**
	 * @param resPay the resPay to set
	 */
	public void setResPay(boolean resPay) {
		this.resPay = resPay;
	}
	/**
	 * @return the reserveIt
	 */
	public boolean isReserveIt() {
		return reserveIt;
	}
	/**
	 * @param reserveIt the reserveIt to set
	 */
	public void setReserveIt(boolean reserveIt) {
		this.reserveIt = reserveIt;
	}
	/**
	 * @return the sfsElig
	 */
	public boolean isSfsElig() {
		return sfsElig;
	}
	/**
	 * @param sfsElig the sfsElig to set
	 */
	public void setSfsElig(boolean sfsElig) {
		this.sfsElig = sfsElig;
	}
	/**
	 * @return the shipElig
	 */
	public boolean isShipElig() {
		return shipElig;
	}
	/**
	 * @param shipElig the shipElig to set
	 */
	public void setShipElig(boolean shipElig) {
		this.shipElig = shipElig;
	}
	/**
	 * @return the stsElig
	 */
	public boolean isStsElig() {
		return stsElig;
	}
	/**
	 * @param stsElig the stsElig to set
	 */
	public void setStsElig(boolean stsElig) {
		this.stsElig = stsElig;
	}
	/**
	 * @return the auto
	 */
	public boolean isAuto() {
		return auto;
	}
	/**
	 * @param auto the auto to set
	 */
	public void setAuto(boolean auto) {
		this.auto = auto;
	}
	/**
	 * @return the localAd
	 */
	public boolean isLocalAd() {
		return localAd;
	}
	/**
	 * @param localAd the localAd to set
	 */
	public void setLocalAd(boolean localAd) {
		this.localAd = localAd;
	}
	/**
	 * @return the stsXfmt
	 */
	public boolean isStsXfmt() {
		return stsXfmt;
	}
	/**
	 * @param stsXfmt the stsXfmt to set
	 */
	public void setStsXfmt(boolean stsXfmt) {
		this.stsXfmt = stsXfmt;
	}
	/**
	 * @return the mdoPickUp
	 */
	public Boolean isMdoPickUp() {
		return mdoPickUp;
	}
	/**
	 * @param mdoPickUp the mdoPickUp to set
	 */
	public void setMdoPickUp(Boolean mdoPickUp) {
		this.mdoPickUp = mdoPickUp;
	}
	/**
	 * @return the meetExpertElig
	 */
	public boolean isMeetExpertElig() {
		return meetExpertElig;
	}
	/**
	 * @param meetExpertElig the meetExpertElig to set
	 */
	public void setMeetExpertElig(boolean meetExpertElig) {
		this.meetExpertElig = meetExpertElig;
	}
	public List<String> getMeetExpertHier() {
		return meetExpertHier;
	}
	public void setMeetExpertHier(List<String> meetExpertHier) {
		this.meetExpertHier = meetExpertHier;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StrAttrDetails [carryIn=");
		builder.append(carryIn);
		builder.append(", cityLmt=");
		builder.append(cityLmt);
		builder.append(", consoldn=");
		builder.append(consoldn);
		builder.append(", curbSidePkup=");
		builder.append(curbSidePkup);
		builder.append(", extWrhs=");
		builder.append(extWrhs);
		builder.append(", giftReg=");
		builder.append(giftReg);
		builder.append(", hasVan=");
		builder.append(hasVan);
		builder.append(", isPharm=");
		builder.append(isPharm);
		builder.append(", localDelv=");
		builder.append(localDelv);
		builder.append(", multiPickUp=");
		builder.append(multiPickUp);
		builder.append(", payInStr=");
		builder.append(payInStr);
		builder.append(", pharmLd=");
		builder.append(pharmLd);
		builder.append(", pickUp=");
		builder.append(pickUp);
		builder.append(", mdoPickUp=");
		builder.append(mdoPickUp);
		builder.append(", receiveWrhs=");
		builder.append(receiveWrhs);
		builder.append(", resPay=");
		builder.append(resPay);
		builder.append(", reserveIt=");
		builder.append(reserveIt);
		builder.append(", sfsElig=");
		builder.append(sfsElig);
		builder.append(", shipElig=");
		builder.append(shipElig);
		builder.append(", stsElig=");
		builder.append(stsElig);
		builder.append(", auto=");
		builder.append(auto);
		builder.append(", localAd=");
		builder.append(localAd);
		builder.append(", stsXfmt=");
		builder.append(stsXfmt);
		builder.append(", xfmtDDC=");
		builder.append(xfmtDDC);
		builder.append(", xrefStr=");
		builder.append(xrefStr);
		builder.append(", meetExpertElig=");
		builder.append(meetExpertElig);
		builder.append(", meetExpertHier=");
		builder.append(meetExpertHier);
		builder.append("]");
		return builder.toString();
	}
	
}

