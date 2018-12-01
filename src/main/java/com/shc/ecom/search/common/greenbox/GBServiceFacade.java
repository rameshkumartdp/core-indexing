package com.shc.ecom.search.common.greenbox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.shc.common.misc.PropertiesLoader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.shc.ecom.gb.doc.common.DomainObject;
import com.shc.ecom.gb.doc.content.Content;
import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.gb.doc.varattrs.VarAttributes;
import com.shc.ecom.search.common.constants.BucketFilters;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.greenbox.bucket.Buckets;
import com.shc.ecom.search.common.store.Store;
import com.shc.ecom.search.common.vo.buybox.BuyBoxDomain;
import com.shc.ecom.search.config.GlobalConstants;

/**
 * @author rgopala Jul 15, 2015 ssin-producer
 */

@Component
public class GBServiceFacade implements Serializable {

    private static final long serialVersionUID = 662636517624578429L;

    String getApiPath;
    String bucketApiPath;
    String filterApiPath;
    String altKeyApiPath;
    String contentCollection;
    String offerCollection;
    String varattrCollection;
    String storeCollection;
    
    @Autowired
    private RankingService rankingService;
    @Autowired
    private GreenboxService greenboxService;
    private int partitionCount = 2;

    @PostConstruct
    public void init() {
        getApiPath = PropertiesLoader.getProperty(GlobalConstants.GB_API_GET_URL);
        bucketApiPath = PropertiesLoader.getProperty(GlobalConstants.GB_API_BUCKET_URL);
        filterApiPath = PropertiesLoader.getProperty(GlobalConstants.GB_API_GET_FILT_URL);
        altKeyApiPath = PropertiesLoader.getProperty(GlobalConstants.GB_API_GET_ALT_URL);


        contentCollection = PropertiesLoader.getProperty(GlobalConstants.GB_CONTENT_COLLECTION_NAME);
        offerCollection = PropertiesLoader.getProperty(GlobalConstants.GB_OFFER_COLLECTION_NAME);
        varattrCollection = PropertiesLoader.getProperty(GlobalConstants.GB_VARATTR_COLLECTION_NAME);
        storeCollection = PropertiesLoader.getProperty(GlobalConstants.GB_STORE_COLLECTION_NAME);

        partitionCount = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.PARTITION_COUNT));
    }

    public BuyBoxDomain getBuyBoxDocument(String uid, String site) {
        BuyBoxDomain buyBoxDomain = rankingService.getBuyBox(uid, site);
        return buyBoxDomain;
    }

    public Content getContentDoc(String ssin) {

        DomainObject domainObject = getDomainDoc(ssin);
        if (domainObject != null && domainObject.get_blob().getContent() != null) {
            return domainObject.get_blob().getContent();
        }
        return new Content();
    }

    public DomainObject getDomainDoc(String ssin) {
        List<DomainObject> domainObjects = greenboxService.getDomainObject(getApiPath, contentCollection, ssin);
        if (CollectionUtils.isNotEmpty(domainObjects)) {
            return domainObjects.get(0);
        }
        return new DomainObject();
    }

    public DomainObject getOfferDomainDoc(String offerId) {
        List<DomainObject> domainObjects = greenboxService.getDomainObject(getApiPath, offerCollection, offerId);
        if (CollectionUtils.isNotEmpty(domainObjects)) {
            return domainObjects.get(0);
        }
        return new DomainObject();
    }
    public Buckets getBuckets() {
        Map<String, String> bucketsMap = greenboxService.getBuckets(bucketApiPath, offerCollection);
        Buckets bucket = new Buckets();
        String start = bucketsMap.entrySet().iterator().next().getKey();
        String end = bucketsMap.get(start);
        bucket.setStart(Integer.parseInt(start));
        bucket.setEnd(Integer.parseInt(end));
        return bucket;
    }

    public List<String> getSsinList(List<String> offerIds) {
        List<List<String>> partitionedOfferIdsList = Lists.partition(offerIds, partitionCount);
        List<String> ssinList = new ArrayList<>();
        for (List<String> partitionedOfferIds : partitionedOfferIdsList) {
            List<DomainObject> domainObjList = greenboxService.getDomainObjects(getApiPath, offerCollection, partitionedOfferIds);
            if (CollectionUtils.isNotEmpty(domainObjList)) {
                for (DomainObject domainObject : domainObjList) {
                    String ssin = domainObject.get_blob().getOffer().getIdentity().getSsin();
                    if (StringUtils.isNotBlank(ssin)) {
                        ssinList.add(ssin);
                    }
                }
            }
        }

        return ssinList;
    }

    /**
     * Provides a map of SSIN to OFFER-ID for the list of given offerIds based on the offer-type.
     * That is, if given offer type is "NV", only for non-variation items, the returned map contains
     * mapping between SSIN (key) and offer-id (value).  For other types, it just gives SSIN and a null value.
     *
     * @param offerIds
     * @param offerType
     * @return map with key being SSIN and value being either offerId or null
     */
    public Map<String, String> getSsinAndOfferMapBasedOnOfferType(List<String> offerIds, String offerType) {
        Map<String, String> ssinToOfferMap = new HashMap<>();

        List<Offer> offers = getOfferDocsList(offerIds);
        for (Offer offer : offers) {
            String ssin = offer.getIdentity().getSsin();
            String offerId = offer.getId();
            String currentOfferType = offer.getClassifications().getOfferType();
            if (StringUtils.isNotBlank(ssin)) {
                if (StringUtils.endsWithIgnoreCase(currentOfferType, offerType)) {
                    ssinToOfferMap.put(ssin, offerId);
                } else {
                    ssinToOfferMap.put(ssin, null);
                }
            }
        }
        return ssinToOfferMap;
    }


    /**
     * Provides a list of SSINs for the given offerIds based on the offer-type.
     * That is, if given offer type is "NV", only SSINs for non-variations will be returned.
     * The list doesn't contain values for other types.
     *
     * @param offerIds
     * @param offerType
     * @return map with key being SSIN and value being either offerId or null
     */
    public List<String> getSsinForOfferType(List<String> offerIds, String offerType) {
        List<String> ssins = new ArrayList<>();

        List<Offer> offers = getOfferDocsList(offerIds);
        for (Offer offer : offers) {
            String ssin = offer.getIdentity().getSsin();
            String currentOfferType = offer.getClassifications().getOfferType();
            if (StringUtils.isNotBlank(ssin) && StringUtils.endsWithIgnoreCase(currentOfferType, offerType)) {
                    ssins.add(ssin);
            }
        }
        return ssins;
    }

    public List<String> getOfferList(String storeName, String bucketId, String fromDateTime, String toDateTime) {
        BucketFilters bucketFilters = Stores.getBucketFilters(storeName);
        List<String> pgrmTypeFilters = bucketFilters.getPgrmTypeFilters();
        List<String> offerIds = new ArrayList<>();
        for (String pgrmTypeFilter : pgrmTypeFilters) {
            offerIds.addAll(greenboxService.getIdsFromBuckets(filterApiPath, offerCollection, bucketId, "pgrmType", pgrmTypeFilter, fromDateTime, toDateTime));
        }
        return offerIds;
    }

    public Set<List<String>> getSetOfOfferIds(List<String> offerIdList) {
        Set<List<String>> offerIdsSet = new HashSet<List<String>>();
        if (CollectionUtils.isNotEmpty(offerIdList)) {
            List<List<String>> partitionedOfferIdsList = Lists.partition(offerIdList, partitionCount);
            offerIdsSet = new HashSet<List<String>>(partitionedOfferIdsList);
        }
        return offerIdsSet;
    }
    
    public List<String> getActiveStoreIdsByAltKey() {
    	List<String> storeIds = greenboxService.getActiveStoreIds(altKeyApiPath, storeCollection, null);
    	if(storeIds==null) {
    		storeIds = new ArrayList<>();
    	}
    	return storeIds;
    }
    
    public List<String> getActiveStoreIdsByAltKey(String store) {
    	List<String> storeIds = greenboxService.getActiveStoreIds(altKeyApiPath, storeCollection, store);
    	if(storeIds==null) {
    		storeIds = new ArrayList<>();
    	}
    	return storeIds;
    }
    
    

    public List<String> getOfferIdsByAltKey(String ssin) {
        List<String> offerIds = greenboxService.getOfferIdsForSSIN(altKeyApiPath, offerCollection, ssin);
        if (offerIds == null) {
            offerIds = new ArrayList<>();
        }
        return offerIds;
    }

    public List<String> getOfferIdsbyAltKeyForBundles(String ssin)
    {
        List<String> offerIds =  offerIds = greenboxService.getOfferIdsForParentID(altKeyApiPath,offerCollection,ssin);
        if (offerIds == null) {
            offerIds = new ArrayList<>();
        }
        return offerIds;

    }

    public List<Offer> getOfferDocsList(List<String> offerIds) {
        List<Offer> offerDocs = new ArrayList<Offer>();
        List<List<String>> partitionedOfferIdsList = Lists.partition(offerIds, partitionCount);
        for (List<String> partitionedOfferIds : partitionedOfferIdsList) {
            List<DomainObject> domainObjList = greenboxService.getDomainObjects(getApiPath, offerCollection, partitionedOfferIds);
            if (CollectionUtils.isNotEmpty(domainObjList)) {
                for (DomainObject domainObject : domainObjList) {
                    if (domainObject != null && domainObject.get_blob().getOffer() != null) {
                        offerDocs.add(domainObject.get_blob().getOffer());
                    }
                }
            }
        }
        return offerDocs;
    }

    public Offer getOfferDoc(String offerId) {
        DomainObject domainObject = getOfferDomainDoc(offerId);
        if (domainObject != null && domainObject.get_blob().getOffer() != null) {
            return domainObject.get_blob().getOffer();
        }
        return null;
    }
    public VarAttributes getVarAttrDoc(String ssin) {
        List<DomainObject> domainObjects = greenboxService.getDomainObject(getApiPath, varattrCollection, ssin);
        VarAttributes varAttribute = null;

        if (CollectionUtils.isNotEmpty(domainObjects)) {
            varAttribute = domainObjects.get(0).get_blob().getAttributes();
        }
        if (varAttribute == null) {
            varAttribute = new VarAttributes();
        }
        return varAttribute;
    }

    public Store getStoreDoc(String storeId) {
    	List<Store> stores = greenboxService.getStoreDoc(getApiPath, storeCollection, storeId);
    	Store store = null;
    	if(CollectionUtils.isNotEmpty(stores)) {
    		store = stores.get(0);
    	}
    	if(store==null) {
    		store = new Store();
    	}
    	return store;
    }
    
    
    
    public List<String> getContentIds(String storeName, String bucketId, String fromDateTime, String toDateTime) {
        List<String> contentIds = new ArrayList<>();
        BucketFilters bucketFilters = Stores.getBucketFilters(storeName);
        List<String> catSubTypeFilters = bucketFilters.getCatentrySubTypeFilters();
        for (String filter : catSubTypeFilters) {
            contentIds.addAll(greenboxService.getIdsFromBuckets(filterApiPath, contentCollection, bucketId, "catentrySubType", filter, fromDateTime, toDateTime));
        }
        return contentIds;
    }
}
