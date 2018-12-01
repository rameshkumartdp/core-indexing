package com.shc.ecom.search.common.store;

import java.util.ArrayList;
import java.util.List;

import com.shc.ecom.gb.doc.unit.Break;

public class OpenCloseTime {

	private int cls;
	private int opn;
	private List<Break> brk;
	

	/**
	 * @return the brk
	 */
	public List<Break> getBrk() {
		if(brk==null) {
			brk = new ArrayList<>();
		}
		return brk;
	}
	/**
	 * @param brk the brk to set
	 */
	public void setBrk(List<Break> brk) {
		this.brk = brk;
	}
	/**
	 * @return the cls
	 */
	public int getCls() {
		return cls;
	}
	/**
	 * @param cls the cls to set
	 */
	public void setCls(int cls) {
		this.cls = cls;
	}
	/**
	 * @return the opn
	 */
	public int getOpn() {
		return opn;
	}
	/**
	 * @param opn the opn to set
	 */
	public void setOpn(int opn) {
		this.opn = opn;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OpenCloseTime [cls=");
		builder.append(cls);
		builder.append(", opn=");
		builder.append(opn);
		builder.append(", brk=");
		builder.append(brk);
		builder.append("]");
		return builder.toString();
	}
}
