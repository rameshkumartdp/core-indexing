package com.shc.ecom.search.rules;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;

public class OfferDisplayEligibleRule implements IRule<Offer> {

    private static final long serialVersionUID = 5538117453219132310L;

    @Override
    public boolean evaluate(Offer offer, ContextMessage context) {
        Stores store = Stores.getStore(context.getStoreName());
        boolean dispEligOnSears = BooleanUtils.toBoolean(offer.getOperational().getSites().getSears().getIsDispElig());
        boolean soldBySears = StringUtils.equalsIgnoreCase(offer.getFfm().getSoldBy(), "Sears");
        boolean soldByKmart = StringUtils.equalsIgnoreCase(offer.getFfm().getSoldBy(), "Kmart");
        boolean dispEligOnKmart = BooleanUtils.toBoolean(offer.getOperational().getSites().getKmart().getIsDispElig());
        boolean hasSearsAssociation = BooleanUtils.toBoolean(offer.getSites().contains("sears"));
        boolean hasKmartAssociation = BooleanUtils.toBoolean(offer.getSites().contains("kmart"));
        switch (store) {
            case SEARS:
                return isSearsDisplayEligible(soldByKmart, dispEligOnKmart, offer);
            case FBM:
                return fbmHasSiteAssociation(dispEligOnSears, hasSearsAssociation) || fbmHasSiteAssociation(dispEligOnKmart, hasKmartAssociation);
            case AUTO:
                return isAutoDisplayEligible(dispEligOnKmart, soldByKmart, dispEligOnSears, hasSearsAssociation, hasKmartAssociation, soldBySears);
            case CPC:
                return dispEligOnSears;
            case C2C:
                return false;
            case KMART:
                return (dispEligOnKmart || (dispEligOnSears && soldBySears)) && hasKmartAssociation;
            case MYGOFER:
                return BooleanUtils.toBoolean(offer.getOperational().getSites().getMygofer().getIsDispElig());
            case SEARSPR:
                return BooleanUtils.toBoolean(offer.getOperational().getSites().getPuertorico().getIsDispElig());
            case TABLET:
                // inverse of Sears
                return isTabletDisplyEligible(dispEligOnSears, soldByKmart, dispEligOnKmart);
            case COMMERCIAL:
                return BooleanUtils.toBoolean(offer.getOperational().getSites().getScom().getIsDispElig());
            default:
                return false;
        }
    }

    private boolean isSearsDisplayEligible(boolean soldByKmart, boolean dispEligOnKmart, Offer offer) {
        if (soldByKmart && dispEligOnKmart) {
            return true;
        }
        if (BooleanUtils.toBoolean(offer.getClassifications().getIsUvd())) {
            if (!BooleanUtils.toBoolean(offer.getOperational().getSites().getSears().getIsDispElig())) {
                return false;
            }
        }

        if (!BooleanUtils.toBoolean(offer.getOperational().getSites().getSears().checkEmpty())) {
            return true;
        }
        return false;
    }

    private boolean isAutoDisplayEligible(boolean dispEligOnKmart, boolean soldByKmart, boolean dispEligOnSears, boolean hasSearsAssociation, boolean hasKmartAssociation, boolean soldBySears) {
        if (dispEligOnSears && hasSearsAssociation) {
            return true;
        }
        if (dispEligOnKmart && hasKmartAssociation) {
            return true;
        }
        if (dispEligOnSears && soldBySears) {
            return true;
        }

        if (dispEligOnKmart && soldByKmart) {
            return true;
        }
        return false;

    }

    private boolean isTabletDisplyEligible(boolean dispEligOnSears, boolean soldByKmart, boolean dispEligOnKmart) {
        return !(dispEligOnSears || (soldByKmart && dispEligOnKmart));
    }

    private boolean fbmHasSiteAssociation(boolean dispEligOnSite, boolean hasSiteAssociation) {
        return (dispEligOnSite && hasSiteAssociation);
    }
}

