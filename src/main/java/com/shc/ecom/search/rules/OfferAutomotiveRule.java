package com.shc.ecom.search.rules;

import org.apache.commons.lang.BooleanUtils;

import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;

public class OfferAutomotiveRule implements IRule<Offer> {

    private static final long serialVersionUID = 1498823037017273494L;

    @Override
    public boolean evaluate(Offer offer, ContextMessage context) {
        Stores store = Stores.getStore(context.getStoreName());
        boolean isUvd = BooleanUtils.toBoolean(offer.getClassifications().getIsUvd());
        boolean isAutomotive = BooleanUtils.toBoolean(offer.getClassifications().getIsAutomotive());
        boolean isFitmentRequired = BooleanUtils.toBoolean(offer.getClassifications().getIsFitmentRequired());
        switch (store) {
            case SEARS:
            case KMART:
            case FBM:
            case SEARSPR:
            case MYGOFER:
            case TABLET:
            case COMMERCIAL:
            	if(isUvd || (isAutomotive && isFitmentRequired)) {
                    return false;
                }
                return true;
            case AUTO:
            	if(isUvd || (isAutomotive && isFitmentRequired)) {
                	return true;
                }
                return false;
            default:
                // TODO: Need to inject rules based on the store.
                return false;
        }
    }
}
