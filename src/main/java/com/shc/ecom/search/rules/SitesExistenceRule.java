package com.shc.ecom.search.rules;

import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;

public class SitesExistenceRule implements IRule<Offer> {

    private static final long serialVersionUID = -3535481911370509700L;

    @Override
    public boolean evaluate(Offer offer, ContextMessage context) {
        //TODO: Needs clean up. Should get covered in OfferDisplayEligibleRule
        Stores store = Stores.getStore(context.getStoreName());
        switch (store) {
            case SEARS:
                return isSiteExistence(offer, "sears");
            case FBM:
                return true;
            case AUTO:
                return offer.getSites().contains("sears") || offer.getSites().contains("kmart");
            case C2C:
                return false;
            case KMART:
                return isSiteExistence(offer, "kmart");
            case MYGOFER:
                return isSiteExistence(offer, "mygofer");
            case SEARSPR:
                return isSiteExistence(offer, "puertorico");
            case TABLET:
                return isSiteExistence(offer, "sears");
            case COMMERCIAL:
                return isSiteExistence(offer, "scom");
            default:
                return false;

        }
    }

    private boolean isSiteExistence(Offer offer, String siteName) {
        return offer.getSites().contains(siteName);
    }
}
