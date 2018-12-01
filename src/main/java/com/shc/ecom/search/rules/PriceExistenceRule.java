package com.shc.ecom.search.rules;

import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.components.pricing.Price;

public class PriceExistenceRule implements IRule<Price> {

    private static final long serialVersionUID = -2160630541344469275L;

    @Override
    public boolean evaluate(Price price, ContextMessage context) {

        if (price == null) {
            return false;
        }

        // TODO: Correct isSale check and to include fbs check if necessary!
        // TODO: Is it ok to check storeName from context rather than price?
        if (context.getStoreName().equalsIgnoreCase("FBM") && price.getNetDownPrice() <= 0 && price.getSellPrice() <= 0 && price.getRegPrice() <= 0 && price.getWasPrice() <= 0 && !price.isSale()) {
            return false;
        }
        return true;

    }
}
