package com.shc.ecom.search.extract.components.pricing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.shc.common.index.rules.Decision;
import com.shc.common.index.rules.ValidationResults;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.exception.ErrorCode;
import com.shc.ecom.search.common.exception.SearchCommonException;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.rules.IRule;
import com.shc.ecom.search.util.DecisionUtil;
import com.shc.ecom.search.validator.Validator;
import com.shc.ecom.wxs.common.exception.SHCWxsException;

/**
 * @author rgopala Jul 29, 2015 search-doc-builder
 */
public class OnlinePriceExtractComponent extends ExtractComponent<Map<Integer, Price>> {

    private static final long serialVersionUID = -7882169262760423650L;

    private static final Logger LOGGER = LoggerFactory.getLogger(OnlinePriceExtractComponent.class.getName());
    private static final Logger priceLogger = LoggerFactory.getLogger("pricinglog");

    @Autowired
    @Qualifier("priceAPI")
    private PricingService pricingService;


    @Autowired
    @Qualifier("pricingGridService")
    private PricingService pricingGridService;

    @Resource(name = "priceRules")
    private List<IRule<Price>> rules;

    @Autowired
    private Validator validator;
    
    @Autowired
    protected MetricManager metricManager;

    @Autowired
    private PriceAlgorithm priceAlgorithm;
    
    private static final boolean fallbackPriceFlag = BooleanUtils.toBoolean(PropertiesLoader.getProperty(GlobalConstants.FALLBACK_PRICING_GRID));


    @Override
    protected Map<Integer, Price> get(WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();
        List<Integer> storeIds = Stores.getStoreId(wd, context);

        HashMap<Integer, Price> onlinePriceMap = new HashMap<>();

        if (wd.getExtracts().getOfferExtract().isKmartPrElig()) {
            storeIds.add(Stores.getStoreId(Stores.KMARTPR.getStoreName()));
        }

        for (int storeId : storeIds) {
            Price price = (storeId == Stores.MYGOFER.getStoreId())? getPriceBasedOnClassification(Stores.KMART.getStoreId(), wd, extracts, context) : getPriceBasedOnClassification(storeId, wd, extracts, context);

            if (price != null) {
                price.setStoreName(context.getStoreName());
                onlinePriceMap.put(storeId, price);
            }

        }

        return onlinePriceMap;
    }

    private Price getPriceBasedOnClassification(int storeId, WorkingDocument wd, Extracts extracts, ContextMessage context) {
        Price price = null;
        try {
            switch (extracts.getContentExtract().getCatentrySubType()) {
                case "NV":
                    price = getOfferPrice(storeId, wd, extracts);
                    break;
                case "V":
                    price = getProductPrice(storeId, wd, extracts);
                    break;
                case "B":
                case "O":
                    price = getBundlePrice(storeId, extracts);
                    break;
                case "C":
                    price = getCollectionsPrice(storeId, extracts);
                    break;

            }
        } catch (SHCWxsException | SearchCommonException | NullPointerException e) {
        	// This handles a NullPointer runtime exception as the pricing jar throws it outside the agreement - SHCWxsException.
            metricManager.incrementCounter("CountPriceGridOfferForStoreID-"+storeId);
            metricManager.incrementCounter("Exceptions-PricingJAR_"+storeId+"-MISC");
            LOGGER.error(ErrorCode.PRICING_ERROR.name() + " for SSIN " +wd.getExtracts().getSsin() +". " + e.getMessage());
        }
        return price;
    }

    private Price getOfferPrice(int storeId, WorkingDocument wd, Extracts extracts) throws SHCWxsException, SearchCommonException {
        Price price = null;
        try {
        	price = pricingService.getOfferPrice(storeId, extracts.getOfferIds().get(0));
        } catch (SHCWxsException e) {
        	LOGGER.error("Pricing JAR error for offer-id " +extracts.getOfferIds().get(0) +", (SSIN: "+wd.getExtracts().getSsin() +"), store-id: "+storeId+". " + e.getMessage());
        }

        boolean hasZeroPrice = price!=null && price.hasZeroPrice();
        if (fallbackPriceFlag && (price==null || hasZeroPrice) ) {
            metricManager.incrementCounter("CountPriceGridOfferForStoreID-"+storeId);
            metricManager.incrementCounter(hasZeroPrice ? ("CountPriceJAR-ZeroPriceForStoreID-"+storeId) : ("Exceptions-PricingJAR_"+storeId+"-MISC"));
            String priceGridCallReason = hasZeroPrice ? GlobalConstants.PRICEJAR_ZERO_PRICE : GlobalConstants.PRICEJAR_ERROR;
            LOGGER.info(priceGridCallReason + "Calling pricing grid as fallback for offer-id: {}, store-id: {}.", extracts.getOfferIds().get(0), storeId);
            boolean isJarNull = price ==null;
            double jarPriceVal = isJarNull ? -1 : priceAlgorithm.nonZeroPrice(price);
            price = pricingGridService.getOfferPrice(storeId, extracts.getOfferIds().get(0));
            if(isJarNull || jarPriceVal <=0){
                priceLogger.debug("OfferId:" + extracts.getOfferIds().get(0) + "  " + "JARPrice:" + (isJarNull ? null :jarPriceVal) + "  " + "GRIDPrice:" + priceAlgorithm.nonZeroPrice(price));
            }
        } else {
            metricManager.incrementCounter("Counter-PriceJARCalls");
            metricManager.incrementCounter("CountPriceJAROfferForStoreID-  "+storeId);
        }
        return price;
    }

    private Price getProductPrice(int storeId, WorkingDocument wd, Extracts extracts) throws SHCWxsException, SearchCommonException {
        Price price = null;
        try {
        	price = pricingService.getProductPrice(storeId, extracts.getSsin());
        } catch (SHCWxsException e) {
            LOGGER.error("Pricing JAR error for SSIN: " +wd.getExtracts().getSsin() +", store-id: "+storeId+". " + e.getMessage());
        }
        boolean hasZeroPrice = price!=null && price.hasZeroPrice();
        if (fallbackPriceFlag && (price==null || hasZeroPrice) ) {
            metricManager.incrementCounter("CountPriceGridProductForStoreID-"+storeId);
            metricManager.incrementCounter(hasZeroPrice ? ("CountPriceJAR-ZeroPriceForStoreID-"+storeId) : ("Exceptions-PricingJAR_"+storeId+"-MISC"));
            String priceGridCallReason = hasZeroPrice ? GlobalConstants.PRICEJAR_ZERO_PRICE : GlobalConstants.PRICEJAR_ERROR;
            LOGGER.info(priceGridCallReason + "Calling pricing grid as fallback for SSIN: {}, store-id: {}.", extracts.getSsin(), storeId);
            boolean isJarNull = price ==null;
            double jarPriceVal = isJarNull ? -1 : priceAlgorithm.nonZeroPrice(price);
            price = pricingGridService.getProductPrice(storeId, extracts.getSsin());
            if(isJarNull || jarPriceVal <=0){
                priceLogger.debug("SSIN:" + extracts.getSsin() + "  " + "JARPrice:" + (isJarNull ? null :jarPriceVal) + "  " + "GRIDPrice:" + priceAlgorithm.nonZeroPrice(price));
            }
        } else {
            metricManager.incrementCounter("Counter-PriceJARCalls");
            metricManager.incrementCounter("CountPriceJARProductForStoreID-  "+storeId);
        }
        return price;
    }

    private Price getBundlePrice(int storeId, Extracts extracts) throws SHCWxsException, SearchCommonException {
        return pricingService.getBundlePrice(storeId, extracts.getSsin());
    }

    /***
     * @param storeId
     * @param extracts
     * @return
     */
    private Price getCollectionsPrice(int storeId, Extracts extracts) throws SHCWxsException, SearchCommonException {
        return pricingGridService.getProductPrice(storeId, extracts.getSsin());
    }

    @Override
    public Decision validate(Map<Integer, Price> sources, WorkingDocument wd, ContextMessage context) {

        ////Fixing the possible bug --- validity is checked based on the last entry of the loop.
    	boolean productValid =false;
        ValidationResults offerResults = null;

        for (Entry<Integer, Price> source : sources.entrySet()) {
            offerResults = validator.validate(source.getValue(), rules, context);
            productValid |= offerResults.isPassed();
        }

        Decision decision = wd.getDecision();
        decision.setId(wd.getExtracts().getSsin());
        decision.setIdType("SSIN");
        ValidationResults productResult = DecisionUtil.constructValidationResults(productValid, rules.get(0).getClass().getSimpleName());
        decision.addValidationResults(productResult);
        decision.setRejected(!productValid);
        decision.setOfferId(context.getOfferId());
        decision.setStore(context.getStoreName());
        decision.setSites(DecisionUtil.getSitesList(Stores.getStore(context.getStoreName()).getEligibleSites(wd, context)));
        decision.setOpsUuid(context.getTimestamp());

        return decision;
    }

    @Override
    protected Extracts extract(Map<Integer, Price> price, WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();
        extracts.getPriceExtract().setOnlinePrice(price);
        return extracts;
    }
}
