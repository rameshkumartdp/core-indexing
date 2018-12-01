package com.shc.ecom.search.rules;

/**
 * Created by hdargah on 5/22/2016.
 */

import com.shc.ecom.gb.doc.content.Content;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;


public class IsKmartPRDropRule implements IRule<Content> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8924163512537228725L;

	/**
     * The rule enables the user to start the indexer with a KMARTPR mode.
     * It determines whether a product is kmart PR eligible in KMARTPR mode. If not drop the product.
     * Once processing is done, change the mode to KMART as the rest of the processing remains the same for both KMART and KMARTPR.
     * <p>
     * This rule was implemented as part of SEARCH-608 - Add mode for Kmart PR
     *
     * @param content Content Value Object
     * @param context Context with the Store enable
     * @return
     */

    @Override
    public boolean evaluate(Content content, ContextMessage context) {
        String store = context.getStoreName();

        //The rule should evaluate only for KMARTPR. All other stores should remain unaffected.
        if (!store.equals(Stores.KMARTPR.getStoreName())) {
            return true;
        }

        boolean isKmartPREligible;
        if (content.getClassifications().getIskmartPRelig() != null) {
            isKmartPREligible = content.getClassifications().getIskmartPRelig();
        } else {
            isKmartPREligible = false;
        }
        // Once determined whether a product is KMARTPR eligible or not, change the context back to KMART.
        // Remainder of the processing is same for KMART and KMARTPR.
        context.setStoreName(Stores.KMART.getStoreName());

        return isKmartPREligible;
    }
}
