package com.shc.ecom.search.extract.components.content;

import java.util.*;

import javax.annotation.Resource;

import com.shc.common.index.rules.Decision;
import com.shc.common.index.rules.ValidationResults;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.extract.extracts.ContentExtract;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shc.ecom.gb.doc.collection.AssetsImgsVals;
import com.shc.ecom.gb.doc.collection.Bundle;
import com.shc.ecom.gb.doc.collection.BundleGroup;
import com.shc.ecom.gb.doc.collection.Products;
import com.shc.ecom.gb.doc.collection.Sku;
import com.shc.ecom.gb.doc.common.Desc;
import com.shc.ecom.gb.doc.common.DomainObject;
import com.shc.ecom.gb.doc.common.HierarchySites;
import com.shc.ecom.gb.doc.common.ImageAttrs;
import com.shc.ecom.gb.doc.common.Operational;
import com.shc.ecom.gb.doc.common.SpecificHierarchy;
import com.shc.ecom.gb.doc.common.WebIdName;
import com.shc.ecom.gb.doc.content.AssetsImgs;
import com.shc.ecom.gb.doc.content.Content;
import com.shc.ecom.gb.doc.content.FacetSites;
import com.shc.ecom.gb.doc.content.NameValue;
import com.shc.ecom.gb.doc.offer.CountryGrp;
import com.shc.ecom.gb.doc.offer.VocTags;
import com.shc.ecom.search.common.constants.CatentrySubType;
import com.shc.ecom.search.common.constants.ItemCondition;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.greenbox.GBServiceFacade;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.BCOProducts;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.rules.IRule;
import com.shc.ecom.search.util.DecisionUtil;
import com.shc.ecom.search.util.VocTagUtil;
import com.shc.ecom.search.validator.Validator;


@Component
public class ContentExtractComponent extends ExtractComponent<DomainObject> {

    /**
     * @author pchauha
     */
    private static final long serialVersionUID = -1851029751967756368L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentExtractComponent.class.getName());
    @Autowired
    private GBServiceFacade gbServiceFacade;
    @Resource(name = "domainRules")
    private List<IRule<DomainObject>> domainRules;
    @Resource(name = "contentRules")
    private List<IRule<Content>> contentRules;
    @Resource(name = "bundleRules")
    private List<IRule<Bundle>> bundleRules;
    @Autowired
    private Validator validator;


    @Override
    protected DomainObject get(WorkingDocument wd, ContextMessage context) {
        return gbServiceFacade.getDomainDoc(wd.getExtracts().getSsin());
    }

    @Override
    public Decision validate(DomainObject domainObject, WorkingDocument wd, ContextMessage context) {
        ValidationResults results = validator.validate(domainObject, domainRules, context);
        List<String> typesList = new ArrayList<>();
        if (domainObject.get_ft() != null) {
            typesList = domainObject.get_ft().getCatentrySubType();
        }
        switch (CatentrySubType.findCatentrySubType(typesList)) {
            case BUNDLE:
            case COLLECTION:
            case OUTFIT:
                results = validator.validate(domainObject.get_blob().getBundle(), bundleRules, context);
                break;
            case NON_VARIATION:
            case VARIATION:
            case NON_VARIATION_UVD:
            case VARIATION_UVD:
                results = validator.validate(domainObject.get_blob().getContent(), contentRules, context);
                break;
		default:
			break;
        }
        Decision decision = wd.getDecision();
        decision.setId(wd.getExtracts().getSsin());
        decision.setIdType("SSIN");
        decision.setRejected(!results.isPassed());
        decision.addValidationResults(results);
        decision.setOfferId(context.getOfferId());
        decision.setStore(context.getStoreName());
        decision.setSites(DecisionUtil.getSitesList(Stores.getStore(context.getStoreName()).getEligibleSites(wd, context)));
        decision.setOpsUuid(context.getTimestamp());
        return decision;
    }

    @Override
    protected Extracts extract(DomainObject domainObject, WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();
        boolean isCommercialStore = StringUtils.equalsIgnoreCase(context.getStoreName(),Stores.COMMERCIAL.getStoreName());
        switch (CatentrySubType.findCatentrySubType(domainObject.get_ft().getCatentrySubType())) {

            case COLLECTION:
                Bundle bundle = domainObject.get_blob().getBundle();
                extracts = extractBCO(bundle, context, extracts);
                extracts.getContentExtract().setProductIds(getCollectionProductIdsList(bundle));
                extracts.getContentExtract().setBCOProducts(getProducts(bundle));
                extracts.setOfferIds(getOfferIdsForBundles(bundle, wd));
                break;
            case BUNDLE:
            case OUTFIT:
                Bundle bundleBlob = domainObject.get_blob().getBundle();
                extracts = extractBCO(bundleBlob, context, extracts);
                extracts.getContentExtract().setProductIds(getBundleGroupProductIds(bundleBlob.getBundleGroup()));
                extracts.setOfferIds(getOfferIdsForBundles(bundleBlob, wd));
                break;
            case NON_VARIATION:
            case VARIATION:
            case NON_VARIATION_UVD:
            case VARIATION_UVD:
                Content content = domainObject.get_blob().getContent();
                extracts.getContentExtract().setSitesDispEligibility(getAllSitesDispEligInfo(content));
                extracts.getContentExtract().setSitesEligibility(getAllSitesEligInfo(content));
                extracts.getContentExtract().setSearsDisplayElligible(BooleanUtils.toBoolean(content.getOperational().getSites().getSears().getIsDispElig()));
                extracts.getContentExtract().setContentOnline(BooleanUtils.toBoolean(content.getOperational().getSites().getSears().getIsDispElig()));
                extracts.getContentExtract().setModelNo(content.getMfr().getModelNo());
                extracts.getContentExtract().setSsin_Content(content.getIdentity().getSsin());
                extracts.getContentExtract().setOfferCnt(content.getIdentity().getOfferCnt());
                extracts.getContentExtract().setUuid(content.getIdentity().getUid());
                extracts.getContentExtract().setCatentrySubType(content.getClassifications().getCatentrySubType());
                extracts.getContentExtract().setCatentryId(getCatentryId(content.getAltIds().getCatentryId()));
                extracts.getContentExtract().setUrl(content.getSeo().getUrl());
                extracts.getContentExtract().setPrimaryImageUrl(getPrimaryImageUrl(content));
                extracts.getContentExtract().setAlternateImageUrls(getAlternateImgUrls(content));
                extracts.getContentExtract().setShortDescription(getDescription(content.getDesc(), "S"));
                extracts.getContentExtract().setLongDescription(getDescription(content.getDesc(), "L"));
                extracts.getContentExtract().setName(content.getName());
                extracts.getContentExtract().setAutofitment(content.getAutomotive().getAutoFitment());
                extracts.getContentExtract().setImaClassControlPid(content.getAltIds().getImaClassControlPid());
                extracts.getContentExtract().setBrandCodeId(content.getAutomotive().getBrandCodeId());
                extracts.getContentExtract().setBrandName(content.getBrand().getName());
                extracts.getContentExtract().setMatureContent(BooleanUtils.toBoolean(content.getLegal().getIsMatureContent()));
                extracts.getContentExtract().setSiteWebHierarchiesMap(getSiteWebHierarchiesMap(content.getTaxonomy().getWeb().getSites()));
                extracts.getContentExtract().setSiteDapHierarchiesMap(getSiteDapHierarchiesMap(content.getTaxonomy().getWeb().getSites()));
                extracts.getContentExtract().setStaticFacetsSitesMap(getStaticFacetsSitesMap(content.getFacets().getSites()));
                extracts.getContentExtract().setUvd(BooleanUtils.toBoolean(content.getClassifications().getIsUvd()));
                extracts.getContentExtract().setAutomotive(BooleanUtils.toBoolean(content.getClassifications().getIsAutomotive()));
                if(isCommercialStore){
                    extracts.getContentExtract().setB2bName(content.getB2bName());
                    setB2bDesc(extracts.getContentExtract(), content);
                    extracts.getContentExtract().setColorFamily(getScomFilterFieldValues(content, GlobalConstants.COLOR_FAMILY));
                    extracts.getContentExtract().setWidth(getScomFilterFieldValues(content, GlobalConstants.WIDTH));
                    extracts.getContentExtract().setHeight(getScomFilterFieldValues(content,GlobalConstants.HEIGHT));
                    extracts.getContentExtract().setDepth(getScomFilterFieldValues(content,GlobalConstants.DEPTH));
                    extracts.getContentExtract().setEnergyStarCompliant(getScomFilterFieldValues(content, GlobalConstants.ENERGYSTAR_COMPLIANT));
                    extracts.getContentExtract().setAdaCompliant(getScomFilterFieldValues(content,GlobalConstants.ADA_COMPLIANT));
                }
                break;
            default:
                LOGGER.info("Unknown CatentrySubType received" + CatentrySubType.get(domainObject.get_ft().getCatentrySubType().get(0)));
        }
        return extracts;
    }

    private void setB2bDesc(ContentExtract contentExtract, Content content) {
        List<Desc> b2bDesc = content.getB2bDesc();
        if (b2bDesc != null) {
            for (Desc desc : b2bDesc) {
                if (StringUtils.equalsIgnoreCase(desc.getType(),"S")) {
                    contentExtract.setB2bDescShort(desc.getVal());
                }
                if (StringUtils.equalsIgnoreCase(desc.getType(),"L")) {
                    contentExtract.setB2bDescLong(desc.getVal());
                }
            }
        }
    }



    /**
     * Extract rank 1 and rank 2 products that make up a collection
     *
     * @param bundle
     * @return
     */
    private List<BCOProducts> getProducts(Bundle bundle) {

        List<BCOProducts> bcoProducts = Lists.newArrayList();

        for (Products products : bundle.getProducts()) {
            if (StringUtils.equals(products.getRank(), "1") || StringUtils.equals(products.getRank(), "2")) {
                BCOProducts bcoProductsObj = new BCOProducts();
                bcoProductsObj.setId(products.getId());
                bcoProductsObj.setRank(products.getRank());
                bcoProductsObj.setCatentrySubType(products.getCatentrySubType());
                bcoProducts.add(bcoProductsObj);
            }
        }

        return bcoProducts;
    }

    /**
     * Pulls the common content extract for Bundles, Collections and Outfits
     *
     * @param bundle
     * @param context
     * @param extracts
     * @return
     */
    private Extracts extractBCO(Bundle bundle, ContextMessage context, Extracts extracts) {

    	extracts.getContentExtract().setSitesEligibility(getAllSitesEligInfo(bundle));
        extracts.getContentExtract().setSitesDispEligibility(getAllSitesDispEligInfo(bundle));
        extracts.getContentExtract().setBundleDisplayElligible(BooleanUtils.toBoolean(bundle.getOperational().getSites().getSears().getIsDispElig()));
        extracts.getContentExtract().setCatentrySubType(bundle.getClassifications().getCatentrySubType());
        extracts.getContentExtract().setModelNo(bundle.getMfr().getModelNo());
        extracts.getContentExtract().setDivision(getDivision(bundle));
        extracts.getContentExtract().setProgramType(bundle.getMarketplace().getProgramType());
        extracts.getContentExtract().setSoldBy(getSoldBy(bundle, context));
        extracts.getContentExtract().setGeoSpotTag(getGeospotTag(bundle.getVocTags()));
        extracts.getContentExtract().setVocTags(getGeospotTag(bundle.getVocTags()));
        extracts.getContentExtract().setCatentryId(getCatentryId(bundle.getAltIds().getCatentryId()));
        extracts.getContentExtract().setUrl(bundle.getSeo().getUrl());
        extracts.getContentExtract().setPrimaryImageUrl(getPrimaryImageUrl(bundle));
        extracts.getContentExtract().setAlternateImageUrls(getAlternateImgUrls(bundle));
        extracts.getContentExtract().setFirstOnlineDate(getFirstOnlineDate(bundle.getOperational().getFirstOnlineDt()));
        extracts.getContentExtract().setSpuEligible(BooleanUtils.toBoolean(bundle.getFfm().getIsSpuElig()));
        extracts.getContentExtract().setItemCondition(getItemCondition(bundle));
        extracts.getContentExtract().setIntlGroupIds(getIntlGroupIds(bundle));
        extracts.getContentExtract().setRebateId(bundle.getRebate().getId());
        extracts.getContentExtract().setCatConfScore(getCatConfScore(bundle.getMarketplace().getCatConfScore()));
        extracts.getContentExtract().setOutletItem(BooleanUtils.toBoolean(bundle.getDispTags().getIsOutletItem()));
        extracts.getContentExtract().setSiteWebHierarchiesMap(getSiteWebHierarchiesMap(bundle.getTaxonomy().getWeb().getSites()));
        extracts.getContentExtract().setSiteDapHierarchiesMap(getSiteDapHierarchiesMap(bundle.getTaxonomy().getWeb().getSites()));
        extracts.getContentExtract().setName(bundle.getName());
        extracts.getContentExtract().setShortDescription(getDescription(bundle.getDesc(), "S"));
        extracts.getContentExtract().setLongDescription(getDescription(bundle.getDesc(), "L"));
        extracts.getContentExtract().setBrandName(bundle.getBrand().getName());
        extracts.getContentExtract().setEnergyStar(BooleanUtils.toBoolean(bundle.getDispTags().getIsEnergyStar()));
        extracts.getContentExtract().setStreetDt(bundle.getPresell().getStreetDt());
        extracts.getContentExtract().setMailable(BooleanUtils.toBoolean(bundle.getShipping().getIsMailable()));
        extracts.getContentExtract().setChannel(bundle.getFfm().getChannel());
        extracts.getContentExtract().setDoNotEmailMe(BooleanUtils.toBoolean(bundle.getFfm().getIsDoNotEmailMe()));
        extracts.getContentExtract().setDeliveryElig(BooleanUtils.toBoolean(bundle.getFfm().getIsDeliveryElig()));
        extracts.getContentExtract().setShipElig(BooleanUtils.toBoolean(bundle.getFfm().getIsShipElig()));
        extracts.getContentExtract().setStaticFacetsSitesMap(getStaticFacetsSitesMap(bundle.getFacets().getSites()));
        extracts.getContentExtract().setStsChannel(getStsChannel(bundle));
        extracts.getContentExtract().setBrandCodeId(bundle.getAutomotive().getBrandCodeId());
        extracts.getContentExtract().setUvd(BooleanUtils.toBoolean(bundle.getClassifications().getIsUvd()));
        extracts.getContentExtract().setAutomotive(BooleanUtils.toBoolean(bundle.getClassifications().getIsAutomotive()));
        extracts.getContentExtract().setContentOnline(BooleanUtils.toBoolean(bundle.getOperational().getSites().getSears().getIsDispElig()));
        extracts.getContentExtract().setLayawayEligible(BooleanUtils.toBoolean(bundle.getFfm().getIsLayawayElig()));
        return extracts;

    }

    private Map<Sites, Boolean> getAllSitesEligInfo(Content content) {
        Map<Sites, Boolean> sitesDispEligibility = getAllSitesDispEligInfo(content);
        Map<Sites, Boolean> sitesEligibility = new HashMap<>();
        sitesEligibility.put(Sites.SEARS, sitesDispEligibility.get(Sites.SEARS) && BooleanUtils.toBoolean(content.getSites().contains("sears")));
        sitesEligibility.put(Sites.KMART, sitesDispEligibility.get(Sites.KMART) && BooleanUtils.toBoolean(content.getSites().contains("kmart")));
        sitesEligibility.put(Sites.MYGOFER, sitesDispEligibility.get(Sites.MYGOFER) && BooleanUtils.toBoolean(content.getSites().contains("mygofer")));
        sitesEligibility.put(Sites.SEARSPR, sitesDispEligibility.get(Sites.SEARSPR) && BooleanUtils.toBoolean(content.getSites().contains("puertorico")));
        sitesEligibility.put(Sites.COMMERCIAL, sitesDispEligibility.get(Sites.COMMERCIAL) && BooleanUtils.toBoolean(content.getSites().contains("scom")));
        return sitesEligibility;
    }
    
    private Map<Sites, Boolean> getAllSitesEligInfo(Bundle bundle) {
        Map<Sites, Boolean> sitesDispEligibility = getAllSitesDispEligInfo(bundle);
        Map<Sites, Boolean> sitesEligibility = new HashMap<>();
        sitesEligibility.put(Sites.SEARS, sitesDispEligibility.get(Sites.SEARS) && BooleanUtils.toBoolean(bundle.getSites().contains("sears")));
        sitesEligibility.put(Sites.KMART, sitesDispEligibility.get(Sites.KMART) && BooleanUtils.toBoolean(bundle.getSites().contains("kmart")));
        sitesEligibility.put(Sites.MYGOFER, sitesDispEligibility.get(Sites.MYGOFER) && BooleanUtils.toBoolean(bundle.getSites().contains("mygofer")));
        sitesEligibility.put(Sites.SEARSPR, sitesDispEligibility.get(Sites.SEARSPR) && BooleanUtils.toBoolean(bundle.getSites().contains("puertorico")));
        return sitesEligibility;
    }

    private Map<Sites, Boolean> getAllSitesDispEligInfo(Content content) {
        return getAllSitesDispEligContentAndBundlesInfo(content.getOperational());
    }

    private Map<Sites, Boolean> getAllSitesDispEligInfo(Bundle bundle) {
        return getAllSitesDispEligContentAndBundlesInfo(bundle.getOperational());
    }

    private Map<Sites, Boolean> getAllSitesDispEligContentAndBundlesInfo(Operational operational) {

        Map<Sites, Boolean> sitesDispEligibility = Maps.newHashMap();

        sitesDispEligibility.put(Sites.SEARS, BooleanUtils.toBoolean(operational.getSites().getSears().getIsDispElig()));
        sitesDispEligibility.put(Sites.KMART, BooleanUtils.toBoolean(operational.getSites().getKmart().getIsDispElig()));
        sitesDispEligibility.put(Sites.MYGOFER, BooleanUtils.toBoolean(operational.getSites().getMygofer().getIsDispElig()));
        sitesDispEligibility.put(Sites.SEARSPR, BooleanUtils.toBoolean(operational.getSites().getPuertorico().getIsDispElig()));
        sitesDispEligibility.put(Sites.COMMERCIAL, BooleanUtils.toBoolean(operational.getSites().getScom().getIsDispElig()));

        return sitesDispEligibility;
    }

    private String getStsChannel(Bundle bundle) {
        String stsChannel = null;
        if (BooleanUtils.toBoolean(bundle.getFfm().getIsShipFromStoreElig())) {
            stsChannel = bundle.getFfm().getShipToStoreChannel();
        }
        return stsChannel;
    }

    private List<String> getCollectionProductIdsList(Bundle bundle) {

        return getProductIdsList(bundle.getProducts());

    }

    private List<String> getBundleGroupProductIds(List<BundleGroup> bundleGroups) {
        List<String> productIds = Lists.newArrayList();
        for (BundleGroup bundleGroup : bundleGroups) {
            productIds.addAll(getProductIdsList(bundleGroup.getProducts()));
        }
        return productIds;
    }

    private List<String> getProductIdsList(List<Products> products) {
        List<String> productIds = Lists.newArrayList();
        for (Products product : products) {
            productIds.add(product.getId());
        }
        return productIds;
    }


    private List<String> getAlternateImgUrls(Content content) {
        List<String> alternateImgUrls = new ArrayList<>();

        List<AssetsImgs> imageAssets = content.getAssets().getImgs();
        for (AssetsImgs imageAsset : imageAssets) {
            if (StringUtils.equalsIgnoreCase(imageAsset.getType(), "A")) {
                if (CollectionUtils.isNotEmpty(imageAsset.getVals())) {
                    for (ImageAttrs imageAttr : imageAsset.getVals()) {
                        alternateImgUrls.add(imageAttr.getSrc());
                    }
                }
            }
        }
        return alternateImgUrls;
    }

    private List<String> getAlternateImgUrls(Bundle bundle) {
        List<String> alternateImgUrls = new ArrayList<>();

        List<com.shc.ecom.gb.doc.collection.AssetsImgs> imageAssets = bundle.getAssets().getImgs();
        for (com.shc.ecom.gb.doc.collection.AssetsImgs imageAsset : imageAssets) {
            if (StringUtils.equalsIgnoreCase(imageAsset.getType(), "A")) {
                if (CollectionUtils.isNotEmpty(imageAsset.getVals())) {
                    for (AssetsImgsVals assetImgsVals : imageAsset.getVals()) {
                        alternateImgUrls.add(assetImgsVals.getImg().getAttrs().getSrc());
                    }
                }
            }
        }
        return alternateImgUrls;
    }

    private String getPrimaryImageUrl(Content content) {
        String primaryImgUrl = null;

        List<AssetsImgs> imageAssets = content.getAssets().getImgs();
        for (AssetsImgs imageAsset : imageAssets) {
            if (StringUtils.equalsIgnoreCase(imageAsset.getType(), "P")) {
                if (CollectionUtils.isNotEmpty(imageAsset.getVals())) {
                    primaryImgUrl = imageAsset.getVals().get(0).getSrc();
                    break;
                }
            }
        }
        return primaryImgUrl;
    }

    private String getPrimaryImageUrl(Bundle bundle) {
        String primaryImgUrl = null;

        List<com.shc.ecom.gb.doc.collection.AssetsImgs> imageAssets = bundle.getAssets().getImgs();
        for (com.shc.ecom.gb.doc.collection.AssetsImgs imageAsset : imageAssets) {
            if (StringUtils.equalsIgnoreCase(imageAsset.getType(), "P")) {
                if (CollectionUtils.isNotEmpty(imageAsset.getVals())) {
                    primaryImgUrl = imageAsset.getVals().get(0).getImg().getAttrs().getSrc();
                    break;
                }
            }
        }
        return primaryImgUrl;
    }

    private double getCatConfScore(Double catConfScore) {
        if (catConfScore == null) {
            return 0.0;
        }
        return catConfScore;
    }

    private Map<Sites, List<NameValue>> getStaticFacetsSitesMap(FacetSites facetSites) {
        Map<Sites, List<NameValue>> siteFacetsMap = new HashMap<>();
        siteFacetsMap.put(Sites.SEARS, facetSites.getSears().getStatic());
        siteFacetsMap.put(Sites.KMART, facetSites.getKmart().getStatic());
        return siteFacetsMap;
    }

    private String getScomFilterFieldValues(Content content, String fieldName) {
        String filedValue = null;
        Optional<List<NameValue>> staticAttrs = Optional.ofNullable(content.getFacets().getSites().getSears().getStatic());
        if(BooleanUtils.toBoolean(content.getOperational().getSites().getScom().getIsDispElig())) {
            for (NameValue nameValue : staticAttrs.orElse(new ArrayList<>())) {
                if(StringUtils.equalsIgnoreCase(nameValue.getName(), fieldName)) {
                    filedValue = nameValue.getValue();
                }
            }
        }
        return filedValue;
    }

    private Map<Sites, List<List<String>>> getSiteDapHierarchiesMap(HierarchySites hierarchySites) {
        Map<Sites, List<List<String>>> siteDapHierarchyMap = new HashMap<>();
        siteDapHierarchyMap.put(Sites.SEARS, getHierarchiesAsList(hierarchySites.getSearsDap().getHierarchies()));
        siteDapHierarchyMap.put(Sites.KMART, getHierarchiesAsList(hierarchySites.getKmartDap().getHierarchies()));
        siteDapHierarchyMap.put(Sites.MYGOFER, getHierarchiesAsList(hierarchySites.getMygoferDap().getHierarchies()));
        return siteDapHierarchyMap;
    }

    private Map<Sites, List<List<String>>> getSiteWebHierarchiesMap(HierarchySites hierarchySites) {
        Map<Sites, List<List<String>>> siteWebHierarchyMap = new HashMap<>();
        siteWebHierarchyMap.put(Sites.SEARS, getValidHierarchiesAsList(hierarchySites.getSears().getHierarchies(), Sites.SEARS.getCatalogId()));
        siteWebHierarchyMap.put(Sites.KMART, getValidHierarchiesAsList(hierarchySites.getKmart().getHierarchies(), Sites.KMART.getCatalogId()));
        siteWebHierarchyMap.put(Sites.MYGOFER, getValidHierarchiesAsList(hierarchySites.getMygofer().getHierarchies(), Sites.MYGOFER.getCatalogId()));
        siteWebHierarchyMap.put(Sites.SEARSPR, getValidHierarchiesAsList(hierarchySites.getPuertorico().getHierarchies(), Sites.SEARSPR.getCatalogId()));
        siteWebHierarchyMap.put(Sites.KENMORE, getValidHierarchiesAsList(hierarchySites.getKenmore().getHierarchies(), Sites.KENMORE.getCatalogId()));
        siteWebHierarchyMap.put(Sites.CRAFTSMAN, getValidHierarchiesAsList(hierarchySites.getCraftsman().getHierarchies(), Sites.CRAFTSMAN.getCatalogId()));
        siteWebHierarchyMap.put(Sites.COMMERCIAL, getValidHierarchiesAsList(hierarchySites.getScom().getHierarchies(), Sites.COMMERCIAL.getCatalogId()));
        return siteWebHierarchyMap;
    }

    private List<List<String>> getValidHierarchiesAsList(List<SpecificHierarchy> hierarchies, String catalogId) {
        List<List<String>> lNamesList = new ArrayList<>();
        for (SpecificHierarchy hierarchy : hierarchies) {
            List<String> lNames = new ArrayList<>();
            List<WebIdName> webIdNameList = hierarchy.getSpecificHierarchy();
            for (WebIdName webIdName : webIdNameList) {
                lNames.add(webIdName.getName());
            }
            lNamesList.add(lNames);
        }
        return lNamesList;
    }

    private List<List<String>> getHierarchiesAsList(List<SpecificHierarchy> hierarchies) {
        List<List<String>> lNamesList = new ArrayList<>();
        for (SpecificHierarchy hierarchy : hierarchies) {
            List<String> lNames = new ArrayList<>();
            List<WebIdName> webIdNameList = hierarchy.getSpecificHierarchy();
            for (WebIdName webIdName : webIdNameList) {
                lNames.add(webIdName.getName());
            }
            lNamesList.add(lNames);
        }
        return lNamesList;
    }

    private String getDescription(List<Desc> descList, String type) {
        for (Desc desc : descList) {
            if (StringUtils.equalsIgnoreCase(desc.getType(), type)) {
                return desc.getVal();
            }
        }
        return null;
    }

    private List<String> getSoldBy(Bundle bundle, ContextMessage context) {
        List<String> soldByList = new ArrayList<>();
        boolean shcStores = context.getStoreName().matches(Stores.SEARS.getStoreName()) || context.getStoreName().matches(Stores.SEARSPR.getStoreName()) || context.getStoreName().matches(Stores.KMART.getStoreName()) || context.getStoreName().matches(Stores.MYGOFER.getStoreName());
        if (shcStores) {
            soldByList.add(bundle.getFfm().getSoldBy());
            return soldByList;
        }
        String mpSellerName = bundle.getMarketplace().getSeller().getName();
        boolean directSears = BooleanUtils.toBoolean(bundle.getMarketplace().getSeller().getIsDirectSears());
        if (StringUtils.isNotEmpty(mpSellerName)) {
            soldByList.add(mpSellerName);
        } else if (directSears) {
            soldByList.add("Sears");
        }
        return soldByList;
    }

    private String getItemCondition(Bundle bundle) {
        List<Sku> skus = bundle.getVariants().getSku();
        for (Sku sku : skus) {
            if (StringUtils.isNotBlank(sku.getCondition().getStatus())) {
                return ItemCondition.getItemCondition(sku.getCondition().getStatus());
            }
        }
        return ItemCondition.NEW.getStatus();
    }

    private LocalDate getFirstOnlineDate(String firstOnlineDate) {
        if (firstOnlineDate == null) {
            return null;
        }
        return LocalDate.parse(firstOnlineDate, DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
    }

    private String getCatentryId(Long catentryId) {
        if (catentryId == null) {
            return null;
        }
        return String.valueOf(catentryId);
    }

    private String getDivision(Bundle bundle) {
        String division = null;
        if (CollectionUtils.isNotEmpty(bundle.getTaxonomy().getStore().gethierarchy())) {
            division = bundle.getTaxonomy().getStore().gethierarchy().get(0).getName();
        }
        return division;
    }

    private Set<String> getIntlGroupIds(Bundle bundle) {
        Set<String> intllGroupIds = new HashSet<>();

        List<CountryGrp> countryGrpList = bundle.getShipping().getIntlCountryGrps();
        for (CountryGrp countryGrp : countryGrpList) {
            intllGroupIds.add(countryGrp.getId());
        }
        return intllGroupIds;
    }

    /**
     * SEARCH-1271: Function to return set of unique 'name' and 'displayPath' in
     * vocTags of the product. in both content and offer extract component. Need
     * to think about this.
     *
     * @param vocTags
     * @return List of unique name and displayPath attribute
     */
    private List<String> getGeospotTag(List<VocTags> vocTags) {
        Set<String> geoSpotTagList = new HashSet<>();
        for (VocTags vocTag : vocTags) {
			if (!VocTagUtil.isValidTagName(vocTag)) {
				continue;
			}
			geoSpotTagList.add(vocTag.getName());
            if (StringUtils.isNotEmpty(vocTag.getDispPath())) {
                geoSpotTagList.add(vocTag.getDispPath());
            }
        }
        return new ArrayList<>(geoSpotTagList);
    }

    private List<String> getOfferIdsForBundles(Bundle bundle, WorkingDocument wd) {
        List<String> productIds = new ArrayList<>();
        productIds.addAll(getBundleProductIds(bundle));
        productIds.addAll(getCollectionProductIds(bundle));
        return getOfferIds(productIds, wd);
    }

    private List<String> getBundleProductIds(Bundle bundle) {
        List<String> productIds = new ArrayList<>();
        for (BundleGroup bundleGroup : bundle.getBundleGroup()) {
            if (!StringUtils.equalsIgnoreCase(bundleGroup.getType(), "required")) {
                continue;
            }
            for (Products product : bundleGroup.getProducts()) {
                productIds.add(product.getId());
            }
        }
        return productIds;
    }

    private List<String> getCollectionProductIds(Bundle bundle) {
        List<String> productIds = new ArrayList<>();
        for (Products product : bundle.getProducts()) {
            productIds.add(product.getId());
        }
        return productIds;
    }

    @SuppressWarnings("unused")
    private List<String> getOfferIds(String ssin, WorkingDocument wd) {
        List<String> offerIdsExtracted = wd.getExtracts().getOfferIds();
        if (!CollectionUtils.isEmpty(offerIdsExtracted)) {
            return offerIdsExtracted;
        }
        return gbServiceFacade.getOfferIdsByAltKey(wd.getExtracts().getSsin());
    }

    private List<String> getOfferIds(List<String> ssins, WorkingDocument wd) {
        List<String> offerIdsExtracted = wd.getExtracts().getOfferIds();
        if (!CollectionUtils.isEmpty(offerIdsExtracted)) {
            return offerIdsExtracted;
        }
        List<String> offerIds = new ArrayList<>();
        for (String ssin : ssins) {
            offerIds.addAll(gbServiceFacade.getOfferIdsbyAltKeyForBundles(ssin));
        }
        return offerIds;
    }
}
