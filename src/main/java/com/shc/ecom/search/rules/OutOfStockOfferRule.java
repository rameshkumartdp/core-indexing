package com.shc.ecom.search.rules;

import com.shc.common.misc.PropertiesLoader;
import org.apache.commons.lang3.BooleanUtils;

import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.uas.UasDtoByOffer;
import com.shc.ecom.search.config.GlobalConstants;

/**
 * @author rgopala
 */
public class OutOfStockOfferRule implements IRule<UasDtoByOffer> {

    private static final long serialVersionUID = 5252326314146202649L;

    @Override

    public boolean evaluate(UasDtoByOffer domainObj, ContextMessage context) {
        /** This rule is required for the relevancy testing project so that we do not drop Out-Of-Stock products
         * It is turned off by default in the base, it is only required in the ARTXXX.properties
         */
        if (!BooleanUtils.toBoolean(PropertiesLoader.getProperty(GlobalConstants.FEAT_DROP_OOS_PRODUCTS)) ||
                BooleanUtils.toBoolean(PropertiesLoader.getProperty(GlobalConstants.FEAT_RELEVANCY_TEST_OOS_SUSPEND))) {
            return true;
        }

        return domainObj.isViewOnlyOffer() || isAvaliableOffer(domainObj);
    }

    /**
     *
     * @param domainObj - contains offer UAS information.
     * @returns true whenever any of the values in the availability list for the offer has true in it.
     */
    public boolean isAvaliableOffer(UasDtoByOffer domainObj){
        return domainObj.getOfferItemMap()
                .entrySet()
                .stream()
                .filter(e -> e.getValue() == true)
                .findAny()
                .isPresent();
    }
}
