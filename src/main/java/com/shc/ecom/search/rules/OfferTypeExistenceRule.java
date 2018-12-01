package com.shc.ecom.search.rules;

import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.messages.ContextMessage;

public class OfferTypeExistenceRule implements IRule<Offer> {

    private static final long serialVersionUID = -7170026829241099996L;

    @Override
    public boolean evaluate(Offer offer, ContextMessage context) {
        if (offer.getClassifications().getOfferType() != null) {
            return true;
        }
        return false;
    }
}
