package com.shc.ecom.search.rules;

import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.common.misc.PropertiesLoader;


/**
 * Created by jsingar on 7/19/18.
 */
public class OfferEBayRule implements IRule<Offer> {
    private static final long serialVersionUID = -5003873830759876070L;

    @Override
    public boolean evaluate(Offer offer, ContextMessage context) {
        boolean eBayDisableFlag = Boolean.parseBoolean(PropertiesLoader.getProperty(GlobalConstants.DISABLE_EBAY_OFFERS));

        return !eBayDisableFlag || !isEbayOffer(offer);
    }

    public boolean isEbayOffer(Offer offer){
        if(offer.getMarketplace()!=null) {
            if (offer.getMarketplace().getAggregatorId() != null && offer.getMarketplace().getAggregatorId().equalsIgnoreCase(GlobalConstants.EBAY)) {
                return true;
            }
        }
        return false;
    }
}
