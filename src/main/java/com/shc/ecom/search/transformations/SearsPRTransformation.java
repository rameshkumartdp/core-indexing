package com.shc.ecom.search.transformations;

import com.shc.ecom.search.docbuilder.SearchDocBuilder;
import org.springframework.stereotype.Component;

/**
 * This class is used to transform the SearchDocumentBuilder for SEARSPR.
 * There are many fields that are not required by SEARSPR.
 * <p>
 * Feature flag should be checked for in Extractor
 * <p>
 * Created by hdargah on 8/15/2016.
 */
@Component
public class SearsPRTransformation {

    private SearsPRTransformation() {
    }

    public SearchDocBuilder removeFields(SearchDocBuilder sb) {

        if (sb == null) {
            return null;
        }

        //List all the fields which we want to remove from the builder
        //List<String> shipVantageList = new ArrayList<>();
        sb.primaryVertical(null)
                .primaryHierarchy(null)
                //.primaryCategory(null)
                .storePickUp(null)
                .division(null)
                .clearance(null)
                .sellerTier(null)
                .conversion(null)
                .spuEligible(null)
                .layAway(0)
                .trustedSeller(0)
                .promotionTxt(null)
                .countryGroup(null)
                //.freeShipping(null)
                .shipVantage(null)
                .catConfidence(null)
                .instockShipping(null)
                .prdType(null)
                .has991(null)
                .upc(null)
                .discount(null);

        return sb;
    }
}
