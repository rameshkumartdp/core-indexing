package com.shc.ecom.search.rules;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.shc.ecom.gb.doc.content.Content;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;

public class FitmentRule implements IRule<Content> {

    /**
     * Rule evaluates to true if the store is "auto" and the content is a UVD or
     * an automotive that requires fitment. Others stores need not process this
     * type of content as it requires additional fitment calls, hence evaluates
     * to false.
     */
    private static final long serialVersionUID = 1498823037017273494L;

    @Override
    public boolean evaluate(Content content, ContextMessage context) {
        Stores store = Stores.getStore(context.getStoreName());
        switch (store) {
            case AUTO:
                if (BooleanUtils.isTrue(content.getClassifications().getIsUvd())
                        || (BooleanUtils.isTrue(content.getClassifications().getIsAutomotive()) && StringUtils
                        .equalsIgnoreCase(content.getAutomotive().getAutoFitment(), "Requires Fitment"))) {
                    return true;
                }
                return false;
            case SEARS:
            case SEARSPR:
            case KMART:
            case C2C:
            case TABLET:
            case MYGOFER:
                if (content.getAutomotive().getAutoFitment() == null || StringUtils.equalsIgnoreCase(content.getAutomotive().getAutoFitment(), "NO")) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }
}
