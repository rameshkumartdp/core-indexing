package com.shc.ecom.search.rules;

import com.shc.ecom.search.common.constants.BucketFilters;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.vo.buybox.BuyBoxDomain;
import com.shc.ecom.search.common.vo.buybox.Offers;
import com.shc.ecom.search.common.vo.buybox.RankGrps;
import com.shc.ecom.search.config.GlobalConstants;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author rgopala
 */
public class RankingRule implements IRule<Map<Sites, BuyBoxDomain>> {

    private static final long serialVersionUID = 5252326314146202649L;

    @Override
    public boolean evaluate(Map<Sites, BuyBoxDomain> sitesBuyBoxDomainMap, ContextMessage context) {
        if (StringUtils.equalsIgnoreCase(context.getStoreName(), GlobalConstants.MYGOFER)){
            return true;
        }
        for (Map.Entry<Sites, BuyBoxDomain> entry : sitesBuyBoxDomainMap.entrySet()) {
            BuyBoxDomain buyBoxDomain = entry.getValue();

            if (!buyBoxDomain.getGroups().isEmpty()) {
                String offerId = context.getOfferId();
                String storeName = context.getStoreName();
                if (StringUtils.isEmpty(offerId)) {
                    return false;
                }

                RankGrps rankGrp = buyBoxDomain.getGroups().get(0);
                List<Offers> offers = rankGrp.getOffers();
                for (Offers offer : offers) {
                    String offerStoreName = offer.getStoreName();
                    boolean isOfferPresent = StringUtils.equalsIgnoreCase(offer.getId(), offerId);
                    boolean isStoreValid = checkStoreValidity(offerStoreName, storeName);
                    boolean isRankValid = offer.getRank() >= 1;
                    if (isOfferPresent && isStoreValid && isRankValid) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkStoreValidity(String offerStoreName, String storeName) {
        List<String> storesUpperCase = new ArrayList<>(BucketFilters.valueOf(storeName).getPgrmTypeFilters());
        storesUpperCase.replaceAll(String::toUpperCase);
        return storesUpperCase.contains(offerStoreName.toUpperCase());
    }
}
