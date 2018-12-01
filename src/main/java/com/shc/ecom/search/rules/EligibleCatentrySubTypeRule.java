package com.shc.ecom.search.rules;

import java.util.List;

import com.shc.ecom.gb.doc.common.DomainObject;
import com.shc.ecom.search.common.constants.CatentrySubType;
import com.shc.ecom.search.common.messages.ContextMessage;

public class EligibleCatentrySubTypeRule implements IRule<DomainObject> {

	private static final long serialVersionUID = -6157473992184253844L;

	@Override
	public boolean evaluate(DomainObject domainObject, ContextMessage context) {

		List<String> typesList = domainObject.get_ft().getCatentrySubType();
		switch (CatentrySubType.findCatentrySubType(typesList)) {
		case BUNDLE:
		case COLLECTION:
		case OUTFIT:
		case NON_VARIATION:
		case VARIATION:
		case NON_VARIATION_UVD:
		case VARIATION_UVD:
			return true;
		
		case HOME_SERVICES:
		case STORE_ONLY:
		case UNKNOWN:
			return false;
		default:
			return false;
		}
	}
}
