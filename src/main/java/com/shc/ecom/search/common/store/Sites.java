package com.shc.ecom.search.common.store;

import com.shc.ecom.gb.doc.unit.StrAttrDetails;

public class Sites {
	private StrAttrDetails sears;
	private StrAttrDetails kmart;
	private StrAttrDetails puertorico;
	private StrAttrDetails mygofer;
	
	
	public StrAttrDetails getStrAttrDetailsForStore(String storeName) {
		switch (storeName.toLowerCase()) {
		case "sears":
			return sears;
		case "kmart":
			return kmart;
		case "searspr":
			return puertorico;
		case "mygofer":
			return mygofer;
		}
		return new StrAttrDetails();
	}
	/**
	 * @return the sears
	 */
	public StrAttrDetails getSears() {
		if(sears==null) {
			sears = new StrAttrDetails();
		}
		return sears;
	}
	/**
	 * @param sears the sears to set
	 */
	public void setSears(StrAttrDetails sears) {
		this.sears = sears;
	}
	/**
	 * @return the kmart
	 */
	public StrAttrDetails getKmart() {
		if(kmart==null) {
			kmart = new StrAttrDetails();
		}
		return kmart;
	}
	/**
	 * @param kmart the kmart to set
	 */
	public void setKmart(StrAttrDetails kmart) {
		this.kmart = kmart;
	}
	/**
	 * @return the puertorico
	 */
	public StrAttrDetails getPuertorico() {
		if(puertorico==null) {
			puertorico = new StrAttrDetails();
		}
		return puertorico;
	}
	/**
	 * @param puertorico the puertorico to set
	 */
	public void setPuertorico(StrAttrDetails puertorico) {
		this.puertorico = puertorico;
	}
	/**
	 * @return the mygofer
	 */
	public StrAttrDetails getMygofer() {
		if(mygofer==null) {
			mygofer = new StrAttrDetails();
		}
		return mygofer;
	}
	/**
	 * @param mygofer the mygofer to set
	 */
	public void setMygofer(StrAttrDetails mygofer) {
		this.mygofer = mygofer;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Sites [sears=");
		builder.append(sears);
		builder.append(", kmart=");
		builder.append(kmart);
		builder.append(", puertorico=");
		builder.append(puertorico);
		builder.append(", mygofer=");
		builder.append(mygofer);
		builder.append("]");
		return builder.toString();
	}
}
