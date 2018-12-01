package com.shc.ecom.search.rules;

import com.shc.ecom.gb.doc.content.Content;
import com.shc.ecom.search.common.messages.ContextMessage;

public class IdentityExistenceRule implements IRule<Content> {

    private static final long serialVersionUID = -6459681727203870599L;

    @Override
    public boolean evaluate(Content content, ContextMessage context) {

        if (content.getIdentity() != null) {
            return true;
        }
        return false;
    }

}
