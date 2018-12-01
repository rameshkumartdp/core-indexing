package com.shc.ecom.search.rules;

import com.shc.ecom.gb.doc.collection.Bundle;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import org.apache.commons.lang.BooleanUtils;

public class BundleMarkForDeleteRule implements IRule<Bundle> {

    private static final long serialVersionUID = -3897592778099050729L;

    @Override
    public boolean evaluate(Bundle bundle, ContextMessage context) {
        Stores store = Stores.getStore(context.getStoreName());
        switch (store) {
            case SEARS:
                return true; // evaluate later for tablet
            default:
                if (bundle.getOperational().getIsMarkForDelete() != null && BooleanUtils.toBoolean(bundle.getOperational().getIsMarkForDelete())) {
                    return false;
                }
        }
        return true;
    }

}
