package com.shc.ecom.search.rules;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.constants.CatentrySubType;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;

/**
 * This rule is to find out if a variation product should get indexed in a particular run or not.
 */
public class ProductStoreEligibleRule implements IRule<List<Offer>> {

    private static final long serialVersionUID = -7177382891069711376L;

    @Override
    public boolean evaluate(List<Offer> offers, ContextMessage context) {
        Stores store = Stores.getStore(context.getStoreName());

        // This rule only needs to be checked for variation products
        String offerType = offers.get(0).getClassifications().getOfferType();
        if (offerType == null) {
            return false;
        }

        if (!StringUtils.equalsIgnoreCase(offerType, CatentrySubType.VARIATION.getName())) {
            return true;
        }

        Set<String> soldByList = new HashSet<>();
        for (Offer offer : offers) {
            soldByList.add(offer.getFfm().getSoldBy());
        }

        switch (store) {
            case SEARS:
            case KMART:
                return isSearsKmartProductStoreEligible(soldByList);
            case FBM:
                //Index variation products that have no sears and kmart offers.
                return isFBMProductStoreEligible(offers);
            case AUTO:
            case CPC:
            case C2C:
            case MYGOFER:
            case SEARSPR:
            case TABLET:
            case COMMERCIAL:
                return true;
            default:
                return false;
        }
    }

    private boolean isSearsKmartProductStoreEligible(Set<String> soldByList) {
        //Index any variation that has at least one sears/kmart offer
        for (String soldBy : soldByList) {
            if (StringUtils.equalsIgnoreCase(soldBy, "sears") || StringUtils.equalsIgnoreCase(soldBy, "kmart")) {
                return true;
            }
        }
        return false;
    }

    private boolean isFBMProductStoreEligible(List<Offer> offers) {
        //Index variation products that have no sears and kmart offers.
        for (Offer offer : offers) {
            String soldBy = offer.getFfm().getSoldBy();
            String programType = offer.getMarketplace().getProgramType();

            // SEARCH-2047 DSS products will have soldBy as Sears and should not be dropped.
            if (StringUtils.equalsIgnoreCase(programType, "DSS") && StringUtils.equalsIgnoreCase(soldBy, "sears")) {
                continue;
            }
            // Other sears sold and kmart sold products are taken care in sears and kmart run, hence should be dropped in fbm flow.
            if (StringUtils.equalsIgnoreCase(soldBy, "sears") || StringUtils.equalsIgnoreCase(soldBy, "kmart")) {
                return false;
            }
        }
        return true;
    }
}
