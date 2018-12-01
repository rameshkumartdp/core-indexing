package com.shc.ecom.search.classifications;

import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.gb.doc.varattrs.UidDefAttrs;
import com.shc.ecom.search.common.constants.OnlineWarehouse;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.level3CatData.Level3CatDataDto;
import com.shc.ecom.search.common.level3CatData.SubcatAttributes;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.docbuilder.SearchDocBuilder;
import com.shc.ecom.search.extract.components.promo.PromoRegular;
import com.shc.ecom.search.extract.components.promo.Promotions;
import com.shc.ecom.search.extract.extracts.IASExtract;
import com.shc.ecom.search.extract.extracts.PASExtract;
import com.shc.ecom.search.extract.extracts.VarAttrExtract;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.persistence.Level3CatsData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author rgopala
 */

@Component
public class Variation extends Classification {


    public static final String BEANTYPE = "ProductBean";
    public static final String SHIPPING = "Shipping";
    private static final long serialVersionUID = -2794732336111961397L;
    private static final boolean TM_ENABLED = Boolean.parseBoolean(PropertiesLoader.getProperty(GlobalConstants.ENABLE_TOPIC_MODELING));
    private static final Logger LOGGER = LoggerFactory.getLogger(Variation.class);
    private static final boolean STORE_ZIP_ENABLED = Boolean.parseBoolean(PropertiesLoader.getProperty(GlobalConstants.ENABLE_STORE_ZIP));
    
    @Autowired
    private Level3CatsData level3CatsData;

    @Override
    public WorkingDocument extract(WorkingDocument wd, ContextMessage context) {
        WorkingDocument tempWd = wd;
        tempWd = offerExtractComponent.process(tempWd, context);
        if (tempWd.getDecision().isRejected()) {
            return tempWd;
        }

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
            return tempWd;
        }

        String storeName = context.getStoreName();
        if (StringUtils.equalsIgnoreCase(storeName, Stores.SEARS.getStoreName()) ||
                StringUtils.equalsIgnoreCase(storeName, Stores.AUTO.getStoreName()) ||
                StringUtils.equalsIgnoreCase(storeName, Stores.KMART.getStoreName()) ||
                StringUtils.equalsIgnoreCase(storeName, Stores.MYGOFER.getStoreName())) {
            tempWd = iasExtractComponent.process(tempWd, context);
            tempWd = pasExtractComponent.process(tempWd, context);
        }
        if (StringUtils.equalsIgnoreCase(storeName, Stores.SEARS.getStoreName()) || StringUtils.equalsIgnoreCase(storeName, Stores.FBM.getStoreName()) || StringUtils.equalsIgnoreCase(storeName, Stores.AUTO.getStoreName())) {
            tempWd = sellerExtractComponent.process(tempWd, context);
        }
        tempWd = promoExtractComponent.process(tempWd, context);
        tempWd = varAttrExtractComponent.process(tempWd, context);
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

        builder.beanType(BEANTYPE)
                .sellerTier(getSellerTier(wd, context))
                .id(getId(wd, context))
                .sellerTierRank(getSellerTierRank(wd, context))
                .storeOrigin(getStoreOrigin(wd))
                .storeAttributes(getStoreAttributes(wd, context))
                .xref(getXref(wd, context))
                .productAttributes(getProductAttributes(wd, context))
                .itemCondition(getItemCondition(wd))
                .swatches(getSwatchesStatus(wd))
                .swatchInfo(getSwatchInfo(wd))
                .rank(getRank(wd, context))
                .rank_km(getRank_km(wd, context))
                .daysOnline(getDaysOnline(wd, context))
                .daysOnline_km(getDaysOnline_km(wd, context))
                .newItemFlag(getNewItemFlag(wd, context))
                .storeUnit(getStoreUnit(wd))
                .itemCondition(getItemCondition(wd))
                .geospottag(getGeospotTag(wd))
                .tagValues(getTagValues(wd))
                .division(getDivision(wd))
                .giftCardSequence(getGiftCardSequence(wd, context))
                .giftCardType(getGiftCardType(wd, context))
                .clickUrl(getClickUrl(wd, context))
                .offerAttr(getOfferAttr(wd, context))
                .pickUpItemStores(getPickUpItemStores(wd, context))
                .matureContentFlag(getMatureContentFlag(wd))
                .int_ship_eligible(getInternationalShipEligible(wd, context))
                .has991(getHas991(wd))
                .discontinued(getDiscontinued(wd))
                .sears_international(getSearsInternational(wd, context))
                .sin(getSin(wd, context))
                .international_shipping(getInternationalShipping(wd, context))
                .alternateImage(getAlternateImage(wd, context))
                .fbm(getFbmFlag(wd, context))
                .catConfidence(getCatConfidence(wd))
                .eGiftEligible(getEGiftEligible())
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
                .fulfillment(getFulfillment(wd, context))
				.aggregatorId(getAggregatorId(wd));

        builder = getOfferLevelFields(builder, wd, context);
        return builder;
    }



	/* ------------------------------------------------------------------------------------ */
	/* ------------------------------------------------------------------------------------ */
	/* -------------------- Field Specialization per this type follows -------------------- */
	/* ------------------------------------------------------------------------------------ */
	/* ------------------------------------------------------------------------------------ */

	protected String getId(WorkingDocument wd, ContextMessage context) {
		final String variationConstant = "var";

		String ssin = wd.getExtracts().getSsin();
		return ssin + SEPARATOR + variationConstant + SEPARATOR + context.getStoreName().toLowerCase();
	}

	protected List<String> getPromotionTxt(WorkingDocument wd, ContextMessage context) {

		List<String> storePromoTxt = new ArrayList<>();
		List<Integer> storeIds = Stores.getStoreId(wd, context);

		for (Integer storeId : storeIds) {
			StringBuilder prmTxt = new StringBuilder();
			Promotions promotions = new Promotions();
			List<PromoRegular> promoRegularList = new ArrayList<>();
			String prefix = Integer.toString(storeId) + SEPARATOR;
			if (wd.getExtracts().getPromoExtract().getStorePromoExtracts().containsKey(Stores.getStore(storeId))) {
				promoRegularList = wd.getExtracts().getPromoExtract().getStorePromoExtracts().get(Stores.getStore(storeId)).getPromoRegulars();
			}

			String prmTxtShipping = null;

			if (context.getStoreName().equalsIgnoreCase(Stores.SEARS.getStoreName()) ||
					context.getStoreName().equalsIgnoreCase(Stores.SEARSPR.getStoreName()) ||
					context.getStoreName().equalsIgnoreCase(Stores.FBM.getStoreName()) ||
					getCatalogs(wd, context).contains(Sites.KMART.getCatalogId())) {
				String freeShipThreshold = null;
				for (PromoRegular promo : promoRegularList) {
					boolean dateFlag = promotions.isValidPromotion(promo.getStartDt(), promo.getEndDt());
					if (promo.getPromoType().equalsIgnoreCase(SHIPPING) && dateFlag) {
						freeShipThreshold = promo.getMinPurchaseVal();
						break;
					}
				}

				try {
					if (StringUtils.isNotEmpty(freeShipThreshold) && Float.parseFloat(freeShipThreshold) >= 1.0) {
						prmTxtShipping = "S" + "_" + freeShipThreshold;
					}
				} catch (NumberFormatException nfe) {
					LOGGER.error(nfe.toString());
					prmTxtShipping = null;
				}
			}

			if (getFreeShipping(wd, context).contains(prefix + "1") &&
					context.getStoreName().equalsIgnoreCase(Stores.SEARS.getStoreName()) &&
					getStoreOrigin(wd).size() > 1 &&
					prmTxtShipping == null) {
				prmTxtShipping = "S_0.0";
			}

			String prmTxtFreeDelivery = null;
			for (PromoRegular promo : promoRegularList) {
				prmTxtFreeDelivery = promotions.getFreeDeliveryPromoTxt(promo, promo.getMinPurchaseVal(), prmTxtFreeDelivery);
			}

			String prmTxtSyw = getStoreValueFromList(storeId, promotions.getSywPromotionTxt(wd, context));

			promotions.appendPromotion(prmTxtShipping, prmTxt);
			promotions.appendPromotion(prmTxtFreeDelivery, prmTxt);
			promotions.appendPromotion(prmTxtSyw, prmTxt);

			if (StringUtils.isNotEmpty(prmTxt.toString())) {
				storePromoTxt.add(prefix + prmTxt.toString());
			}
		}
		return storePromoTxt;
	}

	private String getEGiftEligible() {
		return "0";
	}

	protected List<String> getPickUpItemStores(WorkingDocument wd, ContextMessage context) {
		IASExtract iasExtract = wd.getExtracts().getIasExtract();
		VarAttrExtract varAttrExtract = wd.getExtracts().getVarAttrExtract();
		PASExtract pasExtract = wd.getExtracts().getPasExtract();

		List<String> pickItemStores = new ArrayList<>();
		List<String> offerIds = wd.getExtracts().getOfferIds();
		for (String offerId : offerIds) {
			String offerAttr = getSingleOfferAttr(wd, context, offerId);
			if (StringUtils.isNotEmpty(offerAttr) && StringUtils.isNotEmpty(offerAttr)
					&& iasExtract.getOfferIdFacilityIdsMap().containsKey(offerId)) {

				List<String> iasStores = iasExtract.getOfferIdFacilityIdsMap().get(offerId);

				if (CollectionUtils.isNotEmpty(iasStores)) {
					String iasStoresString = StringUtils.join(iasStores, ";");
					pickItemStores.add(offerId + SEPARATOR + offerAttr + SEPARATOR + iasStoresString);
				}

			}
		}

		String varAttrssin = null;
		if (varAttrExtract.isSwatchLink()) {
			varAttrssin = varAttrExtract.getSsin();
		}

		// TODO: Do not understand the business logic behind doing this.
		if (StringUtils.isNotEmpty(varAttrssin) && StringUtils.equalsIgnoreCase(varAttrssin, context.getPid())) {
			List<String> staticAttributes = getStaticAttributes(wd, context);
			String staticAttributesString = StringUtils.join(staticAttributes, ";");

			List<String> pasStores = pasExtract.getAvailableStores();
			String pasStoresString = StringUtils.join(pasStores, ";");

			String ssin = context.getPid();

			if (StringUtils.isNotEmpty(staticAttributesString)) {
				pickItemStores.add(ssin + SEPARATOR + staticAttributesString + SEPARATOR + pasStoresString);
			}
		}

		return pickItemStores;
	}

	private String getSingleOfferAttr(WorkingDocument wd, ContextMessage context, String offerId) {
		String offerAttr = StringUtils.EMPTY;

		VarAttrExtract varAttrExtract = wd.getExtracts().getVarAttrExtract();

		Set<String> attributes = new HashSet<>();
		attributes.addAll(getVarAttributes(wd, offerId));
		attributes.addAll(getStaticAttributes(wd, context));

		List<String> attributesList = new ArrayList<>(attributes);

		Map<String, String> nameFamilyNameMap = getNameFamilyNameMap(varAttrExtract.getDefiningAttrsList());
		List<String> attributesToChange = new ArrayList<>(Arrays.asList("Color", "Color Family", "Overall Color"));
		attributesList = convertNameToFamliyName(attributesList, nameFamilyNameMap, attributesToChange);

		Collections.sort(attributesList);

		String attributesNoSpace = StringUtils.deleteWhitespace(StringUtils.join(attributesList, ";")) + ";";

		List<String> zones = wd.getExtracts().getIasExtract().getOfferIdZoneMap().get(offerId);

		if (zones == null) {
			zones = new ArrayList<>();
		}
		Collections.sort(zones);

		StringBuilder zonesWithS = new StringBuilder();
		for (String zone : zones) {
			zonesWithS.append("S" + zone + ";");
		}

		String zonesNoSpace = StringUtils.deleteWhitespace(StringUtils.join(zonesWithS.toString(), StringUtils.EMPTY));
		if (StringUtils.isNotEmpty(zonesNoSpace)) {
			StringBuilder offerAttrSB = new StringBuilder(offerAttr);
			offerAttrSB.append(SEPARATOR).append(zonesNoSpace);
			offerAttr = offerAttrSB.toString();
		}

		if (StringUtils.isNotEmpty(attributesNoSpace)) {
			StringBuilder offerAttrSB = new StringBuilder(offerAttr);
			offerAttrSB.append(SEPARATOR).append(attributesNoSpace);
		}
		return offerAttr;
	}

	protected List<String> getOfferAttr(WorkingDocument wd, ContextMessage context) {
		List<String> offerAttrs = new ArrayList<>();

		VarAttrExtract varAttrExtract = wd.getExtracts().getVarAttrExtract();
		List<String> offerIds = wd.getExtracts().getOfferIds();
		for (String offerId : offerIds) {
			if (varAttrExtract.getOfferIdUiDefAttrsMap().containsKey(offerId)) {
				String offerAttr = getSingleOfferAttr(wd, context, offerId);
				if (StringUtils.isNotEmpty(offerAttr)) {
					StringBuilder offerAttrSB = new StringBuilder(offerId);
					offerAttrSB.append(offerAttr);
					offerAttrs.add(offerAttrSB.toString());
				}
			}

		}
		return offerAttrs;
	}

	public List<String> getVarAttributes(WorkingDocument wd, String offerId) {
		Map<String, List<UidDefAttrs>> offerIdUidDefAttrMap = wd.getExtracts().getVarAttrExtract().getOfferIdUiDefAttrsMap();

		List<String> varAttributes = new ArrayList<>();
		if (offerIdUidDefAttrMap.containsKey(offerId)) {
			List<UidDefAttrs> uidDefAttrs = offerIdUidDefAttrMap.get(offerId);

			for (UidDefAttrs uidDefAttr : uidDefAttrs) {
				varAttributes.add(uidDefAttr.getAttrName() + "=" + uidDefAttr.getAttrVal());
			}

		}
		return varAttributes;
	}

	private List<String> convertNameToFamliyName(List<String> attributes, Map<String, String> nameFamilyNameMap, List<String> attributesToChange) {
		List<String> modifiedAttributes = new ArrayList<>();
		for (String attribute : attributes) {
			String name = attribute.split("=")[0];
			String value = attribute.split("=")[1];
			if (attributesToChange.contains(name) && nameFamilyNameMap.containsKey(value)) {
				value = nameFamilyNameMap.get(value);
			}
			modifiedAttributes.add(name + "=" + value);
		}
		return modifiedAttributes;
	}


	protected List<String> getProductAttributes(WorkingDocument wd, ContextMessage context) {
		List<String> existingProductAttributes = super.getProductAttributes(wd, context);
		existingProductAttributes.add("PRODUCTBEAN_TYPE=VARIATION");
		return existingProductAttributes;
	}


	@Override
	protected List<String> getSubcatAttributes(WorkingDocument wd, ContextMessage context) {
		List<String> subCatAttributes = new ArrayList<>();

		// Get the level3CatsData from the feed file
		Map<String, Level3CatDataDto> level3CatData = level3CatsData.getLevel3Data();

		// Get the static attributes map from the content collection
		Map<String, List<String>> staticAttrsMap = getStaticAttrsMap(wd, context);

		// Get the var attributes map (Only applicable for variations)
		Map<String, List<String>> varAttrsMap = wd.getExtracts().getVarAttrExtract().getAttrsMap();
		Map<String, String> nameFamilyNameMap = getNameFamilyNameMap(wd.getExtracts().getVarAttrExtract().getDefiningAttrsList());
		varAttrsMap = convertNameToFamilyName(varAttrsMap, new ArrayList<>(Arrays.asList("Color", "Color Family", "Overall Color")), nameFamilyNameMap);

		Map<String, List<String>> attrsMap = new HashMap<>();
		attrsMap.putAll(staticAttrsMap);
		attrsMap.putAll(varAttrsMap);

		// Get the level3 categories
		Set<String> level3Categories = getLevelNCategories(wd, context, 3);

		// Check for every category, whether static attribute is searchable or not
		for (String level3Category : level3Categories) {
			String categoryNoSpace = level3Category.replaceAll("\\s", StringUtils.EMPTY); // Removing spaces from the category
			if (level3CatData.containsKey(categoryNoSpace)) { // Check to see if the static attributes is present in the file
				Level3CatDataDto level3CatsDataDto = level3CatData.get(categoryNoSpace);
				List<SubcatAttributes> subcatAttrs = level3CatsDataDto.getSubcatAttributes();
				for (SubcatAttributes subcatAttr : subcatAttrs) {
					if (attrsMap.containsKey(subcatAttr.getName())) {
						String name = subcatAttr.getName();
						List<String> values = attrsMap.get(subcatAttr.getName());
						for (String value : values) {
							subCatAttributes.add(level3Category + SEPARATOR + name + "=" + value);
						}
					}
				}
			}
		}

		return subCatAttributes;
	}

	@Override
	protected List<String> getSearchableAttributesSearchable(WorkingDocument wd, ContextMessage context) {
		return getSearchableAttributeListVariations(wd, context, new ArrayList<String>(Arrays.asList("No", "N/A", "Unknown")), new ArrayList<String>(Arrays.asList("Yes")));
	}

	@Override
	protected List<String> getSearchableAttributes(WorkingDocument wd, ContextMessage context) {
		return getSearchableAttributeListVariations(wd, context, new ArrayList<String>(), new ArrayList<String>());
	}

	protected String getConsumerReportsRated(WorkingDocument wd, ContextMessage context) {
		final String consumerReportFilterValue = "Consumer Reports Rated";
		if (CollectionUtils.isNotEmpty(getConsumerReportRatingContextual(wd, context))) {
			return consumerReportFilterValue;
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

	protected List<String> getLocalAdList(WorkingDocument wd) {
		return wd.getExtracts().getLocalAdExtract().getLocalAdList();
	}

	protected List<String> getFulfillment(WorkingDocument wd, ContextMessage context) {
		// For Mygofer - amosta0
		List<String> fulfillmentList = new ArrayList<String>();

		if(StringUtils.equalsIgnoreCase( GlobalConstants.MYGOFER, context.getStoreName())){
			List<String> defaultFulfillment = wd.getExtracts().getOfferExtract().getChannels();
			if(defaultFulfillment != null && !defaultFulfillment.isEmpty() && (defaultFulfillment.contains("VD") || defaultFulfillment.contains("TW"))) {
				fulfillmentList.add(Stores.MYGOFER.getStoreId() + "_10");
			}

			List<String> storeIds = wd.getExtracts().getPasExtract().getAvailableStores();
			if(storeIds == null || storeIds.isEmpty()){
				return fulfillmentList;
			}
			for(int i=0; i < storeIds.size(); i++){
				fulfillmentList.add(storeIds.get(i) + "_30");
			}
		}

		return fulfillmentList;
	}

	/* ------------------------------------------------------------------------------------ */
	/* ------------------------------------------------------------------------------------ */
	/* -------------------- Offer Level customizations as follows ------------------------- */
	/* ------------------------------------------------------------------------------------ */
	/* ------------------------------------------------------------------------------------ */

	/**
	 * Given an offerId, this method returns the searchableAtrributes for that offer
	 *
	 * @param offerId
	 * @param wd
	 * @param context
	 * @param attrExclusionList
	 * @param valueExclusionList
	 * @return
	 */
	@Override
	protected List<String> getOfferSearchableAttributes(String offerId,
			WorkingDocument wd, ContextMessage context,
			List<String> attrExclusionList, List<String> valueExclusionList) {

		// Get the static attributes map from the content collection
		Map<String, List<String>> staticAttrsMap = getStaticAttrsMap(wd, context);

		//Does not always have the attributes for the offerId in consideration, may be null
		Optional<List<UidDefAttrs>> offerAttrList = Optional.ofNullable(wd.getExtracts().getVarAttrExtract().getOfferIdUiDefAttrsMap().get(offerId));

		Map<String, String> nameFamilyNameMap = getNameFamilyNameMap(wd.getExtracts().getVarAttrExtract().getDefiningAttrsList());
		Map<String, List<String>> offerAttrMap = processOfferAttr(offerAttrList, new ArrayList<>(Arrays.asList("Color", "Color Family", "Overall Color")), nameFamilyNameMap);
		Map<String, List<String>> attrsMap = new HashMap<>();
		attrsMap.putAll(staticAttrsMap);
		attrsMap.putAll(offerAttrMap);

		return eliminateExcludedAttributes(wd, context, attrsMap, attrExclusionList, valueExclusionList);
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
	 * Process the offer attributes from extracts with required changes wrt to family name and create a map out of it
	 *
	 * @param offerAttrList
	 * @param attributesToChange
	 * @param nameFamilyNameMap
	 * @return
	 */
	protected Map<String, List<String>> processOfferAttr(Optional<List<UidDefAttrs>> offerAttrList, List<String> attributesToChange,
			Map<String, String> nameFamilyNameMap) {
		Map<String, List<String>> offerAttrMap = new HashMap<>();
		if (!offerAttrList.isPresent()) {
			return offerAttrMap;
		}
		for (UidDefAttrs attr : offerAttrList.get()) {
			String attrName = attr.getAttrName();
			String attrValue = attr.getAttrVal();

			if (attributesToChange.contains(attrName)) {
				attrValue = nameFamilyNameMap.get(attrValue);
			}

			List<String> attrValueList = new ArrayList<>();
			attrValueList.add(attrValue);

			//Adding the attr and its value once under the assumption that each attrName will occur once and only once.
			offerAttrMap.put(attrName, attrValueList);
		}
		return offerAttrMap;
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

	@Override
	public String getAggregatorId(WorkingDocument wd){
		return wd.getExtracts().getOfferExtract().getAggregatorId();
	}



}
