package com.shc.ecom.search.extract.components.seller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.seller.Offers;
import com.shc.ecom.search.common.seller.OrderAmountChargeRange;
import com.shc.ecom.search.common.seller.SellerDto;
import com.shc.ecom.search.common.seller.WeightRange;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.SellerExtract.SellerTierRank;
import com.shc.ecom.search.extract.extracts.WorkingDocument;

@Component
public class SellerExtractComponent extends ExtractComponent<Map<String, SellerDto>> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7959944651260858687L;

    @Autowired
    private SellerService sellerService;

    @Override
    protected Map<String, SellerDto> get(WorkingDocument wd, ContextMessage context) {
        return sellerService.process(wd.getExtracts().getOfferExtract().getOfferToSellerIdMap());
    }

    @Override
    protected Extracts extract(Map<String, SellerDto> offerToSellerDTO, WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();
        extracts.getSellerExtract().setTrustedSeller(getTrustedSeller(offerToSellerDTO));
        extracts.getSellerExtract().setSellerTier(getSellerTier(offerToSellerDTO));
        extracts.getSellerExtract().setOfferToStoreFrontNames(getStoreFrontNames(offerToSellerDTO));
        extracts.getSellerExtract().setSfNameToFeaturedOffers(getFeatureProducts(offerToSellerDTO));
        Map<String, Boolean> offerMailability = extracts.getOfferExtract().getOfferMailability();
        Map<String, List<WeightRange>> offerWeightRange = new HashMap<>();
        Map<String, List<OrderAmountChargeRange>> offerOrderAmountChargeRange = new HashMap<>();

        for (Map.Entry<String, SellerDto> entry : offerToSellerDTO.entrySet()) {
            SellerDto sellerDto = entry.getValue();
            String offerId = entry.getKey();
            if (isOfferMailable(offerId, offerMailability, sellerDto)) {
                offerWeightRange.put(offerId, sellerDto.get_blob().getSeller().getPrograms().getFbm().getShippingRates().getShippingCharges().getMailable().getWeightRange());
            } else if (isOfferNonMailable(offerId, offerMailability, sellerDto)) {
                offerWeightRange.put(offerId, sellerDto.get_blob().getSeller().getPrograms().getFbm().getShippingRates().getShippingCharges().getNonMailable().getWeightRange());
            }
            offerOrderAmountChargeRange.put(offerId, sellerDto.get_blob().getSeller().getPrograms().getFbm().getShippingRates().getShippingCharges().getOrderAmountBasedShippingCharges().getOrderAmountChargeRange());
        }
        extracts.getSellerExtract().setOfferWeightRange(offerWeightRange);
        extracts.getSellerExtract().setOfferOrderAmountChargeRange(offerOrderAmountChargeRange);
        return extracts;
    }

    /**
     * offer is mailable, seller has a fbm program and has shipping charges with relevant weight array
     *
     * @param offerId
     * @param offerMailability
     * @param sellerDto
     * @return
     */
    private boolean isOfferMailable(String offerId, Map<String, Boolean> offerMailability, SellerDto sellerDto) {
        if (offerMailability.containsKey(offerId) && offerMailability.get(offerId).booleanValue()) {
            if (sellerDto.get_blob().getSeller().getPrograms().getFbm() != null && sellerDto.get_blob().getSeller().getPrograms().getFbm().getShippingRates() != null) {
                if (isSellerMailableWeightRange(sellerDto)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * offer is NOT mailable, seller has a fbm program and has shipping charges with relevant weight array
     *
     * @param offerId
     * @param offerMailability
     * @param sellerDto
     * @return
     */
    private boolean isOfferNonMailable(String offerId, Map<String, Boolean> offerMailability, SellerDto sellerDto) {
        if (offerMailability.containsKey(offerId) && !offerMailability.get(offerId).booleanValue()) {
            if (sellerDto.get_blob().getSeller().getPrograms().getFbm() != null && sellerDto.get_blob().getSeller().getPrograms().getFbm().getShippingRates() != null) {
                if (isSellerNonMailableWeightRange(sellerDto)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Need to verify that each node is present as that isn't handled at the content level
     *
     * @param sellerDto
     * @return
     */
    private boolean isSellerMailableWeightRange(SellerDto sellerDto) {
        if (sellerDto.get_blob().getSeller().getPrograms().getFbm().getShippingRates().getShippingCharges() != null) {
            if (sellerDto.get_blob().getSeller().getPrograms().getFbm().getShippingRates().getShippingCharges().getMailable() != null) {
                if (!CollectionUtils.isEmpty(sellerDto.get_blob().getSeller().getPrograms().getFbm().getShippingRates().getShippingCharges().getMailable().getWeightRange())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Need to verify that each node is present as that isn't handled at the content level
     *
     * @param sellerDto
     * @return
     */
    private boolean isSellerNonMailableWeightRange(SellerDto sellerDto) {
        if (sellerDto.get_blob().getSeller().getPrograms().getFbm().getShippingRates().getShippingCharges() != null) {
            if (sellerDto.get_blob().getSeller().getPrograms().getFbm().getShippingRates().getShippingCharges().getNonMailable() != null) {
                if (!CollectionUtils.isEmpty(sellerDto.get_blob().getSeller().getPrograms().getFbm().getShippingRates().getShippingCharges().getNonMailable().getWeightRange())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a map containing offerId and corresponding seller's storefront name.
     *
     * @param offerToSellerDTO
     * @return
     */
    private Map<String, String> getStoreFrontNames(Map<String, SellerDto> offerToSellerDTO) {
    	Map<String, String> offerToStoreFrontNames = new HashMap<>();
    	Set<Map.Entry<String, SellerDto>> entries = offerToSellerDTO.entrySet();
    	for(Map.Entry<String, SellerDto> entry: entries){
    		offerToStoreFrontNames.put(entry.getKey(), entry.getValue().get_blob().getSeller().getStoreFront().getName());        	
    	}
    	return offerToStoreFrontNames;
    }

    /**
     * Returns a map containing storefront name and associated featured products (offers) if any.
     *
     * @param offerToSellerDTO
     * @return
     */
    private Map<String, List<String>> getFeatureProducts(Map<String, SellerDto> offerToSellerDTO) {
        Map<String, List<String>> sellerIdToFeaturedOffers = new HashMap<>();
        Set<Map.Entry<String, SellerDto>> entries = offerToSellerDTO.entrySet(); 
        for(Map.Entry<String, SellerDto> entry: entries){
        	 String storeFrontName = entry.getValue().get_blob().getSeller().getStoreFront().getName();
        	 List<Offers> featuredOffers = entry.getValue().get_blob().getSeller().getStoreFront().getOffers();
        	 List<String> featuredOfferIds = new ArrayList<>();
        	 for (Offers featuredOffer : featuredOffers) {
                 featuredOfferIds.add(featuredOffer.getOfferId());
             }
             sellerIdToFeaturedOffers.put(storeFrontName, featuredOfferIds);        	
        }
        return sellerIdToFeaturedOffers;
    }

    private String getSellerTier(Map<String, SellerDto> offerToSellerDTO) {
        int highestRank = Integer.MIN_VALUE;
        Set<Map.Entry<String, SellerDto>> entries = offerToSellerDTO.entrySet();
        for(Map.Entry<String, SellerDto> entry: entries){
        	int sellerTierRank = SellerTierRank.getRank(entry.getValue().get_blob().getSeller().getSellerTier());
        	 if (sellerTierRank > highestRank) {
                 highestRank = sellerTierRank;
             }
        }        
        return SellerTierRank.getSellerTier(highestRank);
    }

    // TODO: Need to revisit this logic
    private boolean getTrustedSeller(Map<String, SellerDto> offerToSellerDTO) {
        if (offerToSellerDTO.size() == 0) {
            return false;
        }

        boolean isTrustedSeller = true;
        Set<Map.Entry<String, SellerDto>> entries = offerToSellerDTO.entrySet();
        for(Map.Entry<String, SellerDto> entry: entries){
        	 isTrustedSeller &= entry.getValue().get_blob().getSeller().getPrograms().getFbm().getTrustedSeller();
        }        
        return isTrustedSeller;
    }

}
