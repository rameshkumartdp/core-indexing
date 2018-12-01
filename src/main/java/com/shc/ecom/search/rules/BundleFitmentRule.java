package com.shc.ecom.search.rules;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.shc.ecom.gb.doc.collection.Bundle;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;

public class BundleFitmentRule implements IRule<Bundle> {

	/**
	 * Rule evaluates to true if the store is "auto" and the content is a UVD or
	 * an automotive that requires fitment. Others stores need not process this
	 * type of content as it requires additional fitment calls, hence evaluates
	 * to false.
	 */
	private static final long serialVersionUID = 1498823037017273494L;

	@Override
	public boolean evaluate(Bundle bundle, ContextMessage context) {
		Stores store = Stores.getStore(context.getStoreName());
		switch (store) {
		case SEARS:
		case SEARSPR:
		case FBM:
		case KMART:
		case CPC:
		case C2C:
		case TABLET:
			if (BooleanUtils.isTrue(bundle.getClassifications().getIsUvd())
					|| (BooleanUtils.isTrue(bundle.getClassifications().getIsAutomotive()) && StringUtils
							.equalsIgnoreCase(bundle.getAutomotive().getAutoFitment(), "Requires Fitment"))) {
				return false;
			}
			return true;
		case AUTO:
			if (BooleanUtils.isTrue(bundle.getClassifications().getIsUvd())
					|| (BooleanUtils.isTrue(bundle.getClassifications().getIsAutomotive()) && StringUtils
							.equalsIgnoreCase(bundle.getAutomotive().getAutoFitment(), "Requires Fitment"))) {
				return true;
			}
			return false;
		default:
			return false;
		}
	}

}
