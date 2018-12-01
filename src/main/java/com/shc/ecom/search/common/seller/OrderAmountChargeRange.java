package com.shc.ecom.search.common.seller;

public class OrderAmountChargeRange {
	private String min;

	private String max;

	private String ground;

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getGround() {
		return ground;
	}

	public void setGround(String ground) {
		this.ground = ground;
	}

	@Override
	public String toString() {
		return "OrderAmountChargeRange [min = " + min + ", max = " + max + ", ground = " + ground + "]";
	}
}
