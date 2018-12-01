package com.shc.ecom.search.docbuilder;

import com.shc.ecom.search.common.messages.ContextMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author rgopala We will use this as a static class in the SearchDoc
 */
public class SearchDocBuilder implements Builder<SearchDoc> {

    private static final String PARENTPRODUCTTYPE = "parent";
    private final String partnumber;
    private String id;
    private String timestamp;
    private String beanType;
    private String fullmfpartno;
    private String mfpartno;
    private String itemnumber;
    private List<String> storeOrigin;
    private List<String> storeOriginSearchable;
    private String imageStatus;
    private List<String> alternateImage;
    private String swatches;
    private String swatchInfo;
    private Set<String> xref;
    private Set<String> promoId;
    private String eGiftEligible;
    private int sellerTierRank;
    private String name;
    private String nameSearchable;
    private String image;
    private String description;
    private List<String> promo;
    private List<String> subcatAttributes;
    private List<String> productAttributes;
    private List<String> storeAttributes;
    private List<String> price;
    private List<String> sale;
    private List<String> clearance;
    private List<String> instock;
    private List<Integer> newArrivals;
    private List<String> itemsSold;
    private List<String> revenue;
    private List<String> conversion;
    private List<String> productViews;
    private List<String> behavioral;
    private String multifloat;
    private List<String> sears_international;
    private String mobileReviews;
    private String brand;
    private String veh2prod;
    private List<String> catalogs;
    private Set<String> level1Cats;
    private Set<String> level2Cats;
    private Set<String> level3Cats;
    private Set<String> vNames;
    private Set<String> cNames;
    private Set<String> sNames;
    private String sin;
    private String sellerCount;
    private String cpcFlag;
    private String fbm;
    private String pickUpNowEligible;
    private String vendorId;
    private List<String> sellers;
    private List<String> spuEligible;
    private int layAway;
    private String mailInRebate;
    private List<String> shipVantage;
    private String catConfidence;
    private String has991;
    private int trustedSeller;
    private List<String> international_shipping;
    private Integer int_ship_eligible;
    private String brandSearchable;
    private String partnumberstr;
    private List<String> countryGroup;
    private String browseBoost;
    private String instockShipping;
    private int matureContentFlag;
    private int accessories;
    private String prdType;
    private String cpcClickRate;
    private String searchPhrase;
    private String sellerId;
    private List<String> freeDelivery;
    private List<String> promotionTxt;
    private String url;
    private List<String> discount;
    private List<String> delivery;
    private List<String> offer;
    private List<String> buymore;
    private List<String> avgRatingContextual;
    private List<String> numReviewsContextual;
    private List<String> rebate;
    private String rebateStatus;
    private String asis;
    private String upc;
    private List<String> storePickUp;
    private double numericRevenue;
    private String flashDeal;
    private String giftCardSequence;
    private String giftCardType;
    private Set<String> categories;
    private List<String> searchableAttributes;
    private List<String> tagValues;
    private List<String> searchableAttributesSearchable;
    private List<String> category;
    private Set<String> lnames;
    private Set<String> primaryLnames;
    private List<String> primaryCategory;
    private String catentryId;
    private String programType;
    private String primaryVertical;
    private List<String> primaryHierarchy;
    private String sellerTier;
    private String division;
    private String partType;
    private String partTypeName;
    private String baseVehicle;
    private String rearBrakeType;
    private String frontBrakeType;
    private String bodyType;
    private String bodyNumDoors;
    private String transmissionMfrCode;
    private String transmissionType;
    private String transmissionControlType;
    private String transmissionNumSpeeds;
    private String brakeABS;
    private String brakeSystem;
    private String cylinderHeadType;
    private String engineBase;
    private String brandId;
    private String itemCondition;
    private List<String> geospottag;
    private List<String> storeUnit;
    private String regPrice;
    private String priceRangeInd;
    private String status;
    private String availDate;
    private String search1;
    private String search2;
    private String search3;
    private String search4;
    private String search5;
    private String search6;
    private String search7;
    private String search8;
    private String search9;
    private String search10;
    private List<String> clickUrl;
    private String saveStory;
    private int newItemFlag;
    private int daysOnline;
    private String shippingMsg;
    private int countryGroupExists;
    private List<String> fulfillment;
    private String l4Item;
    private String nameSortable;
    private String cylinderType;
    private String xpromo;
    private String xpromoBogo;
    private String rank;
    private List<String> resStoreUnit;
    private List<String> pickUpItemStores;
    private List<String> resItemStores;
    private List<String> offerAttr;
    private List<String> staticAttributes;
    private String discontinued;
    private List<String> memberSet;
    private List<String> ksn;
    private long impressions;
    private String browseBoostImpr;
    private String consumerReportsRated;
    private List<String> consumerReportRatingContextual;
    private List<String> fitment;
    private List<String> showroomUnit;
    private String localAd;
    private List<String> sellerFP;
    private List<String> sellerSFId;
    private String opsUuid_s;
    private String opsCurrentServer_i;
    private boolean automotive;
    private String level1_dis;
    private String level2_dis;
    private String level3_dis;
    private String level4_dis;
    private String level5_dis;
    private String level6_dis;
    private String level7_dis;
    private List<String> freeShipping;
    private List<OfferDoc> offerDocs;

    // MT-67871 - New fields added for Kmart MP project
    private Set<String> vNames_km;
    private Set<String> cNames_km;
    private Set<String> sNames_km;
    private Set<String> lnames_km;
    private Set<String> primaryLnames_km;
    private String primaryVertical_km;
    private List<String> behavioral_km;
    private String multifloat_km;
    private String browseBoost_km;
    private int daysOnline_km;
    private double numericRevenue_km;
    private String rank_km;
    private int newItemFlag_km;
    private List<Integer> new_km;
    private String level1_dis_km;
    private String level2_dis_km;
    private String level3_dis_km;
    private String level4_dis_km;
    private String level5_dis_km;
    private String level6_dis_km;
    private String level7_dis_km;

    //Topic modeling fields
    private List<String> topicIdWithRank;
    private String topicIdWithNameSearchable;
    private Set<String> parentIds;
    private List<String> LocalAdList;

    // MT-66358 indicator for PLP to know if the product has multiple item conditions for offers under it
    private boolean multipleConditions;

    private List<String> expirableFields;

    private String deliveryCat;
    private String subUpPid;
    private String subDownPid;
    private String reptPid;
    private String altPid;
    private String transRsn;
    private String truckLoadQty;
    private String ffmClass;
    private String discntDt;
    private String b2bName;
    private String b2bDescShort;
    private String b2bDescLong;
    private double scomPrice;
    private String scomInStock;
    private String colorFamily;
    private String width;
    private String height;
    private String depth;
    private String energyStarCompliant;
    private String adaCompliant;
    private String isCommercial;
    private String aggregatorId;


    public SearchDocBuilder(String partnumber) {
        this.partnumber = partnumber;
    }

    public SearchDocBuilder multipleConditions(boolean multipleConditions) {
        this.multipleConditions = multipleConditions;
        return this;
    }

    public SearchDocBuilder parentIds(Set<String> parentIds) {
        this.parentIds = parentIds;
        return this;
    }

    public SearchDocBuilder freeShipping(List<String> freeShipping) {
        this.freeShipping = freeShipping;
        return this;
    }

    public Set<String> getParentIds() {
        return parentIds;
    }

    public SearchDocBuilder alternateImage(List<String> alternateImage) {
        this.alternateImage = alternateImage;
        return this;
    }

    public SearchDocBuilder id(String id) {
        this.id = id;
        return this;
    }

    public SearchDocBuilder timestamp() {
        return this;
    }

    public SearchDocBuilder beanType(String beanType) {
        this.beanType = beanType;
        return this;
    }

    public SearchDocBuilder fullmfpartno(String fullMfPartNo) {
        this.fullmfpartno = fullMfPartNo;
        return this;
    }

    public SearchDocBuilder mfpartno(String mfPartNo) {
        this.mfpartno = mfPartNo;
        return this;
    }

    public SearchDocBuilder partnumber() {
        return this;
    }

    public SearchDocBuilder itemnumber(String itemNumber) {
        this.itemnumber = itemNumber;
        return this;
    }

    public SearchDocBuilder storeOrigin(List<String> storeOrigins) {
        this.storeOrigin = storeOrigins;
        return this;
    }

    public SearchDocBuilder storeOriginSearchable(List<String> storeOrigins) {
        this.storeOriginSearchable = storeOrigins;
        return this;
    }

    public SearchDocBuilder imageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
        return this;
    }

    public SearchDocBuilder swatches(String swatchStatus) {
        this.swatches = swatchStatus;
        return this;
    }

    public SearchDocBuilder swatchInfo(String swatchInfo) {
        this.swatchInfo = swatchInfo;

        return this;
    }

    public SearchDocBuilder xref(Set<String> xRef) {
        this.xref = xRef;
        return this;
    }

    public SearchDocBuilder promoId(Set<String> promoIds) {
        this.promoId = promoIds;
        return this;
    }

    public SearchDocBuilder eGiftEligible(String eGiftEligible) {
        this.eGiftEligible = eGiftEligible;
        return this;
    }

    public SearchDocBuilder sellerTierRank(int sellerTierRank) {
        this.sellerTierRank = sellerTierRank;
        return this;
    }

    public SearchDocBuilder name(String name) {
        this.name = name;
        return this;
    }

    public SearchDocBuilder nameSearchable(String nameSearchable) {
        this.nameSearchable = nameSearchable;
        return this;
    }

    public SearchDocBuilder image(String image) {
        this.image = image;
        return this;
    }

    public SearchDocBuilder description(String description) {
        this.description = description;
        return this;
    }

    public SearchDocBuilder promo(List<String> promoStatus) {
        this.promo = promoStatus;
        return this;
    }

    public SearchDocBuilder subcatAttributes(List<String> subcatAttributes) {
        this.subcatAttributes = subcatAttributes;
        return this;
    }

    public SearchDocBuilder productAttributes(List<String> productAttributes) {
        this.productAttributes = productAttributes;
        return this;
    }

    public SearchDocBuilder storeAttributes(List<String> storeAttributes) {
        this.storeAttributes = storeAttributes;
        return this;
    }

    public SearchDocBuilder searchableAttributes(List<String> searchableAttributes) {
        this.searchableAttributes = searchableAttributes;
        return this;
    }

    public SearchDocBuilder searchableAttributesSearchable(List<String> searchableAttributesSearchable) {
        this.searchableAttributesSearchable = searchableAttributesSearchable;
        return this;
    }

    public SearchDocBuilder price(List<String> priceList) {
        this.price = priceList;
        return this;
    }

    public SearchDocBuilder sale(List<String> saleList) {
        this.sale = saleList;
        return this;
    }

    public SearchDocBuilder clearance(List<String> clearanceList) {
        this.clearance = clearanceList;
        return this;
    }

    public SearchDocBuilder instock(List<String> instockList) {
        this.instock = instockList;
        return this;
    }

    public SearchDocBuilder newArrivals(List<Integer> newArrivals) {
        this.newArrivals = newArrivals;
        return this;
    }

    public SearchDocBuilder itemsSold(List<String> itemsSold) {
        this.itemsSold = itemsSold;
        return this;
    }

    public SearchDocBuilder revenue(List<String> revenue) {
        this.revenue = revenue;
        return this;
    }

    public SearchDocBuilder conversion(List<String> conversion) {
        this.conversion = conversion;
        return this;
    }

    public SearchDocBuilder productViews(List<String> productViews) {
        this.productViews = productViews;
        return this;
    }

    public SearchDocBuilder behavioral(List<String> behavioral) {
        this.behavioral = behavioral;
        return this;
    }

    public SearchDocBuilder multifloat() {
        return this;
    }

    public SearchDocBuilder sears_international(List<String> searsInternational) {
        this.sears_international = searsInternational;
        return this;
    }

    public SearchDocBuilder mobileReviews(String mobileReviews) {
        this.mobileReviews = mobileReviews;
        return this;
    }

    public SearchDocBuilder brand(String brand) {
        this.brand = brand;
        return this;
    }

    public SearchDocBuilder veh2prod() {
        return this;
    }

    public SearchDocBuilder catalogs(List<String> catalogs) {
        this.catalogs = catalogs;
        return this;
    }

    public SearchDocBuilder level1Cats(Set<String> level1Cats) {
        this.level1Cats = level1Cats;
        return this;
    }

    public SearchDocBuilder level2Cats(Set<String> level2Cats) {
        this.level2Cats = level2Cats;
        return this;
    }

    public SearchDocBuilder level3Cats(Set<String> level3Cats) {
        this.level3Cats = level3Cats;
        return this;
    }

    public SearchDocBuilder vNames(Set<String> vNames) {
        this.vNames = vNames;
        return this;
    }

    public SearchDocBuilder cNames(Set<String> cNames) {
        this.cNames = cNames;
        return this;
    }

    public SearchDocBuilder sNames(Set<String> sNames) {
        this.sNames = sNames;
        return this;
    }

    public SearchDocBuilder sin(String sin) {
        this.sin = sin;
        return this;
    }

    public SearchDocBuilder sellerCount(String sellerCount) {
        this.sellerCount = sellerCount;
        return this;
    }

    public SearchDocBuilder cpcFlag(String cpcFlag) {
        this.cpcFlag = cpcFlag;
        return this;
    }

    public SearchDocBuilder fbm(String fbmFlag) {
        this.fbm = fbmFlag;
        return this;
    }

    public SearchDocBuilder pickUpNowEligible() {
        return this;
    }

    public SearchDocBuilder vendorId(String vendorId) {
        this.vendorId = vendorId;
        return this;
    }

    public SearchDocBuilder sellers(List<String> sellers) {
        this.sellers = sellers;
        return this;
    }

    public SearchDocBuilder spuEligible(List<String> spuEligible) {
        this.spuEligible = spuEligible;
        return this;
    }

    public SearchDocBuilder layAway(int layAwayEligible) {
        this.layAway = layAwayEligible;
        return this;
    }

    public SearchDocBuilder mailInRebate(String mailInRebate) {
        this.mailInRebate = mailInRebate;
        return this;
    }

    public SearchDocBuilder shipVantage(List<String> shipVantage) {
        this.shipVantage = shipVantage;
        return this;
    }

    public SearchDocBuilder catConfidence(String catConfidence) {
        this.catConfidence = catConfidence;
        return this;
    }

    public SearchDocBuilder has991(String has991) {
        this.has991 = has991;
        return this;
    }

    public SearchDocBuilder trustedSeller(int trustedSellerFlag) {
        this.trustedSeller = trustedSellerFlag;
        return this;
    }

    public SearchDocBuilder international_shipping(List<String> internationalShipping) {
        this.international_shipping = internationalShipping;
        return this;
    }

    public SearchDocBuilder int_ship_eligible(Integer int_ship_eligible) {
        this.int_ship_eligible = int_ship_eligible;
        return this;
    }

    public SearchDocBuilder brandSearchable() {
        return this;
    }

    public SearchDocBuilder partnumberstr() {
        return this;
    }

    public SearchDocBuilder countryGroup(List<String> countryGroup) {
        this.countryGroup = countryGroup;
        return this;
    }

    public SearchDocBuilder browseBoost(String browseBoostScore) {
        this.browseBoost = browseBoostScore;
        return this;
    }

    public SearchDocBuilder instockShipping(String instockShipping) {
        this.instockShipping = instockShipping;
        return this;
    }

    public SearchDocBuilder matureContentFlag(int matureContentFlag) {
        this.matureContentFlag = matureContentFlag;
        return this;
    }

    public SearchDocBuilder primaryHierarchy(List<String> primaryHierarchy) {
        this.primaryHierarchy = primaryHierarchy;
        return this;
    }

    public SearchDocBuilder accessories(int accessoriesFlag) {
        this.accessories = accessoriesFlag;
        return this;
    }

    public SearchDocBuilder primaryVertical(String primaryVertical) {
        this.primaryVertical = primaryVertical;
        return this;
    }

    public SearchDocBuilder prdType(String prdType) {
        this.prdType = prdType;
        return this;
    }

    public SearchDocBuilder cpcClickRate(String cpcClickRate) {
        this.cpcClickRate = cpcClickRate;
        return this;
    }

    public SearchDocBuilder searchPhrase(String searchPhrase) {
        this.searchPhrase = searchPhrase;
        return this;
    }

    public SearchDocBuilder sellerId(String sellerId) {
        this.sellerId = sellerId;
        return this;
    }

    public SearchDocBuilder freeDelivery(List<String> freeDelivery) {
        this.freeDelivery = freeDelivery;
        return this;
    }

    public SearchDocBuilder promotionTxt(List<String> promotionTxt) {
        this.promotionTxt = promotionTxt;
        return this;
    }

    public SearchDocBuilder url(String url) {
        this.url = url;
        return this;
    }

    public SearchDocBuilder discount(List<String> discount) {
        this.discount = discount;
        return this;
    }

    public SearchDocBuilder delivery(List<String> delivery) {
        this.delivery = delivery;
        return this;
    }

    public SearchDocBuilder offer(List<String> offer) {
        this.offer = offer;
        return this;
    }

    public SearchDocBuilder buymore(List<String> buymore) {
        this.buymore = buymore;
        return this;
    }

    public SearchDocBuilder avgRatingContextual(List<String> avgRatingContextual) {
        this.avgRatingContextual = avgRatingContextual;
        return this;
    }

    public SearchDocBuilder numReviewsContextual(List<String> numReviewsContextual) {
        this.numReviewsContextual = numReviewsContextual;
        return this;
    }

    public SearchDocBuilder rebate(List<String> rebates) {
        this.rebate = rebates;
        return this;
    }

    public SearchDocBuilder rebateStatus(String rebateStatus) {
        this.rebateStatus = rebateStatus;
        return this;
    }

    public SearchDocBuilder asis() {
        return this;
    }

    public SearchDocBuilder upc(String upc) {
        this.upc = upc;
        return this;
    }

    public SearchDocBuilder storePickUp(List<String> storePickUp) {
        this.storePickUp = storePickUp;
        return this;
    }

    public SearchDocBuilder numericRevenue(double numericRevenue) {
        this.numericRevenue = numericRevenue;
        return this;
    }

    public SearchDocBuilder flashDeal(String flashDeal) {
        this.flashDeal = flashDeal;
        return this;
    }

    public SearchDocBuilder giftCardSequence(String giftCardSequence) {
        this.giftCardSequence = giftCardSequence;
        return this;
    }

    public SearchDocBuilder giftCardType(String giftCardType) {
        this.giftCardType = giftCardType;
        return this;
    }

    public SearchDocBuilder categories(Set<String> categories) {
        this.categories = categories;
        return this;
    }

    public SearchDocBuilder tagValues(List<String> tagValues) {
        this.tagValues = tagValues;
        return this;
    }

    public SearchDocBuilder category(List<String> category) {
        this.category = category;
        return this;
    }

    public SearchDocBuilder lnames(Set<String> lnames) {
        this.lnames = lnames;
        return this;
    }

    public SearchDocBuilder primaryLnames(Set<String> primaryLnames) {
        this.primaryLnames = primaryLnames;
        return this;
    }

    public SearchDocBuilder primaryCategory(List<String> primaryCategory) {
        this.primaryCategory = primaryCategory;
        return this;
    }

    public SearchDocBuilder catentryId(String catentryId) {
        this.catentryId = catentryId;
        return this;
    }

    public SearchDocBuilder programType(String programType) {
        this.programType = programType;
        return this;
    }

    public SearchDocBuilder sellerTier(String sellerTier) {
        this.sellerTier = sellerTier;
        return this;
    }

    public SearchDocBuilder division(String division) {
        this.division = division;
        return this;
    }

    public SearchDocBuilder partType() {
        return this;
    }

    public SearchDocBuilder partTypeName() {
        return this;
    }

    public SearchDocBuilder baseVehicle() {
        return this;
    }

    public SearchDocBuilder rearBrakeType() {
        return this;
    }

    public SearchDocBuilder frontBrakeType() {
        return this;
    }

    public SearchDocBuilder bodyType() {
        return this;
    }

    public SearchDocBuilder bodyNumDoors() {
        return this;
    }

    public SearchDocBuilder transmissionMfrCode() {
        return this;
    }

    public SearchDocBuilder transmissionType() {
        return this;
    }

    public SearchDocBuilder transmissionControlType() {
        return this;
    }

    public SearchDocBuilder transmissionNumSpeeds() {
        return this;
    }

    public SearchDocBuilder brakeABS() {
        return this;
    }

    public SearchDocBuilder brakeSystem() {
        return this;
    }

    public SearchDocBuilder cylinderHeadType() {
        return this;
    }

    public SearchDocBuilder engineBase() {
        return this;
    }

    public SearchDocBuilder brandId(String brandId) {
        this.brandId = brandId;
        return this;
    }

    public SearchDocBuilder itemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
        return this;
    }

    public SearchDocBuilder geospottag(List<String> geospottag) {
        this.geospottag = geospottag;
        return this;
    }
    
    public SearchDocBuilder expirableFields(List<String> expirableFields) {
        this.expirableFields = expirableFields;
        return this;
    }

    public final String getLevel1_dis_km() {
        return level1_dis_km;
    }

    public SearchDocBuilder level1_dis_km(String level1_dis_km) {
        this.level1_dis_km = level1_dis_km;
        return this;
    }

    public final String getLevel2_dis_km() {
        return level2_dis_km;
    }

    public SearchDocBuilder level2_dis_km(String level2_dis_km) {
        this.level2_dis_km = level2_dis_km;
        return this;
    }

    public final String getLevel3_dis_km() {
        return level3_dis_km;
    }

    public SearchDocBuilder level3_dis_km(String level3_dis_km) {
        this.level3_dis_km = level3_dis_km;
        return this;
    }

    public final String getLevel4_dis_km() {
        return level4_dis_km;
    }

    public SearchDocBuilder level4_dis_km(String level4_dis_km) {
        this.level4_dis_km = level4_dis_km;
        return this;
    }

    public final String getLevel5_dis_km() {
        return level5_dis_km;
    }

    public SearchDocBuilder level5_dis_km(String level5_dis_km) {
        this.level5_dis_km = level5_dis_km;
        return this;
    }

    public final String getLevel6_dis_km() {
        return level6_dis_km;
    }

    public SearchDocBuilder level6_dis_km(String level6_dis_km) {
        this.level6_dis_km = level6_dis_km;
        return this;
    }

    public final String getLevel7_dis_km() {
        return level7_dis_km;
    }

    public SearchDocBuilder level7_dis_km(String level7_dis_km) {
        this.level7_dis_km = level7_dis_km;
        return this;
    }

    public SearchDocBuilder new_km(List<Integer> new_km) {
        this.new_km = new_km;
        return this;
    }

    public SearchDocBuilder behavioral_km(List<String> behavioral_km) {
        this.behavioral_km = behavioral_km;
        return this;
    }

    public SearchDocBuilder multifloat_km() {
        return this;
    }

    public SearchDocBuilder vNames_km(Set<String> vNames_km) {
        this.vNames_km = vNames_km;
        return this;
    }

    public SearchDocBuilder cNames_km(Set<String> cNames_km) {
        this.cNames_km = cNames_km;
        return this;
    }

    public SearchDocBuilder sNames_km(Set<String> sNames_km) {
        this.sNames_km = sNames_km;
        return this;
    }

    public SearchDocBuilder browseBoost_km(String browseBoostScore_km) {
        this.browseBoost_km = browseBoostScore_km;
        return this;
    }

    public SearchDocBuilder primaryVertical_km(String primaryVertical_km) {
        this.primaryVertical_km = primaryVertical_km;
        return this;
    }

    public SearchDocBuilder numericRevenue_km(double numericRevenue_km) {
        this.numericRevenue_km = numericRevenue_km;
        return this;
    }

    public SearchDocBuilder lnames_km(Set<String> lnames_km) {
        this.lnames_km = lnames_km;
        return this;
    }

    public SearchDocBuilder primaryLnames_km(Set<String> primaryLnames_km) {
        this.primaryLnames_km = primaryLnames_km;
        return this;
    }

    public SearchDocBuilder newItemFlag_km(int newItemFlag_km) {
        this.newItemFlag_km = newItemFlag_km;
        return this;
    }

    public SearchDocBuilder daysOnline_km(int daysOnline_km) {
        this.daysOnline_km = daysOnline_km;
        return this;
    }

    public final List<Integer> getNew_km() {
        return new_km;
    }

    public final List<String> getBehavioral_km() {
        return behavioral_km;
    }

    public final String getMultifloat_km() {
        return multifloat_km;
    }

    public final Set<String> getvNames_km() {
        return vNames_km;
    }

    public final Set<String> getcNames_km() {
        return cNames_km;
    }

    public final Set<String> getsNames_km() {
        return sNames_km;
    }

    public final String getBrowseBoost_km() {
        return browseBoost_km;
    }

    public final double getNumericRevenue_km() {
        return numericRevenue_km;
    }

    public final Set<String> getLnames_km() {
        return lnames_km;
    }

    public final Set<String> getPrimaryLnames_km() {
        return primaryLnames_km;
    }

    public final String getPrimaryVertical_km() {
        return primaryVertical_km;
    }

    public final int getNewItemFlag_km() {
        return newItemFlag_km;
    }

    public final int getDaysOnline_km() {
        return daysOnline_km;
    }

    public SearchDocBuilder storeUnit(List<String> storeUnit) {
        this.storeUnit = storeUnit;
        return this;
    }

    public SearchDocBuilder regPrice(String regPrice) {
        this.regPrice = regPrice;
        return this;
    }

    public SearchDocBuilder priceRangeInd(String priceRangeInd) {
        this.priceRangeInd = priceRangeInd;
        return this;
    }

    public SearchDocBuilder status(String status) {
        this.status = status;
        return this;
    }

    public SearchDocBuilder availDate(String availDate) {
        this.availDate = availDate;
        return this;
    }

    public SearchDocBuilder search1(String search1) {
        this.search1 = search1;
        return this;
    }

    public SearchDocBuilder search2(String search2) {
        this.search2 = search2;
        return this;
    }

    public SearchDocBuilder search3(String search3) {
        this.search3 = search3;
        return this;
    }

    public SearchDocBuilder search4(String search4) {
        this.search4 = search4;
        return this;
    }

    public SearchDocBuilder search5(String search5) {
        this.search5 = search5;
        return this;
    }

    public SearchDocBuilder search6(String search6) {
        this.search6 = search6;
        return this;
    }

    public SearchDocBuilder search7(String search7) {
        this.search7 = search7;
        return this;
    }

    public SearchDocBuilder search8(String search8) {
        this.search8 = search8;
        return this;
    }

    public SearchDocBuilder search9(String search9) {
        this.search9 = search9;
        return this;
    }

    public SearchDocBuilder search10(String search10) {
        this.search10 = search10;
        return this;
    }

    public SearchDocBuilder offerDocs(List<OfferDoc> offerDocs) {
        this.offerDocs = offerDocs;
        return this;
    }

    public List<OfferDoc> getOfferDocs() {
        return offerDocs;
    }

    public final List<String> getStoreAttributes() {
        return storeAttributes;
    }

    public SearchDocBuilder clickUrl(List<String> clickUrl) {
        this.clickUrl = clickUrl;
        return this;
    }

    public SearchDocBuilder saveStory(String saveStory) {
        this.saveStory = saveStory;
        return this;
    }

    public SearchDocBuilder newItemFlag(int newItemFlag) {
        this.newItemFlag = newItemFlag;
        return this;
    }

    public SearchDocBuilder daysOnline(int daysOnline) {
        this.daysOnline = daysOnline;
        return this;
    }

    public SearchDocBuilder shippingMsg(String shippingMsg) {
        this.shippingMsg = shippingMsg;
        return this;
    }

    public SearchDocBuilder countryGroupExists(int countryGroupExists) {
        this.countryGroupExists = countryGroupExists;
        return this;
    }

    public SearchDocBuilder fulfillment(List<String> fulfillment) {
        this.fulfillment = fulfillment;
        return this;
    }

    public SearchDocBuilder l4Item() {
        return this;
    }

    public SearchDocBuilder nameSortable() {
        return this;
    }

    public SearchDocBuilder cylinderType() {
        return this;
    }

    public SearchDocBuilder xpromo() {
        return this;
    }

    public SearchDocBuilder xpromoBogo() {
        return this;
    }

    public SearchDocBuilder rank(String rank) {
        this.rank = rank;
        return this;
    }

    public SearchDocBuilder resStoreUnit(List<String> resStoreUnits) {
        this.resStoreUnit = resStoreUnits;
        return this;
    }

    public SearchDocBuilder pickUpItemStores(List<String> pickUpItemStores) {
        this.pickUpItemStores = pickUpItemStores;
        return this;
    }

    public SearchDocBuilder resItemStores(List<String> resItemStores) {
        this.resItemStores = resItemStores;
        return this;
    }

    public SearchDocBuilder offerAttr(List<String> offerAttr) {
        this.offerAttr = offerAttr;
        return this;
    }

    public SearchDocBuilder staticAttributes(List<String> staticAttributes) {
        this.staticAttributes = staticAttributes;
        return this;
    }

    public SearchDocBuilder discontinued(String discontinued) {
        this.discontinued = discontinued;
        return this;
    }

    public final String getPartnumber() {
        return partnumber;
    }

    public final String getTimestamp() {
        return timestamp;
    }

    public final String getBeanType() {
        return beanType;
    }

    public final String getFullmfpartno() {
        return fullmfpartno;
    }

    public final String getMfpartno() {
        return mfpartno;
    }

    public final String getItemnumber() {
        return itemnumber;
    }

    public final String getId() {
        return id;
    }

    public final List<String> getStoreOrigin() {
        return storeOrigin;
    }

    public final List<String> getStoreOriginSearchable() {
        return storeOriginSearchable;
    }

    public final String getImageStatus() {
        return imageStatus;
    }

    public final String getSwatches() {
        return swatches;
    }

    public final String getSwatchInfo() {
        return swatchInfo;
    }

    public final Set<String> getXref() {
        return xref;
    }

    public final Set<String> getPromoId() {
        return promoId;
    }

    public final String geteGiftEligible() {
        return eGiftEligible;
    }

    public final int getSellerTierRank() {
        return sellerTierRank;
    }

    public final String getName() {
        return name;
    }

    public final String getNameSearchable() {
        return nameSearchable;
    }

    public final String getImage() {
        return image;
    }

    public final List<String> getAlternateImage() {
        return alternateImage;
    }

    public final String getDescription() {
        return description;
    }

    public final List<String> getPromo() {
        return promo;
    }

    public final List<String> getSubcatAttributes() {
        return subcatAttributes;
    }

    public final List<String> getProductAttributes() {
        return productAttributes;
    }

    public final List<String> getPrice() {
        return price;
    }

    public final List<String> getSale() {
        return sale;
    }

    public final List<String> getClearance() {
        return clearance;
    }

    public final List<String> getInstock() {
        return instock;
    }

    public final List<Integer> getNewArrivals() {
        return newArrivals;
    }

    public final List<String> getItemsSold() {
        return itemsSold;
    }

    public final List<String> getRevenue() {
        return revenue;
    }

    public final List<String> getConversion() {
        return conversion;
    }

    public final List<String> getProductViews() {
        return productViews;
    }

    public final List<String> getBehavioral() {
        return behavioral;
    }

    public final String getMultifloat() {
        return multifloat;
    }

    public final List<String> getSears_international() {
        return sears_international;
    }

    public final String getMobileReviews() {
        return mobileReviews;
    }

    public final String getBrand() {
        return brand;
    }

    public final String getVeh2prod() {
        return veh2prod;
    }

    public final List<String> getCatalogs() {
        return catalogs;
    }

    public final Set<String> getLevel1Cats() {
        return level1Cats;
    }

    public final Set<String> getLevel2Cats() {
        return level2Cats;
    }

    public final Set<String> getLevel3Cats() {
        return level3Cats;
    }

    public final Set<String> getvNames() {
        return vNames;
    }

    public final Set<String> getcNames() {
        return cNames;
    }

    public final Set<String> getsNames() {
        return sNames;
    }

    public final String getSin() {
        return sin;
    }

    public final String getSellerCount() {
        return sellerCount;
    }

    public final String getCpcFlag() {
        return cpcFlag;
    }

    public final String getFbm() {
        return fbm;
    }

    public final String getPickUpNowEligible() {
        return pickUpNowEligible;
    }

    public final String getVendorId() {
        return vendorId;
    }

    public final List<String> getSellers() {
        return sellers;
    }

    public final List<String> getSpuEligible() {
        return spuEligible;
    }

    public final int getLayAway() {
        return layAway;
    }

    public final String getMailInRebate() {
        return mailInRebate;
    }

    public final List<String> getShipVantage() {
        if (shipVantage == null) {
            return new ArrayList<>();
        }
        return shipVantage;
    }

    public final String getCatConfidence() {
        return catConfidence;
    }

    public final String getHas991() {
        return has991;
    }

    public final int getTrustedSeller() {
        return trustedSeller;
    }

    public final List<String> getInternational_shipping() {
        return international_shipping;
    }

    public final Integer getInt_ship_eligible() {
        return int_ship_eligible;
    }

    public final String getBrandSearchable() {
        return brandSearchable;
    }

    public final String getPartnumberstr() {
        return partnumberstr;
    }

    public final List<String> getCountryGroup() {
        return countryGroup;
    }

    public final String getBrowseBoost() {
        return browseBoost;
    }

    public final String getInstockShipping() {
        return instockShipping;
    }

    public final int getMatureContentFlag() {
        return matureContentFlag;
    }

    public final int getAccessories() {
        return accessories;
    }

    public final String getPrdType() {
        return prdType;
    }

    public final String getCpcClickRate() {
        return cpcClickRate;
    }

    public final String getSearchPhrase() {
        return searchPhrase;
    }

    public final String getSellerId() {
        return sellerId;
    }

    public final List<String> getFreeDelivery() {
        if (freeDelivery == null) {
            return new ArrayList<>();
        }
        return freeDelivery;
    }

    public final List<String> getPromotionTxt() {
        if (promotionTxt == null) {
            return new ArrayList<>();
        }
        return promotionTxt;
    }

    public final String getUrl() {
        return url;
    }

    public final List<String> getDiscount() {
        return discount;
    }

    public final List<String> getDelivery() {
        return delivery;
    }

    public final List<String> getOffer() {
        return offer;
    }

    public final List<String> getBuymore() {
        return buymore;
    }

    public final List<String> getAvgRatingContextual() {
        return avgRatingContextual;
    }

    public final List<String> getNumReviewsContextual() {
        return numReviewsContextual;
    }

    public final List<String> getRebate() {
        return rebate;
    }

    public final String getRebateStatus() {
        return rebateStatus;
    }

    public final String getAsis() {
        return asis;
    }

    public final String getUpc() {
        return upc;
    }

    public final List<String> getStorePickUp() {
        return storePickUp;
    }

    public final double getNumericRevenue() {
        return numericRevenue;
    }

    public final String getFlashDeal() {
        return flashDeal;
    }

    public final String getGiftCardSequence() {
        return giftCardSequence;
    }

    public final String getGiftCardType() {
        return giftCardType;
    }

    public final Set<String> getCategories() {
        return categories;
    }

    public String getProductParentType() {
        return PARENTPRODUCTTYPE;
    }

    public final List<String> getSearchableAttributes() {
        return searchableAttributes;
    }

    public final List<String> getTagValues() {
        return tagValues;
    }

    public final List<String> getSearchableAttributesSearchable() {
        return searchableAttributesSearchable;
    }

    public final List<String> getCategory() {
        return category;
    }

    public final Set<String> getLnames() {
        return lnames;
    }

    public final Set<String> getPrimaryLnames() {
        return primaryLnames;
    }

    public final List<String> getPrimaryCategory() {
        return primaryCategory;
    }

    public final String getCatentryId() {
        return catentryId;
    }

    public final String getProgramType() {
        return programType;
    }

    public final String getPrimaryVertical() {
        return primaryVertical;
    }

    public final List<String> getPrimaryHierarchy() {
        return primaryHierarchy;
    }

    public final String getSellerTier() {
        return sellerTier;
    }

    public final String getDivision() {
        return division;
    }

    public final String getPartType() {
        return partType;
    }

    public final String getPartTypeName() {
        return partTypeName;
    }

    public final String getBaseVehicle() {
        return baseVehicle;
    }

    public final String getRearBrakeType() {
        return rearBrakeType;
    }

    public final String getFrontBrakeType() {
        return frontBrakeType;
    }

    public final String getBodyType() {
        return bodyType;
    }

    public final String getBodyNumDoors() {
        return bodyNumDoors;
    }

    public final String getTransmissionMfrCode() {
        return transmissionMfrCode;
    }

    public final String getTransmissionType() {
        return transmissionType;
    }

    public final String getTransmissionControlType() {
        return transmissionControlType;
    }

    public final String getTransmissionNumSpeeds() {
        return transmissionNumSpeeds;
    }

    public final String getBrakeABS() {
        return brakeABS;
    }

    public final String getBrakeSystem() {
        return brakeSystem;
    }

    public final String getCylinderHeadType() {
        return cylinderHeadType;
    }

    public final String getEngineBase() {
        return engineBase;
    }

    public final String getBrandId() {
        return brandId;
    }

    public final String getItemCondition() {
        return itemCondition;
    }

    public final List<String> getGeospottag() {
        return geospottag;
    }

    public final List<String> getStoreUnit() {
        return storeUnit;
    }

    public final String getRegPrice() {
        return regPrice;
    }

    public final String getPriceRangeInd() {
        return priceRangeInd;
    }

    public final String getStatus() {
        return status;
    }

    public final String getAvailDate() {
        return availDate;
    }

    public final String getSearch1() {
        return search1;
    }

    public final String getSearch2() {
        return search2;
    }

    public final String getSearch3() {
        return search3;
    }

    public final String getSearch4() {
        return search4;
    }

    public final String getSearch5() {
        return search5;
    }

    public final String getSearch6() {
        return search6;
    }

    public final String getSearch7() {
        return search7;
    }

    public final String getSearch8() {
        return search8;
    }

    public final String getSearch9() {
        return search9;
    }

    public final String getSearch10() {
        return search10;
    }

    public final List<String> getClickUrl() {
        return clickUrl;
    }

    public final String getSaveStory() {
        return saveStory;
    }

    public final int getNewItemFlag() {
        return newItemFlag;
    }

    public final int getDaysOnline() {
        return daysOnline;
    }

    public final String getShippingMsg() {
        return shippingMsg;
    }

    public final int getCountryGroupExists() {
        return countryGroupExists;
    }

    public final List<String> getFulfillment() {
        return fulfillment;
    }

    public final String getL4Item() {
        return l4Item;
    }

    public final String getNameSortable() {
        return nameSortable;
    }

    public final String getCylinderType() {
        return cylinderType;
    }

    public final String getXpromo() {
        return xpromo;
    }

    public final String getXpromoBogo() {
        return xpromoBogo;
    }

    public final String getRank() {
        return rank;
    }


    public final List<String> getResStoreUnit() {
        return resStoreUnit;
    }

    public final List<String> getPickUpItemStores() {
        return pickUpItemStores;
    }

    public final List<String> getResItemStores() {
        return resItemStores;
    }

    public final List<String> getOfferAttr() {
        return offerAttr;
    }

    public final List<String> getStaticAttributes() {
        return staticAttributes;
    }

    public final String getDiscontinued() {
        return discontinued;
    }

    public SearchDocBuilder memberSet(List<String> memberSet) {
        this.memberSet = memberSet;
        return this;
    }

    public final List<String> getMemberSet() {
        return memberSet;
    }

    public final List<String> getKsn() {
        return ksn;
    }

    public SearchDocBuilder ksn(List<String> ksn) {
        this.ksn = ksn;
        return this;
    }

    public final long getImpressions() {
        return impressions;
    }

    public SearchDocBuilder impressions(long impressions) {
        this.impressions = impressions;
        return this;
    }

    public String getBrowseBoostImpr() {
        return browseBoostImpr;
    }

    public SearchDocBuilder browseBoostImpr(String browseBoostImpr) {
        this.browseBoostImpr = browseBoostImpr;
        return this;
    }

    public final String getConsumerReportsRated() {
        return consumerReportsRated;
    }

    public SearchDocBuilder consumerReportsRated(String consumerReportsRated) {
        this.consumerReportsRated = consumerReportsRated;
        return this;
    }

    public final List<String> getConsumerReportRatingContextual() {
        return consumerReportRatingContextual;
    }

    public SearchDocBuilder consumerReportRatingContextual(
            List<String> consumerReportRatingContextual) {
        this.consumerReportRatingContextual = consumerReportRatingContextual;
        return this;
    }

    public final List<String> getFitment() {
        return fitment;
    }

    public SearchDocBuilder fitment(List<String> fitment) {
        this.fitment = fitment;
        return this;
    }

    public final List<String> getShowroomUnit() {
        return showroomUnit;
    }

    public SearchDocBuilder showroomUnit(List<String> showroomUnit) {
        this.showroomUnit = showroomUnit;
        return this;
    }


    public final String getLocalAd() {
        return localAd;
    }

    public SearchDocBuilder localAd(String localAd) {
        this.localAd = localAd;
        return this;
    }

    public final List<String> getLocalAdList() {
        return LocalAdList;
    }

    public SearchDocBuilder localAdList(List<String> localAdList) {
        this.LocalAdList = localAdList;
        return this;
    }

    public final List<String> getSellerFP() {
        return sellerFP;
    }

    public SearchDocBuilder sellerFP(List<String> sellerFP) {
        this.sellerFP = sellerFP;
        return this;
    }

    public final List<String> getSellerSFId() {
        return sellerSFId;
    }

    public SearchDocBuilder sellerSFId(List<String> sellerSFId) {
        this.sellerSFId = sellerSFId;
        return this;
    }

    public final String getOpsUuid_s() {
        return opsUuid_s;
    }

    public SearchDocBuilder opsUuid_s(String opsUuid_s) {
        this.opsUuid_s = opsUuid_s;
        return this;
    }

    public final String getOpsCurrentServer_i() {
        return opsCurrentServer_i;
    }

    public SearchDocBuilder opsCurrentServer_i(String opsCurrentServer_i) {
        this.opsCurrentServer_i = opsCurrentServer_i;
        return this;
    }

    public final boolean isAutomotive() {
        return automotive;
    }

    public SearchDocBuilder automotive(boolean automotive) {
        this.automotive = automotive;
        return this;
    }

    public final String getLevel1_dis() {
        return level1_dis;
    }

    public SearchDocBuilder level1_dis(String level1_dis) {
        this.level1_dis = level1_dis;
        return this;
    }

    public final String getLevel2_dis() {
        return level2_dis;
    }

    public SearchDocBuilder level2_dis(String level2_dis) {
        this.level2_dis = level2_dis;
        return this;
    }

    public final String getLevel3_dis() {
        return level3_dis;
    }

    public SearchDocBuilder level3_dis(String level3_dis) {
        this.level3_dis = level3_dis;
        return this;
    }

    public final String getLevel4_dis() {
        return level4_dis;
    }

    public SearchDocBuilder level4_dis(String level4_dis) {
        this.level4_dis = level4_dis;
        return this;
    }

    public final String getLevel5_dis() {
        return level5_dis;
    }

    public SearchDocBuilder level5_dis(String level5_dis) {
        this.level5_dis = level5_dis;
        return this;
    }

    public final String getLevel6_dis() {
        return level6_dis;
    }

    public SearchDocBuilder level6_dis(String level6_dis) {
        this.level6_dis = level6_dis;
        return this;
    }

    public final String getLevel7_dis() {
        return level7_dis;
    }

    public SearchDocBuilder level7_dis(String level7_dis) {
        this.level7_dis = level7_dis;
        return this;
    }

    public final String getRank_km() {
        return rank_km;
    }

    public List<String> getTopicIdWithRank() {
        return topicIdWithRank;
    }

    public SearchDocBuilder topicIdWithRank(List<String> topicIdWithRank) {
        this.topicIdWithRank = topicIdWithRank;
        return this;
    }

    public String getTopicIdWithNameSearchable() {
        return topicIdWithNameSearchable;
    }

    public SearchDocBuilder topicIdWithNameSearchable(String topicIdWithNameSearchable) {
        this.topicIdWithNameSearchable = topicIdWithNameSearchable;
        return this;
    }

    public SearchDocBuilder rank_km(String rank_km) {
        this.rank_km = rank_km;
        return this;
    }

    public List<String> getFreeShipping() {
        if (freeShipping == null){
            return new ArrayList<>();
        }
        return freeShipping;
    }

    public void setFreeShipping(List<String> freeShipping) {
        this.freeShipping = freeShipping;
    }

    public boolean isMultipleConditions() {
        return multipleConditions;
    }

    public void setMultipleConditions(boolean multipleConditions) {
        this.multipleConditions = multipleConditions;
    }

    public List<String> getExpirableFields() {
        if(expirableFields == null){
            return new ArrayList<>();
        }
        return expirableFields;
    }

    public SearchDocBuilder deliveryCat(String deliveryCat) {
        this.deliveryCat = deliveryCat;
        return this;
    }

    public SearchDocBuilder subUpPid(String subUpPid) {
        this.subUpPid = subUpPid;
        return this;
    }

    public SearchDocBuilder subDownPid(String subDownPid) {
        this.subDownPid = subDownPid;
        return this;
    }

    public SearchDocBuilder reptPid(String reptPid) {
        this.reptPid = reptPid;
        return this;
    }

    public SearchDocBuilder altPid(String altPid) {
        this.altPid = altPid;
        return this;
    }

    public SearchDocBuilder transRsn(String transRsn) {
        this.transRsn = transRsn;
        return this;
    }

    public SearchDocBuilder truckLoadQty(String truckLoadQty) {
        this.truckLoadQty = truckLoadQty;
        return this;
    }

    public SearchDocBuilder ffmClass(String ffmClass) {
        this.ffmClass = ffmClass;
        return this;
    }

    public SearchDocBuilder discntDt(String discntDt) {
        this.discntDt = discntDt;
        return this;
    }

    public SearchDocBuilder b2bName(String b2bName) {
        this.b2bName = b2bName;
        return this;
    }

    public SearchDocBuilder b2bDescShort(String b2bDescShort) {
        this.b2bDescShort = b2bDescShort;
        return this;
    }

    public SearchDocBuilder b2bDescLong(String b2bDescLong) {
        this.b2bDescLong = b2bDescLong;
        return this;
    }

    public SearchDocBuilder scomPrice(double scomPrice) {
        this.scomPrice = scomPrice;
        return this;
    }

    public SearchDocBuilder scomInStock(String scomInStock) {
        this.scomInStock = scomInStock;
        return this;
    }

    public SearchDocBuilder colorFamily(String colorFamily) {
        this.colorFamily = colorFamily;
        return this;
    }

    public SearchDocBuilder width(String width) {
        this.width = width;
        return this;
    }

    public SearchDocBuilder height(String height) {
        this.height = height;
        return this;
    }

    public SearchDocBuilder depth(String depth) {
        this.depth = depth;
        return this;
    }

    public SearchDocBuilder energyStarCompliant(String energyStarCompliant) {
        this.energyStarCompliant = energyStarCompliant;
        return this;
    }

    public SearchDocBuilder adaCompliant(String adaCompliant) {
        this.adaCompliant = adaCompliant;
        return this;
    }

    public SearchDocBuilder isCommercial(String isCommercial) {
        this.isCommercial = isCommercial;
        return this;
    }

    public SearchDocBuilder aggregatorId(String aggregatorId){
        this.aggregatorId = aggregatorId;
        return this;
    }

    public String getDeliveryCat() {
        return deliveryCat;
    }

    public String getSubUpPid() {
        return subUpPid;
    }

    public String getSubDownPid() {
        return subDownPid;
    }

    public String getReptPid() {
        return reptPid;
    }

    public String getAltPid() {
        return altPid;
    }

    public String getTransRsn() {
        return transRsn;
    }

    public String getTruckLoadQty() {
        return truckLoadQty;
    }

    public String getFfmClass() {
        return ffmClass;
    }

    public String getDiscntDt() {
        return discntDt;
    }

    public String getB2bName() {
        return b2bName;
    }

    public String getB2bDescShort() {
        return b2bDescShort;
    }

    public String getB2bDescLong() {
        return b2bDescLong;
    }

    public double getScomPrice() {
        return scomPrice;
    }

    public String getScomInStock() {
        return scomInStock;
    }

    public String getColorFamily() {
        return colorFamily;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getDepth() {
        return depth;
    }

    public String getEnergyStarCompliant() {
        return energyStarCompliant;
    }

    public String getAdaCompliant() {
        return adaCompliant;
    }

    public String isCommercial() {
        return isCommercial;
    }

    public String getaggregatorId(){return aggregatorId;}

    @Override
    public SearchDoc build(ContextMessage context) {
        // TODO: Validations

        return new SearchDoc(this);
    }

}
