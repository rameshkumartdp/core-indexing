package com.shc.ecom.search.classifications;

import java.util.*;

import javax.annotation.Resource;

import com.shc.common.index.rules.ValidationResults;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.search.persistence.DeliveryData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.stereotype.Component;

import com.shc.ecom.gb.doc.offer.ProductOption;
import com.shc.ecom.search.common.constants.OnlineWarehouse;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.kafka.SearchKafkaProducer;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.docbuilder.SearchDocBuilder;
import com.shc.ecom.search.extract.extracts.IASExtract;
import com.shc.ecom.search.extract.extracts.PASExtract;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.rules.IRule;
import com.shc.ecom.search.util.DecisionUtil;
import com.shc.ecom.search.validator.Validator;


/**
 * @author rgopala
 */
@Component
public class NonVariation extends Classification {

    public static final double EGIFT_SELL_PRICE_THRESHOLD = 450;
    public static final String PRODUCT_BEAN_TYPE = "PRODUCTBEAN_TYPE=NONVARIATION";
    private static final long serialVersionUID = 5121132406758105854L;
    private static final boolean TM_ENABLED = Boolean.parseBoolean(PropertiesLoader.getProperty(GlobalConstants.ENABLE_TOPIC_MODELING));
    private static final boolean INSTORE_PRICING_ENABLED = Boolean.parseBoolean(PropertiesLoader.getProperty(GlobalConstants.ENABLE_INSTORE_PRICING));
    private static final boolean NV_SWATCHES_ENABLED = Boolean.parseBoolean(PropertiesLoader.getProperty(GlobalConstants.ENABLE_NV_SWATCHES));
    private static final boolean STORE_ZIP_ENABLED = Boolean.parseBoolean(PropertiesLoader.getProperty(GlobalConstants.ENABLE_STORE_ZIP));
    @Autowired
    private Validator validator;

    @Resource(name = "contentOfferRules")
    private List<IRule<WorkingDocument>> rules;

    private static final Logger LOGGER = LoggerFactory.getLogger("debuglog");

    @Autowired
    protected MetricManager metricManager;
    @Autowired
    private DeliveryData deliveryData;

    @Autowired
    private SearchKafkaProducer kafkaProducer;

    @Override
    public WorkingDocument extract(WorkingDocument wd, ContextMessage context) {
        WorkingDocument tempWd = wd;
        String storeName = context.getStoreName();

        tempWd = offerExtractComponent.process(tempWd, context);
        if (tempWd.getDecision().isRejected()) {
            return tempWd;
        }

        tempWd = filtersExtractComponent.process(tempWd, context);
        if (tempWd.getDecision().isRejected()) {
            return tempWd;
        }

        ValidationResults results = validator.validate(tempWd, rules, context);
        tempWd.getDecision().addValidationResults(results);
        tempWd.getDecision().setRejected(!results.isPassed());
        if (tempWd.getDecision().isRejected()) {
            DecisionUtil.incrementCounterAddToKafka(metricManager, kafkaProducer, tempWd.getDecision(), results.getRejectedRule());
            LOGGER.info("Product dropped: {} {}", wd.getExtracts().getSsin(), wd.getDecision().toString());
            return tempWd;
        }

        int offerCnt = tempWd.getExtracts().getContentExtract().getOfferCnt();
        boolean dispEligAtContent = BooleanUtils.toBoolean(tempWd.getExtracts().getContentExtract().getSitesDispEligibility().get(Stores.getSite(storeName)));
        boolean dispEligAtOffer = BooleanUtils.toBoolean(tempWd.getExtracts().getOfferExtract().getSitesDispEligibility().get(Stores.getSite(storeName)));
        boolean isTabletProduct = Stores.SEARS.matches(context.getStoreName()) && !(dispEligAtContent && dispEligAtOffer);
        if (offerCnt > 1 && !isTabletProduct && !Stores.SEARSPR.matches(storeName) && !isCommercialStore(context)) {
            tempWd = buyBoxExtractComponent.process(tempWd, context);
            if (tempWd.getDecision().isRejected()) {
                return tempWd;
            }
        }

        tempWd = uasExtractComponent.process(tempWd, context);
        if (tempWd.getDecision().isRejected()) {
            return tempWd;
        }

        if (!wd.getExtracts().getBuyboxExtract().getKmartOrSearsOfferIds().isEmpty()) {
            tempWd = auxiliaryOfferExtractComponent.process(tempWd, context);
        }

        tempWd = onlinePriceExtractComponent.process(tempWd, context);
        if (isCommercialStore(context) || tempWd.getDecision().isRejected()) {
            return tempWd;
        }


        if (StringUtils.equalsIgnoreCase(storeName, Stores.SEARS.getStoreName()) || StringUtils.equalsIgnoreCase(storeName, Stores.AUTO.getStoreName())) {
            tempWd = pasExtractComponent.process(tempWd, context);
            if (!tempWd.getExtracts().getContentExtract().isUvd() && INSTORE_PRICING_ENABLED) {
                tempWd = inStorePriceExtractComponent.process(tempWd, context);
            }
        } else if (StringUtils.equalsIgnoreCase(storeName, Stores.KMART.getStoreName()) || StringUtils.equalsIgnoreCase(storeName, Stores.MYGOFER.getStoreName())) {
            tempWd = pasExtractComponent.process(tempWd, context);
        }

        if (StringUtils.equalsIgnoreCase(storeName, Stores.SEARS.getStoreName()) || StringUtils.equalsIgnoreCase(storeName, Stores.FBM.getStoreName()) || StringUtils.equalsIgnoreCase(storeName, Stores.AUTO.getStoreName())) {
            tempWd = sellerExtractComponent.process(tempWd, context);
        }

        if (StringUtils.equalsIgnoreCase(storeName, Stores.SEARS.getStoreName())
                || StringUtils.equalsIgnoreCase(storeName, Stores.SEARSPR.getStoreName())
                || StringUtils.equalsIgnoreCase(storeName, Stores.KMART.getStoreName())
                || StringUtils.equalsIgnoreCase(storeName, Stores.AUTO.getStoreName())) {
            tempWd = iasExtractComponent.process(tempWd, context);
        }

        tempWd = promoExtractComponent.process(tempWd, context);
        tempWd = behavioralExtractComponent.process(tempWd, context);
        tempWd = prodstatsExtractComponent.process(tempWd, context);
        tempWd = impressionExtractComponent.process(tempWd, context);
        if (tempWd.getExtracts().getContentExtract().isAutomotive() && StringUtils.equalsIgnoreCase(tempWd.getExtracts().getContentExtract().getAutofitment(), "Requires Fitment")) {
            tempWd = fitmentExtractComponent.process(tempWd, context);
        }
        if (TM_ENABLED) {
            tempWd = topicModelingExtractComponent.process(tempWd, context);
        }

        tempWd = localAdExtractComponent.process(tempWd, context);

        return tempWd;
    }

    @Override
    public SearchDocBuilder build(WorkingDocument wd, ContextMessage context) {
        SearchDocBuilder builder = super.build(wd, context);
        builder.beanType(getBeanType(wd))
                .id(getId(wd, context))
                .sellerTier(getSellerTier(wd, context))
                .sellerTierRank(getSellerTierRank(wd, context))
                .storeOrigin(getStoreOrigin(wd))
                .storeAttributes(getStoreAttributes(wd, context))
                .fullmfpartno(getFullmfpartno(wd))
                .swatches(getSwatchesStatus(wd))
                .swatchInfo(getSwatchInfo(wd))
                .xref(getXref(wd, context))
                .itemnumber(getItemNumber(wd, context))
                .eGiftEligible(getEGiftEligibile(wd, context))
                .productAttributes(getProductAttributes(wd, context))
                .rank(getRank(wd, context))
                .rank_km(getRank_km(wd, context))
                .daysOnline(getDaysOnline(wd, context))
                .daysOnline_km(getDaysOnline_km(wd, context))
                .newItemFlag(getNewItemFlag(wd, context))
                .storeUnit(getStoreUnit(wd))
                .geospottag(getGeospotTag(wd))
                .tagValues(getTagValues(wd))
                .division(getDivision(wd))
                .itemCondition(getItemCondition(wd))
                .programType(getProgramType(wd))
                .giftCardSequence(getGiftCardSequence(wd, context))
                .giftCardType(getGiftCardType(wd, context))
                .clickUrl(getClickUrl(wd, context))
                .flashDeal(isDealFlash(wd))
                .sin(getSin(wd, context))
                .fbm(getFbmFlag(wd, context))
                .offerAttr(getOfferAttr(wd, context))
                .pickUpItemStores(getPickUpItemStores(wd, context))
                .matureContentFlag(getMatureContentFlag(wd))
                .int_ship_eligible(getInternationalShipEligible(wd, context))
                .catConfidence(getCatConfidence(wd))
                .has991(getHas991(wd))
                .discontinued(getDiscontinued(wd))
                .upc(getUpc(wd))
                .sears_international(getSearsInternational(wd, context))
                .sin(getSin(wd, context))
                .international_shipping(getInternationalShipping(wd, context))
                .alternateImage(getAlternateImage(wd, context))
                .sellers(getSellers(wd))
                .sellerId(getSellerId(wd, context))
                .subcatAttributes(getSubcatAttributes(wd, context))
                .consumerReportsRated(getConsumerReportsRated(wd, context))
                .consumerReportRatingContextual(getConsumerReportRatingContextual(wd, context))
                .localAd(getLocalAd(wd, context))
                .promotionTxt(getPromotionTxt(wd, context))
                .newArrivals(getNewArrivals(wd, context))
                .new_km(getNewKmartMKP(wd, context))
                .name(getName(wd))
                .nameSearchable(getNameSearchable(wd))
                .localAdList(getLocalAdList(wd))
                .multipleConditions(isMultipleConditions(wd, context))
                .fulfillment(getFulfillment(wd, context))
                //commercial specific fields
                .b2bName(getB2bName(wd, context))
                .b2bDescShort(getB2bDescShort(wd, context))
                .b2bDescLong(getB2bDescLong(wd, context))
                .subUpPid(getSubupPid(wd, context))
                .subDownPid(getSubdownPid(wd, context))
                .altPid(getAltPid(wd, context))
                .reptPid(getReptPid(wd, context))
                .transRsn(getTransRsn(wd, context))
                .truckLoadQty(getTruckLoadQty(wd, context))
                .deliveryCat(getDeliveryCat(wd, context))
                .ffmClass(getFfmClass(wd, context))
                .discntDt(getDiscntDt(wd, context))
                .scomPrice(getScomPrice(wd, context))
                .scomInStock(getScomInStock(wd, context))
                .colorFamily(getColorFamily(wd, context))
                .width(getWidth(wd, context))
                .height(getHeight(wd, context))
                .depth(getDepth(wd, context))
                .energyStarCompliant(getEnergyStarCompliant(wd, context))
                .adaCompliant(getAdaCompliant(wd, context))
                .isCommercial(String.valueOf(isCommercialStore(context)))
                .aggregatorId(getAggregatorId(wd));

        builder = getOfferLevelFields(builder, wd, context);
        return builder;
    }

    /**
     * Returns an indicator to show that the offers have different item conditions
     * Return true is the offers are used/refurb and the ssin is new
     *
     * @param wd
     * @return
     */
    private boolean isMultipleConditions(WorkingDocument wd, ContextMessage context) {
        return wd.getExtracts().getBuyboxExtract().getSitesMultipleItemConditionsMap().get(Stores.getSite(context.getStoreName())) == null
                ? false
                : wd.getExtracts().getBuyboxExtract().getSitesMultipleItemConditionsMap().get(Stores.getSite(context.getStoreName()));
    }

	/* ------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------ */
    /* -------------------- Field Specialization per this type follows -------------------- */
    /* ------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------ */

    protected String getName(WorkingDocument wd) {
        return wd.getExtracts().getOfferExtract().getName();
    }

    /**
     * @param wd
     * @return
     */
    protected String getNameSearchable(WorkingDocument wd) {
        String brand = getBrand(wd);
        String name = getName(wd);
        return getNameSearchable(brand, name);
    }

    protected String getId(WorkingDocument wd, ContextMessage context) {

        String ssin = wd.getExtracts().getSsin();

        String storeFrontName;
        List<String> soldByList = wd.getExtracts().getOfferExtract().getSoldBy();
        String soldBy = null;

        if (CollectionUtils.isNotEmpty(soldByList)) {
            soldBy = soldByList.get(0);
        }

        if (Stores.SEARS.matches(soldBy) || Stores.KMART.matches(soldBy)) {
            storeFrontName = "noseller";
        } else {
            storeFrontName = wd.getExtracts().getSellerExtract().getOfferToStoreFrontNames().get(wd.getExtracts().getOfferIds().get(0));
        }

        Integer rank = wd.getExtracts().getBuyboxExtract().getSitesOfferRankMap().get(Stores.getSite(context.getStoreName()));

        String offerId = wd.getExtracts().getOfferExtract().getOfferId();

        /*
         * A qualifier to differentiate each offer.  Rank will be the key differentiator.
         * If rank is unavailable, offer-id will be the unique qualifier to differentiate offers.
         * Rank will be unavailable for offline/shop-sears products.
         */
        String offerQualifier = rank != null ? Integer.toString(rank) : offerId;

        String id = ssin + SEPARATOR + storeFrontName + SEPARATOR + offerQualifier + SEPARATOR + context.getStoreName().toLowerCase();
        return id;
    }

    protected String getUpc(WorkingDocument wd) {
        String upc = null;

        List<String> upcs = wd.getExtracts().getOfferExtract().getUpc();
        if (CollectionUtils.isNotEmpty(upcs)) {
            upc = upcs.get(0);
        }
        return upc;
    }

    @Override
    protected String getSwatchesStatus(WorkingDocument wd) {
    	Map<String, List<ProductOption>> linkedSwatchProductOptions = wd.getExtracts().getOfferExtract().getLinkedSwatchProductOptions();
		if(NV_SWATCHES_ENABLED && !linkedSwatchProductOptions.isEmpty()) {
    		return "YES";
    	}
        return "NO";
    }

    @Override
    protected String getSwatchInfo(WorkingDocument wd) {
    	if(!NV_SWATCHES_ENABLED) {
    		return null;
    	}

    	Map<String, List<ProductOption>> linkedSwatchProductOptions = wd.getExtracts().getOfferExtract().getLinkedSwatchProductOptions();

		if(linkedSwatchProductOptions.isEmpty()) {
    		return null;
    	}
		JSONObject swatchInfo = new JSONObject();
    	List<String> colorAttrNames = Arrays.asList("Color", "Color Family", "Overall Color");
        JSONArray mainList = new JSONArray();
        JSONArray swatchesList = new JSONArray();
        for(Map.Entry<String,List<ProductOption>> entry : linkedSwatchProductOptions.entrySet()) {
        	List<ProductOption> productOptions = entry.getValue();
        	if(CollectionUtils.isEmpty(productOptions)) {
        		continue;
        	}
        	for(ProductOption productOption : productOptions) {
        		String attrName = productOption.getAttr();
        		if (!colorAttrNames.contains(attrName)) {
                    continue;
                }
        		JSONObject singleSwatch = new JSONObject();

        		String swatchImg = productOption.getSwatchImg().getAttrs().getSrc();
				if(StringUtils.isNotEmpty(swatchImg)) {
        			singleSwatch.put("swatchImg", swatchImg);
        		}

				String url = productOption.getUrl();
				String swatchId = productOption.getId();

				if(StringUtils.isNotEmpty(url) && StringUtils.isNotEmpty(swatchId)) {
					singleSwatch.put("swatchLink", url+"/p-"+swatchId);
				}

				swatchesList.put(singleSwatch);
        	}
        }

        // If there are no linked variations, do not set a swatch status (by looking only at current offer)
        if(swatchesList.length()==0) {
        	return null;
        }

        //If there are multiple swatched links, include the current offer itself as a swatch
        // To include current offer's swatch, look primaryImg and swatchImg from offer
        // Get url from PID similar to product level url.
        for(String offerId : wd.getExtracts().getOfferExtract().getMainImgs().keySet()) {
        	JSONObject singleSwatch = new JSONObject();
        	String mainImg = wd.getExtracts().getOfferExtract().getMainImgs().get(offerId);
        	String swatchImg = StringUtils.EMPTY;
        	if(wd.getExtracts().getOfferExtract().getSwatchImgs().containsKey(offerId)) {
        		swatchImg = wd.getExtracts().getOfferExtract().getSwatchImgs().get(offerId);
        	}
        	String url = getUrl(wd);

        	if(StringUtils.isNotEmpty(mainImg)) {
    			singleSwatch.put("primaryImg", mainImg);
    		}

        	if(StringUtils.isNotEmpty(swatchImg)) {
    			singleSwatch.put("swatchImg", swatchImg);
    		}

        	if(StringUtils.isNotEmpty(url)) {
    			singleSwatch.put("swatchLink", url);
    		}
        	swatchesList.put(singleSwatch);
        }
        if(swatchesList.length()>0) {
        	// broadly for all differnt color attibutes, "Color" is the type.  To distinguish from existing variations, "NV" is appended to this type
        	swatchInfo.put("type", "ColorNV");
        	swatchInfo.put("vals", swatchesList);
    	}

        mainList.put(swatchInfo);
        if (mainList.length() > 0) {
            return mainList.toString().replace("\\", "");
        } else {
            return null;
        }
    }

    /**
     * Item-number is offer-id for all stores but Kmart. For Kmart, we use KSN as item-number.
     *
     * @param wd
     * @param context
     * @return
     */
    protected String getItemNumber(WorkingDocument wd, ContextMessage context) {
        String siteContext = context.getStoreName();
        String itemNumber = null;
        if (StringUtils.equalsIgnoreCase(siteContext, Stores.KMART.getStoreName())) {

            String ksn = null;
            if (CollectionUtils.isNotEmpty(wd.getExtracts().getOfferExtract().getKsn())) {
                ksn = wd.getExtracts().getOfferExtract().getKsn().get(0);
            }
            itemNumber = StringUtils.isNotEmpty(ksn) ? ksn : StringUtils.EMPTY;
        } else {
            String offerId = wd.getExtracts().getOfferExtract().getOfferId();
            itemNumber = StringUtils.isNotEmpty(offerId) ? offerId : StringUtils.EMPTY;
        }
        return itemNumber;
    }

    @Override
    protected String getSin(WorkingDocument wd, ContextMessage context) {
        String ssin = null;
        if (CollectionUtils.isNotEmpty(wd.getExtracts().getOfferIds())) {
            ssin = wd.getExtracts().getOfferIds().get(0);
        }
        return ssin;
    }

    protected List<String> getPickUpItemStores(WorkingDocument wd, ContextMessage context) {
        PASExtract pasExtract = wd.getExtracts().getPasExtract();
        List<String> storeUnits = pasExtract.getAvailableStores();

        String storeUnitsString = StringUtils.join(storeUnits, ";");
        StringBuilder storeUnitsSB = new StringBuilder(storeUnitsString);
        storeUnitsSB.append(";");
        storeUnitsString = storeUnitsSB.toString();
        List<String> offerAttrs = getOfferAttr(wd, context);

        List<String> pickUpItemStores = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(offerAttrs) && CollectionUtils.isNotEmpty(storeUnits)) {
            String offerAttrsString = StringUtils.join(offerAttrs, ";");
            pickUpItemStores.add(offerAttrsString + SEPARATOR + storeUnitsString);
        }
        return pickUpItemStores;
    }

    protected List<String> getOfferAttr(WorkingDocument wd, ContextMessage context) {
        String ssin = wd.getExtracts().getSsin();

        List<String> staticAttrs = new ArrayList<>(getStaticAttributes(wd, context));
        Collections.sort(staticAttrs);

        String staticAttrNoSpace = StringUtils.deleteWhitespace(StringUtils.join(staticAttrs, ";"));
        StringBuilder staticAttrNoSpaceSB = new StringBuilder(staticAttrNoSpace);
        staticAttrNoSpaceSB.append(";");
        staticAttrNoSpace = staticAttrNoSpaceSB.toString();
        List<String> offerAttr = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(staticAttrs)) {
            offerAttr.add(ssin + SEPARATOR + staticAttrNoSpace);
        }
        return offerAttr;
    }

    protected String isDealFlash(WorkingDocument wd) {
        return wd.getExtracts().getOfferExtract().isDealFlash() ? "true" : "false";
    }

    protected String getProgramType(WorkingDocument wd) {
        return wd.getExtracts().getOfferExtract().getMpPgmType();
    }

    @Override
    protected String getEGiftEligibile(WorkingDocument wd, ContextMessage context) {
        return isEgiftEligible(wd, context) ? "1" : "0";
    }

    protected String getBeanType(WorkingDocument wd) {
        final String productBeanConstant = "ProductBean";
        final String itemBeanConstant = "ItemBean";
        String mpPgmType = wd.getExtracts().getOfferExtract().getMpPgmType();
        if (StringUtils.equalsIgnoreCase(mpPgmType, Stores.FBM.getStoreName())) {
            return itemBeanConstant;
        } else {
            return productBeanConstant;
        }
    }

    protected boolean isEgiftEligible(WorkingDocument wd, ContextMessage context) {
        // The offer should be EGiftEligible
        if (!wd.getExtracts().getOfferExtract().isEGiftElig()) {
            return false;
        }

        // The offer should be sold by Sears or Kmart
        List<String> soldBy = wd.getExtracts().getOfferExtract().getSoldBy();
        if (!Stores.SEARS.contains(soldBy) || !Stores.KMART.contains(soldBy)) {
            return false;
        }

        // Redundant check - offer should not have market place sellers
        if (StringUtils.isNotEmpty(wd.getExtracts().getOfferExtract().getMpPgmType())) {
            return false;
        }

        // The content should have a single offer - ie, it is not a grouped item.
        if (wd.getExtracts().getContentExtract().getOfferCnt() != 1) {
            return false;
        }

        // Offer cannot be MAP violating
        if (wd.getExtracts().getPriceExtract().getOnlinePrice().get(Stores.getStoreId(context.getStoreName())).isMapViolated()) {
            return false;
        }

        // Offer price should be less than given threshold (Eg: $450)
        double sellPrice = wd.getExtracts().getPriceExtract().getOnlinePrice().get(Stores.getStoreId(context.getStoreName())).getSellPrice();
        if (sellPrice <= 0 || sellPrice > EGIFT_SELL_PRICE_THRESHOLD) {
            return false;
        }

        // The offer should not have isReserveIt flag or, the isReserveIt flag should be set to false
        return !wd.getExtracts().getOfferExtract().isReserveIt();
    }

    protected List<String> getProductAttributes(WorkingDocument wd, ContextMessage context) {
        List<String> existingProductAttributes = super.getProductAttributes(wd, context);
        existingProductAttributes.add(PRODUCT_BEAN_TYPE);
        return existingProductAttributes;
    }

    protected String getCatConfidence(WorkingDocument wd) {
        final String defaultCatConfidenceConstant = "1";

        String catConfidence = defaultCatConfidenceConstant;
        if (wd.getExtracts().getOfferExtract().getCatConfScore() != null) {
            catConfidence = String.valueOf(wd.getExtracts().getOfferExtract().getCatConfScore());
        }
        return catConfidence;
    }

    protected String getConsumerReportsRated(WorkingDocument wd, ContextMessage context) {
        final String consumerReportFilterValueConstant = "Consumer Reports Rated";
        if (CollectionUtils.isNotEmpty(getConsumerReportRatingContextual(wd, context))) {
            return consumerReportFilterValueConstant;
        }
        return null;
    }

    protected List<String> getConsumerReportRatingContextual(WorkingDocument wd, ContextMessage context) {
        List<String> crrList = new ArrayList<>();
        String consumerReportRating = null;
        if (Stores.SEARS.matches(context.getStoreName()) && wd.getExtracts().getOfferExtract().getSoldBy().contains("Sears")) {
            consumerReportRating = consumerReportsRating.getConsumerReportRating(wd.getExtracts().getSsin());
        }
        if (StringUtils.isNotEmpty(consumerReportRating)) {
            crrList.add(Stores.getStoreId(context.getStoreName()) + "_" + consumerReportRating);
        }
        return crrList;
    }

    protected String getRank(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return null;
        }
        if (StringUtils.equalsIgnoreCase(GlobalConstants.MYGOFER, context.getStoreName())) {
            return "1"; // All products are rank 1 for Mygofer, there is no ranking service for Mygofer
        }
        Integer rank = wd.getExtracts().getBuyboxExtract().getSitesOfferRankMap().get(Stores.getSite(context.getStoreName()));
        if (rank == null) {
            return "1";
        }

        return String.valueOf(rank);
    }

    @Override
    protected String getRank_km(WorkingDocument wd, ContextMessage context) {
        Sites site = Sites.KMART;
        //Following exceptions for Automotives (uvd and autofitment check) should be properly designed in the system as a whole.  Adding this to match old indexer.
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context) || wd.getExtracts().getContentExtract().isUvd() || isAutofitment(wd)) {
            Integer rank = wd.getExtracts().getBuyboxExtract().getSitesOfferRankMap().get(site);
            if (rank == null) {
                return "1";
            }
            return String.valueOf(rank);
        }
        return null;
    }

    protected List<String> getLocalAdList(WorkingDocument wd) {
        return wd.getExtracts().getLocalAdExtract().getLocalAdList();
    }

    protected List<String> getFulfillment(WorkingDocument wd, ContextMessage context) {
        // For Mygofer - amosta0
        List<String> fulfillmentList = new ArrayList<String>();

        if (StringUtils.equalsIgnoreCase(GlobalConstants.MYGOFER, context.getStoreName())) {

            List<String> defaultFulfillment = wd.getExtracts().getOfferExtract().getChannels();
			if (!CollectionUtils.isEmpty(defaultFulfillment)
					&& (defaultFulfillment.contains("VD") || defaultFulfillment.contains("TW"))) {

				fulfillmentList.add(Stores.MYGOFER.getStoreId() + "_10");

			}

            List<String> storeIds = wd.getExtracts().getPasExtract().getAvailableStores();
            if (storeIds == null || storeIds.isEmpty()) {
                return fulfillmentList;
            }

            for (int i = 0; i < storeIds.size(); i++) {
                fulfillmentList.add(storeIds.get(i) + "_30");
            }
        }

        return fulfillmentList;
    }

    /**
     * As part of Ensure Availability, for NV we also index the DDC region where a product is deliverable
     * This will help us provide the members with only the products that can be delivered in their zip
     * Search-1765
     *
     * @param offerId
     * @param wd
     * @param context
     * @return
     */


    /**
     * We are now getting zone information from IAS instead of PAS and at the offer level
     *
     * @param offerId
     * @param wd
     * @param context
     * @return
     */
    @Override
    protected List<String> getOfferZoneList(String offerId, WorkingDocument wd, ContextMessage context) {
        IASExtract iasExtract = wd.getExtracts().getIasExtract();
        if (iasExtract.getOfferIdZoneMap().containsKey(offerId)) {
            return new ArrayList<>(iasExtract.getOfferIdZoneMap().get(offerId)); //Converting the list to set
        }
        return new ArrayList<>();
    }


    /**
     * See Parent Documentation in BaseFieldTransformation
     *
     * @param offerId
     * @param wd
     * @param context
     * @return
     */
    @Override
    protected  List<String> getOfferStoreUnits(String offerId, WorkingDocument wd, ContextMessage context) {
        IASExtract iasExtract = wd.getExtracts().getIasExtract();
        if (iasExtract.getOfferIdStoreUnits().containsKey(offerId)) {
            return new ArrayList<>(iasExtract.getOfferIdStoreUnits().get(offerId)); //Converting the list to set
        }
        return new ArrayList<>();
    }

    /**
     * See parent documentation in BaseFieldTransformation
     *
     * @param offerId
     * @param wd
     * @param context
     * @return
     */
    @Override
    protected List<String> getReservableStoresList(String offerId, WorkingDocument wd, ContextMessage context) {
        IASExtract iasExtract = wd.getExtracts().getIasExtract();
        Set<String> reservableStores = new HashSet<>();
        if (iasExtract.getOfferIdReservableStoreMap().containsKey(offerId)) {
            Set<String> facilityIds = iasExtract.getOfferIdReservableStoreMap().get(offerId);
            for(String id : facilityIds) {
            	if(STORE_ZIP_ENABLED && OnlineWarehouse.matchesAnyFacility(id)) {
            		reservableStores.addAll(storesExtractService.getCrossFormatShippedToStores(id));
            	} else {
            		reservableStores.add(id);
            	}
            }
        }
        return new ArrayList<>(reservableStores);
    }

    /**
     * See parent documentation in BaseFieldTransformation
     *
     * @param offerId
     * @param wd
     * @param context
     * @return
     */
    @Override
    protected boolean isReservableStoresAll(String offerId, WorkingDocument wd, ContextMessage context) {
        IASExtract iasExtract = wd.getExtracts().getIasExtract();
        if (iasExtract.getOfferIdReservableAllStoresMap().containsKey(offerId)) {
            return iasExtract.getOfferIdReservableAllStoresMap().get(offerId);
        }
        return false;
    }

    /**
     * See parent documentation in BaseFieldTransformation
     *
     * @param offerId
     * @param wd
     * @param context
     * @return
     */

    @Override
    protected boolean isShipAvaialable(String offerId, WorkingDocument wd, ContextMessage context) {
    	final Map<String, Map<String, Boolean>> itemAvailabilityMap = wd.getExtracts().getUasExtract().getItemAvailabilityMap();
    	if(itemAvailabilityMap.containsKey(offerId)){
    		 Map<String, Boolean> offerAvailabilityMap = itemAvailabilityMap.get(offerId);
    		 return offerAvailabilityMap.get("shipAvail");
    	}
    	return false;
    }

    public boolean isCommercialStore(ContextMessage context){
        return StringUtils.equalsIgnoreCase(context.getStoreName(), Stores.COMMERCIAL.getStoreName());
    }

    @Override
    protected List<String> getDeliveryAreaList(String offerId,WorkingDocument wd, ContextMessage context) {
        List<String> deliveryAreaList = new ArrayList<>();
        if (StringUtils.equalsIgnoreCase(context.getStoreName(), Stores.SEARS.getStoreName())
                || StringUtils.equalsIgnoreCase(context.getStoreName(), Stores.KMART.getStoreName())) {
            deliveryAreaList.addAll(deliveryData.getDeliveryData(offerId));

        }
        return deliveryAreaList;
        }
    @Override
    public String getAggregatorId(WorkingDocument wd){
        return wd.getExtracts().getOfferExtract().getAggregatorId();
    }

}
