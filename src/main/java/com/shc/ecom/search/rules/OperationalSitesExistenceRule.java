package com.shc.ecom.search.rules;

import org.apache.commons.lang.BooleanUtils;

import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;

/**
 * @author pchauha
 */
public class OperationalSitesExistenceRule implements IRule<Offer> {

    private static final long serialVersionUID = -3509530989928275961L;

    @Override
    public boolean evaluate(Offer offer, ContextMessage context) {
        Stores store = Stores.getStore(context.getStoreName());

        boolean hasSearsAssociation = BooleanUtils.toBoolean(offer.getSites().contains("sears"));
        boolean hasKmartAssociation = BooleanUtils.toBoolean(offer.getSites().contains("kmart"));

        switch (store) {
            case SEARS:
            case FBM:
                return offer.getOperational().getSites().getSears() != null;
            case AUTO:
                return isAutoOperationalSitesExistenceRule(offer, hasSearsAssociation, hasKmartAssociation);
            case C2C:
                return false;
            case KMART:
                return isKmartOperationalSitesExistence(offer);
            case MYGOFER:
                return isMyGoferOperationalSitesExistence(offer);
            case SEARSPR:
                return isSearsPROperationalSitesExistence(offer);
            case TABLET:
                return isSearsFBMTabletOperationalSitesExistence(offer);
            case COMMERCIAL:
                return offer.getOperational().getSites().getScom() != null;
            default:
                return false;
        }
    }

    private boolean isAutoOperationalSitesExistenceRule(Offer offer, boolean hasSearsAssociation, boolean hasKmartAssociation) {
        if (hasSearsAssociation && offer.getOperational().getSites().getSears() != null) {
            return true;
        }
        if (hasKmartAssociation && offer.getOperational().getSites().getKmart() != null) {
            return true;
        }
        return false;
    }

    private boolean isSearsPROperationalSitesExistence(Offer offer) {
        if (offer.getOperational().getSites().getPuertorico() != null) {
            return true;
        }
        return false;
    }

    private boolean isMyGoferOperationalSitesExistence(Offer offer) {
        if (offer.getOperational().getSites().getMygofer() != null) {
            return true;
        }
        return false;
    }

    private boolean isKmartOperationalSitesExistence(Offer offer) {
        if (offer.getOperational().getSites().getKmart() != null) {
            return true;
        }
        return false;
    }

    private boolean isSearsFBMTabletOperationalSitesExistence(Offer offer) {
        if (offer.getOperational().getSites().getSears() != null) {
            return true;
        }
        return false;
    }

}
