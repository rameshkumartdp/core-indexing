package com.shc.ecom.search.rules;

import org.apache.commons.lang.BooleanUtils;

import com.shc.ecom.gb.doc.collection.Bundle;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;

public class BundleDisplayEligibleRule implements IRule<Bundle> {

    private static final long serialVersionUID = -7527271627201932069L;

    @Override
    public boolean evaluate(Bundle bundle, ContextMessage context) {
        Stores store = Stores.getStore(context.getStoreName());
        boolean isDispEligOnSears = BooleanUtils.toBoolean(bundle.getOperational().getSites().getSears().getIsDispElig());
        boolean isDispEligOnKmart = BooleanUtils.toBoolean(bundle.getOperational().getSites().getKmart().getIsDispElig());
        boolean hasSearsAssociation = BooleanUtils.toBoolean(bundle.getSites().contains("sears"));
        boolean hasKmartAssociation = BooleanUtils.toBoolean(bundle.getSites().contains("kmart"));
        
        switch (store) {
            case SEARS:
                return BooleanUtils.toBoolean(bundle.getOperational().getSites().getSears().getIsDispElig());

            case KMART:
                return BooleanUtils.toBoolean(bundle.getOperational().getSites().getKmart().getIsDispElig());

            case AUTO:
            	if (isDispEligOnSears && hasSearsAssociation) {
                    return true;
                }

                if (isDispEligOnKmart && hasKmartAssociation) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
}