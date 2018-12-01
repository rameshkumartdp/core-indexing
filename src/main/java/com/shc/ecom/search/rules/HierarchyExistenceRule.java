package com.shc.ecom.search.rules;

import org.apache.commons.lang.BooleanUtils;

import com.shc.ecom.gb.doc.content.Content;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;

/**
 * @author pchauha
 */
public class HierarchyExistenceRule implements IRule<Content> {

    private static final long serialVersionUID = -6181141021384563011L;

    @Override
    public boolean evaluate(Content content, ContextMessage context) {
        Stores store = Stores.getStore(context.getStoreName());
        
        boolean hasSearsAssociation = BooleanUtils.toBoolean(content.getSites().contains("sears"));
        boolean hasKmartAssociation = BooleanUtils.toBoolean(content.getSites().contains("kmart"));
        
        switch (store) {
            case SEARS:
            case FBM:
            	return !content.getTaxonomy().getWeb().getSites().getSears().getHierarchies().isEmpty();
            case AUTO:
            	if(hasSearsAssociation && !content.getTaxonomy().getWeb().getSites().getSears().getHierarchies().isEmpty()) {
            		return true;
            	}
            	if(hasKmartAssociation && !content.getTaxonomy().getWeb().getSites().getKmart().getHierarchies().isEmpty()) {
            		return true;
            	}
            	return false;
            case C2C:
                return false;
            case KMART:
                return !content.getTaxonomy().getWeb().getSites().getKmart().getHierarchies().isEmpty();
            case MYGOFER:
                return !content.getTaxonomy().getWeb().getSites().getMygofer().getHierarchies().isEmpty();
            case SEARSPR:
                return !content.getTaxonomy().getWeb().getSites().getPuertorico().getHierarchies().isEmpty();
            case TABLET:
                return !content.getTaxonomy().getWeb().getSites().getSears().getHierarchies().isEmpty(); // same as Sears
            case COMMERCIAL:
                return !content.getTaxonomy().getWeb().getSites().getScom().getHierarchies().isEmpty();
            default:
                return false;
        }
    }
}
