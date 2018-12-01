package com.shc.ecom.search.extract.extracts;

import com.shc.ecom.gb.doc.collection.AssetsImgsVals;
import com.shc.ecom.gb.doc.content.NameValue;
import com.shc.ecom.search.common.constants.Sites;
import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author rgopala
 */
public class ContentExtract implements Serializable {

    private static final long serialVersionUID = 7179211972518966400L;
    private String ssin_Content;
    private String uuid;
    private String catentrySubType;
    private String catentryId;
    private String modelNo;
    private String primaryImageUrl;
    private List<String> alternateImageUrls;
    private List<AssetsImgsVals> bucketPrimaryImagAttr;
    private List<AssetsImgsVals> bucketAlternateImagAttr;
    private String name;
    private String brandCodeId;
    private String brandName;
    private String shortDescription;
    private String longDescription;
    private boolean isSearsDisplayElligible;
    private boolean isBundleDisplayElligible;
    private boolean contentOnline;

    private Map<Sites, List<List<String>>> siteWebHierarchiesMap = new HashMap<>();
    private Map<Sites, List<List<String>>> siteDapHierarchiesMap = new HashMap<>();
    private Map<Sites, List<NameValue>> staticFacetsSitesMap;
    private List<BCOProducts> bcoProducts;

    private boolean spuEligible;

    // This is for bundle. Bundle don't have date specific to site, so don't have a map here.
    private LocalDate firstOnlineDate;
    private List<String> productIds;

    // TODO: Need to check why two fields with the same value
    private List<String> geoSpotTag;
    private List<String> vocTags;

    private String itemCondition;
    private String division;

    private Integer offerCnt = 0;
    private String autofitment;
    private String imaClassControlPid;

    private String programType;
    private String url;
    private boolean isMatureContent;
    private Set<String> intlGroupIds;
    private String rebateId;
    private double catConfScore;
    private boolean outletItem;
    private String upc;
    private List<String> soldBy;
    private boolean isEnergyStar;
    private String streetDt;
    private boolean isMailable;
    private String channel;
    private String stsChannel;
    private boolean isDoNotEmailMe;
    private boolean isDeliveryElig;
    private boolean isShipElig;
    private boolean automotive;
    private boolean uvd;
    private boolean layawayEligible;
    private Map<Sites, Boolean> sitesDispEligibility;
    private Map<Sites, Boolean> sitesEligibility;
    private String b2bName;
    private String b2bDescShort;
    private String b2bDescLong;
    private String colorFamily;
    private String width;
    private String height;
    private String depth;
    private String energyStarCompliant;
    private String adaCompliant;

    public Map<Sites, Boolean> getSitesEligibility() {
    	if(sitesEligibility==null) {
    		sitesEligibility = new HashMap<>();
    	}
        return sitesEligibility;
    }

    public void setSitesEligibility(Map<Sites, Boolean> sitesEligibility) {
        this.sitesEligibility = sitesEligibility;
    }

    public boolean isSearsDisplayElligible() {
        return isSearsDisplayElligible;
    }

    public void setSearsDisplayElligible(boolean isDisplayElligible) {
        this.isSearsDisplayElligible = isDisplayElligible;
    }


    public boolean isBundleDisplayElligible() {
        return isBundleDisplayElligible;
    }

    public void setBundleDisplayElligible(boolean isDisplayElligible) {
        this.isBundleDisplayElligible = isDisplayElligible;
    }

    public String getStsChannel() {
        return stsChannel;
    }

    public void setStsChannel(String stsChannel) {
        this.stsChannel = stsChannel;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public List<String> getAlternateImageUrls() {
        return alternateImageUrls;
    }

    public void setAlternateImageUrls(List<String> alternateImageUrls) {
        this.alternateImageUrls = alternateImageUrls;
    }

    public String getPrimaryImageUrl() {
        return primaryImageUrl;
    }

    public void setPrimaryImageUrl(String primaryImageUrl) {
        this.primaryImageUrl = primaryImageUrl;
    }

    public List<AssetsImgsVals> getBucketPrimaryImagAttr() {
        return bucketPrimaryImagAttr;
    }

    public void setBucketPrimaryImagAttr(List<AssetsImgsVals> bucketPrimaryImagAttr) {
        this.bucketPrimaryImagAttr = bucketPrimaryImagAttr;
    }

    public List<AssetsImgsVals> getBucketAlternateImagAttr() {
        return bucketAlternateImagAttr;
    }

    public void setBucketAlternateImagAttr(List<AssetsImgsVals> bucketAlternateImagAttr) {
        this.bucketAlternateImagAttr = bucketAlternateImagAttr;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public Set<String> getIntlGroupIds() {
        return intlGroupIds;
    }

    public void setIntlGroupIds(Set<String> intlGroupIds) {
        this.intlGroupIds = intlGroupIds;
    }

    public boolean isMatureContent() {
        return isMatureContent;
    }

    public void setMatureContent(boolean isMatureContent) {
        this.isMatureContent = isMatureContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getVocTags() {
        return vocTags;
    }

    public void setVocTags(List<String> vocTags) {
        this.vocTags = vocTags;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public Map<Sites, List<List<String>>> getSiteWebHierarchiesMap() {
        return siteWebHierarchiesMap;
    }

    public void setSiteWebHierarchiesMap(Map<Sites, List<List<String>>> siteWebHierarchiesMap) {
        this.siteWebHierarchiesMap = siteWebHierarchiesMap;
    }

    public Map<Sites, List<List<String>>> getSiteDapHierarchiesMap() {
        return siteDapHierarchiesMap;
    }

    public void setSiteDapHierarchiesMap(Map<Sites, List<List<String>>> siteDapHierarchiesMap) {
        this.siteDapHierarchiesMap = siteDapHierarchiesMap;
    }

    public List<String> getGeoSpotTag() {
        return geoSpotTag;
    }

    public void setGeoSpotTag(List<String> geoSpotTag) {
        this.geoSpotTag = geoSpotTag;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getSsin_Content() {
        return ssin_Content;
    }

    public void setSsin_Content(String ssin_Content) {
        this.ssin_Content = ssin_Content;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCatentrySubType() {
        return catentrySubType;
    }

    public void setCatentrySubType(String catentrySubType) {
        this.catentrySubType = catentrySubType;
    }

    public String getCatentryId() {
        return catentryId;
    }

    public void setCatentryId(String catentryId) {
        this.catentryId = catentryId;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandCodeId() {
        return brandCodeId;
    }

    public void setBrandCodeId(String brandCodeId) {
        this.brandCodeId = brandCodeId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    /**
     * @return the offerCnt
     */
    public Integer getOfferCnt() {
        return offerCnt;
    }

    /**
     * @param offerCnt the offerCnt to set
     */
    public void setOfferCnt(Integer offerCnt) {
        if (offerCnt == null) {
            offerCnt = 0;
        }
        this.offerCnt = offerCnt;
    }

    public String getAutofitment() {
        return autofitment;
    }

    public void setAutofitment(String autofitment) {
        this.autofitment = autofitment;
    }

    public String getImaClassControlPid() {
        return imaClassControlPid;
    }

    public void setImaClassControlPid(String imaClassControlPid) {
        this.imaClassControlPid = imaClassControlPid;
    }

    public Map<Sites, List<NameValue>> getStaticFacetsSitesMap() {
        return staticFacetsSitesMap;
    }

    public void setStaticFacetsSitesMap(Map<Sites, List<NameValue>> staticFacetsSitesMap) {
        this.staticFacetsSitesMap = staticFacetsSitesMap;
    }

    public LocalDate getFirstOnlineDate() {
        return firstOnlineDate;
    }

    public void setFirstOnlineDate(LocalDate firstOnlineDate) {
        this.firstOnlineDate = firstOnlineDate;
    }

    public boolean isSpuEligible() {
        return spuEligible;
    }

    public void setSpuEligible(boolean spuEligible) {
        this.spuEligible = spuEligible;
    }

    public String getRebateId() {
        return rebateId;
    }

    public void setRebateId(String rebateId) {
        this.rebateId = rebateId;
    }

    public double getCatConfScore() {
        return catConfScore;
    }

    public void setCatConfScore(Double catConfScore) {
        this.catConfScore = catConfScore;
    }

    public void setCatConfScore(double catConfScore) {
        this.catConfScore = catConfScore;
    }

    public boolean isOutletItem() {
        return outletItem;
    }

    public void setOutletItem(boolean outletItem) {
        this.outletItem = outletItem;
    }

    public List<String> getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(List<String> soldBy) {
        this.soldBy = soldBy;
    }

    public boolean isEnergyStar() {
        return isEnergyStar;
    }

    public void setEnergyStar(boolean isEnergyStar) {
        this.isEnergyStar = isEnergyStar;
    }

    public String getStreetDt() {
        return streetDt;
    }

    public void setStreetDt(String streetDt) {
        this.streetDt = streetDt;
    }

    public boolean isMailable() {
        return isMailable;
    }

    public void setMailable(boolean isMailable) {
        this.isMailable = isMailable;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isDoNotEmailMe() {
        return isDoNotEmailMe;
    }

    public void setDoNotEmailMe(boolean isDoNotEmailMe) {
        this.isDoNotEmailMe = isDoNotEmailMe;
    }

    public boolean isDeliveryElig() {
        return isDeliveryElig;
    }

    public void setDeliveryElig(boolean isDeliveryElig) {
        this.isDeliveryElig = isDeliveryElig;
    }

    public boolean isShipElig() {
        return isShipElig;
    }

    public void setShipElig(boolean isShipElig) {
        this.isShipElig = isShipElig;
    }

    public boolean isUvd() {
        return uvd;
    }

    public void setUvd(boolean uvd) {
        this.uvd = uvd;
    }

    public boolean isAutomotive() {
        return automotive;
    }

    public void setAutomotive(boolean automotive) {
        this.automotive = automotive;
    }

    public boolean isContentOnline() {
        return contentOnline;
    }

    public void setContentOnline(boolean contentOnline) {
        this.contentOnline = contentOnline;
    }

    public boolean isLayawayEligible() {
        return layawayEligible;
    }

    public void setLayawayEligible(boolean layawayEligible) {
        this.layawayEligible = layawayEligible;
    }

    public Map<Sites, Boolean> getSitesDispEligibility() {
        if (sitesDispEligibility == null) {
            sitesDispEligibility = new HashMap<>();
        }
        return sitesDispEligibility;
    }

    public void setSitesDispEligibility(Map<Sites, Boolean> sitesDispEligibility) {
        this.sitesDispEligibility = sitesDispEligibility;
    }

    public List<BCOProducts> getBCOProducts() {
        return bcoProducts;
    }

    public void setBCOProducts(List<BCOProducts> bcoProducts) {
        this.bcoProducts = bcoProducts;
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
}
