package com.shc.ecom.search.rules;

import org.apache.commons.lang.BooleanUtils;

import com.shc.ecom.gb.doc.collection.Bundle;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;

/**
 * @author pchauha
 */
public class BundleHierarchyExistenceRule implements IRule<Bundle> {

    private static final long serialVersionUID = -6181141021384563011L;

    @Override
    public boolean evaluate(Bundle bundle, ContextMessage context) {
        Stores store = Stores.getStore(context.getStoreName());
        
        boolean hasSearsAssociation = BooleanUtils.toBoolean(bundle.getSites().contains("sears"));
        boolean hasKmartAssociation = BooleanUtils.toBoolean(bundle.getSites().contains("kmart"));
        
        switch (store) {
            case SEARS:
                return !bundle.getTaxonomy().getWeb().getSites().getSears().getHierarchies().isEmpty();

            case KMART:
                return !bundle.getTaxonomy().getWeb().getSites().getKmart().getHierarchies().isEmpty();
                
            case AUTO:
            	if(hasSearsAssociation && !bundle.getTaxonomy().getWeb().getSites().getSears().getHierarchies().isEmpty()) {
            		return true;
            	}
            	if(hasKmartAssociation && !bundle.getTaxonomy().getWeb().getSites().getKmart().getHierarchies().isEmpty()) {
            		return true;
            	}
            	return false;

            default:
                return false;
        }
    }

}
