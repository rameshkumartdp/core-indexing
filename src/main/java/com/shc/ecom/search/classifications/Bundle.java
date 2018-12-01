package com.shc.ecom.search.classifications;


import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.shc.common.misc.PropertiesLoader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.docbuilder.SearchDocBuilder;
import com.shc.ecom.search.extract.extracts.WorkingDocument;

/**
 * @author rgopala
 */

@Component
public class Bundle extends BaseBundleClassification {

    private static final long serialVersionUID = -5788841130847722300L;
    private static final boolean TM_ENABLED = Boolean.parseBoolean(PropertiesLoader.getProperty(GlobalConstants.ENABLE_TOPIC_MODELING));
    //SEARCH-2845 constants for bundle and mattressBundle
    public static final String BUNDLE_BEAN = "BundleBean";
    public static final String MATTRESS_BUNDLE_SSIN_PATTERN = "082CO.*B";
    public static final String BUNDLE = "bundle";
    public static final String MATTRESS_BUNDLE = "mattressBundle";

    @Override
    public WorkingDocument extract(WorkingDocument wd, ContextMessage context) {
        WorkingDocument tempWd = wd;
        tempWd = filtersExtractComponent.process(tempWd, context);
        if (tempWd.getDecision().isRejected()) {
            return tempWd;
        }

        tempWd = uasExtractComponent.process(tempWd, context);
        if (tempWd.getDecision().isRejected()) {
            return tempWd;
        }

        tempWd = onlinePriceExtractComponent.process(tempWd, context);
        if (tempWd.getDecision().isRejected()) {
            return wd;
        }

        if (StringUtils.equalsIgnoreCase(context.getStoreName(), Stores.SEARS.getStoreName()) || StringUtils.equalsIgnoreCase(context.getStoreName(), Stores.KMART.getStoreName()) || StringUtils.equalsIgnoreCase(context.getStoreName(), Stores.AUTO.getStoreName())) {
            tempWd = pasExtractComponent.process(tempWd, context);
        }

        tempWd = behavioralExtractComponent.process(tempWd, context);
        tempWd = prodstatsExtractComponent.process(tempWd, context);
        tempWd = impressionExtractComponent.process(tempWd, context);
        if (tempWd.getExtracts().getContentExtract().isAutomotive() && StringUtils.equalsIgnoreCase(wd.getExtracts().getContentExtract().getAutofitment(), "Requires Fitment")) {
            tempWd = fitmentExtractComponent.process(tempWd, context);
        }
        if (TM_ENABLED) {
            tempWd = topicModelingExtractComponent.process(tempWd, context);
        }

        return tempWd;
    }


    // TODO: Check what is done for xref for Bundle
    @Override
    public SearchDocBuilder build(WorkingDocument wd, ContextMessage context) {
        SearchDocBuilder builder = super.build(wd, context);

        builder.beanType(getBeanType(wd))
                .xref(getXref(wd, context))
                .id(getId(wd, context))
                .sellerTier(getSellerTier(wd, context))
                .sellerTierRank(getSellerTierRank(wd, context))
                .swatches(getSwatchesStatus(wd))
                .swatchInfo(getSwatchInfo(wd))
                .storeOrigin(getStoreOrigin(wd))
                .storeAttributes(getStoreAttributes(wd, context))
                .productAttributes(getProductAttributes(wd, context))
                .daysOnline(getDaysOnline(wd, context))
                .daysOnline_km(getDaysOnline_km(wd, context))
                .newItemFlag(getNewItemFlag(wd, context))
                .itemCondition(getItemCondition(wd))
                .geospottag(getGeospotTag(wd))
                .tagValues(getTagValues(wd))
                .rank(getRank(wd, context))
                .rank_km(getRank_km(wd, context))
                .division(getDivision(wd))
                .programType(getProgramType(wd))
                .spuEligible(getSpuEligible(wd, context))
                .int_ship_eligible(getInternationalShipEligible(wd, context))
                .catConfidence(getCatConfidence(wd))
                .has991(getHas991(wd))
                .staticAttributes(getStaticAttributes(wd, context))
                .newArrivals(getNewArrivals(wd, context))
                .new_km(getNewKmartMKP(wd, context))
                .sears_international(getSearsInternational(wd, context))
                .sin(getSin(wd, context))
                .international_shipping(getInternationalShipping(wd, context))
                .giftCardSequence(getGiftCardSequence(wd, context))
                .giftCardType(getGiftCardType(wd, context))
                .eGiftEligible(getEGiftEligible())
                .fbm(getFbmFlag(wd, context)).discontinued(getDiscontinued(wd))
                .subcatAttributes(getSubcatAttributes(wd, context))
                .memberSet(getMemberSet(wd, context))
                .consumerReportsRated(getConsumerReportsRated(wd, context))
                .consumerReportRatingContextual(getConsumerReportRatingContextual(wd, context))
                .localAd(getLocalAd(wd, context))
                .layAway(getLayAway(wd, context))
                .promotionTxt(getPromotionTxt(wd, context))
                .name(getName(wd))
                .nameSearchable(getNameSearchable(wd));

        builder = getOfferLevelFields(builder, wd, context);
        return builder;
    }

    private String getEGiftEligible() {
        return "0";
    }

    @Override
    protected String getBeanType(WorkingDocument wd) {
        return BUNDLE_BEAN;
    }

    @Override
    protected List<String> getMemberSet(WorkingDocument wd, ContextMessage context) {
        List<String> memberSet = Lists.newArrayList();
        String storeName = context.getStoreName();
        memberSet.add(BUNDLE);
        if ((Stores.SEARS.matches(storeName) || Stores.KMART.matches(storeName))
                && (Pattern.matches(MATTRESS_BUNDLE_SSIN_PATTERN, wd.getExtracts().getSsin()))) {
            memberSet.add(MATTRESS_BUNDLE);
        }
        return memberSet;
    }

    @Override
    protected boolean isShipAvaialable(String offerId, WorkingDocument wd, ContextMessage context) {
        final Map<String, Map<String, Boolean>> itemAvailabilityMap = wd.getExtracts().getUasExtract().getItemAvailabilityMap();
        if (itemAvailabilityMap.containsKey(offerId)) {
            Map<String, Boolean> offerAvailabilityMap = itemAvailabilityMap.get(offerId);
            return offerAvailabilityMap.get("shipAvail");
        }
        return false;
    }
}
