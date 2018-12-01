package com.shc.ecom.search.classifications;

import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.gb.doc.common.DomainObject;
import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.gb.doc.varattrs.VarAttributes;
import com.shc.ecom.search.common.behavioral.BehavioralDto;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.ias.IAS;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.nFiltersData.NFiltersDataDto;
import com.shc.ecom.search.common.pas.PasDto;
import com.shc.ecom.search.common.prodstats.Retail;
import com.shc.ecom.search.common.promo.PromoDto;
import com.shc.ecom.search.common.seller.SellerDto;
import com.shc.ecom.search.common.tm.TopicModelingDTO;
import com.shc.ecom.search.common.uas.UasDtoOfferChannels;
import com.shc.ecom.search.common.vo.buybox.BuyBoxDomain;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.docbuilder.SearchDocBuilder;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.components.fitments.AutomotiveOffer;
import com.shc.ecom.search.extract.components.impressions.ProductImpressionSummary;
import com.shc.ecom.search.extract.components.pricing.Price;
import com.shc.ecom.search.extract.components.store.StoresExtractService;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.persistence.PersistenceFactory;
import com.shc.ecom.search.transformations.BaseFieldTransformations;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author rgopala
 */

@Component
public abstract class Classification extends BaseFieldTransformations {

    private static final long serialVersionUID = 1229658301380536480L;

    @Autowired
    @Qualifier("contentExtractComponent")
    protected ExtractComponent<DomainObject> contentExtractComponent;

    @Autowired
    @Qualifier("buyBoxExtractComponent")
    protected ExtractComponent<Map<Sites, BuyBoxDomain>> buyBoxExtractComponent;

    @Autowired
    @Qualifier("offerExtractComponent")
    protected ExtractComponent<List<Offer>> offerExtractComponent;

    @Autowired
    @Qualifier("auxiliaryOfferExtractComponent")
    protected ExtractComponent<List<Offer>> auxiliaryOfferExtractComponent;


    @Autowired
    @Qualifier("filtersExtractComponent")
    protected ExtractComponent<Map<String, NFiltersDataDto>> filtersExtractComponent;

    @Autowired
    @Qualifier("onlinePriceExtractComponent")
    protected ExtractComponent<Map<Integer, Price>> onlinePriceExtractComponent;

    @Autowired
    @Qualifier("uasExtractComponent")
    protected ExtractComponent<UasDtoOfferChannels> uasExtractComponent;

    @Autowired
    @Qualifier("pasExtractComponent")
    protected ExtractComponent<PasDto> pasExtractComponent;

    @Autowired
    protected ExtractComponent<Map<String, IAS>> iasExtractComponent;

    @Autowired
    @Qualifier("sellerExtractComponent")
    protected ExtractComponent<Map<String, SellerDto>> sellerExtractComponent;

    @Autowired
    @Qualifier("varAttrExtractComponent")
    protected ExtractComponent<VarAttributes> varAttrExtractComponent;

    @Autowired
    @Qualifier("inStorePriceExtractComponent")
    protected ExtractComponent<Map<String, Price>> inStorePriceExtractComponent;

    @Autowired
    @Qualifier("prodstatsExtractComponent")
    protected ExtractComponent<Retail> prodstatsExtractComponent;

    @Autowired
    @Qualifier("impressionExtractComponent")
    protected ExtractComponent<List<ProductImpressionSummary>> impressionExtractComponent;

    @Autowired
    @Qualifier("behavioralExtractComponent")
    protected ExtractComponent<BehavioralDto> behavioralExtractComponent;

    @Autowired
    @Qualifier("promoExtractComponent")
    protected ExtractComponent<Map<Stores, List<PromoDto>>> promoExtractComponent;

    @Autowired
    @Qualifier("fitmentExtractComponent")
    protected ExtractComponent<List<AutomotiveOffer>> fitmentExtractComponent;

    @Autowired
    @Qualifier("topicModelingExtractComponent")
    protected ExtractComponent<Map<String, TopicModelingDTO>> topicModelingExtractComponent;

    @Autowired
    @Qualifier("localAdExtractComponent")
    protected ExtractComponent<List<String>> localAdExtractComponent;

    @Autowired
    protected StoresExtractService storesExtractService;
    
    @Autowired
    protected PersistenceFactory persistenceFactory;

    public abstract WorkingDocument extract(WorkingDocument wd, ContextMessage context);

    public SearchDocBuilder build(WorkingDocument wd, ContextMessage context) {
        // The mandatory fields.
        String partnumber = getPartNumber(wd);
        SearchDocBuilder builder = new SearchDocBuilder(partnumber);
        builder.mfpartno(getFullmfpartno(wd))
                .opsUuid_s(getOpsUuid_s(context))
                .opsCurrentServer_i(getOpsCurrentServer_i(context))
                .image(getImage(wd))
                .imageStatus(getImageStatus(wd))
                .promoId(getPromoIds(wd, context))
                .expirableFields(getExpirableFields(wd))
                .description(getDescription(wd, context))
                .promo(getPromo(wd, context))
                .itemCondition(getItemCondition(wd))
                .price(getPrice(wd, context))
                .sale(getSale(wd, context))
                .clearance(getClearance(wd, context))
                .instock(getInstock(wd, context))
                .itemsSold(getItemsSold(wd, context))
                .productViews(getProductViews(wd, context))
                .revenue(getRevenue(wd, context))
                .conversion(getConversion(wd, context))
                .mobileReviews(getMobileReviews(wd, context))
                .brand(getBrand(wd))
                .catalogs(getCatalogs(wd, context))
                .lnames(getLnames(wd, context))
                .lnames_km(getLnames_km(wd, context))
                .primaryLnames(getPrimaryLnames(wd, context))
                .primaryLnames_km(getPrimaryLnames_km(wd, context))
                .primaryVertical(getPrimaryVertical(wd, context))
                .primaryVertical_km(getPrimaryVertical_km(wd, context))
                .primaryHierarchy(getPrimaryHierarchy(wd, context))
                .primaryCategory(getPrimaryCategory(wd, context))
                .category(getCategory(wd, context))
                .categories(getCategories(wd, context))
                .vNames(getVNames(wd, context))
                .vNames_km(getVNames_km(wd, context))
                .cNames(getCNames(wd, context))
                .cNames_km(getCNames_km(wd, context))
                .sNames(getSNames(wd, context))
                .sNames_km(getSNames_km(wd, context))
                .level1Cats(getLevel1Cats(wd, context))
                .level2Cats(getLevel2Cats(wd, context))
                .level3Cats(getLevel3Cats(wd, context))
                .catentryId(getCatentryId(wd))
                .sellerCount(getSellerCount(wd))
                .cpcFlag(getCpcFlag(context))
                .spuEligible(getSpuEligible(wd, context))
                .url(getUrl(wd))
                .layAway(getLayAway(wd, context))
                .freeDelivery(getFreeDelivery(wd, context))
                .shipVantage(getShipVantage(wd, context))
                .trustedSeller(getTrustedSeller(wd, context))
                .storePickUp(getStorePickUp(wd, context))
                .newArrivals(getNewArrivals(wd, context))
                .impressions(getImpressions(wd, context))
                .browseBoost(getBrowseBoost(wd, context))
                .browseBoost_km(getBrowseBoostKmartMKP(wd, context))
                .browseBoostImpr(getBrowseBoostImpr(wd, context))
                .discount(getDiscount(wd, context))
                .numReviewsContextual(getNumReviewsContextual(wd, context))
                .avgRatingContextual(getAvgRatingContextual(wd, context))
                .searchPhrase(getSearchPhrase(wd, context))
                .offer(getOffer(wd, context))
                .numericRevenue(getNumericRevenue(wd, context))
                .numericRevenue_km(getNumericRevenue_km(wd, context))
                .behavioral(getBehavioral(wd, context))
                .behavioral_km(getBehavioral_km(wd, context))
                .accessories(getAccessories(wd, context))
                .instockShipping(getInstockShipping(wd, context))
                .prdType(getPrdType(wd, context))
                .ksn(getKsn(wd))
                .fitment(getFitment(wd, context))
                .rebate(getRebate(wd, context))
                .rebateStatus(getRebateStatus(wd, context))
                .sellerSFId(getSellerSFId(wd, context))
                .sellerFP(getSellerFP(wd, context))
                .automotive(getAutomotive(wd))
                .freeDelivery(getFreeDelivery(wd, context))
                .showroomUnit(getShowroomUnit(wd, context))
                .level1_dis(getLevel1_dis(wd, context))
                .level2_dis(getLevel2_dis(wd, context))
                .level3_dis(getLevel3_dis(wd, context))
                .level4_dis(getLevel4_dis(wd, context))
                .level5_dis(getLevel5_dis(wd, context))
                .level6_dis(getLevel6_dis(wd, context))
                .level7_dis(getLevel7_dis(wd, context))
                .offerDocs(generateOfferDocs(wd, context))
                .level1_dis_km(getLevel1_dis_km(wd, context))
                .level2_dis_km(getLevel2_dis_km(wd, context))
                .level3_dis_km(getLevel3_dis_km(wd, context))
                .level4_dis_km(getLevel4_dis_km(wd, context))
                .level5_dis_km(getLevel5_dis_km(wd, context))
                .level6_dis_km(getLevel6_dis_km(wd, context))
                .level7_dis_km(getLevel7_dis_km(wd, context))
                .topicIdWithRank(getTopicIdWithRank(wd))
                .topicIdWithNameSearchable(getTopicModelingWithNameSearchable(wd))
                .parentIds(getParentIds(wd, context))
                .localAdList(getLocalAdList(wd))
                .freeShipping(getFreeShipping(wd, context))
                .fulfillment(getFulfillment(wd, context))
                .b2bName(getB2bName(wd, context))
                .b2bDescShort(getB2bDescShort(wd, context))
                .b2bDescLong(getB2bDescLong(wd, context));

        builder = getOfferLevelFields(builder, wd, context);
        return builder;
    }

    protected SearchDocBuilder getOfferLevelFields(SearchDocBuilder sb, WorkingDocument wd, ContextMessage context) {
        boolean removeFieldsFromParent = BooleanUtils.toBoolean(PropertiesLoader.getProperty(GlobalConstants.FEAT_REMOVE_OFFER_FIELDS_FROM_PARENT));
        //Return the old fields if we are not removing them
        if (!removeFieldsFromParent) {
            sb.searchableAttributes(getSearchableAttributes(wd, context))
                    .searchableAttributesSearchable(getSearchableAttributesSearchable(wd, context));
        }
        return sb;
    }
    
    protected List<String> getFulfillment(WorkingDocument wd, ContextMessage context) {
    	
    	// For Mygofer - amosta0
    	List<String> fulfillmentList = new ArrayList<String>();
    	
    	if(StringUtils.equalsIgnoreCase(GlobalConstants.MYGOFER, context.getStoreName())){
    		List<String> defaultFulfillment = wd.getExtracts().getOfferExtract().getChannels();

    		if(defaultFulfillment != null && !defaultFulfillment.isEmpty() && (defaultFulfillment.contains("VD") || defaultFulfillment.contains("TW"))){	
    	    	fulfillmentList.add(Stores.MYGOFER.getStoreId() + "_10");
        	}

        	List<String> storeIds = wd.getExtracts().getPasExtract().getAvailableStores();
        	if( storeIds == null || storeIds.isEmpty()){
        		return fulfillmentList;
        	}
        	
        	for(int i=0; i < storeIds.size(); i++){
        		fulfillmentList.add(storeIds.get(i) + "_30");
        	}	
    	}

    	
        return fulfillmentList;
    }

}
