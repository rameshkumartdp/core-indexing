package com.shc.ecom.search.rules;

import org.apache.commons.lang.StringUtils;

import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.messages.ContextMessage;

public class SywExclusiveRule implements IRule<Offer> {

    public static final String SYW_EXCLUSIVE_TAG = "SYW_EXCLUSIVE";
    public static final String SYW_EXCLUSIVE_ATTR_TAG = "SYW_EXCLUSIVE_ATTR";

    private static final long serialVersionUID = 1601810698166934182L;

    @Override
    public boolean evaluate(Offer offer, ContextMessage context) {
        String sywTag = offer.getDispTags().getSywItemType();
        if (isSywTagEqual(sywTag)) {
                return false;
            }
            return true;
    }

    public boolean isSywTagEqual(String sywTag){
        return StringUtils.equalsIgnoreCase(sywTag, SYW_EXCLUSIVE_TAG) || StringUtils.equalsIgnoreCase(sywTag, SYW_EXCLUSIVE_ATTR_TAG);
    }
}
