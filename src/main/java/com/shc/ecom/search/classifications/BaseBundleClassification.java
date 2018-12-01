package com.shc.ecom.search.classifications;

import com.google.common.collect.Lists;
import com.shc.ecom.search.common.constants.ItemCondition;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.extracts.ContentExtract;
import com.shc.ecom.search.extract.extracts.SellerExtract;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tgulati on 7/12/16.
 */
public abstract class BaseBundleClassification extends Classification {

    private static final long serialVersionUID = -3479012406936629593L;

    public static final String MATTRESS_BUNDLE_SSIN_PATTERN = "082CO.*B";

    protected String getId(WorkingDocument wd, ContextMessage context) {
        String ssin = wd.getExtracts().getSsin();
        return ssin + SEPARATOR + "other" + SEPARATOR + context.getStoreName().toLowerCase();
    }

    @Override
    protected String getGiftCardType(WorkingDocument wd, ContextMessage context) {
        return "N";
    }

    @Override
    protected String getDiscontinued(WorkingDocument wd) {
        return "0";
    }

    @Override
    protected String getGiftCardSequence(WorkingDocument wd, ContextMessage context) {
        return "999";
    }

    @Override
    protected int getLayAway(WorkingDocument wd, ContextMessage context) {
        return wd.getExtracts().getContentExtract().isLayawayEligible() ? 1 : 0;
    }

    @Override
    protected List<String> getSearsInternational(WorkingDocument wd, ContextMessage context) {
        List<String> searsInternational = new ArrayList<>();
        if (Stores.SEARS.matches(context.getStoreName()) || Stores.SEARSPR.matches(context.getStoreName())) {
            int storeId = Stores.getStoreId(context.getStoreName());
            if (CollectionUtils.isNotEmpty(wd.getExtracts().getContentExtract().getIntlGroupIds())) {
                searsInternational.add(storeId + "_YES");
            }
        }
        return searsInternational;
    }

    @Override
    protected Set<String> getXref(WorkingDocument wd, ContextMessage context) {
        Set<String> xRef = new HashSet<>();

        List<String> productIds = wd.getExtracts().getContentExtract().getProductIds();
        xRef.addAll(productIds);

        String bundleId = wd.getExtracts().getSsin();
        xRef.addAll(getOfferIdCombinations(bundleId));

        if (StringUtils.isNotEmpty(wd.getExtracts().getContentExtract().getModelNo())) {
            xRef.add(wd.getExtracts().getContentExtract().getModelNo());
        }
        return xRef;
    }

    @Override
    protected int getSellerTierRank(WorkingDocument wd, ContextMessage context) {
        return SellerExtract.SellerTierRank.valueOf(getSellerTier(wd, context).toUpperCase()).getRank();
    }

    @Override
    protected String getSellerTier(WorkingDocument wd, ContextMessage context) {
        return "REGULAR";
    }

    @Override
    protected String getSwatchesStatus(WorkingDocument wd) {
        return "NO";
    }

    @Override
    protected String getSwatchInfo(WorkingDocument wd) {
        return null;
    }

    @Override
    protected String getMailInRebate(WorkingDocument wd, ContextMessage context) {
        if (Stores.MYGOFER.matches(context.getStoreName())) {
            return "0";
        }
        if (StringUtils.isEmpty(wd.getExtracts().getContentExtract().getRebateId())) {
            return "0";
        }
        return "1";
    }

    @Override
    protected List<String> getSpuEligible(WorkingDocument wd, ContextMessage context) {
        List<String> spuEligList = new ArrayList<>();
        if (wd.getExtracts().getContentExtract().isSpuEligible()) {
            spuEligList.add(Stores.getStoreId(context.getStoreName()) + "_1");
        }
        return spuEligList;
    }

    protected List<Integer> getNewArrivals(WorkingDocument wd, ContextMessage context) {
        return getDays(wd);
    }

    protected List<Integer> getNewKmartMKP(WorkingDocument wd, ContextMessage context) {

        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            return getDays(wd);
        }
        return Lists.newArrayList();
    }

    protected List<Integer> getDays(WorkingDocument wd) {

        List<Integer> daysList = Lists.newArrayList();
        LocalDate onlineDate = wd.getExtracts().getContentExtract().getFirstOnlineDate();
        if (onlineDate == null) {
            return daysList;
        }
        LocalDate today = new LocalDate();
        int days = Days.daysBetween(onlineDate, today).getDays();
        if (days != 0) {
            if (days <= 30) {
                daysList.add(30);
            }
            if (days <= 60) {
                daysList.add(60);
            }
            if (days <= 90) {
                daysList.add(90);
            }
        }
        return daysList;
    }

    /**
     * TODO: Need to recheck with the requirement for SIN
     */
    @Override
    protected String getSin(WorkingDocument wd, ContextMessage context) {
        String contextStore = context.getStoreName();
        if (Stores.SEARS.matches(contextStore) || Stores.KMART.matches(contextStore)
                || Stores.SEARSPR.matches(contextStore) || Stores.MYGOFER.matches(contextStore)) {
            return wd.getExtracts().getSsin();
        }

        // TODO: Return group id from market place node.
        return wd.getExtracts().getOfferExtract().getOfferId();
    }

    @Override
    protected Integer getInternationalShipEligible(WorkingDocument wd, ContextMessage context) {
        Integer intShipElig = null;
        if (CollectionUtils.isNotEmpty(wd.getExtracts().getContentExtract().getIntlGroupIds())) {
            intShipElig = 1;
        }
        return intShipElig;
    }

    @Override
    protected List<String> getTagValues(WorkingDocument wd) {
        return wd.getExtracts().getContentExtract().getVocTags();
    }

    protected String getProgramType(WorkingDocument wd) {
        return wd.getExtracts().getContentExtract().getProgramType();
    }

    @Override
    protected String getDivision(WorkingDocument wd) {
        return wd.getExtracts().getContentExtract().getDivision();
    }

    @Override
    protected List<String> getGeospotTag(WorkingDocument wd) {
        return wd.getExtracts().getContentExtract().getGeoSpotTag();
    }

    @Override
    protected String getItemCondition(WorkingDocument wd) {
        String defaultItemConditionStatus = ItemCondition.NEW.getStatus();
        String itemCondition = wd.getExtracts().getContentExtract().getItemCondition();
        if (StringUtils.isNotBlank(itemCondition)) {
            return ItemCondition.getItemCondition(itemCondition);
        }
        return defaultItemConditionStatus;
    }

    @Override
    protected int getNewItemFlag(WorkingDocument wd, ContextMessage context) {
        return 0;
    }

    @Override
    protected int getDaysOnline(WorkingDocument wd, ContextMessage context) {
        return 999999;
    }

    @Override
    protected int getDaysOnline_km(WorkingDocument wd, ContextMessage context) {
        return 999999;
    }

    protected List<String> getProductAttributes(WorkingDocument wd, ContextMessage context) {
        List<String> existingProductAttributes = super.getProductAttributes(wd, context);

        // Apparently only BUNDLE with "B" as the catentry type are eligible for
        // the following "NEW_BUNDLE_EXPERIENCE=YES" attribute; not Outfits (O),
        // not Collections (C).
        if (StringUtils.equalsIgnoreCase(wd.getExtracts().getContentExtract().getCatentrySubType(), "B")) {
            existingProductAttributes.add("NEW_BUNDLE_EXPERIENCE=YES");
        }
        if (wd.getExtracts().getContentExtract().isDeliveryElig()) {
            existingProductAttributes.add("BNDL_DELIVERY_ELIGIBLE=YES");
        }

        if (!wd.getExtracts().getContentExtract().isShipElig()
                && !wd.getExtracts().getContentExtract().isDeliveryElig()) {
            existingProductAttributes.add("BNDL_PICKUP_ONLY=YES");
        }
        return existingProductAttributes;
    }

    protected String getCatConfidence(WorkingDocument wd) {

        String catConfidence = "1";
        if (wd.getExtracts().getContentExtract().getCatConfScore() != 0.0) {
            catConfidence = String.valueOf(wd.getExtracts().getContentExtract().getCatConfScore());
        }

        return catConfidence;
    }

    @Override
    protected String getHas991(WorkingDocument wd) {
        String has991 = null;
        if (wd.getExtracts().getContentExtract().isOutletItem()) {
            has991 = "YES";
        }
        return has991;
    }

    @Override
    protected List<String> getStoreOrigin(WorkingDocument wd) {
        List<String> storeOriginList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(wd.getExtracts().getContentExtract().getSoldBy())) {
            storeOriginList = wd.getExtracts().getContentExtract().getSoldBy();
        }
        return storeOriginList;
    }

    protected List<String> getStoreAttributes(WorkingDocument wd, ContextMessage context) {
        List<String> storeAttributes = new ArrayList<>();

        // The outgoing indexing job populates a list of store ids (one for the
        // actual store and one for the cross format store). This list is
        // iterated to add store attributes.

        List<Integer> storeIds = new ArrayList<>();
        final String contextStoreName = context.getStoreName();
        storeIds.add(Stores.getStore(contextStoreName).getStoreId());
        ContentExtract contentExtract = wd.getExtracts().getContentExtract();

        for (Integer storeID : storeIds) {
            if (contentExtract.isEnergyStar()) {
                storeAttributes.add(storeID + "_ENERGY STAR Compliant=Yes");
            }
            if (StringUtils.isNotEmpty(contentExtract.getStreetDt())) {
                storeAttributes.add(storeID + "_STREET_DATE=" + contentExtract.getStreetDt());
            }
            if (CollectionUtils.isNotEmpty(contentExtract.getIntlGroupIds())) {
                storeAttributes.add(storeID + "_INT_SHIP_ELIGIBLE=YES");
            }
            if (contentExtract.isMailable()) {
                storeAttributes.add(storeID + "_MAILABLE_FLAG=1");
            }

            if (StringUtils.isNotEmpty(contentExtract.getChannel())) {
                storeAttributes.add(storeID + "_DEFAULT_FULFILLMENT=" + contentExtract.getChannel());
            }
            if (contentExtract.isDoNotEmailMe()) {
                storeAttributes.add(storeID + "_DONT_EMAILME=1");
            }

            if (contentExtract.isSpuEligible()) {
                storeAttributes.add(storeID + "_STOREPICKUPELIGIBLE=1");
            }

            if (Stores.SEARS.matches(contextStoreName) && contentExtract.getFirstOnlineDate() != null
                    && StringUtils.isNotEmpty(contentExtract.getFirstOnlineDate().toString())) {
                storeAttributes.add(storeID + "_What's New=Yes");
            } else {
                storeAttributes.add(storeID + "_What's New=No");
            }

            if (Stores.MYGOFER.matches(contextStoreName) && contentExtract.isSpuEligible()) {
                storeAttributes.add("10175_PU_NOW=1");
                storeAttributes.add("10175_PU_TODAY=1");
            }

        }
        return storeAttributes;
    }

    /**
     * Routine context: Classification:B. Store: Sears (non-Auto). For NV, V,
     * see in BaseFieldTransformations.
     *
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getInternationalShipping(WorkingDocument wd, ContextMessage context) {
        if (Stores.SEARS.matches(context.getStoreName())) {
            Set<String> intlGroupIds = wd.getExtracts().getOfferExtract().getIntlGroupIds();
            if (CollectionUtils.isNotEmpty(intlGroupIds)) {
                return new ArrayList<String>(intlGroupIds);
            }
        }
        return new ArrayList<String>();
    }

    @Override
    protected List<String> getStorePickUp(WorkingDocument wd, ContextMessage context) {
        List<String> storePickList = new ArrayList<>();

        String storeName = context.getStoreName();

        String channel = wd.getExtracts().getContentExtract().getChannel();
        String stsChannel = wd.getExtracts().getContentExtract().getStsChannel();

        if (StringUtils.equalsIgnoreCase(stsChannel, "CRES") || StringUtils.equalsIgnoreCase(stsChannel, "VRES")
                || StringUtils.equalsIgnoreCase(stsChannel, "CVDS")
                || wd.getExtracts().getContentExtract().isSpuEligible()) {

            // Check BFT's getStorePickUp()
            if (wd.getExtracts().getContentExtract().isSpuEligible()) {
                if (Stores.SEARS.matches(storeName) && !getStoreOrigin(wd).contains("Kmart")) {
                    storePickList.add("spuEligible");
                } else if (!Stores.SEARS.matches(storeName)) {
                    storePickList.add("spuEligible");
                }
            }
        }

        if (StringUtils.equalsIgnoreCase(channel, "DDC") || StringUtils.equalsIgnoreCase(channel, "KHD")) {
            storePickList.add("delivery");
        }

        if ((StringUtils.equalsIgnoreCase(channel, "TW") || StringUtils.equalsIgnoreCase(channel, "VD"))
                && getInstockShipping(wd, context).contains("1")) {
            storePickList.add("shipping");
        }

        if (storePickList.contains("spuEligible") && getInstockShipping(wd, context).contains("0_1")) {
            storePickList.add("spuAvailable");
        }
        return storePickList;
    }

    protected String getConsumerReportsRated(WorkingDocument wd, ContextMessage context) {
        if (CollectionUtils.isNotEmpty(getConsumerReportRatingContextual(wd, context))) {
            return "Consumer Reports Rated";
        }
        return null;
    }

    protected List<String> getConsumerReportRatingContextual(WorkingDocument wd, ContextMessage context) {
        List<String> crrList = new ArrayList<>();
        String consumerReportRating = null;
        if (Stores.SEARS.matches(context.getStoreName())
                && wd.getExtracts().getContentExtract().getSoldBy().contains("Sears")) {
            consumerReportRating = consumerReportsRating.getConsumerReportRating(wd.getExtracts().getSsin());
        }
        if (StringUtils.isNotEmpty(consumerReportRating)) {
            crrList.add(Stores.getStoreId(context.getStoreName()) + "_" + consumerReportRating);
        }
        return crrList;
    }
}
