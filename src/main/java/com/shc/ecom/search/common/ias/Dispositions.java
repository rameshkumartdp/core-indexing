package com.shc.ecom.search.common.ias;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Dispositions {

    @JsonProperty("urn:disposition:threshold")
    private int urnDispositionThreshold;

    @JsonProperty("urn:disposition:mailcode")
    private int urnDispositionMailcode;

    @JsonProperty("urn:disposition:inbound")
    private int urnDispositionInbound;

    @JsonProperty("urn:disposition:onhand")
    private int urnDispositionOnhand;

    @JsonProperty("urn:disposition:onhand-reserved")
    private int urnDispositionOnhandReserved;

    public int getUrnDispositionThreshold() {
		return urnDispositionThreshold;
	}

	public void setUrnDispositionThreshold(int urnDispositionThreshold) {
		this.urnDispositionThreshold = urnDispositionThreshold;
	}

	public int getUrnDispositionMailcode() {
		return urnDispositionMailcode;
	}

	public void setUrnDispositionMailcode(int urnDispositionMailcode) {
		this.urnDispositionMailcode = urnDispositionMailcode;
	}

	public int getUrnDispositionInbound() {
		return urnDispositionInbound;
	}

	public void setUrnDispositionInbound(int urnDispositionInbound) {
		this.urnDispositionInbound = urnDispositionInbound;
	}

	public int getUrnDispositionOnhand() {
		return urnDispositionOnhand;
	}

	public void setUrnDispositionOnhand(int urnDispositionOnhand) {
		this.urnDispositionOnhand = urnDispositionOnhand;
	}

	public int getUrnDispositionOnhandReserved() {
		return urnDispositionOnhandReserved;
	}

	public void setUrnDispositionOnhandReserved(int urnDispositionOnhandReserved) {
		this.urnDispositionOnhandReserved = urnDispositionOnhandReserved;
	}

	@Override
    public String toString() {
        return "[" +
                "urn:disposition:threshold = " + urnDispositionThreshold +
                ", urn:disposition:mailcode = " + urnDispositionMailcode +
                ", urn:disposition:inbound = " + urnDispositionInbound +
                ", urn:disposition:onhand = " + urnDispositionOnhand +
                ", urn:disposition:onhand-reserved = " + urnDispositionOnhandReserved +
                "]";
    }
}
