package com.shc.ecom.search.docbuilder;

/**
 * Created by jsingar on 1/24/18.
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Created by jsingar on 1/22/18.
 */
@JsonInclude(Include.NON_NULL)
public class GroupByParentDoc {
    private  String catentryId;
    private  String imageStatus;
    private  String image;
    private  List<String> alternateImage;
    private  String brandId;
    private  String brand;
    private  String description;
    private  List<String> searchableAttributes;
    private  List<String> searchableAttributesSearchable;
    private  List<String> subcatAttributes;
    private  String id;
    private  String fullmfpartno;
    private  String mfpartno;
    private  String name;
    private String title;
    private  String url;
    private  Set<String> level1Cats;
    private  Set<String> vNames;
    private  String primaryVertical;
    private  Set<String> level2Cats;
    private  Set<String> cNames;
    private  Set<String> level3Cats;
    private  Set<String> sNames;
    private  List<String> primaryHierarchy;
    private  String accessories;
    private  Set<String> primaryLnames;
    private  List<String> primaryCategory;
    private  Set<String> categories;
    private Set<String> shcCategories;
    private  List<String> category;
    private  Set<String> lnames;
    private  String itemnumber;
    private  String partnumber;
    private  String itemCondition;
    private  String beantype;
    private  List<String> storeOrigin;
    private  String eGiftEligible;
    private  List<String> productAttributes;
    private  List<String> storeAttributes;
    private List<String> sears_international;
    private  List<Map<String,String>> gbiSears_international;
    private  String sin;
    private  int sellerCount;
    private  List<String> sellers;
    private  String layAway;
    private  String has991;
    private  List<String> international_shipping;
    private  int int_ship_eligible;
    private  String matureContentFlag;
    private  String upc;
    private  List<String> storePickUp;
    private  String flashDeal;
    private  String giftCardSequence;
    private  List<String> tagValues;
    private  String programType;
    private  String division;
    private  List<String> geospottag;
    private  List<String> clickUrl;
    private  List<String> staticAttributes;
    private  String discontinued;
    private List<String> price;
    private  List<Map<String,String>> gbiPrice;
    private  List<String> discount;
    private List<String> sale;
    private  List<Map<String,String>> gbiSale;
    private List<String> clearance;
    private  List<Map<String,String>> gbiClearance;
    private  String searchPhrase;
    private List<String> behavioral;
    private  List<Map<String,String>> gbiBehavioral;
    private  String sellerTierRank;
    private  String sellerTier;
    private  int trustedSeller;
    private  List<String> storeUnit;
    private  String sellerId;
    private List<String> offer;
    private  List<Map<String,String>> gbiOffer;
    private List<String> avgRatingContextual;
    private  List<Map<String,String>> gbiAvgRatingContextual;
    private List<String> numReviewsContextual;
    private  List<Map<String,String>> gbiNumReviewsContextual;
    private  String numericRevenue;
    private  String giftCardType;
    private  String newItemFlag;
    private  String daysOnline;
    private  List<String> pickUpItemStores;
    private  List<String> offerAttr;
    private  String swatches;
    private  String swatchInfo;
    private  Set<String> xref;
    private  Set<String> promoId;
    private  List<Map<String,String>> gbiPromoId;
    private  String nameSearchable;
    private List<String> promo;
    private  List<Map<String,String>> gbiPromo;
    private List<String> instock;
    private  List<Map<String,String>> gbiInstock;
    private  List<String> newArrivals; // previously named "new" has been renamed to "newArrivals"
    private List<String> itemsSold;
    private  List<Map<String,String>> gbiItemsSold;
    private List<String> revenue;
    private  List<Map<String,String>> gbiRevenue;
    private List<String> conversion;
    private  List<Map<String,String>> gbiConversion;
    private List<String> productViews;
    private  List<Map<String,String>> gbiProductViews;
    private  String mobileReviews;
    private  List<String> catalogs;
    private  int cpcFlag;
    private  int fbm;
    private List<String> spuEligible;
    private  List<Map<String,String>> gbiSpuEligible;
    private  String mailInRebate;
    private List<String> shipVantage;
    private  List<Map<String,String>> gbiShipVantage;
    private  String catConfidence;
    private  String browseBoost;
    private  List<String> countryGroup;
    private  int instockShipping;
    private  String prdType;
    private  String cpcClickRate;
    private List<String> freeDelivery;
    private  List<Map<String,String>> gbiFreeDelivery;
    private List<String> promotionTxt;
    private  List<Map<String,String>> gbiPromotionTxt;
    private  List<String> memberSet;
    private  List<String> ksn;
    private  String impressions;
    private  String browseBoostImpr;
    private  String consumerReportsRated;
    private List<String> consumerReportRatingContextual;
    private  List<Map<String,String>> gbiConsumerReportRatingContextual;
    private  List<String> fitment;
    private  List<String> showroomUnit;
    private  List<String> buymore;
    private List<String> delivery;
    private  List<Map<String,String>> gbiDelivery;
    private List<String> fulfillment;
    private  List<Map<String,String>> gbiFulfillment;
    private  List<String> rebate;
    private  List<String> resItemStores;
    private  List<String> resStoreUnit;
    private  String localAd;
    private  List<String> localAdList;
    private  String rank;
    private  String rebateStatus;
    private  List<String> sellerFP;
    private  List<String> sellerSFId;
    private  String opsUuid_s;
    private  String opsCurrentServer_i;
    private  boolean automotive;
    private  String level1_dis;
    private  String level2_dis;
    private  String level3_dis;
    private  String level4_dis;
    private  String level5_dis;
    private  String level6_dis;
    private  String level7_dis;
    private  String product_type_s;
    private  List<GroupByOfferDoc> _childDocuments_;
    private  List<Map<String, String>> gbiCategories;
    private  String level1_dis_km;
    private  String level2_dis_km;
    private  String level3_dis_km;
    private  String level4_dis_km;
    private  String level5_dis_km;
    private  String level6_dis_km;
    private  String level7_dis_km;
    private  Set<String> vNames_km;
    private  Set<String> cNames_km;
    private  Set<String> sNames_km;
    private  Set<String> lnames_km;
    private Set<String> primaryLnames_km;
    private String primaryVertical_km;
    private List<String> behavioral_km;
    private List<Map<String,String>> gbiBehavioral_km;
    private String browseBoost_km;
    private String daysOnline_km;
    private String numericRevenue_km;
    private String rank_km;
    private String newItemFlag_km;
    private List<String> new_km;
    //Topic modeling fields
    private List<String> topicIdWithRank;
    private String topicIdWithNameSearchable;
    private String topicIdWithNameSearchableAndDescription;
    private Set<String> parentIds;
    private String regPrice;
    private String shippingMsg;
    private String vendorId;
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
    private String saveStory;
    private String countryGroupExists;
    private List<String> freeShipping;
    private List<Map<String,String>> gbiFreeShipping;
    private boolean multipleConditions_b;
    private List<String> expirableFields;
    private List<Map<String,String>> gbiExpirableFields;
    private  String subUpPid;
    private  String subDownPid;
    private  String altPid;
    private  String reptPid;
    private  String transRsn;
    private  String truckLoadQty;
    private  String delivCat;
    private  String ffmClass;
    private  String discntDt;
    private  String b2bName;
    private  String b2bDescShort;
    private  String b2bDescLong;
    private  String scomPrice;
    private  String scomInStock;
    private  String colorFamily;
    private  String width;
    private  String height;
    private  String depth;
    private  String energyStarCompliant;
    private  String adaCompliant;
    private List<Map<String,String>> filters;

    // added by ajunaga to combine fields for qt
    private List<String> genericPartNumber;

    public List<String> getGenericPartNumber() {
        return genericPartNumber;
    }

    public void setGenericPartNumber(List<String> genericPartNumber) {
        this.genericPartNumber = genericPartNumber;
    }

    // combining partnumber and ssin for GBY deletes
    private List<String> gbiPartNumberOrSSIN;

    public List<String> getGbiPartNumberOrSSIN() {
        return gbiPartNumberOrSSIN;
    }

    public void setGbiPartNumberOrSSIN(List<String> gbiPartNumberOrSSIN) {
        this.gbiPartNumberOrSSIN = gbiPartNumberOrSSIN;
    }

    public List<Map<String, String>> getFilters() {
        return filters;
    }

    public void setFilters(List<Map<String, String>> filters) {
        this.filters = filters;
    }

    public String getCatentryId() {
        return catentryId;
    }

    public void setCatentryId(String catentryId) {
        this.catentryId = catentryId;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getAlternateImage() {
        return alternateImage;
    }

    public void setAlternateImage(List<String> alternateImage) {
        this.alternateImage = alternateImage;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSearchableAttributes() {
        return searchableAttributes;
    }

    public void setSearchableAttributes(List<String> searchableAttributes) {
        this.searchableAttributes = searchableAttributes;
    }

    public List<String> getSearchableAttributesSearchable() {
        return searchableAttributesSearchable;
    }

    public void setSearchableAttributesSearchable(List<String> searchableAttributesSearchable) {
        this.searchableAttributesSearchable = searchableAttributesSearchable;
    }

    public List<String> getSubcatAttributes() {
        return subcatAttributes;
    }

    public void setSubcatAttributes(List<String> subcatAttributes) {
        this.subcatAttributes = subcatAttributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullmfpartno() {
        return fullmfpartno;
    }

    public void setFullmfpartno(String fullmfpartno) {
        this.fullmfpartno = fullmfpartno;
    }

    public String getMfpartno() {
        return mfpartno;
    }

    public void setMfpartno(String mfpartno) {
        this.mfpartno = mfpartno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<String> getLevel1Cats() {
        return level1Cats;
    }

    public void setLevel1Cats(Set<String> level1Cats) {
        this.level1Cats = level1Cats;
    }

    public Set<String> getvNames() {
        return vNames;
    }

    public void setvNames(Set<String> vNames) {
        this.vNames = vNames;
    }

    public String getPrimaryVertical() {
        return primaryVertical;
    }

    public void setPrimaryVertical(String primaryVertical) {
        this.primaryVertical = primaryVertical;
    }

    public Set<String> getLevel2Cats() {
        return level2Cats;
    }

    public void setLevel2Cats(Set<String> level2Cats) {
        this.level2Cats = level2Cats;
    }

    public Set<String> getcNames() {
        return cNames;
    }

    public void setcNames(Set<String> cNames) {
        this.cNames = cNames;
    }

    public Set<String> getLevel3Cats() {
        return level3Cats;
    }

    public void setLevel3Cats(Set<String> level3Cats) {
        this.level3Cats = level3Cats;
    }

    public Set<String> getsNames() {
        return sNames;
    }

    public void setsNames(Set<String> sNames) {
        this.sNames = sNames;
    }

    public List<String> getPrimaryHierarchy() {
        return primaryHierarchy;
    }

    public void setPrimaryHierarchy(List<String> primaryHierarchy) {
        this.primaryHierarchy = primaryHierarchy;
    }

    public String getAccessories() {
        return accessories;
    }

    public void setAccessories(String accessories) {
        this.accessories = accessories;
    }

    public Set<String> getPrimaryLnames() {
        return primaryLnames;
    }

    public void setPrimaryLnames(Set<String> primaryLnames) {
        this.primaryLnames = primaryLnames;
    }

    public List<String> getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(List<String> primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public Set<String> getSHCCategories() {
        return shcCategories;
    }

    @JsonProperty("shcCategories")
    public void setSHCCategories(Set<String> SHCCategories) {
        this.shcCategories = SHCCategories;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public Set<String> getLnames() {
        return lnames;
    }

    public void setLnames(Set<String> lnames) {
        this.lnames = lnames;
    }

    public String getItemnumber() {
        return itemnumber;
    }

    public void setItemnumber(String itemnumber) {
        this.itemnumber = itemnumber;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public void setPartnumber(String partnumber) {
        this.partnumber = partnumber;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getBeantype() {
        return beantype;
    }

    public void setBeantype(String beantype) {
        this.beantype = beantype;
    }

    public List<String> getStoreOrigin() {
        return storeOrigin;
    }

    public void setStoreOrigin(List<String> storeOrigin) {
        this.storeOrigin = storeOrigin;
    }

    public String geteGiftEligible() {
        return eGiftEligible;
    }

    public void seteGiftEligible(String eGiftEligible) {
        this.eGiftEligible = eGiftEligible;
    }

    public List<String> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(List<String> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public List<String> getStoreAttributes() {
        return storeAttributes;
    }

    public void setStoreAttributes(List<String> storeAttributes) {
        this.storeAttributes = storeAttributes;
    }

    public List<String> getSears_international() {
        return sears_international;
    }

    public void setSears_international(List<String> sears_international) {
        this.sears_international = sears_international;
    }

    public List<Map<String,String>> getGbiSears_international() {
        return gbiSears_international;
    }

    public void setGbiSears_international(List<Map<String,String>> gbiSears_international) {
        this.gbiSears_international = gbiSears_international;
    }

    public String getSin() {
        return sin;
    }

    public void setSin(String sin) {
        this.sin = sin;
    }

    public int getSellerCount() {
        return sellerCount;
    }

    public void setSellerCount(int sellerCount) {
        this.sellerCount = sellerCount;
    }

    public List<String> getSellers() {
        return sellers;
    }

    public void setSellers(List<String> sellers) {
        this.sellers = sellers;
    }

    public String getLayAway() {
        return layAway;
    }

    public void setLayAway(String layAway) {
        this.layAway = layAway;
    }

    public String getHas991() {
        return has991;
    }

    public void setHas991(String has991) {
        this.has991 = has991;
    }

    public List<String> getInternational_shipping() {
        return international_shipping;
    }

    public void setInternational_shipping(List<String> international_shipping) {
        this.international_shipping = international_shipping;
    }

    public int getInt_ship_eligible() {
        return int_ship_eligible;
    }

    public void setInt_ship_eligible(int int_ship_eligible) {
        this.int_ship_eligible = int_ship_eligible;
    }

    public String getMatureContentFlag() {
        return matureContentFlag;
    }

    public void setMatureContentFlag(String matureContentFlag) {
        this.matureContentFlag = matureContentFlag;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public List<String> getStorePickUp() {
        return storePickUp;
    }

    public void setStorePickUp(List<String> storePickUp) {
        this.storePickUp = storePickUp;
    }

    public String getFlashDeal() {
        return flashDeal;
    }

    public void setFlashDeal(String flashDeal) {
        this.flashDeal = flashDeal;
    }

    public String getGiftCardSequence() {
        return giftCardSequence;
    }

    public void setGiftCardSequence(String giftCardSequence) {
        this.giftCardSequence = giftCardSequence;
    }

    public List<String> getTagValues() {
        return tagValues;
    }

    public void setTagValues(List<String> tagValues) {
        this.tagValues = tagValues;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public List<String> getGeospottag() {
        return geospottag;
    }

    public void setGeospottag(List<String> geospottag) {
        this.geospottag = geospottag;
    }

    public List<String> getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(List<String> clickUrl) {
        this.clickUrl = clickUrl;
    }

    public List<String> getStaticAttributes() {
        return staticAttributes;
    }

    public void setStaticAttributes(List<String> staticAttributes) {
        this.staticAttributes = staticAttributes;
    }

    public String getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(String discontinued) {
        this.discontinued = discontinued;
    }

    public List<String> getPrice() {
        return price;
    }

    public void setPrice(List<String> price) {
        this.price = price;
    }

    public List<Map<String,String>> getGbiPrice() {
        return gbiPrice;
    }

    public void setGbiPrice(List<Map<String,String>> gbiPrice) {
        this.gbiPrice = gbiPrice;
    }

    public List<String> getDiscount() {
        return discount;
    }

    public void setDiscount(List<String> discount) {
        this.discount = discount;
    }

    public List<String> getSale() {
        return sale;
    }

    public void setSale(List<String> sale) {
        this.sale = sale;
    }

    public List<Map<String,String>> getGbiSale() {
        return gbiSale;
    }

    public void setGbiSale(List<Map<String,String>> gbiSale) {
        this.gbiSale = gbiSale;
    }

    public List<String> getClearance() {
        return clearance;
    }

    public void setClearance(List<String> clearance) {
        this.clearance = clearance;
    }

    public List<Map<String,String>> getGbiClearance() {
        return gbiClearance;
    }

    public void setGbiClearance(List<Map<String,String>> gbiClearance) {
        this.gbiClearance = gbiClearance;
    }

    public String getSearchPhrase() {
        return searchPhrase;
    }

    public void setSearchPhrase(String searchPhrase) {
        this.searchPhrase = searchPhrase;
    }

    public List<String> getBehavioral() {
        return behavioral;
    }

    public void setBehavioral(List<String> behavioral) {
        this.behavioral = behavioral;
    }

    public List<Map<String,String>> getGbiBehavioral() {
        return gbiBehavioral;
    }

    public void setGbiBehavioral(List<Map<String,String>> gbiBehavioral) {
        this.gbiBehavioral = gbiBehavioral;
    }

    public String getSellerTierRank() {
        return sellerTierRank;
    }

    public void setSellerTierRank(String sellerTierRank) {
        this.sellerTierRank = sellerTierRank;
    }

    public String getSellerTier() {
        return sellerTier;
    }

    public void setSellerTier(String sellerTier) {
        this.sellerTier = sellerTier;
    }

    public int getTrustedSeller() {
        return trustedSeller;
    }

    public void setTrustedSeller(int trustedSeller) {
        this.trustedSeller = trustedSeller;
    }

    public List<String> getStoreUnit() {
        return storeUnit;
    }

    public void setStoreUnit(List<String> storeUnit) {
        this.storeUnit = storeUnit;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public List<String> getOffer() {
        return offer;
    }

    public void setOffer(List<String> offer) {
        this.offer = offer;
    }

    public List<Map<String,String>> getGbiOffer() {
        return gbiOffer;
    }

    public void setGbiOffer(List<Map<String,String>> gbiOffer) {
        this.gbiOffer = gbiOffer;
    }

    public List<String> getAvgRatingContextual() {
        return avgRatingContextual;
    }

    public void setAvgRatingContextual(List<String> avgRatingContextual) {
        this.avgRatingContextual = avgRatingContextual;
    }

    public List<Map<String,String>> getGbiAvgRatingContextual() {
        return gbiAvgRatingContextual;
    }

    public void setGbiAvgRatingContextual(List<Map<String,String>> gbiAvgRatingContextual) {
        this.gbiAvgRatingContextual = gbiAvgRatingContextual;
    }

    public List<String> getNumReviewsContextual() {
        return numReviewsContextual;
    }

    public void setNumReviewsContextual(List<String> numReviewsContextual) {
        this.numReviewsContextual = numReviewsContextual;
    }

    public List<Map<String,String>> getGbiNumReviewsContextual() {
        return gbiNumReviewsContextual;
    }

    public void setGbiNumReviewsContextual(List<Map<String,String>> gbiNumReviewsContextual) {
        this.gbiNumReviewsContextual = gbiNumReviewsContextual;
    }

    public String getNumericRevenue() {
        return numericRevenue;
    }

    public void setNumericRevenue(String numericRevenue) {
        this.numericRevenue = numericRevenue;
    }

    public String getGiftCardType() {
        return giftCardType;
    }

    public void setGiftCardType(String giftCardType) {
        this.giftCardType = giftCardType;
    }

    public String getNewItemFlag() {
        return newItemFlag;
    }

    public void setNewItemFlag(String newItemFlag) {
        this.newItemFlag = newItemFlag;
    }

    public String getDaysOnline() {
        return daysOnline;
    }

    public void setDaysOnline(String daysOnline) {
        this.daysOnline = daysOnline;
    }

    public List<String> getPickUpItemStores() {
        return pickUpItemStores;
    }

    public void setPickUpItemStores(List<String> pickUpItemStores) {
        this.pickUpItemStores = pickUpItemStores;
    }

    public List<String> getOfferAttr() {
        return offerAttr;
    }

    public void setOfferAttr(List<String> offerAttr) {
        this.offerAttr = offerAttr;
    }

    public String getSwatches() {
        return swatches;
    }

    public void setSwatches(String swatches) {
        this.swatches = swatches;
    }

    public String getSwatchInfo() {
        return swatchInfo;
    }

    public void setSwatchInfo(String swatchInfo) {
        this.swatchInfo = swatchInfo;
    }

    public Set<String> getXref() {
        return xref;
    }

    public void setXref(Set<String> xref) {
        this.xref = xref;
    }

    public Set<String> getPromoId() {
        return promoId;
    }

    public void setPromoId(Set<String> promoId) {
        this.promoId = promoId;
    }

    public List<Map<String,String>> getGbiPromoId() {
        return gbiPromoId;
    }

    public void setGbiPromoId(List<Map<String,String>> gbiPromoId) {
        this.gbiPromoId = gbiPromoId;
    }

    public String getNameSearchable() {
        return nameSearchable;
    }

    public void setNameSearchable(String nameSearchable) {
        this.nameSearchable = nameSearchable;
    }

    public List<String> getPromo() {
        return promo;
    }

    public void setPromo(List<String> promo) {
        this.promo = promo;
    }

    public List<Map<String,String>> getGbiPromo() {
        return gbiPromo;
    }

    public void setGbiPromo(List<Map<String,String>> gbiPromo) {
        this.gbiPromo = gbiPromo;
    }

    public List<String> getInstock() {
        return instock;
    }

    public void setInstock(List<String> instock) {
        this.instock = instock;
    }

    public List<Map<String,String>> getGbiInstock() {
        return gbiInstock;
    }

    public void setGbiInstock(List<Map<String,String>> gbiInstock) {
        this.gbiInstock = gbiInstock;
    }

    public List<String> getNewArrivals() {
        return newArrivals;
    }

    public void setNewArrivals(List<String> newArrivals) {
        this.newArrivals = newArrivals;
    }

    public List<String> getItemsSold() {
        return itemsSold;
    }

    public void setItemsSold(List<String> itemsSold) {
        this.itemsSold = itemsSold;
    }

    public List<Map<String,String>> getGbiItemsSold() {
        return gbiItemsSold;
    }

    public void setGbiItemsSold(List<Map<String,String>> gbiItemsSold) {
        this.gbiItemsSold = gbiItemsSold;
    }

    public List<String> getRevenue() {
        return revenue;
    }

    public void setRevenue(List<String> revenue) {
        this.revenue = revenue;
    }

    public List<Map<String,String>> getGbiRevenue() {
        return gbiRevenue;
    }

    public void setGbiRevenue(List<Map<String,String>> gbiRevenue) {
        this.gbiRevenue = gbiRevenue;
    }

    public List<String> getConversion() {
        return conversion;
    }

    public void setConversion(List<String> conversion) {
        this.conversion = conversion;
    }

    public List<Map<String,String>> getGbiConversion() {
        return gbiConversion;
    }

    public void setGbiConversion(List<Map<String,String>> gbiConversion) {
        this.gbiConversion = gbiConversion;
    }

    public List<String> getProductViews() {
        return productViews;
    }

    public void setProductViews(List<String> productViews) {
        this.productViews = productViews;
    }

    public List<Map<String,String>> getGbiProductViews() {
        return gbiProductViews;
    }

    public void setGbiProductViews(List<Map<String,String>> gbiProductViews) {
        this.gbiProductViews = gbiProductViews;
    }

    public String getMobileReviews() {
        return mobileReviews;
    }

    public void setMobileReviews(String mobileReviews) {
        this.mobileReviews = mobileReviews;
    }

    public List<String> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<String> catalogs) {
        this.catalogs = catalogs;
    }

    public int getCpcFlag() {
        return cpcFlag;
    }

    public void setCpcFlag(int cpcFlag) {
        this.cpcFlag = cpcFlag;
    }

    public int getFbm() {
        return fbm;
    }

    public void setFbm(int fbm) {
        this.fbm = fbm;
    }

    public List<String> getSpuEligible() {
        return spuEligible;
    }

    public void setSpuEligible(List<String> spuEligible) {
        this.spuEligible = spuEligible;
    }

    public List<Map<String,String>> getGbiSpuEligible() {
        return gbiSpuEligible;
    }

    public void setGbiSpuEligible(List<Map<String,String>> gbiSpuEligible) {
        this.gbiSpuEligible = gbiSpuEligible;
    }

    public String getMailInRebate() {
        return mailInRebate;
    }

    public void setMailInRebate(String mailInRebate) {
        this.mailInRebate = mailInRebate;
    }

    public List<String> getShipVantage() {
        return shipVantage;
    }

    public void setShipVantage(List<String> shipVantage) {
        this.shipVantage = shipVantage;
    }

    public List<Map<String,String>> getGbiShipVantage() {
        return gbiShipVantage;
    }

    public void setGbiShipVantage(List<Map<String,String>> gbiShipVantage) {
        this.gbiShipVantage = gbiShipVantage;
    }

    public String getCatConfidence() {
        return catConfidence;
    }

    public void setCatConfidence(String catConfidence) {
        this.catConfidence = catConfidence;
    }

    public String getBrowseBoost() {
        return browseBoost;
    }

    public void setBrowseBoost(String browseBoost) {
        this.browseBoost = browseBoost;
    }

    public List<String> getCountryGroup() {
        return countryGroup;
    }

    public void setCountryGroup(List<String> countryGroup) {
        this.countryGroup = countryGroup;
    }

    public int getInstockShipping() {
        return instockShipping;
    }

    public void setInstockShipping(int instockShipping) {
        this.instockShipping = instockShipping;
    }

    public String getPrdType() {
        return prdType;
    }

    public void setPrdType(String prdType) {
        this.prdType = prdType;
    }

    public String getCpcClickRate() {
        return cpcClickRate;
    }

    public void setCpcClickRate(String cpcClickRate) {
        this.cpcClickRate = cpcClickRate;
    }

    public List<String> getFreeDelivery() {
        return freeDelivery;
    }

    public void setFreeDelivery(List<String> freeDelivery) {
        this.freeDelivery = freeDelivery;
    }

    public List<Map<String,String>> getGbiFreeDelivery() {
        return gbiFreeDelivery;
    }

    public void setGbiFreeDelivery(List<Map<String,String>> gbiFreeDelivery) {
        this.gbiFreeDelivery = gbiFreeDelivery;
    }

    public List<String> getPromotionTxt() {
        return promotionTxt;
    }

    public void setPromotionTxt(List<String> promotionTxt) {
        this.promotionTxt = promotionTxt;
    }

    public List<Map<String,String>> getGbiPromotionTxt() {
        return gbiPromotionTxt;
    }

    public void setGbiPromotionTxt(List<Map<String,String>> gbiPromotionTxt) {
        this.gbiPromotionTxt = gbiPromotionTxt;
    }

    public List<String> getMemberSet() {
        return memberSet;
    }

    public void setMemberSet(List<String> memberSet) {
        this.memberSet = memberSet;
    }

    public List<String> getKsn() {
        return ksn;
    }

    public void setKsn(List<String> ksn) {
        this.ksn = ksn;
    }

    public String getImpressions() {
        return impressions;
    }

    public void setImpressions(String impressions) {
        this.impressions = impressions;
    }

    public String getBrowseBoostImpr() {
        return browseBoostImpr;
    }

    public void setBrowseBoostImpr(String browseBoostImpr) {
        this.browseBoostImpr = browseBoostImpr;
    }

    public String getConsumerReportsRated() {
        return consumerReportsRated;
    }

    public void setConsumerReportsRated(String consumerReportsRated) {
        this.consumerReportsRated = consumerReportsRated;
    }

    public List<String> getConsumerReportRatingContextual() {
        return consumerReportRatingContextual;
    }

    public void setConsumerReportRatingContextual(List<String> consumerReportRatingContextual) {
        this.consumerReportRatingContextual = consumerReportRatingContextual;
    }

    public List<Map<String,String>> getGbiConsumerReportRatingContextual() {
        return gbiConsumerReportRatingContextual;
    }

    public void setGbiConsumerReportRatingContextual(List<Map<String,String>> gbiConsumerReportRatingContextual) {
        this.gbiConsumerReportRatingContextual = gbiConsumerReportRatingContextual;
    }

    public List<String> getFitment() {
        return fitment;
    }

    public void setFitment(List<String> fitment) {
        this.fitment = fitment;
    }

    public List<String> getShowroomUnit() {
        return showroomUnit;
    }

    public void setShowroomUnit(List<String> showroomUnit) {
        this.showroomUnit = showroomUnit;
    }

    public List<String> getBuymore() {
        return buymore;
    }

    public void setBuymore(List<String> buymore) {
        this.buymore = buymore;
    }

    public List<String> getDelivery() {
        return delivery;
    }

    public void setDelivery(List<String> delivery) {
        this.delivery = delivery;
    }

    public List<Map<String,String>> getGbiDelivery() {
        return gbiDelivery;
    }

    public void setGbiDelivery(List<Map<String,String>> gbiDelivery) {
        this.gbiDelivery = gbiDelivery;
    }

    public List<String> getFulfillment() {
        return fulfillment;
    }

    public void setFulfillment(List<String> fulfillment) {
        this.fulfillment = fulfillment;
    }

    public List<Map<String,String>> getGbiFulfillment() {
        return gbiFulfillment;
    }

    public void setGbiFulfillment(List<Map<String,String>> gbiFulfillment) {
        this.gbiFulfillment = gbiFulfillment;
    }

    public List<String> getRebate() {
        return rebate;
    }

    public void setRebate(List<String> rebate) {
        this.rebate = rebate;
    }

    public List<String> getResItemStores() {
        return resItemStores;
    }

    public void setResItemStores(List<String> resItemStores) {
        this.resItemStores = resItemStores;
    }

    public List<String> getResStoreUnit() {
        return resStoreUnit;
    }

    public void setResStoreUnit(List<String> resStoreUnit) {
        this.resStoreUnit = resStoreUnit;
    }

    public String getLocalAd() {
        return localAd;
    }

    public void setLocalAd(String localAd) {
        this.localAd = localAd;
    }

    public List<String> getLocalAdList() {
        return localAdList;
    }

    public void setLocalAdList(List<String> localAdList) {
        this.localAdList = localAdList;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRebateStatus() {
        return rebateStatus;
    }

    public void setRebateStatus(String rebateStatus) {
        this.rebateStatus = rebateStatus;
    }

    public List<String> getSellerFP() {
        return sellerFP;
    }

    public void setSellerFP(List<String> sellerFP) {
        this.sellerFP = sellerFP;
    }

    public List<String> getSellerSFId() {
        return sellerSFId;
    }

    public void setSellerSFId(List<String> sellerSFId) {
        this.sellerSFId = sellerSFId;
    }

    public String getOpsUuid_s() {
        return opsUuid_s;
    }

    public void setOpsUuid_s(String opsUuid_s) {
        this.opsUuid_s = opsUuid_s;
    }

    public String getOpsCurrentServer_i() {
        return opsCurrentServer_i;
    }

    public void setOpsCurrentServer_i(String opsCurrentServer_i) {
        this.opsCurrentServer_i = opsCurrentServer_i;
    }

    public boolean isAutomotive() {
        return automotive;
    }

    public void setAutomotive(boolean automotive) {
        this.automotive = automotive;
    }

    public String getLevel1_dis() {
        return level1_dis;
    }

    public void setLevel1_dis(String level1_dis) {
        this.level1_dis = level1_dis;
    }

    public String getLevel2_dis() {
        return level2_dis;
    }

    public void setLevel2_dis(String level2_dis) {
        this.level2_dis = level2_dis;
    }

    public String getLevel3_dis() {
        return level3_dis;
    }

    public void setLevel3_dis(String level3_dis) {
        this.level3_dis = level3_dis;
    }

    public String getLevel4_dis() {
        return level4_dis;
    }

    public void setLevel4_dis(String level4_dis) {
        this.level4_dis = level4_dis;
    }

    public String getLevel5_dis() {
        return level5_dis;
    }

    public void setLevel5_dis(String level5_dis) {
        this.level5_dis = level5_dis;
    }

    public String getLevel6_dis() {
        return level6_dis;
    }

    public void setLevel6_dis(String level6_dis) {
        this.level6_dis = level6_dis;
    }

    public String getLevel7_dis() {
        return level7_dis;
    }

    public void setLevel7_dis(String level7_dis) {
        this.level7_dis = level7_dis;
    }

    public String getProduct_type_s() {
        return product_type_s;
    }

    public void setProduct_type_s(String product_type_s) {
        this.product_type_s = product_type_s;
    }

    public List<GroupByOfferDoc> get_childDocuments_() {
        return _childDocuments_;
    }

    public void set_childDocuments_(List<GroupByOfferDoc> _childDocuments_) {
        this._childDocuments_ = _childDocuments_;
    }

    public List<Map<String,String>> getGbiCategories() {
        return gbiCategories;
    }

    public void setGbiCategories(List<Map<String,String>> gbiCategories) {
        this.gbiCategories = gbiCategories;
    }

    public String getLevel1_dis_km() {
        return level1_dis_km;
    }

    public void setLevel1_dis_km(String level1_dis_km) {
        this.level1_dis_km = level1_dis_km;
    }

    public String getLevel2_dis_km() {
        return level2_dis_km;
    }

    public void setLevel2_dis_km(String level2_dis_km) {
        this.level2_dis_km = level2_dis_km;
    }

    public String getLevel3_dis_km() {
        return level3_dis_km;
    }

    public void setLevel3_dis_km(String level3_dis_km) {
        this.level3_dis_km = level3_dis_km;
    }

    public String getLevel4_dis_km() {
        return level4_dis_km;
    }

    public void setLevel4_dis_km(String level4_dis_km) {
        this.level4_dis_km = level4_dis_km;
    }

    public String getLevel5_dis_km() {
        return level5_dis_km;
    }

    public void setLevel5_dis_km(String level5_dis_km) {
        this.level5_dis_km = level5_dis_km;
    }

    public String getLevel6_dis_km() {
        return level6_dis_km;
    }

    public void setLevel6_dis_km(String level6_dis_km) {
        this.level6_dis_km = level6_dis_km;
    }

    public String getLevel7_dis_km() {
        return level7_dis_km;
    }

    public void setLevel7_dis_km(String level7_dis_km) {
        this.level7_dis_km = level7_dis_km;
    }

    public Set<String> getvNames_km() {
        return vNames_km;
    }

    public void setvNames_km(Set<String> vNames_km) {
        this.vNames_km = vNames_km;
    }

    public Set<String> getcNames_km() {
        return cNames_km;
    }

    public void setcNames_km(Set<String> cNames_km) {
        this.cNames_km = cNames_km;
    }

    public Set<String> getsNames_km() {
        return sNames_km;
    }

    public void setsNames_km(Set<String> sNames_km) {
        this.sNames_km = sNames_km;
    }

    public Set<String> getLnames_km() {
        return lnames_km;
    }

    public void setLnames_km(Set<String> lnames_km) {
        this.lnames_km = lnames_km;
    }

    public Set<String> getPrimaryLnames_km() {
        return primaryLnames_km;
    }

    public void setPrimaryLnames_km(Set<String> primaryLnames_km) {
        this.primaryLnames_km = primaryLnames_km;
    }

    public String getPrimaryVertical_km() {
        return primaryVertical_km;
    }

    public void setPrimaryVertical_km(String primaryVertical_km) {
        this.primaryVertical_km = primaryVertical_km;
    }

    public List<String> getBehavioral_km() {
        return behavioral_km;
    }

    public void setBehavioral_km(List<String> behavioral_km) {
        this.behavioral_km = behavioral_km;
    }

    public List<Map<String,String>> getGbiBehavioral_km() {
        return gbiBehavioral_km;
    }

    public void setGbiBehavioral_km(List<Map<String,String>> gbiBehavioral_km) {
        this.gbiBehavioral_km = gbiBehavioral_km;
    }

    public String getBrowseBoost_km() {
        return browseBoost_km;
    }

    public void setBrowseBoost_km(String browseBoost_km) {
        this.browseBoost_km = browseBoost_km;
    }

    public String getDaysOnline_km() {
        return daysOnline_km;
    }

    public void setDaysOnline_km(String daysOnline_km) {
        this.daysOnline_km = daysOnline_km;
    }

    public String getNumericRevenue_km() {
        return numericRevenue_km;
    }

    public void setNumericRevenue_km(String numericRevenue_km) {
        this.numericRevenue_km = numericRevenue_km;
    }

    public String getRank_km() {
        return rank_km;
    }

    public void setRank_km(String rank_km) {
        this.rank_km = rank_km;
    }

    public String getNewItemFlag_km() {
        return newItemFlag_km;
    }

    public void setNewItemFlag_km(String newItemFlag_km) {
        this.newItemFlag_km = newItemFlag_km;
    }

    public List<String> getNew_km() {
        return new_km;
    }

    public void setNew_km(List<String> new_km) {
        this.new_km = new_km;
    }

    public List<String> getTopicIdWithRank() {
        return topicIdWithRank;
    }

    public void setTopicIdWithRank(List<String> topicIdWithRank) {
        this.topicIdWithRank = topicIdWithRank;
    }

    public String getTopicIdWithNameSearchable() {
        return topicIdWithNameSearchable;
    }

    public void setTopicIdWithNameSearchable(String topicIdWithNameSearchable) {
        this.topicIdWithNameSearchable = topicIdWithNameSearchable;
    }

    public String getTopicIdWithNameSearchableAndDescription() {
        return topicIdWithNameSearchableAndDescription;
    }

    public void setTopicIdWithNameSearchableAndDescription(String topicIdWithNameSearchableAndDescription) {
        this.topicIdWithNameSearchableAndDescription = topicIdWithNameSearchableAndDescription;
    }

    public Set<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(Set<String> parentIds) {
        this.parentIds = parentIds;
    }

    public String getRegPrice() {
        return regPrice;
    }

    public void setRegPrice(String regPrice) {
        this.regPrice = regPrice;
    }

    public String getShippingMsg() {
        return shippingMsg;
    }

    public void setShippingMsg(String shippingMsg) {
        this.shippingMsg = shippingMsg;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getPriceRangeInd() {
        return priceRangeInd;
    }

    public void setPriceRangeInd(String priceRangeInd) {
        this.priceRangeInd = priceRangeInd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvailDate() {
        return availDate;
    }

    public void setAvailDate(String availDate) {
        this.availDate = availDate;
    }

    public String getSearch1() {
        return search1;
    }

    public void setSearch1(String search1) {
        this.search1 = search1;
    }

    public String getSearch2() {
        return search2;
    }

    public void setSearch2(String search2) {
        this.search2 = search2;
    }

    public String getSearch3() {
        return search3;
    }

    public void setSearch3(String search3) {
        this.search3 = search3;
    }

    public String getSearch4() {
        return search4;
    }

    public void setSearch4(String search4) {
        this.search4 = search4;
    }

    public String getSearch5() {
        return search5;
    }

    public void setSearch5(String search5) {
        this.search5 = search5;
    }

    public String getSearch6() {
        return search6;
    }

    public void setSearch6(String search6) {
        this.search6 = search6;
    }

    public String getSearch7() {
        return search7;
    }

    public void setSearch7(String search7) {
        this.search7 = search7;
    }

    public String getSearch8() {
        return search8;
    }

    public void setSearch8(String search8) {
        this.search8 = search8;
    }

    public String getSearch9() {
        return search9;
    }

    public void setSearch9(String search9) {
        this.search9 = search9;
    }

    public String getSearch10() {
        return search10;
    }

    public void setSearch10(String search10) {
        this.search10 = search10;
    }

    public String getSaveStory() {
        return saveStory;
    }

    public void setSaveStory(String saveStory) {
        this.saveStory = saveStory;
    }

    public String getCountryGroupExists() {
        return countryGroupExists;
    }

    public void setCountryGroupExists(String countryGroupExists) {
        this.countryGroupExists = countryGroupExists;
    }

    public List<String> getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(List<String> freeShipping) {
        this.freeShipping = freeShipping;
    }

    public List<Map<String,String>> getGbiFreeShipping() {
        return gbiFreeShipping;
    }

    public void setGbiFreeShipping(List<Map<String,String>> gbiFreeShipping) {
        this.gbiFreeShipping = gbiFreeShipping;
    }

    public boolean isMultipleConditions_b() {
        return multipleConditions_b;
    }

    public void setMultipleConditions_b(boolean multipleConditions_b) {
        this.multipleConditions_b = multipleConditions_b;
    }

    public List<String> getExpirableFields() {
        return expirableFields;
    }

    public void setExpirableFields(List<String> expirableFields) {
        this.expirableFields = expirableFields;
    }

    public List<Map<String,String>> getGbiExpirableFields() {
        return gbiExpirableFields;
    }

    public void setGbiExpirableFields(List<Map<String,String>> gbiExpirableFields) {
        this.gbiExpirableFields = gbiExpirableFields;
    }

    public String getSubUpPid() {
        return subUpPid;
    }

    public void setSubUpPid(String subUpPid) {
        this.subUpPid = subUpPid;
    }

    public String getSubDownPid() {
        return subDownPid;
    }

    public void setSubDownPid(String subDownPid) {
        this.subDownPid = subDownPid;
    }

    public String getAltPid() {
        return altPid;
    }

    public void setAltPid(String altPid) {
        this.altPid = altPid;
    }

    public String getReptPid() {
        return reptPid;
    }

    public void setReptPid(String reptPid) {
        this.reptPid = reptPid;
    }

    public String getTransRsn() {
        return transRsn;
    }

    public void setTransRsn(String transRsn) {
        this.transRsn = transRsn;
    }

    public String getTruckLoadQty() {
        return truckLoadQty;
    }

    public void setTruckLoadQty(String truckLoadQty) {
        this.truckLoadQty = truckLoadQty;
    }

    public String getDelivCat() {
        return delivCat;
    }

    public void setDelivCat(String delivCat) {
        this.delivCat = delivCat;
    }

    public String getFfmClass() {
        return ffmClass;
    }

    public void setFfmClass(String ffmClass) {
        this.ffmClass = ffmClass;
    }

    public String getDiscntDt() {
        return discntDt;
    }

    public void setDiscntDt(String discntDt) {
        this.discntDt = discntDt;
    }

    public String getB2bName() {
        return b2bName;
    }

    public void setB2bName(String b2bName) {
        this.b2bName = b2bName;
    }

    public String getB2bDescShort() {
        return b2bDescShort;
    }

    public void setB2bDescShort(String b2bDescShort) {
        this.b2bDescShort = b2bDescShort;
    }

    public String getB2bDescLong() {
        return b2bDescLong;
    }

    public void setB2bDescLong(String b2bDescLong) {
        this.b2bDescLong = b2bDescLong;
    }

    public String getScomPrice() {
        return scomPrice;
    }

    public void setScomPrice(String scomPrice) {
        this.scomPrice = scomPrice;
    }

    public String getScomInStock() {
        return scomInStock;
    }

    public void setScomInStock(String scomInStock) {
        this.scomInStock = scomInStock;
    }

    public String getColorFamily() {
        return colorFamily;
    }

    public void setColorFamily(String colorFamily) {
        this.colorFamily = colorFamily;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getEnergyStarCompliant() {
        return energyStarCompliant;
    }

    public void setEnergyStarCompliant(String energyStarCompliant) {
        this.energyStarCompliant = energyStarCompliant;
    }

    public String getAdaCompliant() {
        return adaCompliant;
    }

    public void setAdaCompliant(String adaCompliant) {
        this.adaCompliant = adaCompliant;
    }

    @Override
    public String toString() {
        return "GroupByParentDoc{" +
                "catentryId='" + catentryId + '\'' +
                ", imageStatus='" + imageStatus + '\'' +
                ", image='" + image + '\'' +
                ", alternateImage=" + alternateImage +
                ", brandId='" + brandId + '\'' +
                ", brand='" + brand + '\'' +
                ", description='" + description + '\'' +
                ", searchableAttributes=" + searchableAttributes +
                ", searchableAttributesSearchable=" + searchableAttributesSearchable +
                ", subcatAttributes=" + subcatAttributes +
                ", id='" + id + '\'' +
                ", fullmfpartno='" + fullmfpartno + '\'' +
                ", mfpartno='" + mfpartno + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", level1Cats=" + level1Cats +
                ", vNames=" + vNames +
                ", primaryVertical='" + primaryVertical + '\'' +
                ", level2Cats=" + level2Cats +
                ", cNames=" + cNames +
                ", level3Cats=" + level3Cats +
                ", sNames=" + sNames +
                ", primaryHierarchy=" + primaryHierarchy +
                ", accessories='" + accessories + '\'' +
                ", primaryLnames=" + primaryLnames +
                ", primaryCategory=" + primaryCategory +
                ", categories=" + categories +
                ", SHCCategories=" + shcCategories +
                ", category=" + category +
                ", lnames=" + lnames +
                ", itemnumber='" + itemnumber + '\'' +
                ", partnumber='" + partnumber + '\'' +
                ", itemCondition='" + itemCondition + '\'' +
                ", beantype='" + beantype + '\'' +
                ", storeOrigin=" + storeOrigin +
                ", eGiftEligible='" + eGiftEligible + '\'' +
                ", productAttributes=" + productAttributes +
                ", storeAttributes=" + storeAttributes +
                ", sears_international=" + sears_international +
                ", gbiSears_international=" + gbiSears_international +
                ", sin='" + sin + '\'' +
                ", sellerCount='" + sellerCount + '\'' +
                ", sellers=" + sellers +
                ", layAway='" + layAway + '\'' +
                ", has991='" + has991 + '\'' +
                ", international_shipping=" + international_shipping +
                ", int_ship_eligible='" + int_ship_eligible + '\'' +
                ", matureContentFlag='" + matureContentFlag + '\'' +
                ", upc='" + upc + '\'' +
                ", storePickUp=" + storePickUp +
                ", flashDeal='" + flashDeal + '\'' +
                ", giftCardSequence='" + giftCardSequence + '\'' +
                ", tagValues=" + tagValues +
                ", programType='" + programType + '\'' +
                ", division='" + division + '\'' +
                ", geospottag=" + geospottag +
                ", clickUrl=" + clickUrl +
                ", staticAttributes=" + staticAttributes +
                ", discontinued='" + discontinued + '\'' +
                ", price=" + price +
                ", gbiPrice=" + gbiPrice +
                ", discount=" + discount +
                ", sale=" + sale +
                ", gbiSale=" + gbiSale +
                ", clearance=" + clearance +
                ", gbiClearance=" + gbiClearance +
                ", searchPhrase='" + searchPhrase + '\'' +
                ", behavioral=" + behavioral +
                ", gbiBehavioral=" + gbiBehavioral +
                ", sellerTierRank='" + sellerTierRank + '\'' +
                ", sellerTier='" + sellerTier + '\'' +
                ", trustedSeller='" + trustedSeller + '\'' +
                ", storeUnit=" + storeUnit +
                ", sellerId='" + sellerId + '\'' +
                ", offer=" + offer +
                ", gbiOffer=" + gbiOffer +
                ", avgRatingContextual=" + avgRatingContextual +
                ", gbiAvgRatingContextual=" + gbiAvgRatingContextual +
                ", numReviewsContextual=" + numReviewsContextual +
                ", gbiNumReviewsContextual=" + gbiNumReviewsContextual +
                ", numericRevenue='" + numericRevenue + '\'' +
                ", giftCardType='" + giftCardType + '\'' +
                ", newItemFlag='" + newItemFlag + '\'' +
                ", daysOnline='" + daysOnline + '\'' +
                ", pickUpItemStores=" + pickUpItemStores +
                ", offerAttr=" + offerAttr +
                ", swatches='" + swatches + '\'' +
                ", swatchInfo='" + swatchInfo + '\'' +
                ", xref=" + xref +
                ", promoId=" + promoId +
                ", gbiPromoId=" + gbiPromoId +
                ", nameSearchable='" + nameSearchable + '\'' +
                ", promo=" + promo +
                ", gbiPromo=" + gbiPromo +
                ", instock=" + instock +
                ", gbiInstock=" + gbiInstock +
                ", newArrivals=" + newArrivals +
                ", itemsSold=" + itemsSold +
                ", gbiItemsSold=" + gbiItemsSold +
                ", revenue=" + revenue +
                ", gbiRevenue=" + gbiRevenue +
                ", conversion=" + conversion +
                ", gbiConversion=" + gbiConversion +
                ", productViews=" + productViews +
                ", gbiProductViews=" + gbiProductViews +
                ", mobileReviews='" + mobileReviews + '\'' +
                ", catalogs=" + catalogs +
                ", cpcFlag='" + cpcFlag + '\'' +
                ", fbm='" + fbm + '\'' +
                ", spuEligible='" + spuEligible + '\'' +
                ", gbiSpuEligible=" + gbiSpuEligible +
                ", mailInRebate='" + mailInRebate + '\'' +
                ", shipVantage=" + shipVantage +
                ", gbiShipVantage=" + gbiShipVantage +
                ", catConfidence='" + catConfidence + '\'' +
                ", browseBoost='" + browseBoost + '\'' +
                ", countryGroup=" + countryGroup +
                ", instockShipping='" + instockShipping + '\'' +
                ", prdType='" + prdType + '\'' +
                ", cpcClickRate='" + cpcClickRate + '\'' +
                ", freeDelivery=" + freeDelivery +
                ", gbiFreeDelivery=" + gbiFreeDelivery +
                ", promotionTxt=" + promotionTxt +
                ", gbiPromotionTxt=" + gbiPromotionTxt +
                ", memberSet=" + memberSet +
                ", ksn=" + ksn +
                ", impressions='" + impressions + '\'' +
                ", browseBoostImpr='" + browseBoostImpr + '\'' +
                ", consumerReportsRated='" + consumerReportsRated + '\'' +
                ", consumerReportRatingContextual=" + consumerReportRatingContextual +
                ", gbiConsumerReportRatingContextual=" + gbiConsumerReportRatingContextual +
                ", fitment=" + fitment +
                ", showroomUnit=" + showroomUnit +
                ", buymore=" + buymore +
                ", delivery=" + delivery +
                ", gbiDelivery=" + gbiDelivery +
                ", fulfillment=" + fulfillment +
                ", gbiFulfillment=" + gbiFulfillment +
                ", rebate=" + rebate +
                ", resItemStores=" + resItemStores +
                ", resStoreUnit=" + resStoreUnit +
                ", localAd='" + localAd + '\'' +
                ", localAdList=" + localAdList +
                ", rank='" + rank + '\'' +
                ", rebateStatus='" + rebateStatus + '\'' +
                ", sellerFP=" + sellerFP +
                ", sellerSFId=" + sellerSFId +
                ", opsUuid_s='" + opsUuid_s + '\'' +
                ", opsCurrentServer_i='" + opsCurrentServer_i + '\'' +
                ", automotive=" + automotive +
                ", level1_dis='" + level1_dis + '\'' +
                ", level2_dis='" + level2_dis + '\'' +
                ", level3_dis='" + level3_dis + '\'' +
                ", level4_dis='" + level4_dis + '\'' +
                ", level5_dis='" + level5_dis + '\'' +
                ", level6_dis='" + level6_dis + '\'' +
                ", level7_dis='" + level7_dis + '\'' +
                ", product_type_s='" + product_type_s + '\'' +
                ", _childDocuments_=" + _childDocuments_ +
                ", gbiCategories=" + gbiCategories +
                ", level1_dis_km='" + level1_dis_km + '\'' +
                ", level2_dis_km='" + level2_dis_km + '\'' +
                ", level3_dis_km='" + level3_dis_km + '\'' +
                ", level4_dis_km='" + level4_dis_km + '\'' +
                ", level5_dis_km='" + level5_dis_km + '\'' +
                ", level6_dis_km='" + level6_dis_km + '\'' +
                ", level7_dis_km='" + level7_dis_km + '\'' +
                ", vNames_km=" + vNames_km +
                ", cNames_km=" + cNames_km +
                ", sNames_km=" + sNames_km +
                ", lnames_km=" + lnames_km +
                ", primaryLnames_km=" + primaryLnames_km +
                ", primaryVertical_km='" + primaryVertical_km + '\'' +
                ", behavioral_km=" + behavioral_km +
                ", gbiBehavioral_km=" + gbiBehavioral_km +
                ", browseBoost_km='" + browseBoost_km + '\'' +
                ", daysOnline_km='" + daysOnline_km + '\'' +
                ", numericRevenue_km='" + numericRevenue_km + '\'' +
                ", rank_km='" + rank_km + '\'' +
                ", newItemFlag_km='" + newItemFlag_km + '\'' +
                ", new_km=" + new_km +
                ", topicIdWithRank=" + topicIdWithRank +
                ", topicIdWithNameSearchable='" + topicIdWithNameSearchable + '\'' +
                ", topicIdWithNameSearchableAndDescription='" + topicIdWithNameSearchableAndDescription + '\'' +
                ", parentIds=" + parentIds +
                ", regPrice='" + regPrice + '\'' +
                ", shippingMsg='" + shippingMsg + '\'' +
                ", vendorId='" + vendorId + '\'' +
                ", priceRangeInd='" + priceRangeInd + '\'' +
                ", status='" + status + '\'' +
                ", availDate='" + availDate + '\'' +
                ", search1='" + search1 + '\'' +
                ", search2='" + search2 + '\'' +
                ", search3='" + search3 + '\'' +
                ", search4='" + search4 + '\'' +
                ", search5='" + search5 + '\'' +
                ", search6='" + search6 + '\'' +
                ", search7='" + search7 + '\'' +
                ", search8='" + search8 + '\'' +
                ", search9='" + search9 + '\'' +
                ", search10='" + search10 + '\'' +
                ", saveStory='" + saveStory + '\'' +
                ", countryGroupExists='" + countryGroupExists + '\'' +
                ", freeShipping=" + freeShipping +
                ", gbiFreeShipping=" + gbiFreeShipping +
                ", multipleConditions_b=" + multipleConditions_b +
                ", expirableFields=" + expirableFields +
                ", gbiExpirableFields=" + gbiExpirableFields +
                ", subUpPid='" + subUpPid + '\'' +
                ", subDownPid='" + subDownPid + '\'' +
                ", altPid='" + altPid + '\'' +
                ", reptPid='" + reptPid + '\'' +
                ", transRsn='" + transRsn + '\'' +
                ", truckLoadQty='" + truckLoadQty + '\'' +
                ", delivCat='" + delivCat + '\'' +
                ", ffmClass='" + ffmClass + '\'' +
                ", discntDt='" + discntDt + '\'' +
                ", b2bName='" + b2bName + '\'' +
                ", b2bDescShort='" + b2bDescShort + '\'' +
                ", b2bDescLong='" + b2bDescLong + '\'' +
                ", scomPrice='" + scomPrice + '\'' +
                ", scomInStock='" + scomInStock + '\'' +
                ", colorFamily='" + colorFamily + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", depth='" + depth + '\'' +
                ", energyStarCompliant='" + energyStarCompliant + '\'' +
                ", adaCompliant='" + adaCompliant + '\'' +
                ", genericPartNumber='" + genericPartNumber + '\'' +
                ", gbiPartNumberOrSSIN='" + gbiPartNumberOrSSIN + '\'' +
                '}';
    }
}

