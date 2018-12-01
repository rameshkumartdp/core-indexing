package com.shc.ecom.search.rules;

import com.shc.ecom.gb.doc.collection.Bundle;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;

public class BundleSitesRule implements IRule<Bundle> {

    private static final long serialVersionUID = 2611590128791782831L;

    @Override
    public boolean evaluate(Bundle bundle, ContextMessage context) {
        Stores store = Stores.getStore(context.getStoreName());
        switch (store) {
            case SEARS:
                return bundle.getSites().contains("sears");

            case KMART:
                return bundle.getSites().contains("kmart");
                
            case AUTO:
            	return bundle.getSites().contains("sears") || bundle.getSites().contains("kmart");

            default:
                return false;
        }
    }

}
