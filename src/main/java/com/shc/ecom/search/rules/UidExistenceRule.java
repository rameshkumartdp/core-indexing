package com.shc.ecom.search.rules;

import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.messages.ContextMessage;
import org.apache.commons.lang.StringUtils;

public class UidExistenceRule implements IRule<Offer> {

    private static final long serialVersionUID = 1625422439329302529L;

    @Override
    public boolean evaluate(Offer offer, ContextMessage context) {
        if (StringUtils.isNotEmpty(offer.getIdentity().getUid())) {
            return true;
        }
        return false;
    }

}
