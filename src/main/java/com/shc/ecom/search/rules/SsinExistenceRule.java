package com.shc.ecom.search.rules;

import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.messages.ContextMessage;

public class SsinExistenceRule implements IRule<Offer> {

    private static final long serialVersionUID = -2801045084668904022L;

    @Override
    public boolean evaluate(Offer offer, ContextMessage context) {
        if (offer.getIdentity().getSsin() != null) {
            return true;
        }
        return false;
    }
}
