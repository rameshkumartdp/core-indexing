package com.shc.ecom.search.extract.components.offer;

import com.shc.common.index.rules.Decision;
import com.shc.common.index.rules.ValidationResults;
import com.shc.ecom.gb.doc.common.WebIdName;
import com.shc.ecom.gb.doc.offer.CountryGrp;
import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.gb.doc.offer.ProductOption;
import com.shc.ecom.gb.doc.offer.VocTags;
import com.shc.ecom.search.common.constants.*;
import com.shc.ecom.search.common.exception.ErrorCode;
import com.shc.ecom.search.common.greenbox.GBServiceFacade;
import com.shc.ecom.search.common.kafka.SearchKafkaProducer;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.messages.MessageType;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.components.fitments.AutomotiveOffer;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.SellerExtract.SellerTierRank;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.rules.IRule;
import com.shc.ecom.search.util.DecisionUtil;
import com.shc.ecom.search.util.OfferUtil;
import com.shc.ecom.search.util.VocTagUtil;
import com.shc.ecom.search.validator.Validator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Local;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author rgopala
 */
public class OfferExtractComponent extends ExtractComponent<List<Offer>> {

    private static final long serialVersionUID = 1683453535237061980L;

    private static final Logger LOGGER = LoggerFactory.getLogger(OfferExtractComponent.class);

    @Autowired
    private GBServiceFacade gbServiceFacade;

    @Autowired
    private Validator validator;

    @Resource(name = "offerRules")
    private List<IRule<Offer>> offerRules;

    @Resource(name = "storeRules")
    private List<IRule<List<Offer>>> storeRules;

    @Autowired
    private SearchKafkaProducer kafkaProducer;

    @Override
    protected List<Offer> get(WorkingDocument wd, ContextMessage context) {
        return getOfferDocuments(getOfferIds(wd));
    }

    @Override
    public Decision validate(List<Offer> source, WorkingDocument wd, ContextMessage context) {
        // if all the offers under an SSIN fail validation, only then reject the product.
        boolean productValid = false;
        boolean offerValid = false;
        ValidationResults offerResults = null;
        ValidationResults productResult = null;
        Decision decision = wd.getDecision();
        boolean singleOffer = (source!= null && (source.size() == 1));
        List<Offer> originalSource = new ArrayList<>(source);
        for (Iterator<Offer> offerIterator = source.iterator(); offerIterator.hasNext(); ) {
            Offer currentOffer = offerIterator.next();
            offerResults = validator.validate(currentOffer, offerRules, context);
            offerValid = offerResults.isPassed();

            /*Cant rely on offerResults.isPassed() because it will be true for all offers when in DRYRUN mode,
            we still need to drop invalid offers for future rules, eg: OutOfStockRule
              */
            if (MessageType.getMessageType(context.getMessageId()) == MessageType.DRYRUN_OFFERID) {
                offerValid = offerResults.isOfferValidForAllRules();
            }

            if (!offerValid) {
                Decision offerDecision = DecisionUtil.generateDecision(wd,context,currentOffer.getId(),offerResults);
                DecisionUtil.incrementCounterAddToKafka(metricManager, kafkaProducer, offerDecision, offerResults.getRejectedRule());
                wd.getExtracts().getOfferIds().remove(currentOffer.getId());
                // Remove the offers from the source that failed validation.
                offerIterator.remove();
            }
            productValid |= offerValid;
        }

        decision.setId(wd.getExtracts().getSsin());
        decision.setIdType("SSIN");
        decision.setOfferId("");
        decision.setStore(context.getStoreName());
        decision.setSites(DecisionUtil.getSitesList(Stores.getStore(context.getStoreName()).getEligibleSites(wd, context)));
        decision.setOpsUuid(context.getTimestamp());
        if(singleOffer){
            decision.addValidationResults(offerResults);
        } else{
            productResult = DecisionUtil.constructValidationResults(productValid, Metrics.NO_VALID_OFFERS_FOR_SSIN_RULE);
            decision.addValidationResults(productResult);
        }

        if (!productValid) {
            decision.setRejected(true);
            return decision;
        }

        // If atleast one offer passes the offerRule, then check the validity of the product for the store(flow)
        ValidationResults storeValidityResult = validator.validate(originalSource, storeRules, context);
        if (storeValidityResult != null) {
            decision.addValidationResults(storeValidityResult);
        }

        productValid = storeValidityResult.isPassed();
        decision.setRejected(!productValid);
        decision.setOfferId(context.getOfferId());
        return decision;
    }

    @Override
    protected Extracts extract(List<Offer> offers, WorkingDocument wd, ContextMessage context) {
        Offer offer = offers.get(0);
        Extracts extracts = wd.getExtracts();

        boolean isMartketPlaceForTablet = false;
        boolean isOfferDisplayEligible = false;

        isOfferDisplayEligible = false;
        for (Offer ofr : offers) {
            if (BooleanUtils.toBoolean(ofr.getOperational().getSites().getSears().getIsDispElig())) {
                // or ContentExtract.isBundleDisplayElligible
                isOfferDisplayEligible = true;
                break;
            }
        }

        if (offer.getMarketplace() != null && offer.getMarketplace().getProgramType() != null) {
            isMartketPlaceForTablet = isMarketPlaceForTablet(offer);
        }
        extracts.getOfferExtract().setSearsDisplayElligible(!isMartketPlaceForTablet && isOfferDisplayEligible);

        extracts.getOfferExtract().setName(offer.getName());

        // One offer extraction is ok here
        
        extracts.getOfferExtract().setEGiftElig(BooleanUtils.toBoolean(offer.getDispTags().getIsEGiftElig()));
        extracts.getOfferExtract().setMpPgmType(offer.getClassifications().getIsMpPgmType());
        extracts.getOfferExtract().setCatConfScore(offer.getMarketplace().getCatConfScore());
        extracts.getOfferExtract().setDealFlash(BooleanUtils.toBoolean(offer.getDispTags().getIsDealFlash()));
        extracts.getOfferExtract().setOfferId(StringUtils.defaultString(offer.getId(), StringUtils.EMPTY));
        extracts.getOfferExtract().setGndFreeStartDt(offer.getShipping().getMode().getGnd().getFree().getStartDt());
        extracts.getOfferExtract().setGndFreeEndDt(offer.getShipping().getMode().getGnd().getFree().getEndDt());
        extracts.getOfferExtract().setStreetDt(offer.getPresell().getStreetDt());
        extracts.getOfferExtract().setSites(offer.getSites());
        extracts.getOfferExtract().setSitesDispEligibility(getAllSitesDispEligInfo(offers));
        extracts.getOfferExtract().setSitesEligibility(getAllSitesEligInfo(offers));
        // All other cases where single or multiple offers don't change the logic
        extracts.getOfferExtract().setAutomotiveOffers(getAutoMotiveOffers(offers));
        extracts.getOfferExtract().setItemCondition(getItemCondition(offers));
        extracts.getOfferExtract().setDivision(getDivision(offers));

        //TODO: Remove the old one
        extracts.getOfferExtract().setFreeShipping(isFreeShipping(offers));
        extracts.getOfferExtract().setFreeShippingMap(isFreeShippingOffer(offers));
        extracts.getOfferExtract().setStoreOffersMap(getStoreOffersMap(offers, context));
        extracts.getOfferExtract().setInternalRimSts(getInternalRimSts(offers));
        extracts.getOfferExtract().setSoldBy(getSoldBy(offers));
        extracts.getOfferExtract().setGeoSpotTag(getVocTagNames(offers));
        extracts.getOfferExtract().setVocTagNames(getVocTagNames(offers));
        extracts.getOfferExtract().setNewestOnlineDateBySite(getNewestOnlineDateBySites(offers));
        extracts.getOfferExtract().setOldestOnlineDateBySite(getOldestOnlineDateBySites(offers));
        extracts.getOfferExtract().setGiftCardRank(getGiftCardRank(offers));
        extracts.getOfferExtract().setClickUrl(getClickUrl(offers));
        extracts.getOfferExtract().setIntlShipElig(isInternationShipEligible(offers));
        extracts.getOfferExtract().setIntlGroupIds(getIntlGroupIds(offers));
        extracts.getOfferExtract().setSellerId(getSellerIds(offers));
        extracts.getOfferExtract().setOfferToSellerIdMap(getOfferToSellerIdMap(offers));
        extracts.getOfferExtract().setDoNotEmailMe(isDontEmailMe(offers));
        extracts.getOfferExtract().setMailable(isMailable(offers));
        extracts.getOfferExtract().setAlcohol(isAlcohol(offers));
        extracts.getOfferExtract().setPerishable(isPerishable(offers));
        extracts.getOfferExtract().setRefrigeration(isRefrigeration(offers));
        extracts.getOfferExtract().setTobacco(isTobacco(offers));
        extracts.getOfferExtract().setFreezing(isFreezing(offers));
        extracts.getOfferExtract().setDeliveryElig(isDeliveryElig(offers));
        extracts.getOfferExtract().setAutomotive(isAutomotive(offers));
        extracts.getOfferExtract().setEnergyStar(isEnergyStar(offers));
        extracts.getOfferExtract().setKmartPrElig(isKmartPRElig(offers));
        extracts.getOfferExtract().setConfigureTech(isConfigureTech(offers));
        extracts.getOfferExtract().setOutletItem(isOutletItem(offers));
        extracts.getOfferExtract().setSimiElig(isSimiElig(offers));
        extracts.getOfferExtract().setReserveIt(isReserveIt(offers));
        extracts.getOfferExtract().setWebExcl(isWebExcl(offers));
        extracts.getOfferExtract().setSpuElig(isSpuElig(offers));
        extracts.getOfferExtract().setLayawayElig(isLayaway(offers));
        extracts.getOfferExtract().setSTSElig(isSTSElig(offers));
        extracts.getOfferExtract().setMatureContent(isMatureContent(offers));
        extracts.getOfferExtract().setDirectSears(isDirectSears(offers));
        extracts.getOfferExtract().setShipElig(isShipElig(offers));
        extracts.getOfferExtract().setSResElig(isSResElig(offers));
        extracts.getOfferExtract().setDfltFfmDisplay(getDfltFfmDisplay(offers));
        extracts.getOfferExtract().setViewOnly(getViewOnly(offers));
        extracts.getOfferExtract().setSellerName(getSellers(offers));
        extracts.getOfferExtract().setOfferTier(getOfferTier(offers));
        extracts.getOfferExtract().setOfferIdSoldByMap(getOfferIdSoldByMap(offers));
        extracts.getOfferExtract().setFreeShipThreshold(getFreeShipThreshold(offers));
        extracts.getOfferExtract().setOfferMailability(getOfferMailability(offers));
        extracts.getOfferExtract().setOfferWeight(getOfferWeight(offers));
        // Need to be decommisioned/Needs a logic changes/schema changes
        extracts.getOfferExtract().setKsn(getKsn(offers)); //Do not decommission, being used for ensure availability, see IAS Components
        extracts.getOfferExtract().setUpc(getUpc(offers));
        extracts.getOfferExtract().setChannels(getFulfillmentChannels(offers));
        extracts.getOfferExtract().setViewOnlyOffers(getViewOnlyOffers(offers));
        extracts.getOfferExtract().setSTSChannels(getStsChannel(offers));
        extracts.getOfferExtract().setLinkedSwatchProductOptions(getLinkedSwatchProductOptions(offers));
        extracts.getOfferExtract().setMainImgs(getMainImg(offers));
        extracts.getOfferExtract().setSwatchImgs(getSwatchImg(offers));
        extracts.getOfferExtract().setSearsDispEligibilityStatus(getSearsDispEligStatus(offers));
        
        //Local Ad Parent Id fix ADD JIRA number here
        extracts.getOfferExtract().setParentIds(getParentIds(offers));
        extracts.getOfferExtract().setKmartOfferParentId(getKmartOfferParentId(offers));
        extracts.getOfferExtract().setOfferIdParentId(getOfferIdParentId(offers));

        //Ensure Availability
        extracts.getOfferExtract().setOfferIdKsnMap(getOfferIdKsnMap(offers));

        //eBay Aggregator Id
        extracts.getOfferExtract().setAggregatorId(offer.getMarketplace().getAggregatorId());

        // fields specific to COMMERCIAL
        if(offer.getB2b()!=null){
            extracts.getOfferExtract().setSubUpPid(offer.getB2b().getSubUpPid());
            extracts.getOfferExtract().setSubDownPid(offer.getB2b().getSubDownPid());
            extracts.getOfferExtract().setAltPid(offer.getB2b().getAltPid());
            extracts.getOfferExtract().setReptPid(offer.getB2b().getReptPid());
            extracts.getOfferExtract().setTransRsn(offer.getB2b().getTransRsn());
            extracts.getOfferExtract().setTruckLoadQty(offer.getB2b().getTruckLoadQty()!=null?offer.getB2b().getTruckLoadQty().toString():"0");
            extracts.getOfferExtract().setDeliveryCat(offer.getB2b().getDelivCat());
            extracts.getOfferExtract().setFfmClass(offer.getB2b().getFfmClass());
            extracts.getOfferExtract().setDiscntDt(offer.getB2b().getDiscntDt());
        }
        
        return extracts;
    }

    /**
     * As we index non-display eligible sears offers (for tablets, as offline), we want to maintain the display eligible status of offers in order to 
     * filter products which are not display eligible in certain promotions, say.
     * @param offers
     * @return
     */
    private Map<String, Boolean> getSearsDispEligStatus(List<Offer> offers) {
    	Map<String, Boolean> searsDispEligibility = new HashMap<>();
    	for(Offer offer : offers) {
    		String offerID = offer.getId();
    		boolean dispEligOnSears = BooleanUtils.toBoolean(offer.getOperational().getSites().getSears().getIsDispElig());
    		if(StringUtils.isNotEmpty(offerID)) {
    			searsDispEligibility.put(offerID, dispEligOnSears);
    		}
    	}
    	return searsDispEligibility;
    }
  
    private Map<String, String> getMainImg(List<Offer> offers) {
    	Map<String, String> mainImgs = new HashMap<>();
    	for(Offer offer : offers) {
			String offerID = offer.getId();
			String mainImg = StringUtils.EMPTY;
			if(CollectionUtils.isNotEmpty(offer.getMainImg())) {
				mainImg = offer.getMainImg().get(0).getSrc();
			}
			
			if(StringUtils.isNotEmpty(offerID) && StringUtils.isNotEmpty(mainImg)) {
				mainImgs.put(offerID, mainImg);
			}
    	}
    	return mainImgs;
    }
    
    
    private Map<String, String> getSwatchImg(List<Offer> offers) {
    	Map<String, String> swatchImgs = new HashMap<>();
    	for(Offer offer : offers) {
			String offerID = offer.getId();
			String swatchImg = StringUtils.EMPTY;
			if(CollectionUtils.isNotEmpty(offer.getSwatchImg())) {
				swatchImg = offer.getSwatchImg().get(0).getSrc();
			}
			
			if(StringUtils.isNotEmpty(offerID) && StringUtils.isNotEmpty(swatchImg)) {
				swatchImgs.put(offerID, swatchImg);
			}
    	}
    	return swatchImgs;
    }

	private Map<String, List<ProductOption>> getLinkedSwatchProductOptions(List<Offer> offers) {
		Map<String, List<ProductOption>> swatchProductOptions = new HashMap<>();
		for(Offer offer : offers) {
			String offerID = offer.getId();
			List<ProductOption> productOptions = offer.getAssocs().getLinkedSwatch();
			if(StringUtils.isNotEmpty(offerID) && CollectionUtils.isNotEmpty(productOptions)) {
				swatchProductOptions.put(offerID, productOptions);
			}
		}
		return swatchProductOptions;
	}

	private boolean isMarketPlaceForTablet(Offer offer) {
        String programType = offer.getMarketplace().getProgramType();
        return !("FBM".equalsIgnoreCase(programType)
                || "DSS".equalsIgnoreCase(programType)
                || "FBS".equalsIgnoreCase(programType)
                || "CPC".equalsIgnoreCase(programType));
    }

    private Map<Sites, Boolean> getAllSitesEligInfo(List<Offer> offers) {
        Map<Sites, Boolean> sitesDispEligibility = getAllSitesDispEligInfo(offers);
        Map<Sites, Boolean> sitesEligibility = new HashMap<>();

        //For non-variation offers will have only one offer. For variation, all are supposed to have same eligibilities
        Offer currentOffer = offers.get(0);
        sitesEligibility.put(Sites.SEARS, sitesDispEligibility.get(Sites.SEARS) && org.apache.commons.lang.BooleanUtils.toBoolean(currentOffer.getSites().contains("sears")));
        sitesEligibility.put(Sites.KMART, sitesDispEligibility.get(Sites.KMART) && org.apache.commons.lang.BooleanUtils.toBoolean(currentOffer.getSites().contains("kmart")));
        sitesEligibility.put(Sites.MYGOFER, sitesDispEligibility.get(Sites.MYGOFER) && org.apache.commons.lang.BooleanUtils.toBoolean(currentOffer.getSites().contains("mygofer")));
        sitesEligibility.put(Sites.SEARSPR, sitesDispEligibility.get(Sites.SEARSPR) && org.apache.commons.lang.BooleanUtils.toBoolean(currentOffer.getSites().contains("puertorico")));
        sitesEligibility.put(Sites.COMMERCIAL, sitesDispEligibility.get(Sites.COMMERCIAL) && org.apache.commons.lang.BooleanUtils.toBoolean(currentOffer.getSites().contains("scom")));
        return sitesEligibility;
    }

    //TODO: Check with Ravi!
    private Map<Sites, Boolean> getAllSitesDispEligInfo(List<Offer> offers) {
        Map<Sites, Boolean> sitesDispEligibility = new HashMap<>();
        for (Offer offer : offers) {
            sitesDispEligibility.put(Sites.SEARS, BooleanUtils.toBoolean(sitesDispEligibility.get(Sites.SEARS)) || BooleanUtils.toBoolean(offer.getOperational().getSites().getSears().getIsDispElig()));
            sitesDispEligibility.put(Sites.KMART, BooleanUtils.toBoolean(sitesDispEligibility.get(Sites.KMART)) || BooleanUtils.toBoolean(offer.getOperational().getSites().getKmart().getIsDispElig()));
            sitesDispEligibility.put(Sites.MYGOFER, BooleanUtils.toBoolean(sitesDispEligibility.get(Sites.MYGOFER)) || BooleanUtils.toBoolean(offer.getOperational().getSites().getMygofer().getIsDispElig()));
            sitesDispEligibility.put(Sites.SEARSPR, BooleanUtils.toBoolean(sitesDispEligibility.get(Sites.SEARSPR)) || BooleanUtils.toBoolean(offer.getOperational().getSites().getPuertorico().getIsDispElig()));
            sitesDispEligibility.put(Sites.COMMERCIAL, BooleanUtils.toBoolean(sitesDispEligibility.get(Sites.COMMERCIAL)) || BooleanUtils.toBoolean(offer.getOperational().getSites().getScom().getIsDispElig()));
        }
        return sitesDispEligibility;
    }

    private Set<String> getParentIds(List<Offer> offers) {
        Set<String> parentIds = new HashSet<>();
        for (Offer offer : offers) {
            if (StringUtils.isNotEmpty(offer.getIdentity().getParentId())) {
                parentIds.add(offer.getIdentity().getParentId());
            }
        }
        return parentIds;
    }


    // For cases where multiple kmart offers are grouped under single SSIN (typically a variation) and each offer has different parent-id, we consider only one of the parent-ids.
    // The scope for a kmart specific parent-id is to use it for internal services like BSO and Prodstats which might not have data with "SSIN" but has data with Kmart Parent-ID.
    private String getKmartOfferParentId(List<Offer> offers) {
        String parentId = StringUtils.EMPTY;
        for (Offer offer : offers) {
            if (StringUtils.equalsIgnoreCase(offer.getFfm().getSoldBy(), "Kmart")) {
                parentId = offer.getIdentity().getParentId();
            }
        }
        return parentId;
    }

    /**
     * Stores the map of offerId and associated parentID of corresponding offerId.
     *
     * @param offers for given ssin
     * @return map of offerID and parentID
     */
    private Map<String, String> getOfferIdParentId(List<Offer> offers) {
        Map<String, String> offerIdParentId = new HashMap<>();
        for (Offer offer : offers) {
            String parentSsin = offer.getIdentity().getParentId();
            String offerID = offer.getId();
            offerIdParentId.put(offerID, parentSsin);
        }
        return offerIdParentId;
    }

    private String getFreeShipThreshold(List<Offer> offers) {

        double lowestValue = Double.MAX_VALUE;
        String freeShipThreshold = null;

        for (Offer offer : offers) {

            String offerFreeShipThreshold = offer.getShipping().getFreeShip().getFreeShipThreshold();

            if (!NumberUtils.isNumber(offerFreeShipThreshold)) {
                continue;
            }

            Double offerFreeShipValue = Double.parseDouble(offerFreeShipThreshold);

            if (offerFreeShipValue < lowestValue) {
                lowestValue = offerFreeShipValue;
                freeShipThreshold = offerFreeShipThreshold;
            }
        }

        return freeShipThreshold;

    }

    private List<String> getUpc(List<Offer> offers) {
        List<String> upcs = new ArrayList<>();

        for (Offer offer : offers) {
            if (StringUtils.isNotEmpty(offer.getAltIds().getUpc())) {
                upcs.add(offer.getAltIds().getUpc());
            }
        }
        return upcs;
    }

    private List<String> getKsn(List<Offer> offers) {
        List<String> ksns = new ArrayList<>();

        for (Offer offer : offers) {
            if (StringUtils.isNotEmpty(offer.getAltIds().getKsn())) {
                ksns.add(offer.getAltIds().getKsn());
            }
        }
        return ksns;
    }

    private List<String> getStsChannel(List<Offer> offers) {
        List<String> shcStsChannels = new ArrayList<>();
        for (Offer offer : offers) {

            String stsChannel = offer.getFfm().getSTSChannel();
            if (StringUtils.isEmpty(stsChannel)) {
                continue;
            }
            shcStsChannels.add(stsChannel);
        }
        return shcStsChannels;
    }

    private List<String> getFulfillmentChannels(List<Offer> offers) {
        List<String> fulfillmentChannels = new ArrayList<>();
        for (Offer offer : offers) {
            String fulfillmentChannel = offer.getFfm().getChannel();
            if (StringUtils.isNotEmpty(fulfillmentChannel)) {
                fulfillmentChannels.add(fulfillmentChannel);
            }
        }
        return fulfillmentChannels;
    }

    private Map<String, String> getViewOnlyOffers(List<Offer> offers) {
        Map<String, String> viewOnlyOffers = new HashMap<>();
        for (Offer offer : offers) {
            String fulfillmentChannel = offer.getFfm().getChannel();
            if (StringUtils.isNotEmpty(fulfillmentChannel) && fulfillmentChannel.equals(GlobalConstants.VIEW_ONLY_FFM_CHANNEL)) {
                viewOnlyOffers.put(offer.getId(),fulfillmentChannel);
            }
        }
        return viewOnlyOffers;
    }

    private String getOfferTier(List<Offer> offers) {
        int highestRank = Integer.MIN_VALUE;
        for (Offer offer : offers) {
            String offerTier = offer.getMarketplace().getSeller().getTier().getName();
            int rank = SellerTierRank.getRank(offerTier);

            if (rank > highestRank) {
                highestRank = rank;
            }
        }
        return SellerTierRank.getSellerTier(highestRank);
    }

    // TODO: Need to revisit what should be the logic here.
    private String getDivision(List<Offer> offers) {
        String division = null;
        for (Offer offer : offers) {
            List<WebIdName> wedIdNames = offer.getTaxonomy().getStore().gethierarchy();
            if (CollectionUtils.isNotEmpty(wedIdNames)) {
                WebIdName webIdName = wedIdNames.get(0);
                String name = webIdName.getName();
                if (StringUtils.isNotEmpty(name)) {
                    division = name;
                    break;
                }
            }
        }
        return division;
    }


    private List<AutomotiveOffer> getAutoMotiveOffers(List<Offer> offers) {
        List<AutomotiveOffer> automotiveOffers = new ArrayList<>();
        for (Offer offer : offers) {
            automotiveOffers.add(new AutomotiveOffer().offerId(offer.getId())
                    .uid(offer.getIdentity().getUid())
                    .brandCodeId(offer.getBrandCodeId())
                    .mfrPartNo(offer.getMfrPartno()));
        }
        return automotiveOffers;
    }

    private Map<String, Boolean> isFreeShippingOffer(List<Offer> offers) {
        return OfferUtil.getFreeShippingMap(offers);
    }

    //Kept since other fields rely on the old logic. Changed this method to use the hashMap to perform the old logic.
    private boolean isFreeShipping(List<Offer> offers) {
        boolean isFreeShipping = false;
        Map<String, Boolean> freeShippingOfferMap = isFreeShippingOffer(offers);
        for (Map.Entry<String, Boolean> entry : freeShippingOfferMap.entrySet()) {
            if(entry.getValue().booleanValue()) {
                isFreeShipping = true;
                break;
            }
        }
        return isFreeShipping;
    }

    private Map<String, List<String>> getStoreOffersMap(List<Offer> offers, ContextMessage context) {
        Map<String, List<String>> storeOffersMap = new HashMap<>();
        for (Offer offer : offers) {
            String isMpPgmType = offer.getClassifications().getIsMpPgmType();
            String storeName = StringUtils.isNotEmpty(isMpPgmType) ? isMpPgmType : offer.getFfm().getSoldBy().toLowerCase();

            if (Stores.SEARSPR.matches(context.getStoreName())) {
                storeName = Stores.SEARSPR.getStoreName();
            }

            List<String> offerIds = null;
            if (storeOffersMap.containsKey(storeName)) {
                offerIds = storeOffersMap.get(storeName);
            } else {
                offerIds = new ArrayList<>();
            }
            offerIds.add(offer.getId());
            String aggregatedStoreName = storeName.toLowerCase();
            if (BucketFilters.FBM.getPgrmTypeFilters().contains(aggregatedStoreName.toUpperCase())) {
                aggregatedStoreName = BucketFilters.FBM.name().toLowerCase();
            }
            storeOffersMap.put(aggregatedStoreName, offerIds);
        }
        return storeOffersMap;
    }

    private Map<String, String> getOfferIdSoldByMap(List<Offer> offers) {
        Map<String, String> offerIdSoldByMap = new HashMap<>();
        for (Offer offer : offers) {
            if (StringUtils.isNotEmpty(offer.getFfm().getSoldBy())) {
                offerIdSoldByMap.put(offer.getId(), offer.getFfm().getSoldBy());
            }
        }
        return offerIdSoldByMap;
    }

    private String getItemCondition(List<Offer> offers) {
        for (Offer offer : offers) {
            if (StringUtils.isNotBlank(offer.getCondition().getStatus())) {
                return offer.getCondition().getStatus();
            }
        }
        return ItemCondition.NEW.getStatus().toUpperCase();
    }

    private Set<String> getSellers(List<Offer> offers) {
        Set<String> sellers = new HashSet<>();
        for (Offer offer : offers) {
            if (StringUtils.isNotEmpty(offer.getMarketplace().getSeller().getName())) {
                sellers.add(offer.getMarketplace().getSeller().getName());
            }
        }
        return sellers;
    }

    private Set<String> getViewOnly(List<Offer> offers) {
        Set<String> viewOnlySet = new HashSet<>();
        for (Offer offer : offers) {
            if (StringUtils.isNotEmpty(offer.getDispTags().getViewOnly())) {
                viewOnlySet.add(offer.getDispTags().getViewOnly());
            }
        }
        return viewOnlySet;
    }

    private Set<String> getDfltFfmDisplay(List<Offer> offers) {
        Set<String> dfltFfmDisplay = new HashSet<>();
        for (Offer offer : offers) {
            if (StringUtils.isNotEmpty(offer.getFfm().getDfltFfmDisplay())) {
                dfltFfmDisplay.add(offer.getFfm().getDfltFfmDisplay());
            }
        }
        return dfltFfmDisplay;
    }

    private boolean isSResElig(List<Offer> offers) {
        boolean isSResElig = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getFfm().getIsSResElig())) {
                isSResElig = true;
                break;
            }
        }
        return isSResElig;
    }

    private boolean isShipElig(List<Offer> offers) {
        boolean isShipElig = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getFfm().getIsShipElig())) {
                isShipElig = true;
                break;
            }
        }
        return isShipElig;
    }

    private boolean isDirectSears(List<Offer> offers) {
        boolean isDirectSears = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getMarketplace().getSeller().getIsDirectSears())) {
                isDirectSears = true;
                break;
            }
        }
        return isDirectSears;
    }

    private boolean isMatureContent(List<Offer> offers) {
        boolean isMatureContent = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getLegal().getIsMatureContent())) {
                isMatureContent = true;
                break;
            }
        }
        return isMatureContent;
    }

    private boolean isSTSElig(List<Offer> offers) {
        boolean isSTSElig = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getFfm().getIsSTSElig())) {
                isSTSElig = true;
                break;
            }
        }
        return isSTSElig;
    }

    private boolean isLayaway(List<Offer> offers) {
        boolean isLayaway = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getFfm().getIsLayawayElig())) {
                isLayaway = true;
                break;
            }
        }
        return isLayaway;
    }

    private boolean isSpuElig(List<Offer> offers) {
        boolean isSpuElig = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getFfm().getIsSpuElig())) {
                isSpuElig = true;
                break;
            }
        }
        return isSpuElig;
    }

    private boolean isWebExcl(List<Offer> offers) {
        boolean isWebExcl = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getFfm().getIsWebExcl())) {
                isWebExcl = true;
                break;
            }
        }
        return isWebExcl;
    }

    private boolean isReserveIt(List<Offer> offers) {
        boolean isReserveIt = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getOperational().getSites().getSears().getIsReserveIt())) {
                isReserveIt = true;
                break;
            }
        }
        return isReserveIt;
    }

    private boolean isSimiElig(List<Offer> offers) {
        boolean isSimiElig = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getShipping().getIsSimiElig())) {
                isSimiElig = true;
                break;
            }
        }
        return isSimiElig;
    }

    private boolean isOutletItem(List<Offer> offers) {
        boolean isOutletItem = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getDispTags().getIsOutletItem())) {
                isOutletItem = true;
                break;
            }
        }
        return isOutletItem;
    }

    private boolean isConfigureTech(List<Offer> offers) {
        boolean isConfigureTech = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getDispTags().getIsConfigureTech())) {
                isConfigureTech = true;
                break;
            }
        }
        return isConfigureTech;
    }

    private boolean isKmartPRElig(List<Offer> offers) {
        boolean isKmartPRElig = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getClassifications().getIskmartPRelig())) {
                isKmartPRElig = true;
                break;
            }
        }
        return isKmartPRElig;
    }

    private boolean isEnergyStar(List<Offer> offers) {
        boolean isEnergyStar = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getDispTags().getIsEnergyStar())) {
                isEnergyStar = true;
                break;
            }
        }
        return isEnergyStar;
    }

    private boolean isAutomotive(List<Offer> offers) {
        boolean isAutomotive = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getClassifications().getIsAutomotive())) {
                isAutomotive = true;
                break;
            }
        }
        return isAutomotive;
    }

    private boolean isDeliveryElig(List<Offer> offers) {
        boolean isDeliveryElig = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getFfm().getIsDeliveryElig())) {
                isDeliveryElig = true;
                break;
            }
        }
        return isDeliveryElig;
    }

    private boolean isFreezing(List<Offer> offers) {
        boolean isFreezing = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getGrocery().getIsFreezing())) {
                isFreezing = true;
                break;
            }
        }
        return isFreezing;
    }

    private boolean isTobacco(List<Offer> offers) {
        boolean isTobacco = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getGrocery().getIsTobacco())) {
                isTobacco = true;
                break;
            }
        }
        return isTobacco;
    }

    private boolean isRefrigeration(List<Offer> offers) {
        boolean isRefrigeration = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getGrocery().getIsRefrigeration())) {
                isRefrigeration = true;
                break;
            }
        }
        return isRefrigeration;
    }

    private boolean isPerishable(List<Offer> offers) {
        boolean isPerishable = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getGrocery().getIsPerishable())) {
                isPerishable = true;
                break;
            }
        }
        return isPerishable;
    }

    private boolean isAlcohol(List<Offer> offers) {
        boolean isAlcohol = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getGrocery().getIsAlcohol())) {
                isAlcohol = true;
                break;
            }
        }
        return isAlcohol;
    }

    private boolean isMailable(List<Offer> offers) {
        boolean isMailable = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getShipping().getIsMailable())) {
                isMailable = true;
                break;
            }
        }
        return isMailable;
    }

    private Map<String, Boolean> getOfferMailability(List<Offer> offers) {
    	Map<String, Boolean> offerMailability = new HashMap<>();
    	for (Offer offer : offers) {
            offerMailability.put(offer.getId(), BooleanUtils.toBoolean(offer.getShipping().getIsMailable()));

        }
    	return offerMailability;
    }

    private Map<String, Double> getOfferWeight(List<Offer> offers) {
    	Map<String, Double> offerWeight = new HashMap<>();
    	for(Offer offer : offers) {
    		Double weight = offer.getShipping().getWeight();
    		offerWeight.put(offer.getId(), weight==null? -1 : weight);
    	}
    	return offerWeight;
    }

    private boolean isDontEmailMe(List<Offer> offers) {
        boolean isDontEmailMe = false;
        for (Offer offer : offers) {
            if (BooleanUtils.toBoolean(offer.getFfm().getIsDoNotEmailMe())) {
                isDontEmailMe = true;
                break;
            }
        }
        return isDontEmailMe;
    }

    private Set<Integer> getSellerIds(List<Offer> offers) {
        Set<Integer> sellerIds = new HashSet<>();
        for (Offer offer : offers) {
            if (offer.getMarketplace().getSeller().getId() != null) {
                sellerIds.add(offer.getMarketplace().getSeller().getId());
            }
        }
        return sellerIds;
    }

    private Map<String, Integer> getOfferToSellerIdMap(List<Offer> offers) {
        Map<String, Integer> offerIdToSellerIdMap = new HashMap<>();
        for (Offer offer : offers) {
            if (offer.getMarketplace().getSeller().getId() != null) {
                offerIdToSellerIdMap.put(offer.getId(), offer.getMarketplace().getSeller().getId());
            }
        }
        return offerIdToSellerIdMap;
    }

    private List<String> getSoldBy(List<Offer> offers) {
        Set<String> soldBySet = new HashSet<>();
        for (Offer offer : offers) {
            soldBySet.add(offer.getFfm().getSoldBy());
        }
        List<String> soldByList = new ArrayList<>(soldBySet);
        return soldByList;
    }

    private Set<String> getIntlGroupIds(List<Offer> offers) {
        Set<String> intllGroupIds = new HashSet<>();
        for (Offer offer : offers) {
            List<CountryGrp> countryGrpList = offer.getShipping().getIntlCountryGrps();
            for (CountryGrp countryGrp : countryGrpList) {
                intllGroupIds.add(countryGrp.getId());
            }
        }
        return intllGroupIds;
    }

    private boolean isInternationShipEligible(List<Offer> offers) {
        for (Offer offer : offers) {
            if (CollectionUtils.isNotEmpty(offer.getShipping().getIntlCountryGrps())) {
                return true;
            }
        }
        return false;
    }

    private List<String> getClickUrl(List<Offer> offers) {
        List<String> clickUrlList = new ArrayList<>();
        for (Offer offer : offers) {
            clickUrlList.add(offer.getMarketplace().getCpc().getClickCaptureLink().getAttrs().getHref());
        }
        return clickUrlList;
    }

    private String getGiftCardRank(List<Offer> offers) {
        int defaultRank = 999;

        int giftCardRank = defaultRank;
        for (Offer offer : offers) {
            int offerGiftCardRank = NumberUtils.toInt(offer.getDispTags().getGiftCardRank(), defaultRank);
            if (offerGiftCardRank < giftCardRank) {
                giftCardRank = offerGiftCardRank;
            }
        }
        return String.valueOf(giftCardRank);
    }

    /**
     * SEARCH-1271: Function to return set of unique 'name' and 'displayPath' in vocTags from all offers of the product.
     *
     * @param offers
     * @return List of unique name and displayPath attribute
     */
    private List<String> getVocTagNames(List<Offer> offers) {
        Set<String> geoSpotTagList = new HashSet<>();
        for (Offer offer : offers) {
            List<VocTags> vocTagList = offer.getVocTags();
            for (VocTags vocTag : vocTagList) {
                if (!VocTagUtil.isValidTagName(vocTag)) {
                    continue;
                }
                if (StringUtils.isNotEmpty(vocTag.getName())) {
                    geoSpotTagList.add(vocTag.getName());
                }
                if (StringUtils.isNotEmpty(vocTag.getDispPath())) {
                    geoSpotTagList.add(vocTag.getDispPath());
                }
            }
        }
        return new ArrayList<>(geoSpotTagList);
    }

    /**
     * Internal RIM status of a product is discontinued if all the offers have discontinued status.
     *
     * @param offers the list of offer documents
     * @return the consolidated iternalRimsts
     */
    private String getInternalRimSts(List<Offer> offers) {
        String discontinuedStatus = "D";
        for (Offer offer : offers) {
            if (!StringUtils.equalsIgnoreCase(offer.getReplenishment().getInternalRimSts(), discontinuedStatus)) {
                return StringUtils.EMPTY;
            }
        }
        return discontinuedStatus;
    }

    private Map<Sites, LocalDate> getNewestOnlineDateBySites(List<Offer> offers) {
        Map<Sites, LocalDate> newestOnlineDateBySites = new HashMap<>();
        LocalDate newestSearsDate = null;
        LocalDate newestKmartDate = null;
        LocalDate newestGoferDate = null;
        LocalDate newestSearsPrDate = null;

        for (Offer offer : offers) {
            newestSearsDate = getNewestDate(offer.getOperational().getSites().getSears().getFirstOnlineDt(), newestSearsDate);
            newestKmartDate = getNewestDate(offer.getOperational().getSites().getKmart().getFirstOnlineDt(), newestKmartDate);
            newestSearsPrDate = getNewestDate(offer.getOperational().getSites().getPuertorico().getFirstOnlineDt(), newestSearsPrDate);
            newestGoferDate = getNewestDate(offer.getOperational().getSites().getMygofer().getFirstOnlineDt(), newestGoferDate);
        }

        newestOnlineDateBySites.put(Sites.SEARS, newestSearsDate);
        newestOnlineDateBySites.put(Sites.KMART, newestKmartDate);
        newestOnlineDateBySites.put(Sites.SEARSPR, newestSearsPrDate);
        newestOnlineDateBySites.put(Sites.MYGOFER, newestGoferDate);
        return newestOnlineDateBySites;
    }

    private LocalDate getNewestDate(String offerOnlineDate, LocalDate newestDate) {
        LocalDate newest = newestDate;
        if (StringUtils.isNotEmpty(offerOnlineDate) && offerOnlineDate.length() >= 10) {
            LocalDate offerLocalDate = null;
            try {
                offerLocalDate = LocalDate.parse(offerOnlineDate, DateTimeFormat.forPattern(DateFormat.LONGDATE.getDateFormat()));
            } catch (IllegalArgumentException e) {
                LOGGER.debug("Illegal Date Time {}. Mismatch with {}.", offerOnlineDate, DateFormat.LONGDATE.getDateFormat());
                if (newest == null) {
                    offerLocalDate = LocalDate.parse(offerOnlineDate.substring(0, DateFormat.LONGDATE.getDateFormat().indexOf("'T'")));
                }
            }

            if (offerLocalDate == null) {
                return newest;
            }

            if (newest == null) {
                newest = offerLocalDate;
            } else if (offerLocalDate.isAfter(newest)) {
                newest = offerLocalDate;
            }
        }
        return newest;
    }

    private Map<Sites, LocalDate> getOldestOnlineDateBySites(List<Offer> offers) {
        Map<Sites, LocalDate> firstOnlineDateBySites = new HashMap<>();
        LocalDate oldestSearsDate = null;
        LocalDate oldestKmartDate = null;
        LocalDate oldestGoferDate = null;
        LocalDate oldestSearsPrDate = null;

        for (Offer offer : offers) {
            oldestSearsDate = getOldestDate(offer.getOperational().getSites().getSears().getFirstOnlineDt(), oldestSearsDate);
            oldestKmartDate = getOldestDate(offer.getOperational().getSites().getKmart().getFirstOnlineDt(), oldestKmartDate);
            oldestSearsPrDate = getOldestDate(offer.getOperational().getSites().getPuertorico().getFirstOnlineDt(), oldestGoferDate);
            oldestGoferDate = getOldestDate(offer.getOperational().getSites().getMygofer().getFirstOnlineDt(), oldestSearsPrDate);
        }

        firstOnlineDateBySites.put(Sites.SEARS, oldestSearsDate);
        firstOnlineDateBySites.put(Sites.KMART, oldestKmartDate);
        firstOnlineDateBySites.put(Sites.SEARSPR, oldestSearsPrDate);
        firstOnlineDateBySites.put(Sites.MYGOFER, oldestGoferDate);
        return firstOnlineDateBySites;
    }

    private LocalDate getOldestDate(String offerOnlineDate, LocalDate oldestDate) {
        LocalDate oldest = oldestDate;
        if (StringUtils.isNotEmpty(offerOnlineDate) && offerOnlineDate.length() >= 10) {
            LocalDate offerLocalDate = null;
            try {
                offerLocalDate = LocalDate.parse(offerOnlineDate, DateTimeFormat.forPattern(DateFormat.LONGDATE.getDateFormat()));
            } catch (IllegalArgumentException e) {
                LOGGER.debug("Illegal Date Time {}. Mismatch with {}.", offerOnlineDate, DateFormat.LONGDATE.getDateFormat());
                if (oldest == null) {
                    offerLocalDate = LocalDate.parse(offerOnlineDate.substring(0, DateFormat.LONGDATE.getDateFormat().indexOf("'T'")));
                }
            }

            if (offerLocalDate == null) {
                return oldest;
            }

            if (oldest == null) {
                oldest = offerLocalDate;
            } else if (offerLocalDate.isBefore(oldest)) {
                oldest = offerLocalDate;
            }
        }
        return oldest;
    }

    private List<Offer> getOfferDocuments(List<String> offerIds) {
        List<Offer> offerDocs = null;
        try {
            offerDocs = gbServiceFacade.getOfferDocsList(offerIds);
        } catch (Exception e) {
            LOGGER.error(ErrorCode.GB_OFFER_UNAVAILABLE.name(), e.getMessage());
        }
        if (offerDocs == null) { // Create an empty list
            offerDocs = new ArrayList<Offer>();
        }
        return offerDocs;
    }

    private List<String> getOfferIds(WorkingDocument wd) {
        List<String> offerIds = wd.getExtracts().getOfferIds();

        // Only for variations, it is fine to load the offers here
        boolean variationOffersNotExist = CollectionUtils.isEmpty(offerIds) && StringUtils.equalsIgnoreCase(wd.getExtracts().getContentExtract().getCatentrySubType(), CatentrySubType.VARIATION.getName());
        if (variationOffersNotExist) {
            offerIds = gbServiceFacade.getOfferIdsByAltKey(wd.getExtracts().getSsin());
            wd.getExtracts().setOfferIds(offerIds);
        } else {
            boolean nvSingleOfferNotExist = CollectionUtils.isEmpty(offerIds) && StringUtils.equalsIgnoreCase(wd.getExtracts().getContentExtract().getCatentrySubType(), CatentrySubType.NON_VARIATION.getName()) && wd.getExtracts().getContentExtract().getOfferCnt() == 1;
            if (nvSingleOfferNotExist) {
                String offerId = gbServiceFacade.getOfferIdsByAltKey(wd.getExtracts().getSsin()).get(0);
                offerIds = new ArrayList<>();
                offerIds.add(offerId);
                wd.getExtracts().setOfferIds(offerIds);
            }
        }
        return offerIds;
    }

    /**
     * Get a 1:1 map of offerID to KSN using OfferUtil
     * This is required because we store child documents in terms of offers and not ksn
     *
     * @param offers
     * @return
     */
    private Map<String, String> getOfferIdKsnMap(List<Offer> offers) {
        return OfferUtil.getOfferIdKsnMap(offers);
    }
}
