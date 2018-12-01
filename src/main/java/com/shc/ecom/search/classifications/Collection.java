package com.shc.ecom.search.classifications;

import com.google.common.collect.Lists;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.messages.MessageProcessor;
import com.shc.ecom.search.docbuilder.SearchDocBuilder;
import com.shc.ecom.search.extract.extracts.BCOProducts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.extract.extracts.WorkingDocumentUtility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.shc.ecom.search.common.constants.CatentrySubType.VARIATION;

/**
 * Created by tgulati on 7/12/16.
 */
@Component
public class Collection extends BaseBundleClassification {

    public static final String COLLECTION = "Collection";

    private static final long serialVersionUID = 1573181228275950788L;

    @Autowired
    private WorkingDocumentUtility workingDocumentUtility;

    @Autowired
    private MessageProcessor messageProcessor;

    @Override
    public WorkingDocument extract(WorkingDocument wd, ContextMessage context) {

        WorkingDocument wdLocal = filtersExtractComponent.process(wd, context);
        if (wdLocal.getDecision().isRejected()) {
            return wdLocal;
        }

        wdLocal = uasExtractComponent.process(wdLocal, context);
        if (wdLocal.getDecision().isRejected()) {
            return wdLocal;
        }

        wdLocal = onlinePriceExtractComponent.process(wdLocal, context);
        if (wdLocal.getDecision().isRejected()) {
            return wdLocal;
        }

        String storeName = context.getStoreName();
        if (StringUtils.equalsIgnoreCase(storeName, Stores.SEARS.getStoreName()) || StringUtils.equalsIgnoreCase(storeName, Stores.KMART.getStoreName()) || StringUtils.equalsIgnoreCase(storeName, Stores.AUTO.getStoreName())) {
            wdLocal = pasExtractComponent.process(wdLocal, context);
        }

        wdLocal = behavioralExtractComponent.process(wdLocal, context);
        wdLocal = prodstatsExtractComponent.process(wdLocal, context);
        wdLocal = impressionExtractComponent.process(wdLocal, context);
        if (wdLocal.getExtracts().getContentExtract().isAutomotive() && StringUtils.equalsIgnoreCase(wdLocal.getExtracts().getContentExtract().getAutofitment(), "Requires Fitment")) {
            wdLocal = fitmentExtractComponent.process(wdLocal, context);
        }

//      loop over bco products and fet var attributes if variations or else fetch facets form content collection
        wdLocal.setSubWorkingDocumentList(getChildWorkingDocuments(wdLocal, context));

        return wdLocal;
    }

    /**
     * This method takes in the parent working document and the context message, and
     * returns the child working documents for the parent document with the corresponding extracts for it
     *
     * @param wd
     * @param context
     * @return
     */
    private List<WorkingDocument> getChildWorkingDocuments(WorkingDocument wd, ContextMessage context) {
        List<WorkingDocument> subWDList = Lists.newArrayList();
        for (BCOProducts bcoProduct : wd.getExtracts().getContentExtract().getBCOProducts()) {
//            for rank 1 and rank 2 populate searchable in xyz
            ContextMessage contextMessage = messageProcessor.modifyContextMessagePid(context, bcoProduct.getId());

            WorkingDocument subWd = workingDocumentUtility.prepareWorkingDocument(contextMessage);

            subWd.getExtracts().setSsin(bcoProduct.getId());
            subWd = contentExtractComponent.process(subWd, contextMessage);

            if (StringUtils.equals(bcoProduct.getCatentrySubType(), VARIATION.getName())) {
                subWd = varAttrExtractComponent.process(subWd, contextMessage);
            }

            subWDList.add(subWd);
        }

        return subWDList;
    }

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

    @Override
    protected String getBeanType(WorkingDocument wd) {
        return COLLECTION;
    }

    @Override
    protected List<String> getMemberSet(WorkingDocument wd, ContextMessage context) {
        List<String> memberSet = Lists.newArrayList();
        memberSet.add("Collection");
        return memberSet;
    }

    /**
     * Given a working document for a collection and the context, this method returns the list of searchable attributes
     * pertaining to rank 1 and rank 2 items that make up the collection
     *
     * @param wd
     * @param context
     * @return
     */
    @Override
    protected List<String> getSearchableAttributes(WorkingDocument wd, ContextMessage context) {

        List<String> searchableAttributes = Lists.newArrayList();
        searchableAttributes.addAll(getSearchableAttributeList(wd, context, new ArrayList<>(), new ArrayList<>()));

        for (WorkingDocument subWD : wd.getSubWorkingDocumentList()) {

            if (StringUtils.equals(subWD.getExtracts().getContentExtract().getCatentrySubType(), VARIATION.getName())) {
                searchableAttributes.addAll(getSearchableAttributeListVariations(subWD, context, new ArrayList<>(), new ArrayList<>()));
            } else {
                searchableAttributes.addAll(getSearchableAttributeList(subWD, context, new ArrayList<>(Arrays.asList("No", "N/A", "Unknown")), new ArrayList<>(Arrays.asList("Yes"))));
            }

        }
        Set<String> searchableAttributesSet = new HashSet<>(searchableAttributes);

        return new ArrayList<>(searchableAttributesSet);
    }

    @Override
    protected List<String> getSearchableAttributesSearchable(WorkingDocument wd, ContextMessage context) {
        return getSearchableAttributes(wd, context);
    }


}
