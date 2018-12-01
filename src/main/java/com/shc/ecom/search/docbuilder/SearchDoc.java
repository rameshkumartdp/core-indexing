package com.shc.ecom.search.docbuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shc.ecom.search.annotation.Child;
import org.apache.solr.client.solrj.beans.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author pchauha
 */
public class SearchDoc {

    @Field
    private final String catentryId;

    @Field
    private final String imageStatus;

    @Field
    private final String image;

    @Field
    private final List<String> alternateImage;

    @Field
    private final String brandId;

    @Field
    private final String brand;

    @Field
    private final String description;

    @Field
    private final List<String> searchableAttributes;

    @Field
    private final List<String> searchableAttributesSearchable;

    @Field
    private final List<String> subcatAttributes;

    @Field
    private final String id;

    @Field
    private final String fullmfpartno;

    @Field
    private final String mfpartno;

    @Field
    private final String name;

    @Field
    private final String url;

    @Field
    private final Set<String> level1Cats;

    @Field
    private final Set<String> vNames;

    @Field
    private final String primaryVertical;

    @Field
    private final Set<String> level2Cats;

    @Field
    private final Set<String> cNames;

    @Field
    private final Set<String> level3Cats;

    @Field
    private final Set<String> sNames;

    @Field
    private final List<String> primaryHierarchy;

    @Field
    private final int accessories;

    @Field
    private final Set<String> primaryLnames;

    @Field
    private final List<String> primaryCategory;

    @Field
    private final Set<String> categories;

    @Field
    private final List<String> category;

    @Field
    private final Set<String> lnames;

    @Field
    private final String itemnumber;

    @Field
    private final String partnumber;

    @Field
    private final String itemCondition;

    @Field
    private final String beantype;

    @Field
    private final List<String> storeOrigin;

    @Field
    private final String eGiftEligible;

    @Field
    private final List<String> productAttributes;

    @Field
    private final List<String> storeAttributes;

    @Field
    private final List<String> sears_international;

    @Field
    private final String sin;

    @Field
    private final String sellerCount;

    @Field
    private final List<String> sellers;

    @Field
    private final int layAway;

    @Field
    private final String has991;

    @Field
    private final List<String> international_shipping;

    @Field
    private final Integer int_ship_eligible;

    @Field
    private final int matureContentFlag;

    @Field
    private final String upc;

    @Field
    private final List<String> storePickUp;

    @Field
    private final String flashDeal;

    @Field
    private final String giftCardSequence;

    @Field
    private final List<String> tagValues;

    @Field
    private final String programType;

    @Field
    private final String division;

    @Field
    private final List<String> geospottag;

    @Field
    private final List<String> clickUrl;

    @Field
    private final List<String> staticAttributes;

    @Field
    private final String discontinued;

    @Field
    private final List<String> price;

    @Field
    private final List<String> discount;

    @Field
    private final List<String> sale;

    @Field
    private final List<String> clearance;

    @Field
    private final String searchPhrase;

    @Field
    private final List<String> behavioral;

    @Field
    private final int sellerTierRank;

    @Field
    private final String sellerTier;

    @Field
    private final int trustedSeller;

    @Field
    private final List<String> storeUnit;

    @Field
    private final String sellerId;

    @Field
    private final List<String> offer;

    @Field
    private final List<String> avgRatingContextual;

    @Field
    private final List<String> numReviewsContextual;

    @Field
    private final double numericRevenue;

    @Field
    private final String giftCardType;

    @Field
    private final int newItemFlag;

    @Field
    private final int daysOnline;

    @Field
    private final List<String> pickUpItemStores;

    @Field
    private final List<String> offerAttr;

    @Field
    private final String swatches;

    @Field
    private final String swatchInfo;

    @Field
    private final Set<String> xref;

    @Field
    private final Set<String> promoId;

    @Field
    private final String nameSearchable;

    @Field
    private final List<String> promo;

    @Field
    private final List<String> instock;

    @Field
    private final List<Integer> newArrivals; // previously named "new" has been renamed to "newArrivals"

    @Field
    private final List<String> itemsSold;

    @Field
    private final List<String> revenue;

    @Field
    private final List<String> conversion;

    @Field
    private final List<String> productViews;

    @Field
    private final String mobileReviews;

    @Field
    private final List<String> catalogs;

    @Field
    private final String cpcFlag;

    @Field
    private final String fbm;

    @Field
    private final List<String> spuEligible;

    @Field
    private final String mailInRebate;

    @Field
    private final List<String> shipVantage;

    @Field
    private final String catConfidence;

    @Field
    private final String browseBoost;

    @Field
    private final List<String> countryGroup;

    @Field
    private final String instockShipping;

    @Field
    private final String prdType;

    @Field
    private final String cpcClickRate;

    @Field
    private final List<String> freeDelivery;

    @Field
    private final List<String> promotionTxt;

    @Field
    private final List<String> memberSet;

    @Field
    private final List<String> ksn;

    @Field
    private final long impressions;

    @Field
    private final String browseBoostImpr;

    @Field
    private final String consumerReportsRated;

    @Field
    private final List<String> consumerReportRatingContextual;

    @Field
    private final List<String> fitment;

    @Field
    private final List<String> showroomUnit;

    @Field
    private final List<String> buymore;

    @Field
    private final List<String> delivery;

    @Field
    private final List<String> fulfillment;

    @Field
    private final List<String> rebate;

    @Field
    private final List<String> resItemStores;

    @Field
    private final List<String> resStoreUnit;

    @Field
    private final String localAd;

    @Field
    private final List<String> localAdList;

    @Field
    private final String rank;

    @Field
    private final String rebateStatus;

    @Field
    private final List<String> sellerFP;

    @Field
    private final List<String> sellerSFId;

    @Field
    private final String opsUuid_s;

    @Field
    private final String opsCurrentServer_i;


    @Field
    private final boolean automotive;

    @Field
    private final String level1_dis;

    @Field
    private final String level2_dis;

    @Field
    private final String level3_dis;

    @Field
    private final String level4_dis;

    @Field
    private final String level5_dis;

    @Field
    private final String level6_dis;

    @Field
    private final String level7_dis;

    @Field("product_type_s")
    private final String parentProductType;

    @Child
    private final List<OfferDoc> offerDocs;

    @Field
    private final String level1_dis_km;

    @Field
    private final String level2_dis_km;

    @Field
    private final String level3_dis_km;

    @Field
    private final String level4_dis_km;

    @Field
    private final String level5_dis_km;

    @Field
    private final String level6_dis_km;

    @Field
    private final String level7_dis_km;

    @Field
    private final Set<String> vNames_km;

    @Field
    private final Set<String> cNames_km;

    @Field
    private final Set<String> sNames_km;

    @Field
    private final Set<String> lnames_km;

    @Field
    private Set<String> primaryLnames_km;

    @Field
    private String primaryVertical_km;

    @Field
    private List<String> behavioral_km;

    @Field
    private String browseBoost_km;

    @Field
    private int daysOnline_km;

    @Field
    private double numericRevenue_km;

    @Field
    private String rank_km;

    @Field
    private int newItemFlag_km;

    @Field
    private List<Integer> new_km;


    //Topic modeling fields
    @Field
    private List<String> topicIdWithRank;

    @Field
    private String topicIdWithNameSearchable;

    @Field
    private String topicIdWithNameSearchableAndDescription;

    @Field
    private Set<String> parentIds;

    @Field
    private String regPrice;

    @Field
    private String shippingMsg;

    @Field
    private String vendorId;

    @Field
    private String priceRangeInd;

    @Field
    private String status;

    @Field
    private String availDate;

    @Field
    private String search1;

    @Field
    private String search2;

    @Field
    private String search3;

    @Field
    private String search4;

    @Field
    private String search5;

    @Field
    private String search6;

    @Field
    private String search7;

    @Field
    private String search8;

    @Field
    private String search9;

    @Field
    private String search10;

    @Field
    private String saveStory;

    @Field
    private int countryGroupExists;

    @Field
    private List<String> freeShipping;

    @Field("multipleConditions_b")
    private boolean multipleConditions;
    
    @Field
    private List<String> expirableFields;

    @Field("subUpPid_s")
    private  String subUpPid;

    @Field("subDownPid_s")
    private  String subDownPid;

    @Field("altPid_s")
    private  String altPid;

    @Field("reptPid_s")
    private  String reptPid;

    @Field("transRsn_s")
    private  String transRsn;

    @Field("truckLoadQty_s")
    private  String truckLoadQty;

    @Field("delivCat_s")
    private  String delivCat;

    @Field("ffmClass_s")
    private  String ffmClass;

    @Field("discntDt_s")
    private  String discntDt;

    @Field("b2bName_s")
    private  String b2bName;

    @Field("b2bDescShort_s")
    private  String b2bDescShort;

    @Field("b2bDescLong_s")
    private  String b2bDescLong;

    @Field("scomPrice_td")
    private  double scomPrice;

    @Field("scomInStock_s")
    private  String scomInStock;

    @Field("colorFamily_s")
    private  String colorFamily;

    @Field("width_s")
    private  String width;

    @Field("height_s")
    private  String height;

    @Field("depth_s")
    private  String depth;

    @Field("energyStarCompliant_s")
    private  String energyStarCompliant;

    @Field("adaCompliant_s")
    private  String adaCompliant;

    @Field("isCommercial_s")
    private  String isCommercial;

    @Field("aggregatorId_s")
    private String aggregatorId;


    public SearchDoc(SearchDocBuilder builder) {
        this.parentProductType = builder.getProductParentType();
        this.id = builder.getId();
        this.beantype = builder.getBeanType();
        this.fullmfpartno = builder.getFullmfpartno();
        this.mfpartno = builder.getMfpartno();
        this.partnumber = builder.getPartnumber();
        this.itemnumber = builder.getItemnumber();
        this.storeOrigin = builder.getStoreOrigin();
        this.imageStatus = builder.getImageStatus();
        this.rank = builder.getRank();
        this.swatches = builder.getSwatches();
        this.swatchInfo = builder.getSwatchInfo();
        this.xref = builder.getXref();
        this.promoId = builder.getPromoId();
        this.eGiftEligible = builder.geteGiftEligible();
        this.sellerTierRank = builder.getSellerTierRank();
        this.name = builder.getName();
        this.nameSearchable = builder.getNameSearchable();
        this.image = builder.getImage();
        this.alternateImage = builder.getAlternateImage();
        this.description = builder.getDescription();
        this.promo = builder.getPromo();
        this.subcatAttributes = builder.getSubcatAttributes();
        this.productAttributes = builder.getProductAttributes();
        this.storeAttributes = builder.getStoreAttributes();
        this.searchableAttributes = builder.getSearchableAttributes();
        this.searchableAttributesSearchable = builder.getSearchableAttributesSearchable();
        this.itemCondition = builder.getItemCondition();
        this.price = builder.getPrice();
        this.sale = builder.getSale();
        this.clearance = builder.getClearance();
        this.instock = builder.getInstock();
        this.newArrivals = builder.getNewArrivals();
        this.itemsSold = builder.getItemsSold();
        this.revenue = builder.getRevenue();
        this.conversion = builder.getConversion();
        this.productViews = builder.getProductViews();
        this.behavioral = builder.getBehavioral();
        this.sears_international = builder.getSears_international();
        this.mobileReviews = builder.getMobileReviews();
        this.brand = builder.getBrand();
        this.catalogs = builder.getCatalogs();
        this.level1Cats = builder.getLevel1Cats();
        this.level2Cats = builder.getLevel2Cats();
        this.level3Cats = builder.getLevel3Cats();
        this.vNames = builder.getvNames();
        this.cNames = builder.getcNames();
        this.sNames = builder.getsNames();
        this.sin = builder.getSin();
        this.sellerCount = builder.getSellerCount();
        this.cpcFlag = builder.getCpcFlag();
        this.fbm = builder.getFbm();
        this.sellers = builder.getSellers();
        this.spuEligible = builder.getSpuEligible();
        this.layAway = builder.getLayAway();
        this.mailInRebate = builder.getMailInRebate();
        this.shipVantage = builder.getShipVantage();
        this.catConfidence = builder.getCatConfidence();
        this.has991 = builder.getHas991();
        this.trustedSeller = builder.getTrustedSeller();
        this.international_shipping = builder.getInternational_shipping();
        this.int_ship_eligible = builder.getInt_ship_eligible();
        this.browseBoost = builder.getBrowseBoost();
        this.countryGroup = builder.getCountryGroup();
        this.instockShipping = builder.getInstockShipping();
        this.matureContentFlag = builder.getMatureContentFlag();
        this.primaryHierarchy = builder.getPrimaryHierarchy();
        this.accessories = builder.getAccessories();
        this.primaryVertical = builder.getPrimaryVertical();
        this.prdType = builder.getPrdType();
        this.cpcClickRate = builder.getCpcClickRate();
        this.searchPhrase = builder.getSearchPhrase();
        this.sellerId = builder.getSellerId();
        this.freeDelivery = builder.getFreeDelivery();
        this.promotionTxt = builder.getPromotionTxt();
        this.url = builder.getUrl();
        this.discount = builder.getDiscount();
        this.offer = builder.getOffer();
        this.avgRatingContextual = builder.getAvgRatingContextual();
        this.numReviewsContextual = builder.getNumReviewsContextual();
        this.upc = builder.getUpc();
        this.storePickUp = builder.getStorePickUp();
        this.numericRevenue = builder.getNumericRevenue();
        this.flashDeal = builder.getFlashDeal();
        this.giftCardSequence = builder.getGiftCardSequence();
        this.giftCardType = builder.getGiftCardType();
        this.categories = builder.getCategories();
        this.tagValues = builder.getTagValues();
        this.category = builder.getCategory();
        this.lnames = builder.getLnames();
        this.primaryLnames = builder.getPrimaryLnames();
        this.primaryCategory = builder.getPrimaryCategory();
        this.catentryId = builder.getCatentryId();
        this.programType = builder.getProgramType();
        this.sellerTier = builder.getSellerTier();
        this.division = builder.getDivision();
        this.brandId = builder.getBrandId();
        this.geospottag = builder.getGeospottag();
        this.storeUnit = builder.getStoreUnit();
        this.clickUrl = builder.getClickUrl();
        this.newItemFlag = builder.getNewItemFlag();
        this.daysOnline = builder.getDaysOnline();
        this.pickUpItemStores = builder.getPickUpItemStores();
        this.offerAttr = builder.getOfferAttr();
        this.staticAttributes = builder.getStaticAttributes();
        this.discontinued = builder.getDiscontinued();
        this.memberSet = builder.getMemberSet();
        this.ksn = builder.getKsn();
        this.impressions = builder.getImpressions();
        this.browseBoostImpr = builder.getBrowseBoostImpr();
        this.consumerReportsRated = builder.getConsumerReportsRated();
        this.consumerReportRatingContextual = builder.getConsumerReportRatingContextual();
        this.fitment = builder.getFitment();
        this.showroomUnit = builder.getShowroomUnit();
        this.buymore = builder.getBuymore();
        this.delivery = builder.getDelivery();
        this.fulfillment = builder.getFulfillment();
        this.localAd = builder.getLocalAd();
        this.localAdList = builder.getLocalAdList();
        this.rebate = builder.getRebate();
        this.rebateStatus = builder.getRebateStatus();
        this.resItemStores = builder.getResItemStores();
        this.resStoreUnit = builder.getResStoreUnit();
        this.sellerFP = builder.getSellerFP();
        this.sellerSFId = builder.getSellerSFId();
        this.opsUuid_s = builder.getOpsUuid_s();
        this.opsCurrentServer_i = builder.getOpsCurrentServer_i();
        this.automotive = builder.isAutomotive();
        this.level1_dis = builder.getLevel1_dis();
        this.level2_dis = builder.getLevel2_dis();
        this.level3_dis = builder.getLevel3_dis();
        this.level4_dis = builder.getLevel4_dis();
        this.level5_dis = builder.getLevel5_dis();
        this.level6_dis = builder.getLevel6_dis();
        this.level7_dis = builder.getLevel7_dis();
        this.offerDocs = builder.getOfferDocs();
        this.level1_dis_km = builder.getLevel1_dis_km();
        this.level2_dis_km = builder.getLevel2_dis_km();
        this.level3_dis_km = builder.getLevel3_dis_km();
        this.level4_dis_km = builder.getLevel4_dis_km();
        this.level5_dis_km = builder.getLevel5_dis_km();
        this.level6_dis_km = builder.getLevel6_dis_km();
        this.level7_dis_km = builder.getLevel7_dis_km();
        this.rank_km = builder.getRank_km();
        this.new_km = builder.getNew_km();
        this.behavioral_km = builder.getBehavioral_km();
        this.vNames_km = builder.getvNames_km();
        this.cNames_km = builder.getcNames_km();
        this.sNames_km = builder.getsNames_km();
        this.browseBoost_km = builder.getBrowseBoost_km();
        this.primaryVertical_km = builder.getPrimaryVertical_km();
        this.numericRevenue_km = builder.getNumericRevenue_km();
        this.lnames_km = builder.getLnames_km();
        this.primaryLnames_km = builder.getPrimaryLnames_km();
        this.newItemFlag_km = builder.getNewItemFlag_km();
        this.daysOnline_km = builder.getDaysOnline_km();
        this.topicIdWithRank = builder.getTopicIdWithRank();
        this.topicIdWithNameSearchable = builder.getTopicIdWithNameSearchable();
        this.parentIds = builder.getParentIds();
        this.regPrice = builder.getRegPrice();
        this.shippingMsg = builder.getShippingMsg();
        this.vendorId = builder.getVendorId();
        this.priceRangeInd = builder.getRegPrice();
        this.status = builder.getRegPrice();
        this.availDate = builder.getRegPrice();
        this.search1 = builder.getSearch1();
        this.search2 = builder.getSearch2();
        this.search3 = builder.getSearch3();
        this.search4 = builder.getSearch4();
        this.search5 = builder.getSearch5();
        this.search6 = builder.getSearch6();
        this.search7 = builder.getSearch7();
        this.search8 = builder.getSearch8();
        this.search9 = builder.getSearch9();
        this.search10 = builder.getSearch10();
        this.saveStory = builder.getSaveStory();
        this.countryGroupExists = builder.getCountryGroupExists();
        this.freeShipping = builder.getFreeShipping();
        this.multipleConditions = builder.isMultipleConditions();
        this.expirableFields = builder.getExpirableFields();
        this.subUpPid = builder.getSubUpPid();
        this.subDownPid =builder.getSubDownPid();
        this.altPid =builder.getAltPid();
        this.reptPid =builder.getReptPid();
        this.transRsn =builder.getTransRsn();
        this.truckLoadQty =builder.getTruckLoadQty();
        this.delivCat =builder.getDeliveryCat();
        this.ffmClass =builder.getFfmClass();
        this.discntDt =builder.getDiscntDt();
        this.b2bName =builder.getB2bName();
        this.b2bDescShort =builder.getB2bDescShort();
        this.b2bDescLong =builder.getB2bDescLong();
        this.scomPrice =builder.getScomPrice();
        this.colorFamily =builder.getColorFamily();
        this.width =builder.getWidth();
        this.height =builder.getHeight();
        this.depth =builder.getDepth();
        this.energyStarCompliant =builder.getEnergyStarCompliant();
        this.adaCompliant =builder.getAdaCompliant();
        this.scomInStock =builder.getScomInStock();
        this.isCommercial =builder.isCommercial();
        this.aggregatorId = builder.getaggregatorId();
    }

    public Set<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(Set<String> parentIds) {
        this.parentIds = parentIds;
    }

    public String getCatentryId() {
        return catentryId;
    }


    public String getImageStatus() {
        return imageStatus;
    }


    public String getImage() {
        return image;
    }


    public List<String> getAlternateImage() {
        return alternateImage;
    }


    public String getBrandId() {
        return brandId;
    }


    public String getBrand() {
        return brand;
    }


    public String getDescription() {
        return description;
    }


    public List<String> getSearchableAttributes() {
        return searchableAttributes;
    }


    public List<String> getSearchableAttributesSearchable() {
        return searchableAttributesSearchable;
    }


    public List<String> getSubcatAttributes() {
        return subcatAttributes;
    }


    public String getId() {
        return id;
    }


    public String getFullmfpartno() {
        return fullmfpartno;
    }


    public String getMfpartno() {
        return mfpartno;
    }


    public String getName() {
        return name;
    }


    public String getUrl() {
        return url;
    }


    public Set<String> getLevel1Cats() {
        return level1Cats;
    }


    public Set<String> getvNames() {
        return vNames;
    }


    public Set<String> getvNames_km() {
        return vNames_km;
    }


    public String getPrimaryVertical() {
        return primaryVertical;
    }


    public String getPrimaryVertical_km() {
        return primaryVertical_km;
    }


    public Set<String> getLevel2Cats() {
        return level2Cats;
    }


    public Set<String> getcNames() {
        return cNames;
    }


    public Set<String> getLevel3Cats() {
        return level3Cats;
    }


    public Set<String> getsNames() {
        return sNames;
    }


    public List<String> getPrimaryHierarchy() {
        return primaryHierarchy;
    }


    public int getAccessories() {
        return accessories;
    }


    public Set<String> getPrimaryLnames() {
        return primaryLnames;
    }


    public List<String> getPrimaryCategory() {
        return primaryCategory;
    }


    public Set<String> getCategories() {
        return categories;
    }


    public List<String> getCategory() {
        return category;
    }


    public Set<String> getLnames() {
        return lnames;
    }


    public String getItemnumber() {
        return itemnumber;
    }


    public String getPartnumber() {
        return partnumber;
    }


    public String getItemCondition() {
        return itemCondition;
    }


    public String getBeantype() {
        return beantype;
    }


    public List<String> getStoreOrigin() {
        return storeOrigin;
    }


    public String geteGiftEligible() {
        return eGiftEligible;
    }


    public List<String> getProductAttributes() {
        return productAttributes;
    }


    public List<String> getStoreAttributes() {
        return storeAttributes;
    }


    public List<String> getSears_international() {
        return sears_international;
    }


    public String getSin() {
        return sin;
    }


    public String getSellerCount() {
        return sellerCount;
    }


    public List<String> getSellers() {
        return sellers;
    }


    public int getLayAway() {
        return layAway;
    }


    public String getHas991() {
        return has991;
    }


    public List<String> getInternational_shipping() {
        return international_shipping;
    }


    public Integer getInt_ship_eligible() {
        return int_ship_eligible;
    }


    public int getMatureContentFlag() {
        return matureContentFlag;
    }


    public String getUpc() {
        return upc;
    }


    public List<String> getStorePickUp() {
        return storePickUp;
    }


    public String getFlashDeal() {
        return flashDeal;
    }


    public String getGiftCardSequence() {
        return giftCardSequence;
    }


    public List<String> getTagValues() {
        return tagValues;
    }


    public String getProgramType() {
        return programType;
    }


    public String getDivision() {
        return division;
    }


    public List<String> getGeospottag() {
        return geospottag;
    }


    public List<String> getClickUrl() {
        return clickUrl;
    }

    public List<String> getStaticAttributes() {
        return staticAttributes;
    }

    public String getDiscontinued() {
        return discontinued;
    }


    public List<String> getPrice() {
        return price;
    }


    public List<String> getDiscount() {
        return discount;
    }


    public List<String> getSale() {
        return sale;
    }


    public List<String> getClearance() {
        return clearance;
    }


    public String getSearchPhrase() {
        return searchPhrase;
    }


    public List<String> getBehavioral() {
        return behavioral;
    }


    public int getSellerTierRank() {
        return sellerTierRank;
    }


    public String getSellerTier() {
        return sellerTier;
    }


    public int getTrustedSeller() {
        return trustedSeller;
    }


    public List<String> getStoreUnit() {
        return storeUnit;
    }


    public String getSellerId() {
        return sellerId;
    }


    public List<String> getOffer() {
        return offer;
    }


    public List<String> getAvgRatingContextual() {
        return avgRatingContextual;
    }


    public List<String> getNumReviewsContextual() {
        return numReviewsContextual;
    }


    public double getNumericRevenue() {
        return numericRevenue;
    }


    public String getGiftCardType() {
        return giftCardType;
    }

    public int getNewItemFlag() {
        return newItemFlag;
    }


    public int getDaysOnline() {
        return daysOnline;
    }


    public List<String> getPickUpItemStores() {
        return pickUpItemStores;
    }


    public List<String> getOfferAttr() {
        return offerAttr;
    }


    public String getSwatches() {
        return swatches;
    }


    public String getSwatchInfo() {
        return swatchInfo;
    }


    public Set<String> getXref() {
        return xref;
    }


    public Set<String> getPromoId() {
        return promoId;
    }


    public String getNameSearchable() {
        return nameSearchable;
    }


    public List<String> getPromo() {
        if (promo == null) {
            return new ArrayList<>();
        }
        return promo;
    }


    public List<String> getInstock() {
        return instock;
    }


    public List<Integer> getNewArrivals() {
        return newArrivals;
    }


    public List<String> getItemsSold() {
        return itemsSold;
    }


    public List<String> getRevenue() {
        return revenue;
    }


    public List<String> getConversion() {
        return conversion;
    }


    public List<String> getProductViews() {
        return productViews;
    }


    public String getMobileReviews() {
        return mobileReviews;
    }


    public List<String> getCatalogs() {
        return catalogs;
    }


    public String getCpcFlag() {
        return cpcFlag;
    }


    public String getFbm() {
        return fbm;
    }


    public List<String> getSpuEligible() {
        return spuEligible;
    }


    public String getMailInRebate() {
        return mailInRebate;
    }

    public List<String> getShipVantage() {
        if (shipVantage == null) {
            return new ArrayList<>();
        }
        return shipVantage;
    }


    public String getCatConfidence() {
        return catConfidence;
    }


    public String getBrowseBoost() {
        return browseBoost;
    }


    public List<String> getCountryGroup() {
        return countryGroup;
    }


    public String getInstockShipping() {
        return instockShipping;
    }


    public String getPrdType() {
        return prdType;
    }


    public String getCpcClickRate() {
        return cpcClickRate;
    }


    public List<String> getFreeDelivery() {
        if (freeDelivery == null) {
            return new ArrayList<>();
        }
        return freeDelivery;
    }


    public List<String> getPromotionTxt() {
        if (promotionTxt == null) {
            return new ArrayList<>();
        }
        return promotionTxt;
    }


    public List<String> getMemberSet() {
        return memberSet;
    }


    public List<String> getKsn() {
        return ksn;
    }


    public long getImpressions() {
        return impressions;
    }


    public String getBrowseBoostImpr() {
        return browseBoostImpr;
    }


    public String getConsumerReportsRated() {
        return consumerReportsRated;
    }


    public List<String> getConsumerReportRatingContextual() {
        return consumerReportRatingContextual;
    }


    public List<String> getFitment() {
        return fitment;
    }


    public List<String> getShowroomUnit() {
        return showroomUnit;
    }


    public List<String> getBuymore() {
        return buymore;
    }


    public List<String> getDelivery() {
        return delivery;
    }


    public List<String> getFulfillment() {
        return fulfillment;
    }


    public List<String> getRebate() {
        return rebate;
    }


    public List<String> getResItemStores() {
        return resItemStores;
    }


    public List<String> getResStoreUnit() {
        return resStoreUnit;
    }


    public String getLocalAd() {
        return localAd;
    }


    public String getRank() {
        return rank;
    }


    public String getRebateStatus() {
        return rebateStatus;
    }


    public List<String> getSellerFP() {
        return sellerFP;
    }


    public List<String> getSellerSFId() {
        return sellerSFId;
    }


    public String getOpsUuid_s() {
        return opsUuid_s;
    }


    public String getOpsCurrentServer_i() {
        return opsCurrentServer_i;
    }


    public boolean isAutomotive() {
        return automotive;
    }


    public String getLevel1_dis() {
        return level1_dis;
    }


    public String getLevel2_dis() {
        return level2_dis;
    }


    public String getLevel3_dis() {
        return level3_dis;
    }


    public String getLevel4_dis() {
        return level4_dis;
    }


    public String getLevel5_dis() {
        return level5_dis;
    }


    public String getLevel6_dis() {
        return level6_dis;
    }


    public String getLevel7_dis() {
        return level7_dis;
    }

    @JsonProperty("_childDocuments_")
    public List<OfferDoc> getOfferDocs() {
        return offerDocs;
    }

    @JsonProperty("product_type_s")
    public String getParentProductType() {
        return parentProductType;
    }

    public String getLevel1_dis_km() {
        return level1_dis_km;
    }

    public String getLevel2_dis_km() {
        return level2_dis_km;
    }

    public String getLevel3_dis_km() {
        return level3_dis_km;
    }

    public String getLevel4_dis_km() {
        return level4_dis_km;
    }

    public String getLevel5_dis_km() {
        return level5_dis_km;
    }

    public String getLevel6_dis_km() {
        return level6_dis_km;
    }

    public String getLevel7_dis_km() {
        return level7_dis_km;
    }

    public Set<String> getcNames_km() {
        return cNames_km;
    }

    public Set<String> getsNames_km() {
        return sNames_km;
    }

    public Set<String> getPrimaryLnames_km() {
        return primaryLnames_km;
    }

    public Set<String> getLnames_km() {
        return lnames_km;
    }

    public List<String> getBehavioral_km() {
        return behavioral_km;
    }

    public double getNumericRevenue_km() {
        return numericRevenue_km;
    }


    public int getNewItemFlag_km() {
        return newItemFlag_km;
    }

    public int getDaysOnline_km() {
        return daysOnline_km;
    }

    public List<Integer> getNew_km() {
        return new_km;
    }

    public String getBrowseBoost_km() {
        return browseBoost_km;
    }

    public String getRank_km() {
        return rank_km;
    }

    public List<String> getTopicIdWithRank() {
        return topicIdWithRank;
    }

    public void setTopicIdWithRank(List<String> topicIdWithRank) {
        this.topicIdWithRank = topicIdWithRank;
    }


    public String getTopicIdWithNameSearchableAndDescription() {
        return topicIdWithNameSearchableAndDescription;
    }

    public void setTopicIdWithNameSearchableAndDescription(String topicIdWithNameSearchableAndDescription) {
        this.topicIdWithNameSearchableAndDescription = topicIdWithNameSearchableAndDescription;
    }

    public String getTopicIdWithNameSearchable() {
        return topicIdWithNameSearchable;
    }

    public void setTopicIdWithNameSearchable(String topicIdWithNameSearchable) {
        this.topicIdWithNameSearchable = topicIdWithNameSearchable;
    }

    public List<String> getLocalAdList() {
        return localAdList;
    }

    public String getRegPrice() {
        return regPrice;
    }

    public String getShippingMsg() {
        return shippingMsg;
    }

    public String getVendorId() {
        return vendorId;
    }

    public String getPriceRangeInd() {
        return priceRangeInd;
    }

    public String getStatus() {
        return status;
    }

    public String getAvailDate() {
        return availDate;
    }

    public String getSearch1() {
        return search1;
    }

    public String getSearch2() {
        return search2;
    }

    public String getSearch3() {
        return search3;
    }

    public String getSearch4() {
        return search4;
    }

    public String getSearch5() {
        return search5;
    }

    public String getSearch6() {
        return search6;
    }

    public String getSearch7() {
        return search7;
    }

    public String getSearch8() {
        return search8;
    }

    public String getSearch9() {
        return search9;
    }

    public String getSearch10() {
        return search10;
    }

    public String getSaveStory() {
        return saveStory;
    }

    public int getCountryGroupExists() {
        return countryGroupExists;
    }

    public List<String> getFreeShipping() {
        if (freeShipping == null) {
            return new ArrayList<>();
        }
        return freeShipping;
    }

    public void setFreeShipping(List<String> freeShipping) {
        this.freeShipping = freeShipping;
    }

    @JsonProperty("multipleConditions_b")
    public boolean isMultipleConditions() {
        return multipleConditions;
    }
    
    public List<String> getExpirableFields() {
        return expirableFields;
    }

    public String getSubUpPid() {
        return subUpPid;
    }

    public String getSubDownPid() {
        return subDownPid;
    }

    public String getAltPid() {
        return altPid;
    }

    public String getReptPid() {
        return reptPid;
    }

    public String getTransRsn() {
        return transRsn;
    }

    public String getTruckLoadQty() {
        return truckLoadQty;
    }

    public String getDelivCat() {
        return delivCat;
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

    public String getAggregatorId() {
        return aggregatorId;
    }
}