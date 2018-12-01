/**
 *
 */
package com.shc.ecom.search.extract.extracts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.joda.time.LocalDate;

import com.shc.ecom.gb.doc.offer.ProductOption;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.extract.components.fitments.AutomotiveOffer;

/**
 * @author rgopala
 */
public class OfferExtract implements Serializable {

    private static final long serialVersionUID = 1192787350821844975L;

    private String ssin;
    private String offerId;
    private List<String> soldBy;
    private List<String> ksn;
    private boolean isEGiftElig;
    private String mpPgmType;
    private Integer offerCnt;
    private boolean isReserveIt;
    private boolean isAlcohol;
    private boolean isPerishable;
    private boolean isRefrigeration;
    private boolean isTobacco;
    private boolean isFreezing;
    private boolean isWebExcl;
    private Set<String> dfltFfmDisplay;
    private boolean isDeliveryElig;
    private boolean isShipElig;
    private String offerType;
    private Set<String> viewOnly;
    private boolean isAutomotive;
    private boolean isEnergyStar;
    private boolean isKmartPrElig;
    private boolean isConfigureTech;
    private boolean isOutletItem;
    private boolean isSimiElig;
    private String streetDt;
    private boolean isMailable;
    private boolean isIntlShipElig;
    private boolean isSResElig;
    private List<String> channels;
    private Map<String, String> viewOnlyOffers;
    private boolean isDoNotEmailMe;
    private boolean isSpuElig;
    private boolean isLayawayElig;
    private boolean isSTSElig;
    private List<String> STSChannels;
    private String status;
    private Set<Integer> sellerId;
    private Map<String, Integer> offerToSellerIdMap;
    private Set<String> sellerName;
    private List<String> upc;
    private boolean isMatureContent;
    private boolean isDealFlash;
    private String giftCardRank;
    private List<String> vocTagNames;
    private List<String> storeHierarchyNames;
    private String clickCaptureUrl;
    private String internalRimSts;
    private List<String> sites;
    private Map<Sites, LocalDate> oldestOnlineDateBySite;
    private Map<Sites, LocalDate> newestOnlineDateBySite;
    private boolean directSears;
    private List<String> geoSpotTag;
    private String itemCondition;
    private String division;
    private List<String> clickUrl;
    private Set<String> intlGroupIds;
    private String gndFreeStartDt;
    private String gndFreeEndDt;
    private Double catConfScore;
    private Map<String, List<String>> storeOffersMap;
    private boolean isFreeShipping;
    private Map<String, Boolean> freeShippingMap;
    private String offerTier;
    private Map<String, String> offerIdSoldByMap;
    private List<AutomotiveOffer> automotiveOffers;
    private String freeShipThreshold;
    private String name;
    private boolean isSearsDisplayElligible;
    private boolean isBundleDisplayElligible;
    private Map<Sites, Boolean> sitesDispEligibility;
    private Map<Sites, Boolean> sitesEligibility;
    private Set<String> parentIds;
    private String kmartOfferParentId;
    private Map<String, String> offerIDParentID;
    private Map<String, String> offerIdKsnMap; //One to one mapping of offerID and its corresponding ksn
    private Map<String, Boolean> offerMailability;
    private Map<String, Double> offerWeight;
    private Map<String, List<ProductOption>> linkedSwatchProductOptions;
    private Map<String, String> mainImgs;
    private Map<String, String> swatchImgs;
    private Map<String, Boolean> searsDispEligibilityStatus;

    private String subUpPid;
    private String subDownPid;
    private String altPid;
    private String reptPid;
    private String transRsn;
    private String truckLoadQty;
    private String deliveryCat;
    private String ffmClass;
    private String discntDt;
    private String aggregatorId;

    public Map<Sites, Boolean> getSitesEligibility() {
    	if(sitesEligibility==null) {
    		sitesEligibility = new HashMap<>();
    	}
        return sitesEligibility;
    }

    public String getAggregatorId() {
        return aggregatorId;
    }

    public void setAggregatorId(String aggregatorId) {
        this.aggregatorId = aggregatorId;
    }

    public void setSitesEligibility(Map<Sites, Boolean> sitesEligibility) {
        this.sitesEligibility = sitesEligibility;
    }

    public Set<String> getParentIds() {
        if (CollectionUtils.isEmpty(parentIds)) {
            parentIds = new HashSet<>();
        }
        return parentIds;
    }

    public void setParentIds(Set<String> parentIds) {
        this.parentIds = parentIds;
    }

    public List<String> getKsn() {
    	if(ksn==null) {
    		ksn = new ArrayList<>();
    	}
        return ksn;
    }

    public void setKsn(List<String> ksn) {
        this.ksn = ksn;
    }

    public List<String> getUpc() {
        return upc;
    }

    public void setUpc(List<String> upc) {
        this.upc = upc;
    }

    public Map<String, String> getOfferIdSoldByMap() {
        return offerIdSoldByMap;
    }

    public void setOfferIdSoldByMap(Map<String, String> offerIdSoldByMap) {
        this.offerIdSoldByMap = offerIdSoldByMap;
    }

    public String getOfferTier() {
        return offerTier;
    }

    public void setOfferTier(String offerTier) {
        this.offerTier = offerTier;
    }

    public Map<Sites, LocalDate> getNewestOnlineDateBySite() {
        return newestOnlineDateBySite;
    }

    public void setNewestOnlineDateBySite(Map<Sites, LocalDate> newestOnlineDateBySite) {
        this.newestOnlineDateBySite = newestOnlineDateBySite;
    }

    public boolean isFreeShipping() {
        return isFreeShipping;
    }

    public void setFreeShipping(boolean isFreeShipping) {
        this.isFreeShipping = isFreeShipping;
    }

    public Map<String, Boolean> getFreeShippingMap() {
        if (MapUtils.isEmpty(freeShippingMap)) {
            return new HashMap<>();
        }
        return freeShippingMap;
    }

    public void setFreeShippingMap(Map<String, Boolean> freeShippingMap) {
        this.freeShippingMap = freeShippingMap;
    }

    public Map<String, List<String>> getStoreOffersMap() {
        return storeOffersMap;
    }

    public void setStoreOffersMap(Map<String, List<String>> storeOffersMap) {
        this.storeOffersMap = storeOffersMap;
    }

    public List<String> getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(List<String> soldBy) {
        this.soldBy = soldBy;
    }

    public Set<String> getIntlGroupIds() {
        return intlGroupIds;
    }

    public void setIntlGroupIds(Set<String> intlGroupIds) {
        this.intlGroupIds = intlGroupIds;
    }

    public List<String> getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(List<String> clickUrl) {
        this.clickUrl = clickUrl;
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

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public Map<String, String> getViewOnlyOffers() {
        return viewOnlyOffers;
    }

    public void setViewOnlyOffers(Map<String, String> viewOnlyOffers) {
        this.viewOnlyOffers = viewOnlyOffers;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getSsin() {
        return ssin;
    }

    public void setSsin(String ssin) {
        this.ssin = ssin;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public boolean isEGiftElig() {
        return isEGiftElig;
    }

    public void setEGiftElig(boolean isEGiftElig) {
        this.isEGiftElig = isEGiftElig;
    }

    public String getMpPgmType() {
        return mpPgmType;
    }

    public void setMpPgmType(String mpPgmType) {
        this.mpPgmType = mpPgmType;
    }

    public Integer getOfferCnt() {
        return offerCnt;
    }

    public void setOfferCnt(Integer offerCnt) {
        this.offerCnt = offerCnt;
    }

    public boolean isReserveIt() {
        return isReserveIt;
    }

    public void setReserveIt(boolean isReserveIt) {
        this.isReserveIt = isReserveIt;
    }

    public boolean isAlcohol() {
        return isAlcohol;
    }

    public void setAlcohol(boolean isAlcohol) {
        this.isAlcohol = isAlcohol;
    }

    public boolean isPerishable() {
        return isPerishable;
    }

    public void setPerishable(boolean isPerishable) {
        this.isPerishable = isPerishable;
    }

    public boolean isRefrigeration() {
        return isRefrigeration;
    }

    public void setRefrigeration(boolean isRefrigeration) {
        this.isRefrigeration = isRefrigeration;
    }

    public boolean isTobacco() {
        return isTobacco;
    }

    public void setTobacco(boolean isTobacco) {
        this.isTobacco = isTobacco;
    }

    public boolean isFreezing() {
        return isFreezing;
    }

    public void setFreezing(boolean isFreezing) {
        this.isFreezing = isFreezing;
    }

    public boolean isWebExcl() {
        return isWebExcl;
    }

    public void setWebExcl(boolean isWebExcl) {
        this.isWebExcl = isWebExcl;
    }

    public Set<String> getDfltFfmDisplay() {
        return dfltFfmDisplay;
    }

    public void setDfltFfmDisplay(Set<String> dfltFfmDisplay) {
        this.dfltFfmDisplay = dfltFfmDisplay;
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

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public Set<String> getViewOnly() {
        return viewOnly;
    }

    public void setViewOnly(Set<String> viewOnly) {
        this.viewOnly = viewOnly;
    }

    public boolean isAutomotive() {
        return isAutomotive;
    }

    public void setAutomotive(boolean isAutomotive) {
        this.isAutomotive = isAutomotive;
    }

    public boolean isEnergyStar() {
        return isEnergyStar;
    }

    public void setEnergyStar(boolean isEnergyStar) {
        this.isEnergyStar = isEnergyStar;
    }

    public boolean isKmartPrElig() {
        return isKmartPrElig;
    }

    public void setKmartPrElig(boolean isKmartPrElig) {
        this.isKmartPrElig = isKmartPrElig;
    }

    public boolean isConfigureTech() {
        return isConfigureTech;
    }

    public void setConfigureTech(boolean isConfigureTech) {
        this.isConfigureTech = isConfigureTech;
    }

    public boolean isOutletItem() {
        return isOutletItem;
    }

    public void setOutletItem(boolean isOutletItem) {
        this.isOutletItem = isOutletItem;
    }

    public boolean isSimiElig() {
        return isSimiElig;
    }

    public void setSimiElig(boolean isSimiElig) {
        this.isSimiElig = isSimiElig;
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

    public boolean isIntlShipElig() {
        return isIntlShipElig;
    }

    public void setIntlShipElig(boolean isIntlShipElig) {
        this.isIntlShipElig = isIntlShipElig;
    }

    public boolean isSResElig() {
        return isSResElig;
    }

    public void setSResElig(boolean isSResElig) {
        this.isSResElig = isSResElig;
    }

    public boolean isDoNotEmailMe() {
        return isDoNotEmailMe;
    }

    public void setDoNotEmailMe(boolean isDoNotEmailMe) {
        this.isDoNotEmailMe = isDoNotEmailMe;
    }

    public boolean isSpuElig() {
        return isSpuElig;
    }

    public void setSpuElig(boolean isSpuElig) {
        this.isSpuElig = isSpuElig;
    }

    public boolean isLayawayElig() {
        return isLayawayElig;
    }

    public void setLayawayElig(boolean isLayawayElig) {
        this.isLayawayElig = isLayawayElig;
    }

    public boolean isSTSElig() {
        return isSTSElig;
    }

    public void setSTSElig(boolean isSTSElig) {
        this.isSTSElig = isSTSElig;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Integer> getSellerId() {
        return sellerId;
    }

    public void setSellerId(Set<Integer> sellerId) {
        this.sellerId = sellerId;
    }

    public Set<String> getSellerName() {
        return sellerName;
    }

    public void setSellerName(Set<String> sellerName) {
        this.sellerName = sellerName;
    }

    public boolean isMatureContent() {
        return isMatureContent;
    }

    public void setMatureContent(boolean isMatureContent) {
        this.isMatureContent = isMatureContent;
    }

    public boolean isDealFlash() {
        return isDealFlash;
    }

    public void setDealFlash(boolean isDealFlash) {
        this.isDealFlash = isDealFlash;
    }

    public String getGiftCardRank() {
        return giftCardRank;
    }

    public void setGiftCardRank(String giftCardRank) {
        this.giftCardRank = giftCardRank;
    }

    public List<String> getVocTagNames() {
        return vocTagNames;
    }

    public void setVocTagNames(List<String> vocTagNames) {
        this.vocTagNames = vocTagNames;
    }

    public List<String> getStoreHierarchyNames() {
        return storeHierarchyNames;
    }

    public void setStoreHierarchyNames(List<String> storeHierarchyNames) {
        this.storeHierarchyNames = storeHierarchyNames;
    }

    public String getClickCaptureUrl() {
        return clickCaptureUrl;
    }

    public void setClickCaptureUrl(String clickCaptureUrl) {
        this.clickCaptureUrl = clickCaptureUrl;
    }

    public String getInternalRimSts() {
        return internalRimSts;
    }

    public void setInternalRimSts(String internalRimSts) {
        this.internalRimSts = internalRimSts;
    }

    public List<String> getSites() {
        return sites;
    }

    public void setSites(List<String> sites) {
        this.sites = sites;
    }

    public Map<Sites, LocalDate> getOldestOnlineDateBySite() {
        return oldestOnlineDateBySite;
    }

    public void setOldestOnlineDateBySite(Map<Sites, LocalDate> oldestOnlineDateBySite) {
        this.oldestOnlineDateBySite = oldestOnlineDateBySite;
    }

    public boolean isDirectSears() {
        return directSears;
    }

    public void setDirectSears(boolean directSears) {
        this.directSears = directSears;
    }

    public String getGndFreeStartDt() {
        return gndFreeStartDt;
    }

    public void setGndFreeStartDt(String gndFreeStartDt) {
        this.gndFreeStartDt = gndFreeStartDt;
    }

    public String getGndFreeEndDt() {
        return gndFreeEndDt;
    }

    public void setGndFreeEndDt(String gndFreeEndDt) {
        this.gndFreeEndDt = gndFreeEndDt;
    }

    public Double getCatConfScore() {
        return catConfScore;
    }

    public void setCatConfScore(Double catConfScore) {
        this.catConfScore = catConfScore;
    }

    public List<String> getSTSChannels() {
        return STSChannels;
    }

    public void setSTSChannels(List<String> sTSChannels) {
        STSChannels = sTSChannels;
    }

    public List<AutomotiveOffer> getAutomotiveOffers() {
        if (automotiveOffers == null) {
            automotiveOffers = new ArrayList<>();
        }
        return automotiveOffers;
    }

    public void setAutomotiveOffers(List<AutomotiveOffer> automotiveOffers) {
        this.automotiveOffers = automotiveOffers;
    }

    public Map<String, Integer> getOfferToSellerIdMap() {
        if (offerToSellerIdMap == null) {
            return new HashMap<>();
        }
        return offerToSellerIdMap;
    }

    public void setOfferToSellerIdMap(Map<String, Integer> offerToSellerIdMap) {
        this.offerToSellerIdMap = offerToSellerIdMap;
    }

    public String getFreeShipThreshold() {
        return freeShipThreshold;
    }

    public void setFreeShipThreshold(String freeShipThreshold) {
        this.freeShipThreshold = freeShipThreshold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Map<Sites, Boolean> getSitesDispEligibility() {
        if (sitesDispEligibility == null) {
            sitesDispEligibility = new HashMap<>();
        }
        return sitesDispEligibility;
    }

    public void setSitesDispEligibility(Map<Sites, Boolean> sitesDispEligibility) {
        this.sitesDispEligibility = sitesDispEligibility;
    }

    public String getKmartOfferParentId() {
        return kmartOfferParentId;
    }

    public void setKmartOfferParentId(String kmartOfferParentId) {
        this.kmartOfferParentId = kmartOfferParentId;
    }
    
    public Map<String, String> getOfferIdParentId() {
        if (offerIDParentID == null){
            offerIDParentID = new HashMap<>();
        }
        return offerIDParentID;
    }

    public void setOfferIdParentId(Map<String, String> offerIDParentID) {
        this.offerIDParentID = offerIDParentID;
    }

    public Map<String, String> getOfferIdKsnMap() {
        return offerIdKsnMap;
    }

    public void setOfferIdKsnMap(Map<String, String> offerIdKsnMap) {
        this.offerIdKsnMap = offerIdKsnMap;
    }

	/**
	 * @return the offerMailability
	 */
	public Map<String, Boolean> getOfferMailability() {
		if(offerMailability==null) {
			offerMailability = new HashMap<>();
		}
		return offerMailability;
	}

	/**
	 * @param offerMailability the offerMailability to set
	 */
	public void setOfferMailability(Map<String, Boolean> offerMailability) {
		this.offerMailability = offerMailability;
	}

	/**
	 * @return the offerWeight
	 */
	public Map<String, Double> getOfferWeight() {
		if(offerWeight==null) {
			offerWeight = new HashMap<>();
		}
		return offerWeight;
	}

	/**
	 * @param offerWeight the offerWeight to set
	 */
	public void setOfferWeight(Map<String, Double> offerWeight) {
		this.offerWeight = offerWeight;
	}

	/**
	 * Returns the map of offerId and swatch product options for corresponding offer-id.
	 * @return the linkedSwatchProductOptions
	 */
	public Map<String, List<ProductOption>> getLinkedSwatchProductOptions() {
		if(linkedSwatchProductOptions==null) {
			linkedSwatchProductOptions = new HashMap<>();
		}
		return linkedSwatchProductOptions;
	}

	/**
	 * @param linkedSwatchProductOptions the linkedSwatchProductOptions to set
	 */
	public void setLinkedSwatchProductOptions(Map<String, List<ProductOption>> linkedSwatchProductOptions) {
		this.linkedSwatchProductOptions = linkedSwatchProductOptions;
	}

	/**
	 * @return the mainImgs
	 */
	public Map<String, String> getMainImgs() {
		return mainImgs;
	}

	/**
	 * @param mainImgs the mainImgs to set
	 */
	public void setMainImgs(Map<String, String> mainImgs) {
		this.mainImgs = mainImgs;
	}

	/**
	 * @return the swatchImgs
	 */
	public Map<String, String> getSwatchImgs() {
		return swatchImgs;
	}

	/**
	 * @param swatchImgs the swatchImgs to set
	 */
	public void setSwatchImgs(Map<String, String> swatchImgs) {
		this.swatchImgs = swatchImgs;
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

    public String getDeliveryCat() {
        return deliveryCat;
    }

    public void setDeliveryCat(String deliveryCat) {
        this.deliveryCat = deliveryCat;
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

	/**
	 * @return the searsDispEligibilityStatus
	 */
	public Map<String, Boolean> getSearsDispEligibilityStatus() {
		if(searsDispEligibilityStatus==null) {
			searsDispEligibilityStatus = new HashMap<>();
		}
		return searsDispEligibilityStatus;
	}

	/**
	 * @param searsDispEligibilityStatus the searsDispEligibilityStatus to set
	 */
	public void setSearsDispEligibilityStatus(Map<String, Boolean> searsDispEligibilityStatus) {
		this.searsDispEligibilityStatus = searsDispEligibilityStatus;
	}
}
