package com.shc.ecom.search.common.greenbox;

import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.gb.doc.common.DomainObject;
import com.shc.ecom.gb.doc.content.Content;
import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.gb.doc.varattrs.VarAttributes;
import com.shc.ecom.rtc.util.GBCommonUtils;
import com.shc.ecom.search.common.constants.BucketFilters;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.exception.ErrorCode;
import com.shc.ecom.search.common.exception.SearchCommonException;
import com.shc.ecom.search.common.greenbox.bucket.Buckets;
import com.shc.ecom.search.common.service.GBServiceUtil;
import com.shc.ecom.search.common.vo.buybox.BuyBoxDomain;
import com.shc.ecom.search.config.GlobalConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;

/**
 * @author rgopala Jul 15, 2015 ssin-producer
 */

public class GBFacade implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GBFacade.class.getName());

    private static final long serialVersionUID = 662636517624578429L;
    @Autowired
    protected GBServiceUtil gbServiceUtil;
    @Autowired
    private Buckets bucket;
    private String contentCollectionName;
    private String offerCollectionName;
    private String varAttrCollectionName;
    private String gbApiHostName;
    private int gbApiport;
    private int apiReqLimit;
    private boolean gbRetryLogic;
    private int gbRetryCount;
    private String rankCollectionName;
    private String buyBoxHostName;
    private String buyBoxGetUrl;
    private int buyBoxPort;

    @PostConstruct
    public void init() {
        contentCollectionName = PropertiesLoader.getProperty(GlobalConstants.GB_CONTENT_COLLECTION_NAME);
        offerCollectionName = PropertiesLoader.getProperty(GlobalConstants.GB_OFFER_COLLECTION_NAME);
        varAttrCollectionName = PropertiesLoader.getProperty(GlobalConstants.GB_VARATTR_COLLECTION_NAME);
        gbApiHostName = PropertiesLoader.getProperty(GlobalConstants.GB_HOST);
        gbApiport = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.GB_PORT));
        //This proeprty does not exist anywhere
        apiReqLimit = 10;
        gbRetryLogic = Boolean.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_FLAG));
        gbRetryCount = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_COUNT));

        rankCollectionName = PropertiesLoader.getProperty(GlobalConstants.BUYBOX_RANK_COLLECTION);
        buyBoxHostName = PropertiesLoader.getProperty(GlobalConstants.BUYBOX_HOST_URL);
        buyBoxGetUrl = PropertiesLoader.getProperty(GlobalConstants.BUYBOX_RANK_GET_URL);
        buyBoxPort = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.BUYBOX_PORT));
    }

    public BuyBoxDomain getBuyBoxDocument(String uuid) {
        StringBuilder uidParam = new StringBuilder("uid=");
        uidParam.append(uuid);
        BuyBoxDomain buyBoxDomain = null;
        try {
            buyBoxDomain = gbServiceUtil.getJsonDomainObject(uidParam.toString(), rankCollectionName, buyBoxHostName, buyBoxPort, buyBoxGetUrl, BuyBoxDomain.class, "No offers to rank");
        } catch (Exception e) {
            LOGGER.error("Error retrieving the buyBox document. ", e);
        }
        if (buyBoxDomain == null) {
            buyBoxDomain = new BuyBoxDomain();
        }
        return buyBoxDomain;
    }

    public Content getContentDoc(String ssin) {
        Content content = null;
        try {
            DomainObject domainObject = gbServiceUtil.getJsonDomainObject(ssin, contentCollectionName, gbApiHostName, gbApiport);
            if (domainObject != null && domainObject.get_blob().getContent() != null) {
                content = domainObject.get_blob().getContent();
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving the content document. ", e);
        }
        if (content == null) {
            content = new Content();
        }
        return content;
    }

    public DomainObject getDomainDoc(String ssin) {
        DomainObject domainObject = null;
        try {
            domainObject = gbServiceUtil.getJsonDomainObject(ssin, contentCollectionName, gbApiHostName, gbApiport);
        } catch (Exception e) {
            LOGGER.error("Error retrieving the domain document. ", e);
        }
        if (domainObject == null) {
            domainObject = new DomainObject();
        }
        return domainObject;
    }

    public Buckets getBuckets() {
        try {
            Map<String, String> bucketIdMap = gbServiceUtil.getBucketIdsMap(offerCollectionName, gbApiHostName, gbApiport);

            if (bucketIdMap == null || bucketIdMap.isEmpty()) {
                throw new SearchCommonException(ErrorCode.GB_OFFER_BUCKET_EMPTY, offerCollectionName + gbApiHostName + gbApiport);
            }

            String start = bucketIdMap.entrySet().iterator().next().getKey();
            String end = bucketIdMap.get(start);
            bucket.setStart(Integer.parseInt(start));
            bucket.setEnd(Integer.parseInt(end));

        } catch (Exception e) {
            LOGGER.error("Error retrieving buckets. ", e);
        }

        return bucket;
    }

    public List<String> getSsinList(List<String> offerIds) {
        List<DomainObject> domainObjList = new ArrayList<>();
        List<String> ssinList = new ArrayList<>();
        try {
            gbServiceUtil.getJsonDomainList(offerIds, domainObjList, null, offerCollectionName, gbApiHostName, gbApiport);
            if (CollectionUtils.isNotEmpty(domainObjList)) {
                for (DomainObject domainObject : domainObjList) {
                    String ssin = domainObject.get_blob().getOffer().getIdentity().getSsin();
                    if (StringUtils.isNotBlank(ssin)) {
                        ssinList.add(ssin);
                    }
                }
            }
        } catch (Exception tw) {
            LOGGER.error("Error retrieving the ssinList. ", tw);
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
        List<DomainObject> domainObjList = new ArrayList<>();
        Map<String, String> ssinToOfferMap = new HashMap<>();
        try {
            gbServiceUtil.getJsonDomainList(offerIds, domainObjList, null, offerCollectionName, gbApiHostName, gbApiport);
            if (CollectionUtils.isNotEmpty(domainObjList)) {
                for (DomainObject domainObject : domainObjList) {
                    String ssin = domainObject.get_blob().getOffer().getIdentity().getSsin();
                    String offerId = domainObject.get_blob().getOffer().getId();
                    String currentOfferType = domainObject.get_blob().getOffer().getClassifications().getOfferType();
                    if (StringUtils.isNotBlank(ssin)) {
                        if (StringUtils.endsWithIgnoreCase(currentOfferType, offerType)) {
                            ssinToOfferMap.put(ssin, offerId);
                        } else {
                            ssinToOfferMap.put(ssin, null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving the ssinList. ", e);
        }

        return ssinToOfferMap;
    }

    public List<String> getOfferList(String storeName, String bucketId) {
        return gbServiceUtil.getOfferIds(storeName, bucketId);
    }

    public Set<List<String>> getSetOfOfferIds(List<String> offerIdList) {
        if (CollectionUtils.isNotEmpty(offerIdList)) {
            return GBCommonUtils.getPidSet(offerIdList, apiReqLimit);
        } else {
            return new HashSet<List<String>>();
        }
    }

    public List<String> getOfferIdsByAltKey(String ssin) {
        StringBuilder altKey = new StringBuilder("ssin=");
        altKey.append(ssin);
        return gbServiceUtil.getIdsByAltId(altKey.toString(), offerCollectionName, gbApiHostName, gbApiport);
    }

    public List<Offer> getOfferDocsList(List<String> offerIds) throws Exception {
        List<Offer> offerDocs = new ArrayList<Offer>();
        int retryCount = 0;
        List<DomainObject> offerDomainObjList = new ArrayList<>();
        while (retryCount < gbRetryCount) {
            int response = gbServiceUtil.getJsonDomainList(offerIds, offerDomainObjList, null, offerCollectionName, gbApiHostName, gbApiport);
            if (response == -1 && gbRetryLogic) {
                retryCount++;
            } else {
                break;
            }
        }
        for (DomainObject domainObject : offerDomainObjList) {
            offerDocs.add(domainObject.get_blob().getOffer());
        }
        return offerDocs;
    }

    public VarAttributes getVarAttrDoc(String ssin) throws Exception {
        DomainObject domainObject = gbServiceUtil.getJsonDomainObject(ssin, varAttrCollectionName, gbApiHostName, gbApiport);
        return domainObject.get_blob().getAttributes();
    }

    public List<VarAttributes> getVarAttrDocsList(List<String> ssins) throws Exception {
        List<DomainObject> varAttrDomainObjList = new ArrayList<>();
        List<VarAttributes> varAttributesList = new ArrayList<>();
        int retryCount = 0;
        while (retryCount < gbRetryCount) {
            int response = gbServiceUtil.getJsonDomainList(ssins, varAttrDomainObjList, null, varAttrCollectionName, gbApiHostName, gbApiport);
            if (response == -1 && gbRetryLogic) {
                retryCount++;
            } else {
                break;
            }
        }
        for (DomainObject domainObject : varAttrDomainObjList) {
            varAttributesList.add(domainObject.get_blob().getAttributes());
        }
        return varAttributesList;
    }

    public List<String> getContentIds(String storeName, String bucketId) {
        List<String> contentIds = new ArrayList<>();

        BucketFilters bucketFilters = Stores.getBucketFilters(storeName);
        List<String> catSubTypeFilters = bucketFilters.getCatentrySubTypeFilters();

        for (String filter : catSubTypeFilters) {
            contentIds.addAll(gbServiceUtil.getPartnumbers(contentCollectionName, bucketId, "?catentrySubType=" + filter));
        }
        return contentIds;
    }
}
