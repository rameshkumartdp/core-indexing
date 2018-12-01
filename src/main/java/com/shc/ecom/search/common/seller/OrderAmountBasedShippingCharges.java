package com.shc.ecom.search.common.seller;

import java.util.ArrayList;
import java.util.List;

public class OrderAmountBasedShippingCharges {
	private List<OrderAmountChargeRange> orderAmountChargeRange;

	public List<OrderAmountChargeRange> getOrderAmountChargeRange() {
		if (orderAmountChargeRange == null) {
			orderAmountChargeRange = new ArrayList<>();
		}
		return orderAmountChargeRange;
	}

	public void setOrderAmountChargeRange(List<OrderAmountChargeRange> orderAmountChargeRange) {
		this.orderAmountChargeRange = orderAmountChargeRange;
	}

	@Override
	public String toString() {
		return "OrderAmountBasedShippingCharges [orderAmountChargeRange = " + orderAmountChargeRange + "]";
	}
}