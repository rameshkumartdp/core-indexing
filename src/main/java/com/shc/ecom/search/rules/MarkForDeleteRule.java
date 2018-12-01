package com.shc.ecom.search.rules;

import com.shc.ecom.gb.doc.content.Content;
import com.shc.ecom.search.common.messages.ContextMessage;

public class MarkForDeleteRule implements IRule<Content> {

    private static final long serialVersionUID = -3897592778099050729L;

    @Override
    public boolean evaluate(Content content, ContextMessage context) {
        if (content.getOperational().getIsMarkForDelete() != null && content.getOperational().getIsMarkForDelete()) {
            return false;
        }
        return true;
    }

}
