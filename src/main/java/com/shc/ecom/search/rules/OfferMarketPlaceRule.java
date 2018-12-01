package com.shc.ecom.search.rules;

import org.apache.commons.lang3.StringUtils;

import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;

public class OfferMarketPlaceRule implements IRule<Offer> {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean evaluate(Offer offer, ContextMessage context) {
        Stores store = Stores.getStore(context.getStoreName());

        switch (store) {
            case SEARS:
                return true;
            case FBM:
            case AUTO:
            case C2C:
            case KMART:
            case MYGOFER:
            case SEARSPR:
            case COMMERCIAL:
                return true;
            default:
                return false;
        }
    }
}
