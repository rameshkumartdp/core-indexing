package com.shc.ecom.search.rules;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.extracts.WorkingDocument;

public class SiteEligRule implements IRule<WorkingDocument> {

    private static final long serialVersionUID = -8153908747151375513L;

    @Override
    public boolean evaluate(WorkingDocument wd, ContextMessage context) {
        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        if (Stores.FBM.matches(context.getStoreName())) {
            return !CollectionUtils.isEmpty(eligibleSites);
        }
        return true;
    }
}
