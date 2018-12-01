package com.shc.ecom.search.transformations;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.search.persistence.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codahale.metrics.Timer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.shc.ecom.gb.doc.common.ImageAttrs;
import com.shc.ecom.gb.doc.content.NameValue;
import com.shc.ecom.gb.doc.varattrs.DefiningAttrs;
import com.shc.ecom.gb.doc.varattrs.Value;
import com.shc.ecom.search.classifications.Classification;
import com.shc.ecom.search.classifications.ClassificationFactory;
import com.shc.ecom.search.common.constants.ItemCondition;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.customerrating.CustomerRatingDto;
import com.shc.ecom.search.common.exception.SearchCommonException;
import com.shc.ecom.search.common.expirablefields.ExpirableSolrField;
import com.shc.ecom.search.common.level3CatData.Level3CatDataDto;
import com.shc.ecom.search.common.level3CatData.SubcatAttributes;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.nFiltersData.NFiltersDataDto;
import com.shc.ecom.search.common.nFiltersData.SearchableAttributes;
import com.shc.ecom.search.common.seller.OrderAmountChargeRange;
import com.shc.ecom.search.common.seller.WeightRange;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.docbuilder.OfferDoc;
import com.shc.ecom.search.docbuilder.OfferDocBuilder;
import com.shc.ecom.search.docbuilder.SearchDocBuilder;
import com.shc.ecom.search.extract.components.filters.Taxonomy;
import com.shc.ecom.search.extract.components.fitments.AutomotiveOffer;
import com.shc.ecom.search.extract.components.pricing.Price;
import com.shc.ecom.search.extract.components.pricing.PriceAlgorithm;
import com.shc.ecom.search.extract.components.prodstats.ProdStats;
import com.shc.ecom.search.extract.components.promo.PromoRegular;
import com.shc.ecom.search.extract.components.promo.Promotions;
import com.shc.ecom.search.extract.components.tm.TopicModelingService;
import com.shc.ecom.search.extract.extracts.ContentExtract;
import com.shc.ecom.search.extract.extracts.OfferExtract;
import com.shc.ecom.search.extract.extracts.PASExtract;
import com.shc.ecom.search.extract.extracts.PriceExtract;
import com.shc.ecom.search.extract.extracts.PromoExtract;
import com.shc.ecom.search.extract.extracts.SellerExtract;
import com.shc.ecom.search.extract.extracts.UasExtract;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.util.DateUtil;
import com.shc.ecom.search.util.ExpirableFieldsUtil;
import com.shc.ecom.search.util.OfferUtil;

import static com.shc.common.misc.GlobalConstants.COMMA;
import static com.shc.common.misc.GlobalConstants.ONE;
import static com.shc.common.misc.GlobalConstants.ZERO;

/**
 * This is an abstract class that defines transformations for all fields in
 * Search Index. <p/>
 * that provides extraction/transformation for common fields for all
 * classifications (<i>Variation, Non-variation and Bundle</i>). Classification class is further extended by actual
 * classification types and they specialize these transformations as required by
 * the type.
 * <br/>
 * Following are either the <i>default</i> transformations or the <i>final</i>
 * transformation for search fields. Some of the transformations would be
 * specialized and overridden by appropriate types. Eg: NonVariation overrides
 * #getId() appropriate to non-variations. <br/>
 * Transformation which are not overridden by any variation types will be set in
 * the Classification that directly inherits this.
 * <p/>
 * Transformations which will be overridden will be set in their respective
 * classes. <br/>
 * <br/>
 *
 * @author rgopala
 */
@Component
public abstract class BaseFieldTransformations implements Serializable {

    protected static final String SEPARATOR = "_";
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseFieldTransformations.class.getName());
    private static final long serialVersionUID = -7826303640994791447L;
    private static final String DEFAULT_RANK = "1";

    private static final String DEFAULT_LOCALAD = "0";
    @Autowired
    protected ConsumerReportsRating consumerReportsRating;
    @Autowired
    protected CommonTransformationLogic commonTransformationLogic;
    @Autowired
    protected TopicModelingService topicModelingService;
    @Autowired
    private PriceAlgorithm priceAlgorithm;
    @Autowired
    private BrowseBoost browseBoost;
    @Autowired
    private PersistenceFactory persistenceFactory;
    @Autowired
    private Level3CatsData level3CatsData;

    @Autowired
    private NFiltersData nFiltersData;

    @Autowired
    private CustomerRating customerRating;
    @Autowired
    private Rebates rebates;

    @Autowired
    private ClassificationFactory factory;
    @Autowired
    private Taxonomy taxonomy;
    //	@org.springframework.beans.factory.annotation.Value("${excluded.category}")
    private String excludedCategory = PropertiesLoader.getProperty(GlobalConstants.EXCLUDED_CATEGORY);
    private List<String> accessoryIdentifiers = PropertiesLoader.getPropertyAsList(GlobalConstants.ACCESSORY_IDENTIFIERS, COMMA);
    @Autowired
    private MetricManager metricManager;

    public static List<Stores> freeShippingStoreList = Lists.newArrayList();
    public static final List<Integer> RANGE_CUT_OFF = new ArrayList<>(Arrays.asList(10, 15, 20, 25, 30, 40, 50, 60, 70, 80));

    static {
        freeShippingStoreList.add(Stores.SEARS);
        freeShippingStoreList.add(Stores.SEARSPR);
        freeShippingStoreList.add(Stores.KMART);
        freeShippingStoreList.add(Stores.FBM);
    }


    @PostConstruct
    public void init() {

    }

	/* Dynamic Fields Transformation Begin */

    protected String getOpsUuid_s(ContextMessage context) {
        return context.getTimestamp();
    }

    protected String getOpsCurrentServer_i(ContextMessage context) {
        return context.getCurrentServer();
    }


    protected String getLevel1_dis(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return null;
        }
        return taxonomy.getPrimaryHierarchyLevelName(wd, Stores.getSite(context.getStoreName()), 1);
    }

    protected String getLevel1_dis_km(WorkingDocument wd, ContextMessage context) {
        String level1_dis_km = null;
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            level1_dis_km = taxonomy.getPrimaryHierarchyLevelName(wd, Sites.KMART, 1);
        }
        return level1_dis_km;
    }

    protected String getLevel2_dis(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return null;
        }
        return taxonomy.getPrimaryHierarchyLevelName(wd, Stores.getSite(context.getStoreName()), 2);
    }

    protected String getLevel2_dis_km(WorkingDocument wd, ContextMessage context) {
        String level2_dis_km = null;
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            level2_dis_km = taxonomy.getPrimaryHierarchyLevelName(wd, Sites.KMART, 2);
        }
        return level2_dis_km;
    }

    protected String getLevel3_dis(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return null;
        }
        return taxonomy.getPrimaryHierarchyLevelName(wd, Stores.getSite(context.getStoreName()), 3);
    }

    protected String getLevel3_dis_km(WorkingDocument wd, ContextMessage context) {
        String level3_dis_km = null;
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            level3_dis_km = taxonomy.getPrimaryHierarchyLevelName(wd, Sites.KMART, 3);
        }
        return level3_dis_km;
    }

    protected String getLevel4_dis(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return null;
        }
        return taxonomy.getPrimaryHierarchyLevelName(wd, Stores.getSite(context.getStoreName()), 4);
    }

    protected String getLevel4_dis_km(WorkingDocument wd, ContextMessage context) {
        String level4_dis_km = null;
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            level4_dis_km = taxonomy.getPrimaryHierarchyLevelName(wd, Sites.KMART, 4);
        }
        return level4_dis_km;
    }

    protected String getLevel5_dis(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return null;
        }
        return taxonomy.getPrimaryHierarchyLevelName(wd, Stores.getSite(context.getStoreName()), 5);
    }

    protected String getLevel5_dis_km(WorkingDocument wd, ContextMessage context) {
        String level5_dis_km = null;
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            level5_dis_km = taxonomy.getPrimaryHierarchyLevelName(wd, Sites.KMART, 5);
        }
        return level5_dis_km;
    }

    protected String getLevel6_dis(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return null;
        }
        return taxonomy.getPrimaryHierarchyLevelName(wd, Stores.getSite(context.getStoreName()), 6);
    }

    protected String getLevel6_dis_km(WorkingDocument wd, ContextMessage context) {
        String level6_dis_km = null;
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            level6_dis_km = taxonomy.getPrimaryHierarchyLevelName(wd, Sites.KMART, 6);
        }
        return level6_dis_km;
    }

    protected String getLevel7_dis(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return null;
        }
        return taxonomy.getPrimaryHierarchyLevelName(wd, Stores.getSite(context.getStoreName()), 7);
    }

    protected String getLevel7_dis_km(WorkingDocument wd, ContextMessage context) {
        String level7_dis_km = null;
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            level7_dis_km = taxonomy.getPrimaryHierarchyLevelName(wd, Sites.KMART, 7);
        }
        return level7_dis_km;
    }

	/* Dynamic Fields Transformation End */

    /**
     * Overridden by: NV, V and B
     *
     * @param wd
     * @param context
     * @return
     */
    protected String getId(WorkingDocument wd, ContextMessage context) {
        return wd.getExtracts().getSsin() + "_" + context.getStoreName().toLowerCase();
    }

    /**
     * Overridden by: NV, V and B
     *
     * @param wd
     * @return
     */
    protected String getBeanType(WorkingDocument wd) {
        return null;

    }

    /**
     * @param wd
     * @return
     */
    protected String getFullmfpartno(WorkingDocument wd) {
        return wd.getExtracts().getContentExtract().getModelNo();
    }

    /**
     * SSIN is used as partnumber in Search
     *
     * @param wd
     * @return
     */
    protected String getPartNumber(WorkingDocument wd) {
        return wd.getExtracts().getSsin();
    }

    /**
     * Overridden by: NV
     *
     * @param wd
     * @param context
     * @return
     */
    protected String getItemNumber(WorkingDocument wd, ContextMessage context) {
        return null;
    }

    /**
     * @param wd
     * @return
     */
    protected List<String> getStoreOrigin(WorkingDocument wd) {
        List<String> storeOriginList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(wd.getExtracts().getOfferExtract().getSoldBy())) {
            storeOriginList = wd.getExtracts().getOfferExtract().getSoldBy();
        }
        return storeOriginList;
    }

    /**
     * Taking only primary image attribute into consideration to set the image status.
     *
     * @param wd
     * @return
     */
    protected String getImageStatus(WorkingDocument wd) {
        if (StringUtils.isNotEmpty(wd.getExtracts().getContentExtract().getPrimaryImageUrl())) {
            return ONE;
        }
        return ZERO;
    }

    /**
     * Checks if the isSwatchLink node is set in greenbox. Return YES, if more than one distinct family names are available in
     * the collection.
     *
     * @param wd
     * @return
     */
    protected String getSwatchesStatus(WorkingDocument wd) {
        List<String> colorAttrNames = Arrays.asList("Color", "Color Family", "Overall Color");

        // MT-67273 Storing familyNameVals in List instead of Set to count different swatches with same familyName.
        List<String> familyNameVals = new ArrayList<>();

        for (DefiningAttrs attrs : wd.getExtracts().getVarAttrExtract().getDefiningAttrsList()) {
            if (!colorAttrNames.contains(attrs.getName())) {
                continue;
            }
            List<Value> values = attrs.getVal();

            for (Value value : values) {
                String familyName = value.getFamilyName();
                ImageAttrs swatchImage = value.getSwatchImg();
                ImageAttrs primaryImage = value.getPrimaryImg();
                String swatchImageURL = null;
                String primaryImageURL = null;
                if (swatchImage != null) {
                    swatchImageURL = swatchImage.getSrc();
                }
                if (primaryImage != null) {
                    primaryImageURL = primaryImage.getSrc();
                }

                // MT-67273 Fix. Count only that swatch which has a valid swatchImageURL and valid primaryImageURL
                if (StringUtils.isNotEmpty(familyName) &&
                        StringUtils.isNotEmpty(swatchImageURL) &&
                        StringUtils.isNotEmpty(primaryImageURL)) {
                    familyNameVals.add(familyName);
                }
            }

        }

        // Display swatches on PLP only when its more than one
        if (familyNameVals.size() > 1) {
            return "YES";
        } else {
            return "NO";
        }
    }

    /**
     * Returns the swatch details in JSON format. The JSON object is converted to String and stored in document. The structure of
     * returned string can be found in SEARCH-543. The different swatch details are sorted in ascending order of their sequence
     * number.
     *
     * @param wd
     * @return
     */
    protected String getSwatchInfo(WorkingDocument wd) {
        List<String> colorAttrNames = Arrays.asList("Color", "Color Family", "Overall Color");
        JSONArray mainList = new JSONArray();
        if (wd.getExtracts().getVarAttrExtract().getDefiningAttrsList().isEmpty()) {
            return null;
        }

        for (DefiningAttrs attrs : wd.getExtracts().getVarAttrExtract().getDefiningAttrsList()) {
            JSONArray swatchesList = new JSONArray();
            if (!colorAttrNames.contains(attrs.getName())) {
                continue;
            }
            JSONObject swatchInfo = new JSONObject();
            List<Value> values = attrs.getVal();
            Map<Double, List<JSONObject>> valMap = new TreeMap<>();

            for (Value value : values) {
                JSONObject singleSwatch = new JSONObject();
                Double sequence = 0.0;
                String swatchImgURL = StringUtils.EMPTY;
                String primaryImgURL = StringUtils.EMPTY;
                String familyName = StringUtils.EMPTY;

                if (value.getPrimaryImg() != null && value.getPrimaryImg().getSrc() != null) {
                    primaryImgURL = value.getPrimaryImg().getSrc();
                    singleSwatch.put("primaryImg", primaryImgURL);
                }

                if (value.getSwatchImg() != null && value.getSwatchImg().getSrc() != null) {
                    swatchImgURL = value.getSwatchImg().getSrc();
                    singleSwatch.put("swatchImg", swatchImgURL);
                }

                if (value.getFamilyName() != null) {
                    familyName = value.getFamilyName();
                    singleSwatch.put("familyName", familyName);
                }

                if (value.getName() != null) {
                    singleSwatch.put("name", value.getName());
                }

                if (value.getSeq() != null) {
                    sequence = value.getSeq();
                }

                // MT-67273 Fix. Add only that swatch which has a valid swatchImageURL and valid primaryImageURL
                if (StringUtils.isNotEmpty(swatchImgURL) &&
                        StringUtils.isNotEmpty(primaryImgURL) &&
                        StringUtils.isNotEmpty(familyName)) {
                    if (valMap.containsKey(sequence)) {
                        List<JSONObject> swatchListWithSameSeq = valMap.get(sequence);
                        swatchListWithSameSeq.add(singleSwatch);
                        valMap.put(sequence, swatchListWithSameSeq);
                    } else {
                        List<JSONObject> swatchListWithSameSeq = new ArrayList<>();
                        swatchListWithSameSeq.add(singleSwatch);
                        valMap.put(sequence, swatchListWithSameSeq);
                    }
                }
            }

            for (Map.Entry<Double, List<JSONObject>> entry : valMap.entrySet()) {
                List<JSONObject> swatchesWithSameSeq = entry.getValue();
                for (JSONObject singleSwatch : swatchesWithSameSeq) {
                    swatchesList.put(singleSwatch);
                }
            }

            // Add to main list only if number of swatches is more than one
            if (swatchesList.length() > 1) {
                swatchInfo.put("vals", swatchesList);
                swatchInfo.put("type", attrs.getName());
                mainList.put(swatchInfo);
            }
        }

        // Return null for products with no type of swatch.
        if (mainList.length() > 0) {
            return mainList.toString().replace("\\", "");
        } else {
            return null;
        }

    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getXref(WorkingDocument wd, ContextMessage context) {
        Set<String> xRef = new HashSet<>();

        Map<String, String> offerIdSoldByMap = wd.getExtracts().getOfferExtract().getOfferIdSoldByMap();

        for (Map.Entry<String, String> entry : offerIdSoldByMap.entrySet()) {
            String offerId = entry.getKey();
            String soldBy = entry.getValue();

            xRef.add(offerId);
            if (Stores.SEARS.matches(soldBy) || Stores.KMART.matches(soldBy)) {
                xRef.addAll(getOfferIdCombinations(offerId));
            }

            if (CollectionUtils.isNotEmpty(wd.getExtracts().getOfferExtract().getUpc())) {
                xRef.addAll(wd.getExtracts().getOfferExtract().getUpc());
            }

        }

        Set<String> setKmartOrSearsOfferIds = wd.getExtracts().getBuyboxExtract().getKmartOrSearsOfferIds();
        for(String offerId : setKmartOrSearsOfferIds){
            xRef.addAll(getOfferIdCombinations(offerId));
        }

        /* SEARCH-2282 Indexing documents offer-id and grouped offer-ids to xref field to make rank 1 document searchable */
        if (CollectionUtils.isNotEmpty(wd.getExtracts().getOfferIds())) {
            xRef.addAll(wd.getExtracts().getOfferIds());
        }
        Map<Sites, List<String>> groupedOfferIds = wd.getExtracts().getBuyboxExtract().getGroupedOfferIds();
        if (groupedOfferIds.size() > 0) {
            List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
            for (Sites site : eligibleSites) {
                if (groupedOfferIds.containsKey(site) && CollectionUtils.isNotEmpty(groupedOfferIds.get(site))) {
                    xRef.addAll(groupedOfferIds.get(site));
                }
            }
        }

        // TODO: Remember to deal with UVD
        if (StringUtils.isNotEmpty(wd.getExtracts().getContentExtract().getModelNo())) {
            xRef.add(wd.getExtracts().getContentExtract().getModelNo());
        }
        return xRef;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getPromoIds(WorkingDocument wd, ContextMessage context) {
        Set<String> storePromoIds = new HashSet<>();
        List<Integer> storeIds = Stores.getStoreId(wd, context);

        for (Integer storeId : storeIds) {
            String prefix = Integer.toString(storeId) + SEPARATOR;
            Set<String> promoIds = new HashSet<>();
            if (wd.getExtracts().getPromoExtract().getStorePromoExtracts().containsKey(Stores.getStore(storeId))) {
                promoIds = wd.getExtracts().getPromoExtract().getStorePromoExtracts().get(Stores.getStore(storeId)).getPromoIds();
            }
            promoIds.forEach(promoId -> storePromoIds.add(prefix + promoId));
        }
        return storePromoIds;
    }

    /**
     * @param wd
     * @return
     */
    protected List<String> getExpirableFields(WorkingDocument wd) {
        List<ExpirableSolrField> expirableSolrFields = wd.getExtracts().getExpirableFields();
        List<String> expirableFields = new ArrayList<>();
        for (ExpirableSolrField singleNode : expirableSolrFields){
            if(!ExpirableFieldsUtil.isValidExpirableField(singleNode)){
                continue;
            }

            String fieldValue = singleNode.getFieldValue();
            fieldValue = fieldValue.replaceAll("[^A-Za-z0-9_]", "_").replaceAll("[_]+", "_");

            String startDate = DateUtil.convertToStandardDateTime(singleNode.getStartDate(), false);
            String endDate = DateUtil.convertToStandardDateTime(singleNode.getEndDate(), true);

            String singleFieldStartType = singleNode.getFieldType() + "_" + fieldValue + "_" + ExpirableSolrField.STARTDATECONST + "_" + startDate;
            String singleFieldEndType = singleNode.getFieldType() + "_" + fieldValue + "_" + ExpirableSolrField.ENDDATECONST + "_" + endDate;

            expirableFields.add(singleFieldStartType);
            expirableFields.add(singleFieldEndType);
        }

        return expirableFields;
    }

    /**
     * Overridden by: NV
     *
     * @param wd
     * @param context
     * @return
     */
    protected String getEGiftEligibile(WorkingDocument wd, ContextMessage context) {
        return null;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected int getSellerTierRank(WorkingDocument wd, ContextMessage context) {
        return SellerExtract.SellerTierRank.valueOf(getSellerTier(wd, context).toUpperCase()).getRank();
    }

    /**
     * @param wd
     * @return
     */
    protected String getName(WorkingDocument wd) {
        //moving the logic to common place accessible during extract phase of topic modeling
        return commonTransformationLogic.getName(wd);
    }

    /**
     * @param wd
     * @return
     */
    protected String getNameSearchable(WorkingDocument wd) {
        String brand = getBrand(wd);
        String name = getName(wd);
        return getNameSearchable(brand, name);
        //moving the logic to common place accessible during extract phase of topic modeling

    }


    protected String getNameSearchable(String brand, String name) {
        return commonTransformationLogic.getNameSearchableFromCommonTransformationLogic(brand, name);
    }

    /**
     * @param wd
     * @return
     */
    protected String getImage(WorkingDocument wd) {
        return wd.getExtracts().getContentExtract().getPrimaryImageUrl();
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getAlternateImage(WorkingDocument wd, ContextMessage context) {
        if (Stores.SEARS.matches(context.getStoreName()) || Stores.KMART.matches(context.getStoreName()) || Stores.TABLET.matches(context.getStoreName())) {
            return wd.getExtracts().getContentExtract().getAlternateImageUrls();
        }
        return new ArrayList<String>();
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected String getDescription(WorkingDocument wd, ContextMessage context) {
        //moving the logic to common place accessible during extract phase of topic modeling
        return commonTransformationLogic.getDescriptionFromContentTransformationLogic(wd, context);
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getPromo(WorkingDocument wd, ContextMessage context) {
        List<String> storePromos = new ArrayList<>();
        List<Integer> storeIds = Stores.getStoreId(wd, context);
        for (Integer storeId : storeIds) {
            String prefix = Integer.toString(storeId) + SEPARATOR;
            String promo = ZERO;
            if (wd.getExtracts().getPromoExtract().getStorePromoExtracts().containsKey(Stores.getStore(storeId))) {
                promo = !CollectionUtils.isEmpty(wd.getExtracts().getPromoExtract().getStorePromoExtracts().get(Stores.getStore(storeId)).getRegularPromos()) ? ONE : ZERO;
            }
            storePromos.add(prefix + promo);
        }
        return storePromos;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getSubcatAttributes(WorkingDocument wd, ContextMessage context) {
        List<String> subCatAttributes = new ArrayList<>();

        // Get the level3CatsData from the feed file
        Map<String, Level3CatDataDto> level3CatData = level3CatsData.getLevel3Data();

        // Get the static attributes map from the content collection
        Map<String, List<String>> staticAttrsMap = getStaticAttrsMap(wd, context);

        // Get the level3 categories
        Set<String> level3Categories = getLevelNCategories(wd, context, 3);

        // Check for every category, whether static attribute is searchable or not
        for (String level3Category : level3Categories) {
            String categoryNoSpace = level3Category.replaceAll("\\s", StringUtils.EMPTY); // Removing spaces from the category
            if (level3CatData.containsKey(categoryNoSpace)) { // Check to see if the static attributes is present in the file
                Level3CatDataDto level3CatsDataDto = level3CatData.get(categoryNoSpace);
                List<SubcatAttributes> subcatAttrs = level3CatsDataDto.getSubcatAttributes();
                for (SubcatAttributes subcatAttr : subcatAttrs) {
                    if (staticAttrsMap.containsKey(subcatAttr.getName())) {
                        String name = subcatAttr.getName();
                        List<String> values = staticAttrsMap.get(subcatAttr.getName());
                        for (String value : values) {
                            subCatAttributes.add(level3Category + SEPARATOR + name + "=" + value);
                        }
                    }
                }
            }
        }

        return subCatAttributes;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getProductAttributes(WorkingDocument wd, ContextMessage context) {
        List<String> productAttributes = new ArrayList<String>();
        OfferExtract offerExtract = wd.getExtracts().getOfferExtract();
        ContentExtract contentExtract = wd.getExtracts().getContentExtract();
        if (offerExtract.isAlcohol()) {
            productAttributes.add("CONTAINS_ALCOHOL=YES");
        }
        if (offerExtract.isPerishable()) {
            productAttributes.add("PERISHABLE=YES");
        }
        if (offerExtract.isRefrigeration()) {
            productAttributes.add("REQUIRES_REFRIGERATION=YES");
        }
        if (offerExtract.isTobacco()) {
            productAttributes.add("CONTAINS_TOBACCO=YES");
        }
        if (offerExtract.isFreezing()) {
            productAttributes.add("REQUIRES_FREEZING=YES");
        }
        if (offerExtract.isWebExcl()) {
            productAttributes.add("WEB_EXCLUSIVE=YES");
        }
        if (CollectionUtils.isNotEmpty(offerExtract.getDfltFfmDisplay())) {
            for (String dfltFfmDisplay : offerExtract.getDfltFfmDisplay()) {
                productAttributes.add("DEFAULT_FULFILL_OPTION=" + dfltFfmDisplay);
            }
        }
        if (CollectionUtils.isNotEmpty(offerExtract.getViewOnly())) {
            if (StringUtils.equalsIgnoreCase(context.getStoreName(), Stores.SEARSPR.getStoreName())) {
                for (String viewOnly : offerExtract.getViewOnly()) {
                    productAttributes.add("VIEW_ONLY=" + viewOnly);
                }
            } else {
                for (String viewOnly : offerExtract.getViewOnly()) {
                    if (StringUtils.equalsIgnoreCase(viewOnly, "true")) {
                        productAttributes.add("VIEW_ONLY=YES");
                        if (offerExtract.isAutomotive()) {
                            productAttributes.add("VIEW_ONLY=AUTOMOTIVE");
                        }
                    }
                }

            }
        }

        if (offerExtract.isEnergyStar()) {
            productAttributes.add("ENERGY STAR Compliant=Yes");
        }
        if (StringUtils.equalsIgnoreCase(context.getStoreName(), Stores.KMART.getStoreName()) && offerExtract.isKmartPrElig()) {
            productAttributes.add("KMART_PR_ELIGIBLE=YES");
        }

        if (StringUtils.isNotEmpty(contentExtract.getAutofitment())) {
            switch (contentExtract.getAutofitment().toLowerCase()) {
                case "requires fitment":
                    productAttributes.add("VEHICLE_FITMENT=YES");
                    productAttributes.add("auto_fitment_required=YES");
                    productAttributes.add("autoFitment=Requires Fitment");
                    break;
                case "au cross fit":
                    productAttributes.add("autoFitment=AU Cross Fit");
                    break;
                case "no":
                    productAttributes.add("autoFitment=NO");
                    break;
                default:
                    productAttributes.add("auto_fitment_required=NO");
            }
        }

        if (StringUtils.isNotEmpty(contentExtract.getBrandCodeId())) {
            productAttributes.add("brand_id=" + contentExtract.getBrandCodeId());
        }

        if (StringUtils.isNotEmpty(contentExtract.getImaClassControlPid())) {
            productAttributes.add("UVD_CLASS_CONTROL_PID=" + contentExtract.getImaClassControlPid());
        }

        if (offerExtract.isConfigureTech()) {
            productAttributes.add("CONFIGURE_TECH=YES");
        }

        if (offerExtract.isOutletItem()) {
            productAttributes.add("HAS_991=YES");
        }

        if (offerExtract.isSimiElig()) {
            productAttributes.add("SIMI=YES");
        }

        Map<String, List<String>> staticAttrsMap = getStaticAttrsMap(wd, context);
        if (staticAttrsMap.containsKey("Tire Size")) {
            productAttributes.add("Tire Size=" + staticAttrsMap.get("Tire Size").get(0));
        }
        return productAttributes;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getStoreAttributes(WorkingDocument wd, ContextMessage context) {
        List<String> storeAttributes = new ArrayList<>();
        final String storeName = context.getStoreName();
        // The outgoing indexing job populates a list of store ids (one for the
        // actual store and one for the cross format store). This list is
        // iterated to add store attributes.

        List<Integer> storeIds = new ArrayList<>();
        storeIds.addAll(Stores.getStoreId(wd, context));

        OfferExtract offerExtract = wd.getExtracts().getOfferExtract();
        PromoExtract promoExtract = wd.getExtracts().getPromoExtract();
        for (Integer storeID : storeIds) {
            if (offerExtract.isEnergyStar()) {
                storeAttributes.add(storeID + "_ENERGY STAR Compliant=Yes");
            }
            if (StringUtils.isNotEmpty(offerExtract.getStreetDt())) {
                storeAttributes.add(storeID + "_STREET_DATE=" + offerExtract.getStreetDt());
            }
            if (offerExtract.isIntlShipElig()) {
                storeAttributes.add(storeID + "_INT_SHIP_ELIGIBLE=YES");
            }
            if (offerExtract.isMailable()) {
                storeAttributes.add(storeID + "_MAILABLE_FLAG=1");
            }
            if (offerExtract.isSResElig()) {
                storeAttributes.add(storeID + "_STORERESERVATIONELIGIBLE=1");
            }

            for (String fulfillmentChannel : new HashSet<String>(offerExtract.getChannels())) {
                storeAttributes.add(storeID + "_DEFAULT_FULFILLMENT=" + fulfillmentChannel);
            }

            if (offerExtract.isDoNotEmailMe()) {
                storeAttributes.add(storeID + "_DONT_EMAILME=1");
                if (Stores.SEARS.matches(storeName) && offerExtract.getSites().contains("insco")) {
                    storeAttributes.add(Stores.INSURANCE.getStoreId() + "_DONT_EMAILME=1");
                }
            }

            if (offerExtract.isSpuElig()) {
                storeAttributes.add(storeID + "_STOREPICKUPELIGIBLE=1");
            }
            if (offerExtract.isSTSElig()) {
                for (String stsChannel : new HashSet<String>(offerExtract.getSTSChannels())) {
                    storeAttributes.add(storeID + "_SHIP_TO_STORE=" + stsChannel);
                    if (Stores.MYGOFER.matches(storeName) || Stores.SEARSPR.matches(storeName)) {
                        storeAttributes.add(storeID + "_RES_TYPE=" + stsChannel);
                    }
                }
            }

            if (promoExtract.getStorePromoExtracts().containsKey(Stores.getStore(storeID))
                    && StringUtils.isNotEmpty(promoExtract.getStorePromoExtracts().get(Stores.getStore(storeID)).getDelayInMonths())) {
                storeAttributes.add(storeID + "_ZERO_FINANCE=" + promoExtract.getStorePromoExtracts().get(Stores.getStore(storeID)).getDelayInMonths());
            }

            // TODO: UNABLE TO GET ZERO_FINANCE information from offer
            // collection as it is unavailable in domain object and no valid
            // examples to see in the offer collection.
            Sites site = Stores.getSite(context.getStoreName());

            if (Stores.SEARS.matches(storeName) || Stores.KMART.matches(storeName) || Stores.SEARSPR.matches(storeName)) {
                if (offerExtract.getNewestOnlineDateBySite().get(site) != null) {
                    storeAttributes.add(storeID + "_What's New=Yes");
                } else {
                    storeAttributes.add(storeID + "_What's New=No");
                }
            }

            if (Stores.MYGOFER.matches(storeName) && offerExtract.isSpuElig()) {
                storeAttributes.add("10175_PU_NOW=1");
                storeAttributes.add("10175_PU_TODAY=1");
            }

        }
        return storeAttributes;
    }


    /**
     * Online and store prices are added to one field with store unit context prefixing the price.
     * <p/>
     * <pre>storeunit_price</pre>
     *
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getPrice(WorkingDocument wd, ContextMessage context) {
        List<String> allStoresPrices = new ArrayList<>();
        if(isCommercialStore(context)){
            return allStoresPrices;
        }
        final PriceExtract priceExtract = wd.getExtracts().getPriceExtract();

        List<Integer> onlineStoreIds = Stores.getStoreId(wd, context);
        final double ZEROPRICE = 0.0;

        for (Integer onlineStoreId : onlineStoreIds) {
            Price onlineStorePrice = priceExtract.getOnlinePrice().get(onlineStoreId);
            double nonZeroPrice = priceAlgorithm.nonZeroPrice(onlineStorePrice);
            if (nonZeroPrice != ZEROPRICE) {
                allStoresPrices.add(onlineStoreId + SEPARATOR + nonZeroPrice);
            }
        }

        if (Stores.KMART.matches(context.getStoreName()) && wd.getExtracts().getOfferExtract().isKmartPrElig()) {
            int onlineStoreId = Stores.KMARTPR.getStoreId();
            Price onlineStorePrice = priceExtract.getOnlinePrice().get(onlineStoreId);

            double nonZeroPrice = onlineStorePrice.getRegPrice();
            if (nonZeroPrice != ZEROPRICE) {
                allStoresPrices.add("kmartpr" + SEPARATOR + nonZeroPrice);
            }
        }

        if (Stores.SEARS.matches(context.getStoreName())) {
            for (String inStoreId : priceExtract.getStorePrice().keySet()) {
                Price inStorePrice = priceExtract.getStorePrice().get(inStoreId);

                if (priceAlgorithm.nonZeroPrice(inStorePrice) != ZEROPRICE) {
                    allStoresPrices.add(inStoreId + SEPARATOR + priceAlgorithm.nonZeroPrice(inStorePrice));
                }
            }
        }
        return allStoresPrices;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getSale(WorkingDocument wd, ContextMessage context) {
        List<String> allStoresSaleIndicators = new ArrayList<>();
        if(isCommercialStore(context)){
            return allStoresSaleIndicators;
        }
            final PriceExtract priceExtract = wd.getExtracts().getPriceExtract();

            List<Integer> onlineStoreIds = Stores.getStoreId(wd, context);

            for (Integer onlineStoreId : onlineStoreIds) {
                Price onlineStorePrice = priceExtract.getOnlinePrice().get(onlineStoreId);
                int indicator = onlineStorePrice.isSale() ? 1 : 0;

                if (indicator != 0) {
                    allStoresSaleIndicators.add(onlineStoreId + SEPARATOR + indicator);
                    if (Stores.KMART.matches(context.getStoreName()) && wd.getExtracts().getOfferExtract().isKmartPrElig()) {
                        onlineStoreId = Stores.KMARTPR.getStoreId();
                        onlineStorePrice = priceExtract.getOnlinePrice().get(onlineStoreId);
                        indicator = onlineStorePrice.isSale() ? 1 : 0;
                        if (onlineStorePrice.isSale()) {
                            allStoresSaleIndicators.add(onlineStoreId + SEPARATOR + indicator);
                        }
                    }
                }
            }

            //Do not include 0_0 for SEARSPR
            if (CollectionUtils.isNotEmpty(allStoresSaleIndicators) && !Stores.SEARSPR.matches(context.getStoreName())) {
                allStoresSaleIndicators.add("0_0");
                for(Integer onlineStoreId: onlineStoreIds){
                    allStoresSaleIndicators.add(onlineStoreId + SEPARATOR + ZERO);
                }
            }

        // All the instore sale information is not added in current business case.
        return allStoresSaleIndicators;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getClearance(WorkingDocument wd, ContextMessage context) {
        List<String> allStoresClearanceIndicators = new ArrayList<>();
        final PriceExtract priceExtract = wd.getExtracts().getPriceExtract();

        List<Integer> onlineStoreIds = Stores.getStoreId(wd, context);

        for (Integer onlineStoreId : onlineStoreIds) {
            Price onlineStorePrice = priceExtract.getOnlinePrice().get(onlineStoreId);
            int indicator = onlineStorePrice.isClearance() ? 1 : 0;
            if (indicator != 0) {
                allStoresClearanceIndicators.add(onlineStoreId + SEPARATOR + indicator);
                if (Stores.KMART.matches(context.getStoreName()) && wd.getExtracts().getOfferExtract().isKmartPrElig()) {
                    onlineStoreId = Stores.KMARTPR.getStoreId();
                    onlineStorePrice = priceExtract.getOnlinePrice().get(onlineStoreId);

                    indicator = onlineStorePrice.isClearance() ? 1 : 0;
                    if (onlineStorePrice.isClearance()) {
                        allStoresClearanceIndicators.add(onlineStoreId + SEPARATOR + indicator);
                    }
                }
            }
        }

        if (allStoresClearanceIndicators.isEmpty()) {
            allStoresClearanceIndicators.add("0_0");
            for(Integer onlineStoreId: onlineStoreIds){
                allStoresClearanceIndicators.add(onlineStoreId + SEPARATOR + ZERO);
            }
        }
        // All the instore clearance information is not added in current
        // business case.
        return allStoresClearanceIndicators;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getInstock(WorkingDocument wd, ContextMessage context) {
        final UasExtract uasExtract = wd.getExtracts().getUasExtract();
        final PASExtract pasExtract = wd.getExtracts().getPasExtract();
        List<Integer> storeIds = Stores.getStoreId(wd, context);

        List<String> instock = new ArrayList<>();
        boolean instockAvailable = false;

        @SuppressWarnings("unused") // Due to removal of PAS zone availability constraint on instock field.
                boolean zoneAvailable = false;

        final Map<String, Map<String, Boolean>> itemAvailabilityMap = uasExtract.getItemAvailabilityMap();

        //First loop through the item map
        for (Iterator<Entry<String, Map<String, Boolean>>> itemAvailIterator = itemAvailabilityMap.entrySet().iterator(); itemAvailIterator.hasNext(); ) {
            Map.Entry<String, Map<String, Boolean>> itemAvailabilityEntry = itemAvailIterator.next();
            //For each item, loop through all the offers to see if any is available
            for (Iterator<Entry<String, Boolean>> offerAvailabilityIterator = itemAvailabilityEntry.getValue().entrySet().iterator(); offerAvailabilityIterator.hasNext(); ) {
                Map.Entry<String, Boolean> offerAvailabilityEntry = offerAvailabilityIterator.next();
                if (offerAvailabilityEntry.getValue()) {
                    //If any offer is available, set instockAvailable true and break
                    instockAvailable = true;
                    break;
                }
            }
        }

        if (CollectionUtils.isNotEmpty(pasExtract.getAvailableZones())) {
            zoneAvailable = true;
        }

        // If available in zone
        for (String zone : pasExtract.getAvailableZones()) {
            instock.add("S" + zone + SEPARATOR + ONE);
        }

		/* instock will have 0_1 only based on UAS service response; that is if instockAvailable=true
         *
		 * zoneAvailability from PAS cannot be depended since PAS doesn't check for eligibility criteria before checking availability criteria.
		 * */
       // int storeId = Stores.getStore(context.getStoreName()).getStoreId();
        if(instockAvailable)   instock.add(ZERO + SEPARATOR + ONE);
        for(Integer storeId: storeIds) {
            if (instockAvailable) {


                instock.add(storeId + SEPARATOR + ONE);
            } else {

                instock.add(storeId + SEPARATOR + ZERO);
            }
        }
        return instock;
    }

    /**
     * Also called "new" field in Solr. Renamed to "newArrivals" to match the filter-name.
     *
     * @param wd
     * @param context
     * @return
     */
    protected List<Integer> getNewArrivals(WorkingDocument wd, ContextMessage context) {
        if (isNotSearsSiteMktplaceProd(wd, context)) {
            return new ArrayList<>();
        }

        List<Integer> newArrivalBuckets = new ArrayList<>();
        Sites site = Stores.getSite(context.getStoreName());
        if (site.isCrossFormat(wd, context)) {
            site = site.getCrossFormatSite();
        }

        LocalDate onlineDate = wd.getExtracts().getOfferExtract().getNewestOnlineDateBySite().get(site);
        if (onlineDate == null) {
            return newArrivalBuckets;
        }
        LocalDate today = new LocalDate();
        int days = Days.daysBetween(onlineDate, today).getDays();
        if (days != 0) {
            if (days <= 30) {
                newArrivalBuckets.add(30);
            }
            if (days <= 60) {
                newArrivalBuckets.add(60);
            }
            if (days <= 90) {
                newArrivalBuckets.add(90);
            }
        }
        return newArrivalBuckets;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<Integer> getNewKmartMKP(WorkingDocument wd, ContextMessage context) {
        List<Integer> newArrivalBucketsKM = new ArrayList<>();
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            Sites site = Sites.KMART;
            if (site.isCrossFormat(wd, context)) {
                site = site.getCrossFormatSite();
            }

            LocalDate onlineDate = wd.getExtracts().getOfferExtract().getNewestOnlineDateBySite().get(site);
            if (onlineDate == null) {
                return newArrivalBucketsKM;
            }
            LocalDate today = new LocalDate();
            int days = Days.daysBetween(onlineDate, today).getDays();
            if (days != 0) {
                if (days <= 30) {
                    newArrivalBucketsKM.add(30);
                }
                if (days <= 60) {
                    newArrivalBucketsKM.add(60);
                }
                if (days <= 90) {
                    newArrivalBucketsKM.add(90);
                }
            }
        }
        return newArrivalBucketsKM;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getItemsSold(WorkingDocument wd, ContextMessage context) {
        List<String> itemsSoldList = new ArrayList<>();
        if(isCommercialStore(context)){
            return itemsSoldList;
        }
        Map<Sites, ProdStats> sitesProdStatsMap = wd.getExtracts().getProdstatsExtract().getSitesProdStatsMap();
        for (Map.Entry<Sites, ProdStats> entry : sitesProdStatsMap.entrySet()) {
            Sites site = entry.getKey();
            int storeId = Stores.getStoreId(site.getSiteName());

            ProdStats prodStats = entry.getValue();
            final int itemsSold = prodStats.getItemsSold();
            if (itemsSold != 0) {
                itemsSoldList.add(storeId + SEPARATOR + itemsSold);
            }
        }

        return itemsSoldList;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getRevenue(WorkingDocument wd, ContextMessage context) {
        List<String> revenueList = new ArrayList<>();
        if(isCommercialStore(context)){
            return revenueList;
        }
        Map<Sites, ProdStats> sitesProdStatsMap = wd.getExtracts().getProdstatsExtract().getSitesProdStatsMap();

        for (Map.Entry<Sites, ProdStats> entry : sitesProdStatsMap.entrySet()) {
            Sites site = entry.getKey();
            int storeId = Stores.getStoreId(site.getSiteName());

            ProdStats prodStats = entry.getValue();
            final double revenue = prodStats.getRevenue();
            if (revenue != 0) {
                revenueList.add(storeId + SEPARATOR + revenue);
            }
        }
        return revenueList;
    }

    /**
     * Store this field only for Kmart to support part number searchers using kmart sku or partnumber which is not same as SSIN for many sears-kmart grouped products (which acquires sears ssin).
     * <p/>
     * Today, applicable only for NV and V.
     *
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getParentIds(WorkingDocument wd, ContextMessage context) {
        Set<String> parentIds = wd.getExtracts().getOfferExtract().getParentIds();
        if (Stores.KMART.matches(context.getStoreName()) && CollectionUtils.isNotEmpty(parentIds)) {
            return parentIds;
        }
        return new HashSet<>();
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getConversion(WorkingDocument wd, ContextMessage context) {
        List<String> conversionList = new ArrayList<>();
        if(isCommercialStore(context)){
            return conversionList;
        }
        Map<Sites, ProdStats> sitesProdStatsMap = wd.getExtracts().getProdstatsExtract().getSitesProdStatsMap();

        for (Map.Entry<Sites, ProdStats> entry : sitesProdStatsMap.entrySet()) {
            Sites site = entry.getKey();
            int storeId = Stores.getStoreId(site.getSiteName());

            ProdStats prodStats = entry.getValue();
            final int itemsSold = prodStats.getItemsSold();
            final int productViews = prodStats.getProductViews();

            double conversion = 0;
            if (productViews != 0) {
                conversion = (double) itemsSold / productViews;
            }
            conversionList.add(storeId + SEPARATOR + conversion);
        }
        return conversionList;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getProductViews(WorkingDocument wd, ContextMessage context) {
        List<String> productViewsList = new ArrayList<>();
        if(isCommercialStore(context)){
            return productViewsList;
        }
        Map<Sites, ProdStats> sitesProdStatsMap = wd.getExtracts().getProdstatsExtract().getSitesProdStatsMap();

        for (Map.Entry<Sites, ProdStats> entry : sitesProdStatsMap.entrySet()) {
            Sites site = entry.getKey();
            int storeId = Stores.getStoreId(site.getSiteName());

            ProdStats prodStats = entry.getValue();
            final int productViews = prodStats.getProductViews();
            if (productViews != 0) {
                productViewsList.add(storeId + SEPARATOR + productViews);
            }
        }
        return productViewsList;
    }

    /**
     * @param wd
     * @return
     */
    protected long getImpressions(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)){
            return 0;
        }
        List<Sites> eligSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        long impressions = 0L;
        for (Sites site : eligSites) {
            impressions += browseBoost.getBaseImpressions(wd, site);
        }
        return impressions;
    }

    /**
     * @param wd
     * @return
     */
    protected List<String> getBehavioral(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return new ArrayList<>();
        }

        List<Sites> eligSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        List<String> behavioral = new ArrayList<>();

        for (Sites site : eligSites) {
            if (wd.getExtracts().getBsoExtract().getSitesBehavioralMap().containsKey(site)) {
                behavioral.addAll(wd.getExtracts().getBsoExtract().getSitesBehavioralMap().get(site));
            }
        }
        return behavioral;
    }

    /**
     * @param wd
     * @return
     */
    protected List<String> getBehavioral_km(WorkingDocument wd, ContextMessage context) {
        List<String> behavioral = new ArrayList<>();
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context) &&
                wd.getExtracts().getBsoExtract().getSitesBehavioralMap().containsKey(Sites.KMART)) {

            behavioral = wd.getExtracts().getBsoExtract().getSitesBehavioralMap().get(Sites.KMART);
        }

        return behavioral;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getSearsInternational(WorkingDocument wd, ContextMessage context) {
        List<String> searsInternational = new ArrayList<>();
        if (Stores.SEARS.matches(context.getStoreName()) || Stores.SEARSPR.matches(context.getStoreName())) {
            int storeId = Stores.getStoreId(context.getStoreName());
            if (CollectionUtils.isNotEmpty(wd.getExtracts().getOfferExtract().getIntlGroupIds())) {
                searsInternational.add(storeId + "_YES");
            }
        }
        return searsInternational;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected String getMobileReviews(WorkingDocument wd, ContextMessage context) {
        final int MOBILESTOREID = 9;
        String mobileReviews = null;

        String ssin = wd.getExtracts().getSsin();
        Map<String, Map<Integer, CustomerRatingDto>> customerRatingMap = customerRating.getCustomerRating();

        if (customerRatingMap.containsKey(ssin)) {
            Map<Integer, CustomerRatingDto> customerRatingDto = customerRatingMap.get(ssin);
            if (customerRatingDto.containsKey(MOBILESTOREID)) {
                int reviewCount = customerRatingDto.get(MOBILESTOREID).getReviewCount();
                if (reviewCount != 0) {
                    mobileReviews = String.valueOf(reviewCount);
                }
            }
        }
        return mobileReviews;
    }


    /**
     * @param wd
     * @return
     */
    protected String getBrand(WorkingDocument wd) {
        String brand = null;
        if (StringUtils.isNotEmpty(wd.getExtracts().getContentExtract().getBrandName())) {
            brand = wd.getExtracts().getContentExtract().getBrandName();
        }
        return brand;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getCatalogs(WorkingDocument wd, ContextMessage context) {
        List<String> catalogIds = new ArrayList<>();

        List<Sites> eligSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        for (Sites eligSite : eligSites) {
            catalogIds.add(eligSite.getCatalogId());
        }

        if (Stores.SEARS.matches(context.getStoreName())) {
            if (CollectionUtils.isNotEmpty(wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap().get(Sites.CRAFTSMAN))) {
                catalogIds.add(Sites.CRAFTSMAN.getCatalogId());
            }
            if (CollectionUtils.isNotEmpty(wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap().get(Sites.KENMORE))) {
                catalogIds.add(Sites.KENMORE.getCatalogId());
            }
        }
        return catalogIds;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getLevel1Cats(WorkingDocument wd, ContextMessage context) {
        Set<String> level1Cats = new HashSet<>();
        if (Stores.MYGOFER.matches(context.getStoreName()) ||
                Stores.C2C.matches(context.getStoreName()) ||
                Stores.SEARSPR.matches(context.getStoreName())) {
            level1Cats = getLevelNCategories(wd, context, 1);
        }
        return level1Cats;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getLevel2Cats(WorkingDocument wd, ContextMessage context) {
        Set<String> level2Cats = new HashSet<>();
        if (Stores.MYGOFER.matches(context.getStoreName()) ||
                Stores.C2C.matches(context.getStoreName()) ||
                Stores.SEARSPR.matches(context.getStoreName())) {
            level2Cats = getLevelNCategories(wd, context, 2);
        }
        return level2Cats;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getLevel3Cats(WorkingDocument wd, ContextMessage context) {
        Set<String> level3Cats = new HashSet<>();
        if (Stores.MYGOFER.matches(context.getStoreName()) ||
                Stores.C2C.matches(context.getStoreName()) ||
                Stores.SEARSPR.matches(context.getStoreName())) {
            level3Cats = getLevelNCategories(wd, context, 3);
        }
        return level3Cats;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getVNames(WorkingDocument wd, ContextMessage context) {

        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return new HashSet<>();
        }

        Set<String> vNames = new HashSet<>();
        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();

        for (Sites site : eligibleSites) {
            vNames.addAll(getLNamesSet(1, siteWebHierarchiesMap.get(site)));
        }
        if (Stores.SEARS.matches(context.getStoreName())) {
            vNames.addAll(getLNamesSet(1, siteWebHierarchiesMap.get(Sites.CRAFTSMAN)));
            vNames.addAll(getLNamesSet(1, siteWebHierarchiesMap.get(Sites.KENMORE)));
        }
        return vNames;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getVNames_km(WorkingDocument wd, ContextMessage context) {
        Set<String> vNames_km = new HashSet<>();
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            Sites site = Sites.KMART;
            Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();
            vNames_km.addAll(getLNamesSet(1, siteWebHierarchiesMap.get(site)));
        }
        return vNames_km;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getCNames(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return new HashSet<>();
        }

        Set<String> cNames = new HashSet<>();
        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();
        for (Sites site : eligibleSites) {
            cNames.addAll(getLNamesSet(2, siteWebHierarchiesMap.get(site)));
        }
        if (Stores.SEARS.matches(context.getStoreName())) {
            cNames.addAll(getLNamesSet(2, siteWebHierarchiesMap.get(Sites.CRAFTSMAN)));
            cNames.addAll(getLNamesSet(2, siteWebHierarchiesMap.get(Sites.KENMORE)));
        }
        return cNames;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getCNames_km(WorkingDocument wd, ContextMessage context) {
        Set<String> cNames_km = new HashSet<>();
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            Sites site = Sites.KMART;
            Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();
            cNames_km.addAll(getLNamesSet(2, siteWebHierarchiesMap.get(site)));
        }
        return cNames_km;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getSNames(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return new HashSet<>();
        }

        Set<String> sNames = new HashSet<>();
        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();

        for (Sites site : eligibleSites) {
            sNames.addAll(getLNamesSet(3, siteWebHierarchiesMap.get(site)));
        }
        if (Stores.SEARS.matches(context.getStoreName())) {
            sNames.addAll(getLNamesSet(3, siteWebHierarchiesMap.get(Sites.CRAFTSMAN)));
            sNames.addAll(getLNamesSet(3, siteWebHierarchiesMap.get(Sites.KENMORE)));
        }
        return sNames;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getSNames_km(WorkingDocument wd, ContextMessage context) {
        Set<String> sNames_km = new HashSet<>();
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            Sites site = Sites.KMART;
            Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();
            sNames_km.addAll(getLNamesSet(3, siteWebHierarchiesMap.get(site)));
        }
        return sNames_km;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected String getSin(WorkingDocument wd, ContextMessage context) {
        return wd.getExtracts().getSsin();
    }

    /**
     * @param wd
     * @return
     */
    protected String getSellerCount(WorkingDocument wd) {
        return String.valueOf(getStoreOrigin(wd).size());
    }


    /**
     * @param context
     * @return
     */
    protected String getCpcFlag(ContextMessage context) {
        return ZERO;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected String getFbmFlag(WorkingDocument wd, ContextMessage context) {
        return Stores.FBM.matches(context.getStoreName()) ? ONE : ZERO;
        // Remember to index only products that have atleast one sears offer
    }

    /**
     * @param wd
     * @return
     */
    protected List<String> getSellers(WorkingDocument wd) {
        if (CollectionUtils.isEmpty(wd.getExtracts().getOfferExtract().getSellerName())) {
            return new ArrayList<>();
        }
        return new ArrayList<>(wd.getExtracts().getOfferExtract().getSellerName());
    }



    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getSpuEligible(WorkingDocument wd, ContextMessage context) {
        List<String> spuEligList = new ArrayList<>();
        if (wd.getExtracts().getOfferExtract().isSpuElig()) {
            List<Integer> onlineStoreIds = Stores.getStoreId(wd, context);
            for (Integer onlineStoreId : onlineStoreIds) {
                spuEligList.add(onlineStoreId + SEPARATOR + ONE);
            }
        }
        return spuEligList;
    }

    /**
     * @param wd
     * @return
     */
    protected int getLayAway(WorkingDocument wd, ContextMessage context) {

        Stores store = Stores.getStore(context.getStoreName());
        if (Stores.SEARSPR.matches(store.getStoreName())) {
            return 0;
        }

        return wd.getExtracts().getOfferExtract().isLayawayElig() ? 1 : 0;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected String getMailInRebate(WorkingDocument wd, ContextMessage context) {
        if (Stores.MYGOFER.matches(context.getStoreName())) {
            return ZERO;
        }
        Price onlinePrice = wd.getExtracts().getPriceExtract().getOnlinePrice().get(Stores.getStoreId(context.getStoreName()));
        if (StringUtils.isEmpty(onlinePrice.getRebateIds())) {
            return ZERO;
        }
        return ONE;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    //Kept since other fields rely on this. TODO: Check how we can get rid of this
    protected List<String> getFreeShipping(WorkingDocument wd, ContextMessage context) {
        List<String> storeFreeShipping = new ArrayList<>();
        List<Integer> storeIds = Stores.getStoreId(wd, context);
        for (Integer storeId : storeIds) {
            boolean isFreeShipping = eligibleAndShipAvailable(wd, context) && isFreeShippingForStore(wd, storeId, context);
            String prefix = Integer.toString(storeId) + SEPARATOR;
            storeFreeShipping.add(prefix + (isFreeShipping ? ONE : ZERO));
        }
        return storeFreeShipping;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getShipVantage(WorkingDocument wd, ContextMessage context) {
        List<String> storeShipVantage = new ArrayList<>();
        List<Integer> storeIds = Stores.getStoreId(wd, context);

        for (Integer storeId : storeIds) {
            boolean isFreeShipping = eligibleAndShipAvailable(wd, context) && isFreeShippingForSYWM(wd, storeId, context);
            String prefix = Integer.toString(storeId) + SEPARATOR;
            if (isFreeShipping) {
                storeShipVantage.add(prefix + ONE);
            }
        }
        return storeShipVantage;
    }

    /**
     * Verify for the existence of the node _blob.promorel.promoList.sywMax
     * If sywMax node is present do the below check
     * a.	promoType =”Shipping”
     * b.	status=”a”
     * c.	startDt<= CurrentDateTime < endDt (Promo should be active)
     * d.	freePromoFlag = true
     * If any one of the promotion under sywMax node satisfies the mentioned condition, item is eligible for maxFreeShipping
     *
     * @param wd
     * @param storeId
     * @param context
     * @return
     */
    private boolean isFreeShippingForSYWM(WorkingDocument wd, Integer storeId, ContextMessage context) {
    	return checkPromorelForSYWMaxFreeShipping(wd, storeId, context);
    }

    /**
     * Verify for the existence of the node _blob.promorel.promoList.sywMax
     * If sywMax node is present do the below check
     * a.	promoType =”Shipping”
     * b.	status=”a”
     * c.	startDt<= CurrentDateTime < endDt (Promo should be active)
     * d.	freePromoFlag = true
     * If any one of the promotion under sywMax node satisfies the mentioned condition, item is eligible for maxFreeShipping
     *
     * @param wd
     * @param storeId
     * @param context
     * @return
     */
    private boolean checkPromorelForSYWMaxFreeShipping(WorkingDocument wd, Integer storeId, ContextMessage context) {
        if (wd.getExtracts().getPromoExtract().getStorePromoExtracts().containsKey(Stores.getStore(storeId))) {
            return wd.getExtracts().getPromoExtract().getStorePromoExtracts().get(Stores.getStore(storeId)).isFreeShipping();
        }
        return false;
    }

    /**
     * @param wd
     * @return
     */
    protected String getCatConfidence(WorkingDocument wd) {
        //Default Cat confidence is 1
        return ONE;
    }

    /**
     * @param wd
     * @return
     */
    protected String getHas991(WorkingDocument wd) {
        String has991 = null;
        if (wd.getExtracts().getOfferExtract().isOutletItem()) {
            has991 = "YES";
        }
        return has991;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected int getTrustedSeller(WorkingDocument wd, ContextMessage context) {
        int trustedSeller = 0;

        String storeName = context.getStoreName();
        if (Stores.FBM.matches(storeName)) {
            boolean trusted = wd.getExtracts().getSellerExtract().getTrustedSeller();
            trustedSeller = trusted ? 1 : 0;
        }
        return trustedSeller;
    }


    /**
     * Routine context: Classification: NV, V. Store: Sears (non-Auto), SearsPr. For Bundles, see in Bundle.
     *
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getInternationalShipping(WorkingDocument wd, ContextMessage context) {

        //SearsPR does not have international shipping
        if (Stores.SEARSPR.matches(context.getStoreName())) {
            return null;
        }

        if (Stores.SEARS.matches(context.getStoreName())) {
            Set<String> intlGroupIds = wd.getExtracts().getOfferExtract().getIntlGroupIds();
            if (CollectionUtils.isNotEmpty(intlGroupIds)) {
                return new ArrayList<String>(intlGroupIds);
            }
        }
        return new ArrayList<String>();
    }

    /**
     * Field int_ship_eligible
     *
     * @param wd
     * @param context
     * @return
     */
    protected Integer getInternationalShipEligible(WorkingDocument wd, ContextMessage context) {
        Integer intShipElig = null;
        if (Stores.SEARSPR.matches(context.getStoreName())) {
            return null;
        }

        if ((Stores.SEARS.matches(context.getStoreName()) ||
                Stores.SEARSPR.matches(context.getStoreName())) &&
                CollectionUtils.isNotEmpty(wd.getExtracts().getOfferExtract().getIntlGroupIds())) {
            intShipElig = 1;
        }

        return intShipElig;
    }


    /**
     * Browse Boost using default algorithm (based on revenue, productViews, itemsSold and their conversion).
     *
     * @param wd
     * @param context
     * @return
     */
    protected String getBrowseBoost(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return "0.0";
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.##################");
        double browseBoostPerSite = 0.0;
        String browseBoostScore;
        try {
            List<Sites> eligSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
            for (Sites site : eligSites) {
                browseBoostPerSite += browseBoost.computeBrowseBoost(wd, context, site);
            }
            browseBoostScore = decimalFormat.format(browseBoostPerSite);
        } catch (ArithmeticException e) {
            LOGGER.error("BrowseBoost error", e);
            browseBoostScore = "0.0";
        }
        return browseBoostScore;
    }

    /**
     * Browse Boost using default algorithm (based on revenue, productViews, itemsSold and their conversion).
     *
     * @param wd
     * @param context
     * @return
     */
    protected String getBrowseBoostKmartMKP(WorkingDocument wd, ContextMessage context) {
        String browseBoostScoreKmartMKP = "0.0";
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            DecimalFormat decimalFormat = new DecimalFormat("0.##################");
            try {
                browseBoostScoreKmartMKP = decimalFormat.format(browseBoost.computeBrowseBoost(wd, context, Sites.KMART));
            } catch (ArithmeticException e) {
                LOGGER.error("BrowseBoost_km error", e);
                browseBoostScoreKmartMKP = "0.0";
            }
        }
        return browseBoostScoreKmartMKP;
    }

    /**
     * Browse Boost using enhanced algorithm taking impressions into account.
     *
     * @param wd
     * @param context
     * @return
     */
    protected String getBrowseBoostImpr(WorkingDocument wd, ContextMessage context) {
        DecimalFormat decimalFormat = new DecimalFormat("0.##################");
        double browseBoostWithImprPerSite = 0.0;
        if(isCommercialStore(context)){
            return "0.0";
        }
        String browseBoostImprScore;
        try {
            List<Sites> eligSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
            for (Sites site : eligSites) {
                browseBoostWithImprPerSite = browseBoost.computeBrowseBoostWithImpressions(wd, context, site);
            }
            browseBoostImprScore = decimalFormat.format(browseBoostWithImprPerSite);
        } catch (ArithmeticException e) {
            LOGGER.error("BrowseBoostImprError", e);
            browseBoostImprScore = "0.0";
        }
        return browseBoostImprScore;
    }


    /**
     * As we deduct from old-indexing code, if "instock" is an empty set, or if the product is available in-stock (0_1) then instockShipping is true (1). Else, false (0). For Kmart, if default-fulfilment-channel is SPU, instockShipping is false (0).
     * <p/>
     * Generalizing across all stores here, instead of checking only for Sears, Kmart, MyGofer
     *
     * @param wd
     * @param context
     * @return
     */
    protected String getInstockShipping(WorkingDocument wd, ContextMessage context) {
        boolean instockShipping = false;
        final Map<String, Map<String, Boolean>> itemAvailabilityMap = wd.getExtracts().getUasExtract().getItemAvailabilityMap();

        for (Iterator<Entry<String, Map<String, Boolean>>> itemAvailIterator = itemAvailabilityMap.entrySet().iterator(); itemAvailIterator.hasNext(); ) {
            Map.Entry<String, Map<String, Boolean>> itemAvailabilityEntry = itemAvailIterator.next();
            //For each item, loop through all the offers to see if any of them has shipAvail as true
            Map<String, Boolean> offerAvailabilityMap = itemAvailabilityEntry.getValue();
            if (offerAvailabilityMap.get("shipAvail")) {
                instockShipping = true;
                break;
            }
        }

        return instockShipping ? ONE : ZERO;
    }
    
    
    /**
     * Utility for checking if a particular offer is ship available
     * @param offerId
     * @param wd
     * @param context
     * @return
     */
    private boolean eligibleAndShipAvailable(String offerId, WorkingDocument wd, ContextMessage context) {
        final Map<String, Map<String, Boolean>> itemAvailabilityMap = wd.getExtracts().getUasExtract().getItemAvailabilityMap();

        for (Iterator<Entry<String, Map<String, Boolean>>> itemAvailIterator = itemAvailabilityMap.entrySet().iterator(); itemAvailIterator.hasNext(); ) {
            Map.Entry<String, Map<String, Boolean>> itemAvailabilityEntry = itemAvailIterator.next();
            //For each item, loop through all the offers to see if any of them has shipAvail as true
            String availableOfferId = itemAvailabilityEntry.getKey();
            
          //SEARCH-2953 Ignore non-display-eligible offers that can be indexed for tablets
            if(Stores.SEARS.matches(context.getStoreName())) {    		
         		Map<String, Boolean> searsOfferDispEligStatus = wd.getExtracts().getOfferExtract().getSearsDispEligibilityStatus();
         		if(searsOfferDispEligStatus.containsKey(availableOfferId) && BooleanUtils.isFalse(searsOfferDispEligStatus.get(availableOfferId))) {
         			continue;
         		}
         	}
            Map<String, Boolean> offerAvailabilityMap = itemAvailabilityEntry.getValue();
            if (StringUtils.equalsIgnoreCase(offerId, availableOfferId) && offerAvailabilityMap.get("shipAvail")) {
                return true;
            }
        }

        return false;
    }
    
    
    /**
     * Utility for checking if all offers are display eligible and available for shipping.
     * @param wd
     * @param context
     * @return
     */
    private boolean eligibleAndShipAvailable(WorkingDocument wd, ContextMessage context) {
        final Map<String, Map<String, Boolean>> itemAvailabilityMap = wd.getExtracts().getUasExtract().getItemAvailabilityMap();
        for (Iterator<Entry<String, Map<String, Boolean>>> itemAvailIterator = itemAvailabilityMap.entrySet().iterator(); itemAvailIterator.hasNext(); ) {
            Map.Entry<String, Map<String, Boolean>> itemAvailabilityEntry = itemAvailIterator.next();
            //For each item, loop through all the offers to see if any of them has shipAvail as true
            String availableOfferId = itemAvailabilityEntry.getKey();
            
          //SEARCH-2953 Ignore non-display-eligible offers that can be indexed for tablets
            if(Stores.SEARS.matches(context.getStoreName())) {    		
         		Map<String, Boolean> searsOfferDispEligStatus = wd.getExtracts().getOfferExtract().getSearsDispEligibilityStatus();
         		if(searsOfferDispEligStatus.containsKey(availableOfferId) && BooleanUtils.isFalse(searsOfferDispEligStatus.get(availableOfferId))) {
         			continue;
         		}
         	}
            Map<String, Boolean> offerAvailabilityMap = itemAvailabilityEntry.getValue();
            if (offerAvailabilityMap.get("shipAvail")) {
                return true;
            }
        }

        return false;
    }


    /**
     * @param wd
     * @return
     */
    protected int getMatureContentFlag(WorkingDocument wd) {
        if (wd.getExtracts().getContentExtract().isMatureContent()) {
            return 1;
        }
        return 0;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected int getAccessories(WorkingDocument wd, ContextMessage context) {
        boolean accessory = false;
        List<String> primaryHierarchies = getPrimaryHierarchy(wd, context);

        for (String primaryHierarchy : primaryHierarchies) {
            String leafNode = primaryHierarchy.substring(primaryHierarchy.lastIndexOf("_") + 1);
            for (String accessoryIdentifier : accessoryIdentifiers) {
                if (leafNode.contains(accessoryIdentifier)) {
                    accessory = true;
                    break;
                }
            }
            if (accessory) {
                break;
            }
        }

        return accessory ? 1 : 0;
    }


    /**
     * Abstract Build method that is overriden by transformation classes by setting the required field for that classification.
     * Common fields go into "Classification" class, rest go to their types (Nonvariation, Variation and Bundle).
     * Product Type (prdType) is <b>online</b> for all stores but Tablet.
     *
     * @param context
     * @return
     */
    protected String getPrdType(WorkingDocument wd, ContextMessage context) {
        if (Stores.KMART.matches(context.getStoreName()) || Stores.FBM.matches(context.getStoreName())) {
            return "online";
        }
        if (Stores.TABLET.matches(context.getStoreName())) {
            return "offline";
        }
        if (wd.getExtracts().getContentExtract().isBundleDisplayElligible()) {
            return "online";
        }
        if (Stores.AUTO.matches(context.getStoreName())) {
            List<Sites> eligSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);

            if (!eligSites.isEmpty()) {
                return "online";
            } else {
                return "offline";
            }
        }
        boolean dispEligAtContent = BooleanUtils.toBoolean(wd.getExtracts().getContentExtract().getSitesDispEligibility().get(Stores.getSite(context.getStoreName())));
        boolean dispEligAtOffer = BooleanUtils.toBoolean(wd.getExtracts().getOfferExtract().getSitesDispEligibility().get(Stores.getSite(context.getStoreName())));
        if (!dispEligAtContent || !dispEligAtOffer) {
            return "offline";
        }

        return "online";
    }


    /**
     * @param wd
     * @return
     */
    protected String getSearchPhrase(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)){
            return "";
        }
        final String spaceConstant = " ";
        final String saleConstant = "sale";
        final String clearanceConstant = "clearance";
        final String layawayConstant = "layaway";
        boolean saleIndicator = false;
        boolean clearanceIndicator = false;
        boolean isLayawayElig = false;

        StringBuilder searchPhrase = new StringBuilder();
        List<Sites> eligSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        for (Sites site : eligSites) {
            Price price = wd.getExtracts().getPriceExtract().getOnlinePrice().get(Stores.getStoreId(site.getSiteName()));
            saleIndicator |= price.isSale();
            clearanceIndicator |= price.isClearance();
            isLayawayElig |= wd.getExtracts().getOfferExtract().isLayawayElig();
        }

        if (saleIndicator) {
            searchPhrase.append(saleConstant);
        }

        if (clearanceIndicator) {
            searchPhrase.append(spaceConstant + clearanceConstant);
        }

        if (isLayawayElig) {
            searchPhrase.append(spaceConstant + layawayConstant);
        }

        if (StringUtils.isEmpty(searchPhrase)) {
            return null;
        }

        return searchPhrase.toString().trim();
    }


    /**
     * Needs revisit on how multiple seller ids to be stored in index, since seller id is a single valued field
     *
     * @param wd
     * @return
     */
    protected String getSellerId(WorkingDocument wd, ContextMessage context) {
        String sellerIdString = null;

        String storeName = context.getStoreName();
        if (Stores.FBM.matches(storeName)) {
            Set<Integer> sellerIds = wd.getExtracts().getOfferExtract().getSellerId();
            if (CollectionUtils.isEmpty(sellerIds)) {
                return StringUtils.EMPTY;
            }
            StringBuilder sellerIdSB = new StringBuilder();
            for (Integer sellerId : sellerIds) {
                sellerIdSB.append(String.valueOf(sellerId));
                sellerIdSB.append(",");
            }
            sellerIdSB.deleteCharAt(sellerIdSB.lastIndexOf(","));
            sellerIdString = sellerIdSB.toString().trim();
        }
        return sellerIdString;
    }

    /**
     * Seller Store Front Id is a list of seller store front name/ids from seller collection.
     *
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getSellerSFId(WorkingDocument wd, ContextMessage context) {
    	Set<String> sellerSFIds = new HashSet<>();
    	if (CollectionUtils.isEmpty(wd.getExtracts().getOfferIds())) {
    		return new ArrayList<>(sellerSFIds);
    	}
    	Map<String, String> offerToStoreFrontNames = wd.getExtracts().getSellerExtract().getOfferToStoreFrontNames();
    	Set<Map.Entry<String, String>> entries = offerToStoreFrontNames.entrySet();
    	for(Map.Entry<String, String> entry: entries){
    		sellerSFIds.add(entry.getValue());
    	}
    	return new ArrayList<>(sellerSFIds);
    }


    /**
     * These seller store front "Feature Product" (FP).  Basically a list of highlighted offers in StoreFront.
     * This means the seller collection will provide a list of offers that are featured for this seller along with rank.
     * Our evaluation is, if the product has one of those offers, and we set for this field a value that is sellerSFId_1.
     *
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getSellerFP(WorkingDocument wd, ContextMessage context) {
        Set<String> sellerFP = new HashSet<>();
        if (CollectionUtils.isEmpty(wd.getExtracts().getOfferIds())) {
            return new ArrayList<>(sellerFP);
        }
        List<String> sellerSFNames = getSellerSFId(wd, context);
        for (String sellerSFName : sellerSFNames) {
            List<String> featuredOffers = wd.getExtracts().getSellerExtract().getSfNameToFeaturedOffers().get(sellerSFName);
            if (CollectionUtils.isEmpty(featuredOffers)) {
                continue;
            }
            if (!Collections.disjoint(featuredOffers, wd.getExtracts().getOfferIds())) {
                sellerFP.add(sellerSFName + "_1");
            }
        }
        return new ArrayList<>(sellerFP);
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getFreeDelivery(WorkingDocument wd, ContextMessage context) {
        List<String> storeFreeDelivery = new ArrayList<>();
        List<Integer> storeIds = Stores.getStoreId(wd, context);

        for (Integer storeId : storeIds) {
            boolean isFreeDelivery = false;
            if ((Stores.SEARS.matches(context.getStoreName()) || Stores.KMART.matches(context.getStoreName())
                    || Stores.FBM.matches(context.getStoreName()))
                    && wd.getExtracts().getPromoExtract().getStorePromoExtracts().containsKey(Stores.getStore(storeId))) {
                isFreeDelivery = !wd.getExtracts().getContentExtract().isAutomotive() && wd.getExtracts().getPromoExtract().getStorePromoExtracts().get(Stores.getStore(storeId)).isFreeDelivery();
                String prefix = Integer.toString(storeId) + SEPARATOR;
                if (isFreeDelivery) {
                    storeFreeDelivery.add(prefix + ONE);
                }
            }
        }
        return storeFreeDelivery;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getPromotionTxt(WorkingDocument wd, ContextMessage context) {

        List<String> storePromotionTxt = new ArrayList<>();
        List<Integer> storeIds = Stores.getStoreId(wd, context);

        for (Integer storeId : storeIds) {
            Promotions promotions = new Promotions();
            String offerMinPurchase = wd.getExtracts().getOfferExtract().getFreeShipThreshold();

            StringBuilder promotionTxt = new StringBuilder();
            String prmTxtShipping = null;
            String prmTxtDelivery = null;
            String prmTxtFreeDelivery = null;
            List<PromoRegular> promoRegularList = new ArrayList<>();

            if (wd.getExtracts().getPromoExtract().getStorePromoExtracts().containsKey(Stores.getStore(storeId))) {
                promoRegularList = wd.getExtracts().getPromoExtract().getStorePromoExtracts().get(Stores.getStore(storeId)).getPromoRegulars();
            }
            for (PromoRegular promo : promoRegularList) {

                String minPurchase = (promo.getMinPurchaseVal() == null) ? offerMinPurchase : promo.getMinPurchaseVal();
                boolean dateFlag = promotions.isValidPromotion(promo.getStartDt(), promo.getEndDt());

                if (minPurchase == null || !dateFlag) {
                    continue;
                }

                prmTxtShipping = promotions.getShippingPromoTxt(promo, minPurchase, prmTxtShipping);
                prmTxtDelivery = promotions.getDeliveryPromoTxt(promo, minPurchase, prmTxtDelivery);
                prmTxtFreeDelivery = promotions.getFreeDeliveryPromoTxt(promo, minPurchase, prmTxtFreeDelivery);
            }

            String prmTxtSyw = getStoreValueFromList(storeId, promotions.getSywPromotionTxt(wd, context));

            promotions.appendPromotion(prmTxtShipping, promotionTxt);
            promotions.appendPromotion(prmTxtDelivery, promotionTxt);
            promotions.appendPromotion(prmTxtFreeDelivery, promotionTxt);
            promotions.appendPromotion(prmTxtSyw, promotionTxt);

            if (StringUtils.isNotEmpty(promotionTxt.toString())) {
                storePromotionTxt.add(Integer.toString(storeId) + SEPARATOR + promotionTxt.toString());
            }
        }
        return storePromotionTxt;
    }

    /**
     * @param wd
     * @return
     */
    protected String getUrl(WorkingDocument wd) {
        String url = null;
        if (StringUtils.isNotEmpty(wd.getExtracts().getContentExtract().getUrl())) {
            url = wd.getExtracts().getContentExtract().getUrl() + "/p-" + wd.getExtracts().getSsin();
        }
        return url;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getDiscount(WorkingDocument wd, ContextMessage context) {
        Set<String> discount = new HashSet<>();
        String storeName = context.getStoreName();

        if (Stores.C2C.matches(storeName) || Stores.COMMERCIAL.matches(storeName)) {
            return new ArrayList<>(discount);
        }

        final PriceExtract priceExtract = wd.getExtracts().getPriceExtract();

        List<Sites> eligSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        for (Sites site : eligSites) {
            Price onlineStorePrice = priceExtract.getOnlinePrice().get(Stores.getStoreId(site.getSiteName()));

            double regPrice = onlineStorePrice.getRegPrice();
            double sellPrice = onlineStorePrice.getSellPrice();
            double netDownPrice = onlineStorePrice.getNetDownPrice();
            double discountPercentage = 0.0;

            if (regPrice > 0) {
                if (netDownPrice > 0) {
                    discountPercentage = ((regPrice - netDownPrice) / regPrice) * 100;
                } else {
                    discountPercentage = ((regPrice - sellPrice) / regPrice) * 100;
                }
            }

            if (discountPercentage > 0) {
                discount = getDiscountRange(discountPercentage);
            }
        }
        return new ArrayList<>(discount);
    }

    protected Set<String> getDiscountRange(double discountPercentage){
            Set<String> discountRange = new HashSet<String>();
            discountRange = RANGE_CUT_OFF.stream()
                    .filter(e -> e<= discountPercentage)
                    .map(String::valueOf)
                    .collect(Collectors.toSet());
            discountRange.add(ONE);

        return discountRange;
    }

    /**
     * @param wd
     * @return
     */
    protected List<String> getOffer(WorkingDocument wd, ContextMessage context) {
        final String offerClearanceConstant = "Clearance";
        final String offerSaleConstant = "All Items On Sale";
        final String offerFreeShippingConstant = "Free Shipping";
        final String offerFreeDeliveryConstant = "Free Delivery";
        final String offerCompanionConstant = "Companion";
        final String offerBuyOneGetOneConstant = "Buy One Get One";
        final String offerEverydayGreatPriceConstant = "Everyday Great Price";

        List<String> offers = new ArrayList<>();
        List<Integer> storeIds = Stores.getStoreId(wd, context);

        List<String> freeShipping = getFreeShipping(wd, context);
        List<String> freeDelivery = getFreeDelivery(wd, context);
        boolean isClearance = false;
        boolean isSale = false;
        List<Sites> eligSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);

        for (Sites site : eligSites) {
            Price price = wd.getExtracts().getPriceExtract().getOnlinePrice().get(Stores.getStoreId(site.getSiteName()));
            if(price!=null) {
                isClearance |= price.isClearance();
                isSale |= price.isSale();
            }
        }


        for (Integer storeId : storeIds) {
            String prefix = Integer.toString(storeId) + SEPARATOR;
            boolean isFreeShipping = freeShipping.contains(prefix + ONE);
            boolean isFreeDelivery = freeDelivery.contains(prefix + ONE);
            boolean isCompanion = false;
            boolean isBuyOneGetOne = false;
            boolean isEverydayGreatPrice = false;
            
            if (wd.getExtracts().getPromoExtract().getStorePromoExtracts().containsKey(Stores.getStore(storeId))) {
                isCompanion = wd.getExtracts().getPromoExtract().getStorePromoExtracts().get(Stores.getStore(storeId)).isCompanion();
                isBuyOneGetOne = wd.getExtracts().getPromoExtract().getStorePromoExtracts().get(Stores.getStore(storeId)).isBuyOneGetOne();
            }
            
            if(wd.getExtracts().getPriceExtract().getOnlinePrice().get(storeId)!=null) {
            	isEverydayGreatPrice = wd.getExtracts().getPriceExtract().getOnlinePrice().get(storeId).isEverydayGreatPrice();
            }

            if (isClearance) {
                offers.add(prefix + offerClearanceConstant);
            }

            if (isSale) {
                offers.add(prefix + offerSaleConstant);
            }

            if (isFreeShipping) {
                offers.add(prefix + offerFreeShippingConstant);
            }

            if (isFreeDelivery) {
                offers.add(prefix + offerFreeDeliveryConstant);
            }

            if (isCompanion) {
                offers.add(prefix + offerCompanionConstant);
            }

            if (isBuyOneGetOne) {
                offers.add(prefix + offerBuyOneGetOneConstant);
            }
            
            if(isEverydayGreatPrice) {
            	offers.add(prefix + offerEverydayGreatPriceConstant);
            }
            
        }
        return offers;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getBuymore(WorkingDocument wd, ContextMessage context) {
        return new ArrayList<String>();

        /**
         * Deleted the code because it was causing violations. It was removed on 1/6/2016 by Huzefa.
         */
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getAvgRatingContextual(WorkingDocument wd, ContextMessage context) {
        List<String> avgRatingContextual = new ArrayList<>();
        String ssin = wd.getExtracts().getSsin();
        Map<String, Map<Integer, CustomerRatingDto>> customerRatingMap = customerRating.getCustomerRating();

        List<Integer> onlineStoreIds = Stores.getStoreId(wd, context);

        if (customerRatingMap.containsKey(ssin)) {
            Map<Integer, CustomerRatingDto> customerRatingDtoMap = customerRatingMap.get(ssin);

            for (Integer onlineStoreId : onlineStoreIds) {

                //Special case for SEARSPR, get the data for sears and add
                if (Stores.SEARSPR.getStoreId() == onlineStoreId && customerRatingDtoMap.containsKey(Stores.SEARS.getStoreId())) {
                    avgRatingContextual.add(onlineStoreId + SEPARATOR + customerRatingDtoMap.get(Stores.SEARS.getStoreId()).getRating());
                    continue;
                }
                if (customerRatingDtoMap.containsKey(onlineStoreId)) {
                    avgRatingContextual.add(onlineStoreId + SEPARATOR + customerRatingDtoMap.get(onlineStoreId).getRating());
                }
            }

            if (Stores.KMART.matches(context.getStoreName()) && customerRatingDtoMap.containsKey(Stores.KMARTPR.getStoreId())) {
                avgRatingContextual.add(Stores.KMARTPR.getStoreId() + SEPARATOR + customerRatingDtoMap.get(Stores.KMARTPR.getStoreId()).getRating());
            }
        }
        return avgRatingContextual;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getNumReviewsContextual(WorkingDocument wd, ContextMessage context) {
        List<String> numReviewsContextual = new ArrayList<>();

        String ssin = wd.getExtracts().getSsin();
        Map<String, Map<Integer, CustomerRatingDto>> customerRatingMap = customerRating.getCustomerRating();

        List<Integer> onlineStoreIds = Stores.getStoreId(wd, context);
        if (customerRatingMap.containsKey(ssin)) {
            Map<Integer, CustomerRatingDto> customerRatingDtoMap = customerRatingMap.get(ssin);

            for (Integer onlineStoreId : onlineStoreIds) {

                //Special case for SEARSPR, get the data for sears and add
                if (Stores.SEARSPR.getStoreId() == onlineStoreId && customerRatingDtoMap.containsKey(Stores.SEARS.getStoreId())) {
                    numReviewsContextual.add(onlineStoreId + SEPARATOR + customerRatingDtoMap.get(Stores.SEARS.getStoreId()).getReviewCount());
                    continue;
                }

                if (customerRatingDtoMap.containsKey(onlineStoreId)) {
                    numReviewsContextual.add(onlineStoreId + SEPARATOR + customerRatingDtoMap.get(onlineStoreId).getReviewCount());
                }
            }

            if (Stores.KMART.matches(context.getStoreName()) && customerRatingDtoMap.containsKey(Stores.KMARTPR.getStoreId())) {
                numReviewsContextual.add(Stores.KMARTPR.getStoreId() + SEPARATOR + customerRatingDtoMap.get(Stores.KMARTPR.getStoreId()).getReviewCount());
            }
        }
        return numReviewsContextual;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected String getConsumerReportsRated(WorkingDocument wd, ContextMessage context) {
        return null;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getConsumerReportRatingContextual(WorkingDocument wd, ContextMessage context) {
        return null;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getRebate(WorkingDocument wd, ContextMessage context) {
        List<String> rebateList = new ArrayList<>();
        boolean mailInRebateExists = false;
        boolean instantUtilityRebateExists = false;
        if (Stores.SEARS.matches(context.getStoreName())) {
            List<String> offerIds = wd.getExtracts().getOfferIds();
            List<String> availableRebates = new ArrayList<>();
            offerIds.stream()
                    .forEach( offer -> availableRebates.addAll(rebates.getRebates(offer)));
            for (String rebate : availableRebates) {
                rebateList.add(rebate);
                mailInRebateExists |= rebate.endsWith("_M");
                instantUtilityRebateExists |= rebate.endsWith("_I");
            }
        }
        if (mailInRebateExists) {
            rebateList.add(GlobalConstants.MAIL_IN_UTILITY_REBATES);
        }
        if (instantUtilityRebateExists) {
            rebateList.add(GlobalConstants.INSTANT_UTILITY_REBATES);
        }
        return rebateList;
    }

    /**
     * Rebate Status. Returning null instead of "0" to avoid rebate status coming up on facets.
     *
     * @param wd
     * @param context
     * @return
     */
    protected String getRebateStatus(WorkingDocument wd, ContextMessage context) {
        return CollectionUtils.isNotEmpty(getRebate(wd, context)) ? ONE : null;
    }

    /**
     * @param wd
     * @return
     */
    protected String getUpc(WorkingDocument wd) {
        return null;
    }


    /**
     * The logic is modified slightly w.r.t to setting "shipping" and "spuAvailable".
     * While in the old indexer these were set based on instock field (which could be true
     * due to any type of availability), the new indexer changes the logic.
     * The "shipping" value is set on instockShipping which in turn is set only if shipAvail is true from UAS.
     * The "spuAvailable" value is set based on pickupAvail from UAS.
     *
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getStorePickUp(WorkingDocument wd, ContextMessage context) {
        List<String> storePickList = new ArrayList<>();

        String storeName = context.getStoreName();

        List<String> channels = wd.getExtracts().getOfferExtract().getChannels();
        List<String> stsChannels = wd.getExtracts().getOfferExtract().getSTSChannels();

        if (stsChannels.contains("CRES") || stsChannels.contains("VRES") || stsChannels.contains("CVDS") || getSpuEligible(wd, context).contains(Stores.getStoreId(storeName) + SEPARATOR + ONE)) {
            /**
             * For Sears product, it should be spuEligble
             * but not be sold by Kmart for it to be classified as store pick up
             * eligible. For others' it should only be spuEligible.
             */
            if (wd.getExtracts().getOfferExtract().isSpuElig()) {
                if (Stores.SEARS.matches(storeName) && !getStoreOrigin(wd).contains("Kmart")) {
                    storePickList.add("spuEligible");
                } else if (!Stores.SEARS.matches(storeName)) {
                    storePickList.add("spuEligible");
                }
            }
        }

        if (channels.contains("DDC") || channels.contains("KHD")) {
            storePickList.add("delivery");
        }
        if ((channels.contains("TW") || channels.contains("VD")) && eligibleAndShipAvailable(wd, context)) {
            storePickList.add("shipping");
        }

        if (storePickList.contains("spuEligible") && isPickUpAvailable(wd, context)) {
            storePickList.add("spuAvailable");
        }

        for (String zone : wd.getExtracts().getPasExtract().getAvailableZones()) {
            storePickList.add("spuAvail_S" + zone);
        }

        return storePickList;
    }

    /**
     * @param wd
     * @return
     */
    protected double getNumericRevenue(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return 0.0;
        }

        Map<Sites, ProdStats> sitesProdStatsMap = wd.getExtracts().getProdstatsExtract().getSitesProdStatsMap();
        double revenue = 0.0;
        Sites site = Stores.getSite(context.getStoreName());
        if (sitesProdStatsMap.containsKey(site)) {
            revenue = sitesProdStatsMap.get(site).getRevenue();
        }

        return revenue;
    }

    /**
     * @param wd
     * @return
     */
    protected double getNumericRevenue_km(WorkingDocument wd, ContextMessage context) {
        double revenue = 0.0;
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            Map<Sites, ProdStats> sitesProdStatsMap = wd.getExtracts().getProdstatsExtract().getSitesProdStatsMap();
            if (sitesProdStatsMap.containsKey(Sites.KMART)) {
                revenue = sitesProdStatsMap.get(Sites.KMART).getRevenue();
            }
        }
        return revenue;
    }


    /**
     * Field flashDeal
     * Overridden by: NV only
     *
     * @param wd
     * @return
     */
    protected String isDealFlash(WorkingDocument wd) {
        return null;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected String getGiftCardSequence(WorkingDocument wd, ContextMessage context) {
        String storeName = context.getStoreName();
        String giftCardRank = null;

        if (!Stores.MYGOFER.matches(storeName)) {
            giftCardRank = wd.getExtracts().getOfferExtract().getGiftCardRank();
        }
        return giftCardRank;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected String getGiftCardType(WorkingDocument wd, ContextMessage context) {
        final String DEFAULT_RANK = "999";

        String giftCardSequence = getGiftCardSequence(wd, context);
        if (StringUtils.equalsIgnoreCase(giftCardSequence, DEFAULT_RANK)) {
            return "N";
        }
        return "Y";
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getCategories(WorkingDocument wd, ContextMessage context) {
        Set<String> categories = new HashSet<>();

        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();

        for (Sites site : eligibleSites) {
            categories.addAll(getHierarchySubListAsString(site.getCatalogId(), siteWebHierarchiesMap.get(site)));
        }

        if (Stores.SEARS.matches(context.getStoreName())) {
            categories.addAll(getHierarchySubListAsString(Sites.CRAFTSMAN.getCatalogId(), siteWebHierarchiesMap.get(Sites.CRAFTSMAN)));
            categories.addAll(getHierarchySubListAsString(Sites.KENMORE.getCatalogId(), siteWebHierarchiesMap.get(Sites.KENMORE)));
        }
        return categories;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getSearchableAttributes(WorkingDocument wd, ContextMessage context) {
        return getSearchableAttributeList(wd, context, new ArrayList<String>(), new ArrayList<String>());
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getSearchableAttributesSearchable(WorkingDocument wd, ContextMessage context) {
        return getSearchableAttributeList(wd, context, new ArrayList<String>(Arrays.asList("No", "N/A", "Unknown")), new ArrayList<String>(Arrays.asList("Yes")));
    }


    /**
     * @param wd
     * @return
     */
    protected List<String> getTagValues(WorkingDocument wd) {
        return wd.getExtracts().getOfferExtract().getVocTagNames();
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getCategory(WorkingDocument wd, ContextMessage context) {
        List<String> category = new ArrayList<>();

        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();

        for (Sites site : eligibleSites) {
            if (isCommercialStore(context)) {
                category.addAll(getScomHierarchiesAsString(site.getCatalogId(), siteWebHierarchiesMap.get(site)));
            } else {
                category.addAll(getHierarchiesAsString(site.getCatalogId(), siteWebHierarchiesMap.get(site)));
            }
        }

        if (Stores.SEARS.matches(context.getStoreName())) {
            category.addAll(getHierarchiesAsString(Sites.CRAFTSMAN.getCatalogId(), siteWebHierarchiesMap.get(Sites.CRAFTSMAN)));
            category.addAll(getHierarchiesAsString(Sites.KENMORE.getCatalogId(), siteWebHierarchiesMap.get(Sites.KENMORE)));
        }

        return category;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getLnames(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return new HashSet<>();
        }

        Set<String> lNames = new HashSet<>();

        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();

        List<List<String>> hierarchies = new ArrayList<>();
        for (Sites site : eligibleSites) {
            hierarchies.addAll(siteWebHierarchiesMap.get(site));
        }
        if (Stores.SEARS.matches(context.getStoreName())) {
            hierarchies.addAll(siteWebHierarchiesMap.get(Sites.CRAFTSMAN));
            hierarchies.addAll(siteWebHierarchiesMap.get(Sites.KENMORE));
        }

        for (List<String> hierarchy : hierarchies) {
            for (String levelName : hierarchy) {
                if (!StringUtils.equalsIgnoreCase(levelName, excludedCategory)) {
                    lNames.add(levelName);
                }
            }
        }
        return lNames;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getLnames_km(WorkingDocument wd, ContextMessage context) {
        Set<String> lNames_km = new HashSet<>();
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            Sites site = Stores.getSite(context.getStoreName());
            Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();
            List<List<String>> hierarchies = new ArrayList<>();
            hierarchies.addAll(siteWebHierarchiesMap.get(site));

            for (List<String> hierarchy : hierarchies) {
                for (String levelName : hierarchy) {
                    if (!StringUtils.equalsIgnoreCase(levelName, excludedCategory)) {
                        lNames_km.add(levelName);
                    }
                }
            }
        }
        return lNames_km;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getPrimaryLnames(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return new HashSet<>();
        }

        Set<String> primaryLnames = new HashSet<>();
        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();

        List<List<String>> hierarchies = new ArrayList<>();
        for (Sites site : eligibleSites) {
            hierarchies.addAll(siteWebHierarchiesMap.get(site));
        }
        primaryLnames.addAll(getPrimaryLnamesSet(hierarchies));

        if (Stores.SEARS.matches(context.getStoreName())) {
            primaryLnames.addAll(getPrimaryLnamesSet(siteWebHierarchiesMap.get(Sites.CRAFTSMAN)));
            primaryLnames.addAll(getPrimaryLnamesSet(siteWebHierarchiesMap.get(Sites.KENMORE)));
        }
        return primaryLnames;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected Set<String> getPrimaryLnames_km(WorkingDocument wd, ContextMessage context) {
        Set<String> primaryLnames_km = new HashSet<>();

        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            Sites site = Sites.KMART;
            Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();
            List<List<String>> hierarchies = new ArrayList<>();
            hierarchies.addAll(siteWebHierarchiesMap.get(site));
            primaryLnames_km.addAll(getPrimaryLnamesSet(hierarchies));
        }
        return primaryLnames_km;
    }

    /**
     * Overridden by: B only
     *
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getMemberSet(WorkingDocument wd, ContextMessage context) {
        return null;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getPrimaryCategory(WorkingDocument wd, ContextMessage context) {
        List<String> primaryCategory = new ArrayList<>();

        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();

        for (Sites site : eligibleSites) {
            String catalogId = site.getCatalogId();
            primaryCategory.addAll(getPrimaryHierarchySubListAsString(catalogId, siteWebHierarchiesMap.get(site)));
        }

        if (Stores.SEARS.matches(context.getStoreName())) {
            primaryCategory.addAll(getPrimaryHierarchySubListAsString(Sites.CRAFTSMAN.getCatalogId(), siteWebHierarchiesMap.get(Sites.CRAFTSMAN)));
            primaryCategory.addAll(getPrimaryHierarchySubListAsString(Sites.KENMORE.getCatalogId(), siteWebHierarchiesMap.get(Sites.KENMORE)));
        }
        return primaryCategory;
    }


    /**
     * @param wd
     * @return
     */
    protected String getCatentryId(WorkingDocument wd) {
        return wd.getExtracts().getContentExtract().getCatentryId();
    }

    /**
     * Overridden by: NV and B only
     *
     * @param wd
     * @return
     */
    protected String getProgramType(WorkingDocument wd) {
        return null;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected String getPrimaryVertical(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return null;
        }

        String primaryVertical = null;
        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();

        List<List<String>> hierarchies = new ArrayList<>();
        for (Sites site : eligibleSites) {
            hierarchies.addAll(siteWebHierarchiesMap.get(site));
        }
        if (!hierarchies.isEmpty()) {
            List<String> primaryHierarchy = hierarchies.get(0);
            primaryVertical = primaryHierarchy.get(0);
        }
        return primaryVertical;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected String getPrimaryVertical_km(WorkingDocument wd, ContextMessage context) {
        String primaryVertical = null;
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            Sites site = Sites.KMART;
            Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();
            List<List<String>> hierarchies = new ArrayList<>();
            hierarchies.addAll(siteWebHierarchiesMap.get(site));
            if (!hierarchies.isEmpty()) {
                List<String> primaryHierarchy = hierarchies.get(0);
                primaryVertical = primaryHierarchy.get(0);
            }
        }
        return primaryVertical;
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getPrimaryHierarchy(WorkingDocument wd, ContextMessage context) {
        List<String> primaryHierarchy = new ArrayList<>();

        List<Sites> eligSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
        Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();

        for (Sites site : eligSites) {
            List<List<String>> hierarchies = new ArrayList<>();
            hierarchies.addAll(siteWebHierarchiesMap.get(site));
            if (StringUtils.isNotEmpty(getPrimaryHierarchyAsString(site.getCatalogId(), hierarchies))) {
                if (isCommercialStore(context)) {
                    primaryHierarchy.add(getScomPrimaryHierarchyAsString(site.getCatalogId(), hierarchies));
                } else {
                    primaryHierarchy.add(getPrimaryHierarchyAsString(site.getCatalogId(), hierarchies));
                }
            }
        }

        if (Stores.SEARS.matches(context.getStoreName())) {
            if (StringUtils.isNotEmpty(getPrimaryHierarchyAsString(Sites.CRAFTSMAN.getCatalogId(), siteWebHierarchiesMap.get(Sites.CRAFTSMAN)))) {
                primaryHierarchy.add(getPrimaryHierarchyAsString(Sites.CRAFTSMAN.getCatalogId(), siteWebHierarchiesMap.get(Sites.CRAFTSMAN)));
            }
            if (StringUtils.isNotEmpty(getPrimaryHierarchyAsString(Sites.KENMORE.getCatalogId(), siteWebHierarchiesMap.get(Sites.KENMORE)))) {
                primaryHierarchy.add(getPrimaryHierarchyAsString(Sites.KENMORE.getCatalogId(), siteWebHierarchiesMap.get(Sites.KENMORE)));
            }

        }
        return primaryHierarchy;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected String getSellerTier(WorkingDocument wd, ContextMessage context) {
        final String DEFAULT_SELLER_TIER = "REGULAR";

        /** this feature is turned off in PROD. */
        /*
        String offerTier = wd.getExtracts().getOfferExtract().getOfferTier();
		if (offerTier != null) {
			return offerTier;
		}*/

        String sellerTier = wd.getExtracts().getSellerExtract().getSellerTier();
        if (sellerTier != null) {
            return sellerTier;
        } else {
            return DEFAULT_SELLER_TIER;
        }
    }


    /**
     * @param wd
     * @return
     */
    protected String getDivision(WorkingDocument wd) {
        return wd.getExtracts().getOfferExtract().getDivision();
    }


    /**
     * Field to determine if the product is automotive or not.
     *
     * @param wd
     * @return
     */
    protected boolean getAutomotive(WorkingDocument wd) {
        return wd.getExtracts().getContentExtract().isAutomotive();
    }


    /**
     * @param wd
     * @return
     */
    protected String getBrandId(WorkingDocument wd) {
        return wd.getExtracts().getContentExtract().getBrandCodeId();
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getFitment(WorkingDocument wd, ContextMessage context) {
        Set<String> fitments = new HashSet<>();
        if (Stores.AUTO.matches(context.getStoreName()) || Stores.SEARS.matches(context.getStoreName()) || Stores.FBM.matches(context.getStoreName())) {
            for (AutomotiveOffer fitmentOffer : wd.getExtracts().getFitmentExtract().getAutomotiveOffers()) {
                for (String eachFitment : fitmentOffer.getFitmentDTO().getData().getFitment()) {
                    fitments.add(eachFitment + "#" + fitmentOffer.getBrandCodeId() + fitmentOffer.getMfrPartno() + "#" + fitmentOffer.getUid() + "#" + fitmentOffer.getOfferId());
                }
            }
        }
        return new ArrayList<>(fitments);
    }


    /**
     * Defaults the itemCondition to new is no item condition is present,
     * if item condition is present then returns the parent mapping for the same
     *
     * @param wd - working document containing the extracts
     * @return
     */
    protected String getItemCondition(WorkingDocument wd) {
        String itemCondition = wd.getExtracts().getOfferExtract().getItemCondition();
        if (StringUtils.isNotBlank(itemCondition)) {
            return getParentItemCondition(itemCondition);
        }
        return ItemCondition.NEW.getStatus();
    }

    /**
     * maps the child condition to the parent condition and returns the parent condition
     *
     * @param itemCondition - string containing the child level
     * @return
     */
    protected String getParentItemCondition(String itemCondition) {
        return ItemCondition.getItemCondition(itemCondition);
    }


    /**
     * @param wd
     * @return
     */
    protected List<String> getGeospotTag(WorkingDocument wd) {
        return wd.getExtracts().getOfferExtract().getGeoSpotTag();
    }


    /**
     * @param wd
     * @return
     */
    protected List<String> getStoreUnit(WorkingDocument wd) {
        return wd.getExtracts().getPasExtract().getAvailableStores();
    }

    /**
     * @param wd
     * @return
     */
    protected List<String> getFulfillment(WorkingDocument wd) {
        return wd.getExtracts().getPasExtract().getAvailableStores();
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getShowroomUnit(WorkingDocument wd, ContextMessage context) {
        return wd.getExtracts().getPasExtract().getAvailableShowRoomUnits();
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getClickUrl(WorkingDocument wd, ContextMessage context) {
        List<String> clickUrlList = new ArrayList<>();
        if (Stores.MYGOFER.matches(context.getStoreName()) && StringUtils.isNotEmpty(wd.getExtracts().getOfferExtract().getClickUrl().get(0))) {
            clickUrlList = wd.getExtracts().getOfferExtract().getClickUrl();
        }
        return clickUrlList;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected int getNewItemFlag(WorkingDocument wd, ContextMessage context) {
        int newItemFlag = 0;
        return newItemFlag;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected int getDaysOnline(WorkingDocument wd, ContextMessage context) {
        final int DEFAULT_DAYS_ONLINE = 9999;

        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return DEFAULT_DAYS_ONLINE;
        }

        Sites site = Stores.getSite(context.getStoreName());
        if (site.isCrossFormat(wd, context)) {
            site = site.getCrossFormatSite();
        }

        if (Stores.AUTO.matches(context.getStoreName())) {
            List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);
            if (CollectionUtils.isNotEmpty(eligibleSites)) {
                site = eligibleSites.get(0); // Taking one of the sites to calculate days the product is online.
            }
        }
        LocalDate firstOnlineDate = wd.getExtracts().getOfferExtract().getOldestOnlineDateBySite().get(site);
        LocalDate today = new LocalDate();
        if (firstOnlineDate == null) {
            return DEFAULT_DAYS_ONLINE;
        }
        return Days.daysBetween(firstOnlineDate, today).getDays() + 1; // Adding 1 to count the day on which is online as well
    }

    /**
     * @param wd
     * @param context
     * @return
     */
    protected int getDaysOnline_km(WorkingDocument wd, ContextMessage context) {
        final int DEFAULT_DAYS_ONLINE = 9999;

        int daysOnline = DEFAULT_DAYS_ONLINE;
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context)) {
            Sites site = Sites.KMART;
            if (site.isCrossFormat(wd, context)) {
                site = site.getCrossFormatSite();
            }

            LocalDate firstOnlineDate = wd.getExtracts().getOfferExtract().getOldestOnlineDateBySite().get(site);
            LocalDate today = new LocalDate();
            if (firstOnlineDate == null) {
                return DEFAULT_DAYS_ONLINE;
            }
            daysOnline = Days.daysBetween(firstOnlineDate, today).getDays() + 1; // Adding 1 to count the day on which is online as well
        }
        return daysOnline;
    }

    protected boolean isKmartWorkflow(ContextMessage context) {
        return BooleanUtils.toBoolean(Stores.KMART.matches(context.getStoreName()));
    }

    protected boolean isNotSearsSiteMktplaceProd(WorkingDocument wd, ContextMessage context) {
        boolean isFbmWorkflow = Stores.FBM.matches(context.getStoreName());
        boolean isSearsAssoc = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context).contains(Sites.SEARS);
        return isFbmWorkflow && !isSearsAssoc;
    }

    protected boolean isKmartSiteMktplaceProd(WorkingDocument wd, ContextMessage context) {
        boolean isFbmWorkflow = Stores.FBM.matches(context.getStoreName());
        boolean isKmartAssoc = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context).contains(Sites.KMART);
        return isFbmWorkflow && isKmartAssoc;
    }

    /**
     * @param wd
     * @return
     */
    protected String getRank(WorkingDocument wd, ContextMessage context) {
        if (isKmartWorkflow(context) || isNotSearsSiteMktplaceProd(wd, context)) {
            return null;
        }
        return DEFAULT_RANK;
    }

    protected String getRank_km(WorkingDocument wd, ContextMessage context) {
        //Following exceptions for Automotives (uvd and autofitment check) should be properly designed in the system as a whole.  Adding this to match old indexer.
        if (isKmartWorkflow(context) || isKmartSiteMktplaceProd(wd, context) || wd.getExtracts().getContentExtract().isUvd() || isAutofitment(wd)) {
            return DEFAULT_RANK;
        }
        return null;
    }

    /**
     * Overridden by: NV and V only
     *
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getPickUpItemStores(WorkingDocument wd, ContextMessage context) {
        return null;
    }


    /**
     * Overridden by: NV and V only
     *
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getOfferAttr(WorkingDocument wd, ContextMessage context) {
        return null;
    }


    /**
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getStaticAttributes(WorkingDocument wd, ContextMessage context) {
        Set<String> staticAttributes = new HashSet<>();

        String storeName = context.getStoreName();
        Sites site = Stores.getSite(storeName);

        if (site.isCrossFormat(wd, context)) {
            site = site.getCrossFormatSite();
        }

        //Fix for SEARSPR as they share the same staticAttr but not site
        if (site == Sites.SEARSPR) {
            site = Sites.SEARS;
        }

        Optional<List<NameValue>> staticAttrs = Optional.ofNullable(wd.getExtracts().getContentExtract().getStaticFacetsSitesMap().get(site));
        for (NameValue staticAttr : staticAttrs.orElse(new ArrayList<NameValue>())) {
            String name = staticAttr.getName();
            String value = staticAttr.getValue();
            staticAttributes.add(name + "=" + value);
        }

        return new ArrayList<>(staticAttributes);
    }


    /**
     * @param wd
     * @return
     */
    protected String getDiscontinued(WorkingDocument wd) {
        final String DISCONTINUED_STATUS = "D";

        String internalRimSts = wd.getExtracts().getOfferExtract().getInternalRimSts();
        if (StringUtils.equalsIgnoreCase(internalRimSts, DISCONTINUED_STATUS)) {
            return ONE;
        }
        return ZERO;
    }


    /**
     * Retaining duplicates to match old indexer.  Duplicate ksns should be removed from the list.
     *
     * @param wd
     * @return
     */
    protected List<String> getKsn(WorkingDocument wd) {
        Set<String> ksnSet = new HashSet<>(wd.getExtracts().getOfferExtract().getKsn());
        List<String> groupedOffersKSNs = wd.getExtracts().getAuxiliaryOfferExtract().getGroupedOffersKSNs();
        if (CollectionUtils.isNotEmpty(groupedOffersKSNs)) {
            ksnSet.addAll(groupedOffersKSNs);
        }
        return new ArrayList<String>(ksnSet);
    }


    /**
     * Returns the value of the localAd field, The Default value of LocalAd = 0, as of 5/12/2016 for all stores
     *
     * @param wd      Current WorkingDocument
     * @param context Current Context
     * @return localAd value
     * @author Huzefa
     * @see <a href="https://mtjira.searshc.com/jira/browse/SEARCH-606">Original JIRA</a>
     */
    protected String getLocalAd(WorkingDocument wd, ContextMessage context) {
        return DEFAULT_LOCALAD;
    }

    protected List<String> getTopicIdWithRank(WorkingDocument wd) {
        return topicModelingService.getTopicIdWithRank(wd.getExtracts().getTMExtract().getMaps());
    }


    protected String getTopicModelingWithNameSearchable(WorkingDocument wd) {
        return topicModelingService.getTopicModelingWithNameSearchable(wd.getExtracts().getTMExtract().getMaps());
    }

    /** --Field Transformations Complete-- **/


    /**
     * --Private and/or protected helper methods for this class and subclasses of this follows --*
     */


    private boolean isPickUpAvailable(WorkingDocument wd, ContextMessage context) {
    	boolean pickUpAvail = false;
    	final Map<String, Map<String, Boolean>> itemAvailabilityMap = wd.getExtracts().getUasExtract().getItemAvailabilityMap();
    	Set<Map.Entry<String, Map<String, Boolean>>> entries = itemAvailabilityMap.entrySet();

    	for(Map.Entry<String, Map<String, Boolean>> entry: entries){
    		if (entry.getValue().get("pickupAvail") == true) {
    			pickUpAvail = true;
    			break;
    		}
    	}
    	return pickUpAvail;
    }

    /**
     * Method to get the list of searchable attributes 1. Get the list of static attributes associated with the product 2. Get the searchableAttributes from the nFilters data as dictated by SPIN 3. Do the intersection of both the attribues to get the final list 4. Ignore the attributes that contain
     * the value present in attrExclusionList 5. Ignore the values that are present in the valueExclusionList
     *
     * @param wd
     * @param context
     * @param attrExclusionList
     * @param valueExclusionList
     * @return
     */
    protected List<String> getSearchableAttributeList(WorkingDocument wd, ContextMessage context, List<String> attrExclusionList, List<String> valueExclusionList) {
        final String SEPARATOR = "=";
        Set<String> searchableAttributes = new HashSet<>();

        // Get the nFilters Data from the feed file
        Map<String, NFiltersDataDto> attachedData = nFiltersData.getAttachedData();

        // Get the static attributes map from the content collection
        Map<String, List<String>> staticAttrsMap = getStaticAttrsMap(wd, context);

        // Get all the categories associated with the product
        List<String> categories = getCategory(wd, context);

        // Check for every category, whether static attribute is searchable or not
        for (String category : categories) {
            String categoryNoSpace = category.replaceAll("\\s", StringUtils.EMPTY); // Removing spaces from the category
            if (attachedData.containsKey(categoryNoSpace)) { // Check to see if the static attributes is present in the file
                NFiltersDataDto nFiltersDataDto = attachedData.get(categoryNoSpace);
                List<SearchableAttributes> searchableAttrs = nFiltersDataDto.getSearchableAttributes();
                for (SearchableAttributes searchableAttr : searchableAttrs) {
                    if (staticAttrsMap.containsKey(searchableAttr.getName())) {
                        String name = searchableAttr.getName();
                        List<String> values = staticAttrsMap.get(searchableAttr.getName());

                        for (String value : values) {
                            if (!attrExclusionList.contains(value)) {
                                if (valueExclusionList.contains(value)) {
                                    searchableAttributes.add(name);
                                } else {
                                    searchableAttributes.add(name + SEPARATOR + value);
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<>(searchableAttributes);
    }


    protected List<String> getSearchableAttributeListVariations(WorkingDocument wd, ContextMessage context, List<String> attrExclusionList, List<String> valueExclusionList) {
        final String SEPARATOR = "=";
        Set<String> searchableAttributes = Sets.newHashSet();

        // Get the nFilters Data from the feed file
        NFiltersData nFiltersData = (NFiltersData) persistenceFactory.obtain("nFiltersData.txt");
        Map<String, NFiltersDataDto> attachedData = nFiltersData.getAttachedData();

        // Get the static attributes map from the content collection
        Map<String, List<String>> staticAttrsMap = getStaticAttrsMap(wd, context);

        // Get the var attributes map (Only applicable for variations)
        Map<String, List<String>> varAttrsMap = wd.getExtracts().getVarAttrExtract().getAttrsMap();
        Map<String, String> nameFamilyNameMap = getNameFamilyNameMap(wd.getExtracts().getVarAttrExtract().getDefiningAttrsList());
        varAttrsMap = convertNameToFamilyName(varAttrsMap, new ArrayList<>(Arrays.asList("Color", "Color Family", "Overall Color", "Size")), nameFamilyNameMap);

        Map<String, List<String>> attrsMap = Maps.newHashMap();
        attrsMap.putAll(staticAttrsMap);

        for (Map.Entry<String, List<String>> entry : varAttrsMap.entrySet()) {
            String attrName = entry.getKey();
            if (attrsMap.containsKey(attrName)) {
                List<String> values = entry.getValue();
                values.addAll(attrsMap.get(attrName));
                attrsMap.put(attrName, values);
            } else {
                attrsMap.put(entry.getKey(), entry.getValue());
            }
        }

        // Get all the categories associated with the product
        List<String> categories = getCategory(wd, context);

        // Check for every category, whether static attribute is searchable or not
        for (String category : categories) {
            String categoryNoSpace = category.replaceAll("\\s", StringUtils.EMPTY); // Removing spaces from the category
            if (attachedData.containsKey(categoryNoSpace)) { // Check to see if the static attributes is present in the file
                NFiltersDataDto nFiltersDataDto = attachedData.get(categoryNoSpace);
                List<SearchableAttributes> searchableAttrs = nFiltersDataDto.getSearchableAttributes();
                for (SearchableAttributes searchableAttr : searchableAttrs) {
                    if (attrsMap.containsKey(searchableAttr.getName())) {
                        String name = searchableAttr.getName();
                        List<String> values = attrsMap.get(searchableAttr.getName());

                        for (String value : values) {
                            if (!attrExclusionList.contains(value)) {
                                if (valueExclusionList.contains(value)) {
                                    searchableAttributes.add(name);
                                } else {
                                    searchableAttributes.add(name + SEPARATOR + value);
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<>(searchableAttributes);
    }

    /**
     * Get the mapping between name and family name from defining attributes
     *
     * @param definingAttrs
     * @return
     */
    protected Map<String, String> getNameFamilyNameMap(List<DefiningAttrs> definingAttrs) {
        Map<String, String> nameFamilyNameMap = Maps.newHashMap();
        for (DefiningAttrs definingAttr : definingAttrs) {
            List<Value> vals = definingAttr.getVal();
            for (Value val : vals) {
                nameFamilyNameMap.put(val.getName(), val.getFamilyName());
            }
        }
        return nameFamilyNameMap;
    }

    protected Map<String, List<String>> convertNameToFamilyName(Map<String, List<String>> attrsMap, List<String> attributesToChange, Map<String, String> nameFamilyNameMap) {
        Map<String, List<String>> attrMapCopy = Maps.newHashMap();
        for (Map.Entry<String, List<String>> entry : attrsMap.entrySet()) {

            String attrName = entry.getKey();
            List<String> attrValues = entry.getValue();

            Set<String> valueSet = Sets.newHashSet();
            for (String attrValue : attrValues) {
                if (attributesToChange.contains(attrName) && nameFamilyNameMap.containsKey(attrValue)) {
                    valueSet.add(nameFamilyNameMap.get(attrValue));
                } else {
                    valueSet.add(attrValue);
                }
            }

            attrMapCopy.put(attrName, new ArrayList<>(valueSet));
        }
        return attrMapCopy;
    }


    protected Map<String, List<String>> getStaticAttrsMap(WorkingDocument wd, ContextMessage context) {
        String storeName = context.getStoreName();
        Sites site = Stores.getSite(storeName);
        if (site.isCrossFormat(wd, context)) {
            site = site.getCrossFormatSite();
        }
        Optional<List<NameValue>> staticAttrs = Optional.ofNullable(wd.getExtracts().getContentExtract().getStaticFacetsSitesMap().get(site));
        if (Stores.SEARSPR.matches(context.getStoreName())) {
            staticAttrs = Optional.ofNullable(wd.getExtracts().getContentExtract().getStaticFacetsSitesMap().get(Stores.getSite(Stores.SEARS.getStoreName())));
        }

        Map<String, List<String>> attributeNamesValMap = new HashMap<>();
        for (NameValue nameValue : staticAttrs.orElse(new ArrayList<NameValue>())) {
            if (attributeNamesValMap.containsKey(nameValue.getName())) {
                List<String> attributeValues = attributeNamesValMap.get(nameValue.getName());
                attributeValues.add(nameValue.getValue());
                attributeNamesValMap.put(nameValue.getName(), attributeValues);
            } else {
                attributeNamesValMap.put(nameValue.getName(), new ArrayList<>(Arrays.asList(nameValue.getValue())));
            }
        }

        return attributeNamesValMap;
    }


    protected Set<String> getLevelNCategories(WorkingDocument wd, ContextMessage context, int level) {
        Set<String> levelNCategories = new HashSet<>();

        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);

        Map<Sites, List<List<String>>> siteWebHierarchiesMap = wd.getExtracts().getContentExtract().getSiteWebHierarchiesMap();

        List<List<String>> hierarchies = new ArrayList<>();
        for (Sites site : eligibleSites) {
            hierarchies.addAll(siteWebHierarchiesMap.get(site));
            String catalogId = site.getCatalogId();
            for (List<String> hierarchy : hierarchies) {
                StringBuilder sb = new StringBuilder();
                sb.append(catalogId);
                for (int index = 0; index < level && index < hierarchy.size(); index++) {
                    sb.append(SEPARATOR + hierarchy.get(index));
                }
                levelNCategories.add(sb.toString());
            }
        }
        return levelNCategories;
    }


    private Set<String> getLNamesSet(int level, List<List<String>> hierarchies) {
        Set<String> lNamesSet = new HashSet<>();

        for (List<String> hierarchy : hierarchies) {
            if (hierarchy.size() >= level) {
                lNamesSet.add(hierarchy.get(level - 1));
            }
        }
        return lNamesSet;
    }


    private List<String> getHierarchySubListAsString(String catalogId, List<List<String>> hierarchies) {
        List<String> hierarchySubListAsString = new ArrayList<>();

        for (List<String> hierarchy : hierarchies) {
            for (int index = 0; index < hierarchy.size(); index++) {
                int level = index + 1;
                List<String> hierarchySubList = hierarchy.subList(0, level);
                hierarchySubListAsString.add(catalogId + SEPARATOR + level + SEPARATOR + StringUtils.join(hierarchySubList, SEPARATOR));
            }
        }
        return hierarchySubListAsString;
    }


    private List<String> getHierarchiesAsString(String catalogId, List<List<String>> hierarchies) {
        List<String> hierarchiesAsString = new ArrayList<>();
        for (List<String> hierarchy : hierarchies) {
            hierarchiesAsString.add(catalogId + SEPARATOR + StringUtils.join(hierarchy, SEPARATOR));
        }
        return hierarchiesAsString;
    }


    private List<String> getPrimaryHierarchySubListAsString(String catalogId, List<List<String>> hierarchies) {
        List<String> hierarchySubListAsString = new ArrayList<>();

        if (!hierarchies.isEmpty()) {
            List<String> primaryHierarchy = hierarchies.get(0);
            for (int index = 0; index < primaryHierarchy.size(); index++) {
                int level = index + 1;
                List<String> hierarchySubList = primaryHierarchy.subList(0, level);
                hierarchySubListAsString.add(catalogId + SEPARATOR + level + SEPARATOR + StringUtils.join(hierarchySubList, SEPARATOR));
            }
        }
        return hierarchySubListAsString;
    }

    /**
     * @param wd
     * @return
     */
    protected List<String> getLocalAdList(WorkingDocument wd) {
        return wd.getExtracts().getLocalAdExtract().getLocalAdList();
    }

    private String getPrimaryHierarchyAsString(String catalogId, List<List<String>> hierarchies) {
        String primaryHierarchyAsString = null;
        if (!hierarchies.isEmpty()) {
            primaryHierarchyAsString = catalogId + SEPARATOR + StringUtils.join(hierarchies.get(0), SEPARATOR);
        }
        return primaryHierarchyAsString;
    }


    private Set<String> getPrimaryLnamesSet(List<List<String>> hierarchies) {
        Set<String> lNamesSet = new HashSet<>();

        if (!hierarchies.isEmpty()) {
            for (String levelName : hierarchies.get(0)) {
                if (!StringUtils.equalsIgnoreCase(levelName, excludedCategory)) {
                    lNamesSet.add(levelName);
                }
            }
        }
        return lNamesSet;
    }


    protected Set<String> getOfferIdCombinations(String id) {
        Set<String> offerIdCombinations = new HashSet<>();
        offerIdCombinations.add(StringUtils.substring(id, 0, 8));
        if (StringUtils.startsWith(id, ZERO)) {
            offerIdCombinations.add(StringUtils.substring(id, 1, 8));
        }
        offerIdCombinations.add(StringUtils.substring(id, 3, 8));
        return offerIdCombinations;
    }

    /**
     * Field to determine if the product is automotive and additionally requires fitment.
     *
     * @param wd
     * @return
     */
    protected boolean isAutofitment(WorkingDocument wd) {
        return wd.getExtracts().getContentExtract().isAutomotive() && StringUtils.equalsIgnoreCase(wd.getExtracts().getContentExtract().getAutofitment(), "Requires Fitment");
    }

    /** --Helper methods end -- **/


	/* ------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------ */
    /* -------------------- Offer Level customizations as follows ------------------------- */
    /* ------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------ */

    /**
     * This method orchestrates all the customizations for fields that require offer level information.
     * Every offer level field depends on the extracts from respective components. If there are fields which require
     * additional information, the applicable extract component is the right place to make sure that information is
     * aggregated.
     * <p/>
     * NOTE - Do not forget to make the same changes to C2C flow in C2CConsumer
     * <p/>
     * For now, there are 3 places this method will reside V, NV, B. Clean up may be required for commonalities in the code
     *
     * @param wd Working Document for the extracts and related info
     * @return List of OfferDoc
     */
    protected List<OfferDoc> generateOfferDocs(WorkingDocument wd, ContextMessage context) {

        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_GENERATE_OFFER_DOCS);
        //Get handle to all the offerDocBuilders in one place
        Map<String, OfferDocBuilder> offerDocBuilderMap = OfferUtil.createOfferDocBuilders(wd, context);
        Set<String> offerIds = offerDocBuilderMap.keySet(); //Get all the offerIds

        //Classify the doc to call the applicable methods of V, NV and bundle
        String catentrySubType = wd.getExtracts().getContentExtract().getCatentrySubType();
        Classification classification;

        List<OfferDoc> offerDocs = new ArrayList<>();
        try {
            classification = factory.classify(catentrySubType);

            for (String offerId : offerIds) {

                metricManager.incrementCounter(Metrics.COUNTER_OFFER_DOCS_GENERATED);

                //Get a handle to offerDocBuilder
                OfferDocBuilder offerDocBuilder = offerDocBuilderMap.get(offerId);

                //Append values one by one, first the classsification dependent ones
                offerDocBuilder.searchableAttributes(classification.getOfferSearchableAttributes(offerId,
                        wd, context,
                        new ArrayList<>(),
                        new ArrayList<>()));
                offerDocBuilder.searchableAttributesSearchable(classification.getOfferSearchableAttributes(offerId,
                        wd, context,
                        new ArrayList<>(Arrays.asList("Yes")),
                        new ArrayList<>(Arrays.asList("No", "N/A", "Unknown"))));

                //Ensure Availability

                offerDocBuilder.reservableStores(classification.getReservableStoresList(offerId, wd, context));
                offerDocBuilder.reservableAllStores(classification.isReservableStoresAll(offerId, wd, context));
                offerDocBuilder.shippingAvailable(classification.isShipAvaialable(offerId, wd, context));
                offerDocBuilder.storeUnits(classification.getOfferStoreUnits(offerId, wd, context));
                offerDocBuilder.zoneList(classification.getOfferZoneList(offerId, wd, context));
                offerDocBuilder.deliveryAreaList(classification.getDeliveryAreaList(offerId, wd, context));

                //Then the classification independent ones
                offerDocBuilder.partnumber(getPartNumber(wd));
                offerDocBuilder.sellerId(getSellerIdForOfferDoc(wd, offerId));
                offerDocBuilder.opsUuid_s(getOpsUuid_s(context));
                offerDocBuilder.opsCurrentServer(getOpsCurrentServer_i(context));
                offerDocBuilder.freeShipping(classification.getOfferFreeShipping(offerId,
                        wd, context));

                //Once everything is done, append the OfferDoc to the list
                offerDocs.add(new OfferDoc(offerDocBuilder));
            }
        } catch (SearchCommonException scE) {
            metricManager.incrementCounter(Metrics.COUNTER_OFFER_DOCS_EXCEPTIONS);
            LOGGER.error("Classification of product failed while generating offer documents", scE);
        } finally {
            metricManager.stopTiming(timerContext);
        }
        return offerDocs;
    }

    /**
     * Given an offerId, this method returns the searchableAtrributes for that offer
     * Abstract Build method that is overriden by transformation classes by setting the required field for that classification.
     * Common fields go into "Classification" class, rest go to their types (Nonvariation, Variation and Bundle).
     *
     * @param offerId
     * @param wd                 WorkingDocument for the current ssin
     * @param context            Context of the current run
     * @param attrExclusionList  Attributes to be excluded in the result
     * @param valueExclusionList Values to be excluded in the result
     * @return
     */
    protected List<String> getOfferSearchableAttributes(String offerId,
                                                        WorkingDocument wd, ContextMessage context,
                                                        List<String> attrExclusionList, List<String> valueExclusionList) {

        // Get the static attributes map from the content collection
        Map<String, List<String>> staticAttrsMap = getStaticAttrsMap(wd, context);
        staticAttrsMap.putAll(staticAttrsMap);
        return eliminateExcludedAttributes(wd, context, staticAttrsMap, attrExclusionList, valueExclusionList);
    }

    protected List<String> getOfferFreeShipping(String offerId, WorkingDocument wd, ContextMessage context) {
        List<String> storeOfferFreeShipping = new ArrayList<>();
        List<Integer> storeIds = Stores.getStoreId(wd, context);
        for (Integer storeId : storeIds) {
            //SEARCH-2962:Offer Level check if product is eligible for Shipping first and then for Free Shipping
            boolean isFreeShipping = eligibleAndShipAvailable(offerId, wd, context) &&
                                        isFreeShippingForOfferStore(wd, storeId, offerId, context);
            String prefix = Integer.toString(storeId) + SEPARATOR;
            storeOfferFreeShipping.add(prefix + (isFreeShipping ? ONE : ZERO));
        }
        return storeOfferFreeShipping;
    }

    protected boolean isFreeShippingForOfferStore(WorkingDocument wd, Integer storeId, String offerId, ContextMessage context) {

        switch (context.getStoreName().toLowerCase()) {
            case "sears":
            case "kmart":
            case "searspr":
                return getOfferFreeShippingPromorel(wd, context, storeId, offerId);
            case "fbm":
                return getOfferFreeShippingPromorel(wd, context, storeId, offerId) || getFreeShippingOffer(wd, offerId) || freeShippingBasedOnWeight(wd, context) || freeShippingBasedOnOrderAmount(wd, context);
            default:
                return false;
        }
    }

    private boolean getFreeShippingOffer(WorkingDocument wd, String offerId) {
        return Optional.ofNullable(wd.getExtracts().getOfferExtract().getFreeShippingMap().get(offerId)).orElse(false);
    }

    /**
     * We are now getting store information from IAS instead of PAS and are storing that info at the child level
     * This is overridden in V and NV
     *
     * @param offerId
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getOfferStoreUnits(String offerId, WorkingDocument wd, ContextMessage context) {
        return null;
    }

    /**
     * We are now getting zone information from IAS instead of PAS and at the offer level
     *
     * @param offerId
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getOfferZoneList(String offerId, WorkingDocument wd, ContextMessage context) {
        return null;
    }

    /**
     * Overridden only in NV
     * As part of Ensure Availability, For NV we also index the DDC region where a product is deliverable
     * This will help us provide the members with only the products that can be delivered in their zip
     * Search-1765
     *
     * @param offerId
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getDeliveryAreaList(String offerId, WorkingDocument wd, ContextMessage context) {
        return null;
    }

    /**
     * Overridden in V and NV (Scope of Ensure Availability)
     * Get the list of all the stores where a product is reservable (SRES)
     * SEARCH-1269
     *
     * @param offerId
     * @param wd
     * @param context
     * @return
     */
    protected List<String> getReservableStoresList(String offerId, WorkingDocument wd, ContextMessage context) {
        return null;
    }

    /**
     * Overridden in V and NV (Scope of Ensure Availability)
     * Get whether a product is available in all stores via the CRES, VRES and DRES channels
     * SEARCH-1269
     *
     * @param offerId
     * @param wd
     * @param context
     * @return
     */
    protected boolean isReservableStoresAll(String offerId, WorkingDocument wd, ContextMessage context) {
        return false;
    }
    
    /**
     * Overridden in V and NV (Scope of Ensure Availability)
     * Consider product availability for ship/pick up while displaying results on PLP
     * SEARCH-2841
     *
     * @param offerId
     * @param wd
     * @param context
     * @return boolean flag on shipping availability 
     * 
     */
    protected boolean isShipAvaialable(String offerId, WorkingDocument wd, ContextMessage context) {
    	return false;
    }

    /**
     * Eliminate the attributes and values which are not required and construct a final list of searchableAttributes
     *
     * @param wd                 WorkingDocument for extracts
     * @param context            ContextMessage for context information
     * @param attrsMap           The map of attribute and their values
     * @param attrExclusionList  Attributes to be excluded
     * @param valueExclusionList Values to be excluded
     * @return Final list of Searchable Attributes
     */
    protected List<String> eliminateExcludedAttributes(WorkingDocument wd, ContextMessage context,
                                                       Map<String, List<String>> attrsMap,
                                                       List<String> attrExclusionList, List<String> valueExclusionList) {
        final String SEPARATOR = "=";
        Set<String> searchableAttributes = new HashSet<>();

        // Get the nFilters Data from the feed file
        NFiltersData nFiltersData = (NFiltersData) persistenceFactory.obtain("nFiltersData.txt");
        Map<String, NFiltersDataDto> attachedData = nFiltersData.getAttachedData();

        // Get all the categories associated with the product
        List<String> categories = getCategory(wd, context);

        // Check for every category, whether static attribute is searchable or not
        for (String category : categories) {
            String categoryNoSpace = category.replaceAll("\\s", StringUtils.EMPTY); // Removing spaces from the category

            if (attachedData.containsKey(categoryNoSpace)) { // Check to see if the static attributes is present in the file
                NFiltersDataDto nFiltersDataDto = attachedData.get(categoryNoSpace);
                List<SearchableAttributes> searchableAttrs = nFiltersDataDto.getSearchableAttributes();
                for (SearchableAttributes searchableAttr : searchableAttrs) {
                    if (attrsMap.containsKey(searchableAttr.getName())) {
                        String name = searchableAttr.getName();
                        List<String> values = attrsMap.get(searchableAttr.getName());

                        for (String value : values) {
                            if (!attrExclusionList.contains(value)) {
                                if (valueExclusionList.contains(value)) {
                                    searchableAttributes.add(name);
                                } else {
                                    searchableAttributes.add(name + SEPARATOR + value);
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<>(searchableAttributes);
    }

    private String getSellerIdForOfferDoc(WorkingDocument wd, String offerId) {
        Optional<Integer> sellerId = Optional.ofNullable(wd.getExtracts().getOfferExtract().getOfferToSellerIdMap().get(offerId));
        if (sellerId.isPresent()) {
            return String.valueOf(sellerId.get());
        } else {
            return null;
        }
    }

    public abstract SearchDocBuilder build(WorkingDocument wd, ContextMessage context);

    public String getStoreValueFromList(int storeId, List<String> stringArray) {
        for (String str : stringArray) {
            List<String> parts = Arrays.asList(str.split("_"));
            if (storeId == Integer.parseInt(parts.get(0))) {
                return parts.get(1);
            }
        }
        return "";
    }

    public boolean isFreeShippingForStore(WorkingDocument wd, int storeId, ContextMessage context) {

        switch (context.getStoreName().toLowerCase()) {
            case "sears":
            case "kmart":
            case "searspr":
                return getFreeShippingPromorel(wd, storeId);
            case "fbm":
                return getFreeShippingPromorel(wd, storeId) || getFreeShippingOffer(wd) || freeShippingBasedOnWeight(wd, context) || freeShippingBasedOnOrderAmount(wd, context);
            default:
                return false;
        }

    }

    /**
     * SEARCH-3064 Price Based Free Shipping
     * @param wd
     * @param context
     * @return
     */
    private boolean freeShippingBasedOnOrderAmount(WorkingDocument wd, ContextMessage context) {
    	PriceExtract priceExtract = wd.getExtracts().getPriceExtract();
    	SellerExtract sellerExtract = wd.getExtracts().getSellerExtract();
    	Set<Map.Entry<String, List<OrderAmountChargeRange>> > entries = sellerExtract.getOfferOrderAmountChargeRange().entrySet(); 
    	
    	 List<Integer> onlineStoreIds = Stores.getStoreId(wd, context);
         final double zeroPrice = 0.0;
         List<Double> prices = new ArrayList<>();
         for (Integer onlineStoreId : onlineStoreIds) {
             Price onlineStorePrice = priceExtract.getOnlinePrice().get(onlineStoreId);
             double nonZeroPrice = priceAlgorithm.nonZeroPrice(onlineStorePrice);
             if (nonZeroPrice != zeroPrice) {
                 prices.add(nonZeroPrice);
             }
         }
         
         for(Map.Entry<String, List<OrderAmountChargeRange>> entry:entries) {
        	 for(double price : prices) {
        		 if(priceFallsWithinRangeAndGndPriceZero(entry.getValue(), price)) {
        			 return true;
        		 }
        	 }
         }
    	
    	return false;
    }

    /**
     * SEARCH-3064 Price Based Free Shipping
     * @param priceRanges
     * @param price
     * @return
     */
    private boolean priceFallsWithinRangeAndGndPriceZero(List<OrderAmountChargeRange> priceRanges, double price) {
    	 if (CollectionUtils.isEmpty(priceRanges) || price <= 0) {
             return false;
         }
         double max = 0.0;
         double min = 0.0;
         double gnd = -1.0;
         for (OrderAmountChargeRange priceRange : priceRanges) {
             if (NumberUtils.isNumber(priceRange.getMax())) {
                 max = Double.parseDouble(priceRange.getMax());
             }
             if (NumberUtils.isNumber(priceRange.getMin())) {
                 min = Double.parseDouble(priceRange.getMin());
             }
             if (NumberUtils.isNumber(priceRange.getGround())) {
                 gnd = Double.parseDouble(priceRange.getGround());
             }
             if (price >= min && price <= max && gnd == 0.0) {
                 return true;
             }
         }
         return false;
	}

	/**
     * SEARCH-198 - Weight Based Free Shipping
     *
     * @param wd
     * @return
     */
    private boolean freeShippingBasedOnWeight(WorkingDocument wd, ContextMessage context) {
        OfferExtract offerExtract = wd.getExtracts().getOfferExtract();
        Map<String, Double> offerWeight = offerExtract.getOfferWeight();

        SellerExtract sellerExtract = wd.getExtracts().getSellerExtract();
        Set<Map.Entry<String, List<WeightRange>>> entries = sellerExtract.getOfferWeightRange().entrySet();
        for (Map.Entry<String, List<WeightRange>> entry: entries) {
            if (fallsWithinRangeAndGroudPriceZero(entry.getValue(), offerWeight.get(entry.getKey()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * SEARCH-198 - Weight Based Free Shipping
     *
     * @param weightRanges
     * @param weight
     * @return
     */
    private boolean fallsWithinRangeAndGroudPriceZero(List<WeightRange> weightRanges, double weight) {
        if (CollectionUtils.isEmpty(weightRanges) || weight <= 0) {
            return false;
        }
        double max = 0.0;
        double min = 0.0;
        double gnd = -1.0;
        for (WeightRange weightRange : weightRanges) {
            if (NumberUtils.isNumber(weightRange.getMax())) {
                max = Double.parseDouble(weightRange.getMax());
            }
            if (NumberUtils.isNumber(weightRange.getMin())) {
                min = Double.parseDouble(weightRange.getMin());
            }
            if (NumberUtils.isNumber(weightRange.getGround())) {
                gnd = Double.parseDouble(weightRange.getGround());
            }
            if (weight >= min && weight <= max && gnd == 0.0) {
                return true;
            }
        }
        return false;
    }

    public boolean getFreeShippingPromorel(WorkingDocument wd, int storeId) {
        return wd.getExtracts().getPromoExtract().getStorePromoExtracts().containsKey(Stores.getStore(storeId))
                && wd.getExtracts().getPromoExtract().getStorePromoExtracts().get(Stores.getStore(storeId)).isFreeShipping();
    }
    
    public boolean getOfferFreeShippingPromorel(WorkingDocument wd, ContextMessage context, int storeId, String offerId) {
        return wd.getExtracts().getPromoExtract().getStorePromoExtracts().containsKey(Stores.getStore(storeId))
                && wd.getExtracts().getPromoExtract().getStorePromoExtracts().get(Stores.getStore(storeId)).isFreeShipping();
    }

    public boolean getFreeShippingOffer(WorkingDocument wd) {
        return wd.getExtracts().getOfferExtract().isFreeShipping();
    }

    public boolean isCommercialStore(ContextMessage context){
        return StringUtils.equalsIgnoreCase(context.getStoreName(), Stores.COMMERCIAL.getStoreName());
    }
    public String getAggregatorId(WorkingDocument wd){
        return null;
    }

    protected double getScomPrice(WorkingDocument wd, ContextMessage context) {
        double price = 0.0;
        final PriceExtract priceExtract = wd.getExtracts().getPriceExtract();
        List<Integer> onlineStoreIds = Stores.getStoreId(wd, context);
        for (Integer onlineStoreId : onlineStoreIds) {
            Price onlineStorePrice = priceExtract.getOnlinePrice().get(onlineStoreId);
            price=priceAlgorithm.nonZeroPrice(onlineStorePrice);
        }
        return price;
    }

    protected String getScomInStock(WorkingDocument wd, ContextMessage context) {
        final UasExtract uasExtract = wd.getExtracts().getUasExtract();
        String inStock ="0";
        final Map<String, Map<String, Boolean>> itemAvailabilityMap = uasExtract.getItemAvailabilityMap();
        //First loop through the item map
        for (Iterator<Entry<String, Map<String, Boolean>>> itemAvailIterator = itemAvailabilityMap.entrySet().iterator(); itemAvailIterator.hasNext(); ) {
            Map.Entry<String, Map<String, Boolean>> itemAvailabilityEntry = itemAvailIterator.next();
            //For each item, loop through all the offers to see if any is available
            for (Iterator<Entry<String, Boolean>> offerAvailabilityIterator = itemAvailabilityEntry.getValue().entrySet().iterator(); offerAvailabilityIterator.hasNext(); ) {
                Map.Entry<String, Boolean> offerAvailabilityEntry = offerAvailabilityIterator.next();
                if (offerAvailabilityEntry.getValue()) {
                    //If any offer is available, set instockAvailable true and break
                    inStock = "1";
                    break;
                }
            }
        }
        return inStock;
    }

    private List<String> getScomHierarchiesAsString(String catalogId, List<List<String>> hierarchies) {
        List<String> hierarchiesAsString = new ArrayList<>();
        Set<String> hierarchiesSet = new HashSet<>();
        String temp = "";
        for (List<String> hierarchy : hierarchies) {
            for(String val : hierarchy) {
                temp = !temp.isEmpty()?new StringBuilder(temp).append(SEPARATOR).append(val).toString():val;
                hierarchiesAsString.add(temp);
            }
            temp = "";
        }
        hierarchiesSet.addAll(hierarchiesAsString);
        hierarchiesAsString.clear();
        hierarchiesAsString.addAll(hierarchiesSet);
        return hierarchiesAsString;
    }

    private String getScomPrimaryHierarchyAsString(String catalogId, List<List<String>> hierarchies) {
        String primaryHierarchyAsString = null;
        if (!hierarchies.isEmpty()) {
            primaryHierarchyAsString = StringUtils.join(hierarchies.get(0), SEPARATOR);
        }
        return primaryHierarchyAsString;
    }

    protected String getB2bName(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getContentExtract().getB2bName();
        }
        return null;
    }

    protected String getB2bDescShort(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getContentExtract().getB2bDescShort();
        }
        return null;
    }

    protected String getB2bDescLong(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getContentExtract().getB2bDescLong();
        }
        return null;
    }

    protected String getSubupPid(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getOfferExtract().getSubUpPid();
        }
        return null;
    }

    protected String getSubdownPid(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getOfferExtract().getSubDownPid();
        }
        return null;
    }

    protected String getAltPid(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getOfferExtract().getAltPid();
        }
        return null;
    }

    protected String getReptPid(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getOfferExtract().getReptPid();
        }
        return null;
    }

    protected String getTransRsn(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getOfferExtract().getTransRsn();
        }
        return null;
    }

    protected String getTruckLoadQty(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getOfferExtract().getTruckLoadQty();
        }
        return null;
    }

    protected String getDeliveryCat(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getOfferExtract().getDeliveryCat();
        }
        return null;
    }

    protected String getFfmClass(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getOfferExtract().getFfmClass();
        }
        return null;
    }

    protected String getDiscntDt(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getOfferExtract().getDiscntDt();
        }
        return null;
    }

    protected String getColorFamily(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getContentExtract().getColorFamily();
        }
        return null;
    }

    protected String getWidth(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getContentExtract().getWidth();
        }
        return null;
    }

    protected String getHeight(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getContentExtract().getHeight();
        }
        return null;
    }

    protected String getDepth(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getContentExtract().getDepth();
        }
        return null;
    }

    protected String getEnergyStarCompliant(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getContentExtract().getEnergyStarCompliant();
        }
        return null;
    }

    protected String getAdaCompliant(WorkingDocument wd, ContextMessage context) {
        if(isCommercialStore(context)) {
            return wd.getExtracts().getContentExtract().getAdaCompliant();
        }
        return null;
    }

}