package com.shc.ecom.search.rules;

import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.messages.ContextMessage;

import java.util.Optional;

public class SearchSupressionRule implements IRule<Offer> {

    private static final long serialVersionUID = 4379282886533405870L;

    @Override
    public boolean evaluate(Offer offer, ContextMessage context) {

        Optional<Boolean> isSearchSuppression = Optional.ofNullable(offer.getDispTags().getIsSearchSupression());
        if (isSearchSuppression.isPresent() && isSearchSuppression.get()) {
            return false;
        }
        return true;
    }
}
