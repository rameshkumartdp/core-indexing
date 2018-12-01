package com.shc.ecom.search.common.store;

public class Blob {
	
	private Unit unit;

	public Unit getUnit() {
		if(unit==null) {
			unit = new Unit();
		}
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "Blob [unit = " + unit + "]";
	}
}
