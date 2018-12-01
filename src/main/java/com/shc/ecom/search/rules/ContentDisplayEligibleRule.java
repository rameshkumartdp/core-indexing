package com.shc.ecom.search.rules;

import com.shc.ecom.gb.doc.content.Content;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import org.apache.commons.lang.BooleanUtils;

public class ContentDisplayEligibleRule implements IRule<Content> {

    private static final long serialVersionUID = -6157473992184253844L;

    @Override
    public boolean evaluate(Content content, ContextMessage context) {
        Stores store = Stores.getStore(context.getStoreName());
        boolean isDispEligOnSears = BooleanUtils.toBoolean(content.getOperational().getSites().getSears().getIsDispElig());
        boolean isDispEligOnKmart = BooleanUtils.toBoolean(content.getOperational().getSites().getKmart().getIsDispElig());
        boolean hasSearsAssociation = BooleanUtils.toBoolean(content.getSites().contains("sears"));
        boolean hasKmartAssociation = BooleanUtils.toBoolean(content.getSites().contains("kmart"));

        switch (store) {
            case SEARS:
                if (BooleanUtils.toBoolean(content.getClassifications().getIsUvd())) {
                    if (!BooleanUtils.toBoolean(content.getOperational().getSites().getSears().getIsDispElig())) {
                        return false;
                    }
                }
                return true; // David
            case FBM:
                if (isDispEligOnSears && hasSearsAssociation) {
                    return true;
                }

                if (isDispEligOnKmart && hasKmartAssociation) {
                    return true;
                }
                return false;
            case AUTO:
            	 if (isDispEligOnSears && hasSearsAssociation) {
                     return true;
                 }

                 if (isDispEligOnKmart && hasKmartAssociation) {
                     return true;
                 }
                 return false;
            case C2C:
                return false;
            case KMART:
                return BooleanUtils.toBoolean(content.getOperational().getSites().getKmart().getIsDispElig());
            case MYGOFER:
                return BooleanUtils.toBoolean(content.getOperational().getSites().getMygofer().getIsDispElig());
            case SEARSPR:
                return BooleanUtils.toBoolean(content.getOperational().getSites().getPuertorico().getIsDispElig());
            case TABLET:
                return !BooleanUtils.toBoolean(content.getOperational().getSites().getSears().getIsDispElig()); // invert of Sears' eligibility
            case COMMERCIAL:
                return BooleanUtils.toBoolean(content.getOperational().getSites().getScom().getIsDispElig());
            default:
                return false;
        }
    }
}
