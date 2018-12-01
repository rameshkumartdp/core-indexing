package com.shc.ecom.search.common.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.gb.doc.common.DomainObject;
import com.shc.ecom.rtc.connector.WebServiceUtil;
import com.shc.ecom.rtc.util.JsonUtil;
import com.shc.ecom.search.common.constants.BucketFilters;
import com.shc.ecom.search.common.constants.SearchCommonConstants;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.config.GlobalConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Serializable;
import java.util.*;

/**
 * This is a util class to serialize/deserialize json using jackson
 *
 * @author hsasiku
 */
public class GBServiceUtil implements Serializable {

    private static final long serialVersionUID = 2556112016452432928L;

    private static final Logger LOGGER = LoggerFactory.getLogger(GBServiceUtil.class.getName());

    @Autowired
    @Qualifier("webServiceUtil")
    private WebServiceUtil webServiceUtil;


    /**
     * To get the bucketIds from the provided collection
     *
     * @param collectionName
     * @return
     * @throws Throwable
     */
    public Map<String, String> getBucketIdsMap(String collectionName, String hostName, int port) throws Exception {
        Map<String, String> bucketIdsMap = null;
        int retryCount = 0;
        Boolean gbRetryLogic = Boolean.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_FLAG));
        Integer gbRetryCount = Integer.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_COUNT));

        try {
            while (true) {
                String response = webServiceUtil.getBucketData(collectionName, hostName, port);

                if (StringUtils.isEmpty(response) || SearchCommonConstants.TIME_OUT_ERROR.equalsIgnoreCase(response)) {
                    LOGGER.error("Got EMPTY response or TIME OUT ERROR in getBucketIdsMap :: " + collectionName);
                } else if (StringUtils.equalsIgnoreCase(response, "[]")) {
                    LOGGER.info("No data found in GB for collection - " + collectionName);
                } else {
                    bucketIdsMap = JsonUtil.toBucketMap(response);
                }

                if (bucketIdsMap == null && gbRetryLogic && retryCount < gbRetryCount) {
                    retryCount++;
                } else {
                    break;
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Exception in getBucketIdsMap:: " + collectionName, ex);
            throw ex;
        }

        return bucketIdsMap;
    }

    /**
     * To get the partnumbers from the provided bucketId and the collection
     *
     * @param collectionName
     * @param bucketId
     * @return
     */
    public List<String> getPartnumbers(String collectionName, String bucketId, String hostName, int port) {
        List<String> partNumberList = Collections.emptyList();
        try {
            // http://green.prod.global.s.com:80/gbox/gb/s/data/get-ids/promo/<<Bucket ID>>
            String partNumResponse = webServiceUtil.getIds(collectionName, bucketId, hostName, port);

            if (StringUtils.equalsIgnoreCase(partNumResponse, SearchCommonConstants.TIME_OUT_ERROR)) {
                return partNumberList;
            } else if (!StringUtils.isEmpty(partNumResponse) && !StringUtils.equalsIgnoreCase(partNumResponse, "[]")) {
                JsonUtil.getObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                partNumberList = JsonUtil.toPartnumbers(partNumResponse);
            }

        } catch (Exception thw) {
            LOGGER.error("Exception while getting partnumbers from collection :: " + collectionName);
        }
        return partNumberList;
    }

    public List<String> getPartnumbers(String collectionName, String bucketId, String bucketFilter) {
        List<String> partnumberList = new ArrayList<>();

        int retryCount = 0;
        Boolean gbRetryLogic = Boolean.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_FLAG));
        Integer gbRetryCount = Integer.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_COUNT));

        try {
            while (true) {
                String partNumberResponse = webServiceUtil.getIdsByFilter(collectionName, bucketId, bucketFilter);
                if (StringUtils.isEmpty(partNumberResponse) || SearchCommonConstants.TIME_OUT_ERROR.equalsIgnoreCase(partNumberResponse)) {
                    LOGGER.error("Got EMPTY response or TIME OUT ERROR in getOfferIds :: ");
                } else if (StringUtils.equalsIgnoreCase(partNumberResponse, "[]")) {
                    LOGGER.info("No data found in GB for collection - for bucket " + bucketId);
                } else if (!StringUtils.isEmpty(partNumberResponse) && !StringUtils.equalsIgnoreCase(partNumberResponse, "[]")) {
                    JsonUtil.getObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                    partnumberList = JsonUtil.toPartnumbers(partNumberResponse);
                }

                if (partnumberList == null && gbRetryLogic && retryCount < gbRetryCount) {
                    retryCount++;
                } else {
                    break;
                }
            }
        } catch (Exception thw) {
            LOGGER.error("Exception in getPartNumbers() for storeName ::" + " bucketId ::" + bucketId, thw);
        }
        return partnumberList;
    }

    public List<String> getOfferIds(String storeName, String bucketId) {
        List<String> partNumberList = new ArrayList<>();

        String offerCollection = PropertiesLoader.getProperty(GlobalConstants.GB_OFFER_COLLECTION_NAME);

        BucketFilters bucketFilters = Stores.getBucketFilters(storeName);
        List<String> pgrmTypeFilters = bucketFilters.getPgrmTypeFilters();

        for (String pgrmTypeFilter : pgrmTypeFilters) {
            partNumberList.addAll(getPartnumbers(offerCollection, bucketId, "?pgrmType=" + pgrmTypeFilter));
        }
        return partNumberList;
    }

    public List<String> getIdsByAltId(String altId, String collectionName, String host, int port) {
        List<String> ids = null;
        int retryCount = 0;
        Boolean gbRetryLogic = Boolean.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_FLAG));
        Integer gbRetryCount = Integer.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_COUNT));

        // http://green.prod.global.s.com/gbox/gb/s/data/get-by-alt-key/offer?parentId=05230893000P

        try {
            while (true) {
                String response = webServiceUtil.getByAltKey(collectionName, altId, host, port);

                if (StringUtils.isEmpty(response) || SearchCommonConstants.TIME_OUT_ERROR.equalsIgnoreCase(response)) {
                    LOGGER.error("Got EMPTY response or TIME OUT ERROR in getIdsByAltId :: " + collectionName);
                } else if (StringUtils.equalsIgnoreCase(response, "[]")) {
                    LOGGER.info("No data found in GB for collection - " + collectionName);
                } else {
                    ids = JsonUtil.toPartnumbers(response);
                }

                if (ids == null && gbRetryLogic && retryCount < gbRetryCount) {
                    retryCount++;
                } else {
                    break;
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Exception in getIdsByAltId:: " + collectionName + "::" + altId, ex);
            // throw new SearchCommonException("Exception in getIdsByAltId:: " + collectionName +"::"+altId, ex);
        }

        return ids;
    }

    public List<String> getBundlePartNumbers(String bucketId, String catentrySubType) {
        List<String> partNumberList = Collections.emptyList();
        try {
            StringBuilder bundleFilter = new StringBuilder("?catentrySubType=");
            bundleFilter.append(catentrySubType);

            // http://green.prod.global.s.com:80/gbox/gb/s/data/filter-ids/content/3975?catentrySubType=B

            String contentCollection = PropertiesLoader.getProperty(GlobalConstants.GB_CONTENT_COLLECTION_NAME);

            String partNumberResponse = webServiceUtil.getIdsByFilter(contentCollection, bucketId, bundleFilter.toString());

            if (StringUtils.equalsIgnoreCase(partNumberResponse, "TIME OUT ERROR")) {
                return partNumberList;
            } else if (!StringUtils.isEmpty(partNumberResponse) && !StringUtils.equalsIgnoreCase(partNumberResponse, "[]")) {
                JsonUtil.getObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                partNumberList = JsonUtil.toPartnumbers(partNumberResponse);
            }

        } catch (Exception thw) {
            LOGGER.error("Exception in getBundlePartNumbers() for bucketId ::" + bucketId, thw);
        }
        return partNumberList;
    }

    /**
     * To get the Ids based on the alternate key from the provided collection name
     *
     * @param altKeyMap
     * @param collectionName
     * @return
     * @throws Throwable
     */
    public List<String> getIdsUsingGetByAltKeys(Map<String, String> altKeyMap, String collectionName, String hostName, int port) throws Exception {
        int count = 0;
        StringBuilder altKeys = new StringBuilder();
        String output = null;
        List<String> idList = null;
        try {
            if (!altKeyMap.isEmpty()) {
                for (String key : altKeyMap.keySet()) {
                    count++;
                    altKeys.append(key).append("=").append(altKeyMap.get(key));
                    if (altKeyMap.size() > 1 && count != altKeyMap.size()) {
                        altKeys.append("&");
                    }
                }
                String response = webServiceUtil.getByAltKey(collectionName, altKeys.toString(), hostName, port);
                if (!StringUtils.isEmpty(response) && !StringUtils.equalsIgnoreCase(response, "[]") && !"TIME OUT ERROR".equalsIgnoreCase(response)) {
                    output = StringUtils.remove(StringUtils.remove(StringUtils.remove(response, '"'), '['), ']');
                    if (StringUtils.isNotBlank(output)) {
                        idList = Arrays.asList(output.split(","));
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Exception in getIdsUsingGetByAltKeys:: " + altKeys, ex);
            throw ex;
        }
        return idList;
    }

    /**
     * expects a empty domainList
     * may or may not pass an empty domainMap. If a map is passed adds the id->domainObjects to the map.
     * returns 200 if success, returns -1 if any request timeouts
     *
     * @param ids
     * @param domainList
     * @param domainMap
     * @param collectionName
     * @return int
     * @throws Throwable
     */
    public int getJsonDomainList(List<String> ids, List<DomainObject> domainList, Map<String, DomainObject> domainMap, String collectionName, String hostName, int port) throws Exception {
        int success = 200;
        int timeout = -1;
        boolean isTimeout = false;
        if (ids == null || ids.isEmpty()) {
            return success;
        }
        Object excepItem = null;
        try {
            Set<List<String>> idSet = getIdSet(ids);
            if (idSet == null || idSet.isEmpty()) {
                return success;
            }
            for (List<String> idList : idSet) {
                excepItem = idList;
                String itmKeysStr = getItemKeysStr(idList);
                int count = (idList == null) ? 0 : idList.size();

                String response = webServiceUtil.getData(collectionName, itmKeysStr, hostName, port);

                if (StringUtils.equalsIgnoreCase(response, SearchCommonConstants.TIME_OUT_ERROR)) {
                    LOGGER.info(SearchCommonConstants.TIME_OUT_ERROR + " :: " + itmKeysStr);
                    gbGetCallCounts(collectionName, count, false);
                    isTimeout = true;
                    continue;
                } else if (!StringUtils.isEmpty(response) && !StringUtils.equalsIgnoreCase(response, "[]")) {
                    try {

                        gbGetCallCounts(collectionName, count, true);
                        List<DomainObject> domainObjectList = JsonUtil.toDomainObjectList(response);
                        for (DomainObject respPojo : domainObjectList) {
                            if (respPojo == null || respPojo.get_id() == null) {
                                continue;
                            }
                            if (domainList != null) {
                                domainList.add(respPojo);
                            }
                            if (domainMap != null) {
                                domainMap.put(respPojo.get_id(), respPojo);
                            }
                        }

                    } catch (Exception e) {
                        LOGGER.error("Exception while converting the json response for collection :: " + collectionName + ", Id List :: " + excepItem, e);
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Exception in getJsonDomainList:: for collection :: " + collectionName + ", Id List :: " + excepItem, ex);
            throw ex;
        }

        if (isTimeout) {
            return timeout;
        }
        return success;
    }

    /**
     * This methods is to get the GB document for a single partnumber from the provided collection name
     *
     * @param id
     * @param collectionName
     * @return
     * @throws Throwable
     */
    public DomainObject getJsonDomainObject(String id, String collectionName, String hostName, int port) throws Exception {
        DomainObject domainObject = null;
        int retryCount = 0;
        try {
            Boolean gbRetryLogic = Boolean.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_FLAG));
            Integer gbRetryCount = Integer.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_COUNT));

            while (true) {
                int count = 1;
                String response = webServiceUtil.getData(collectionName, id, hostName, port);

                if (StringUtils.equalsIgnoreCase(response, SearchCommonConstants.TIME_OUT_ERROR)) {
                    LOGGER.error(SearchCommonConstants.TIME_OUT_ERROR + " :: " + id);
                    gbGetCallCounts(collectionName, count, false);
                } else if (StringUtils.isNotEmpty(response) || !StringUtils.equalsIgnoreCase(response, "[]")) {
                    gbGetCallCounts(collectionName, count, true);
                    List<DomainObject> domainObjectList = JsonUtil.toDomainObjectList(response);
                    if (!CollectionUtils.isEmpty(domainObjectList)) {
                        domainObject = domainObjectList.get(0);
                    }
                } else {
                    LOGGER.info("No data found in GB in " + collectionName + " for id :: " + id);
                }

                if (domainObject == null && gbRetryLogic && retryCount < gbRetryCount) {
                    retryCount++;
                } else {
                    break;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Exception while getting single JSON document from collection :: " + collectionName + " for id :: " + id, ex);
            throw ex;
        }

        return domainObject;
    }

    /**
     * This methods is to get the Deleted GB document for a single partnumber from the provided collection name
     *
     * @param id
     * @param collectionName
     * @return
     * @throws Throwable
     */
    public DomainObject getDelJsonDomainObject(String id, String collectionName, String hostName, int port) throws Exception {
        DomainObject domainObject = null;
        int retryCount = 0;
        try {
            Boolean gbRetryLogic = Boolean.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_FLAG));
            Integer gbRetryCount = Integer.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_COUNT));

            while (true) {
                int count = 1;
                String response = webServiceUtil.getDeletedData(collectionName, id, hostName, port);

                if (StringUtils.equalsIgnoreCase(response, SearchCommonConstants.TIME_OUT_ERROR)) {
                    LOGGER.error(SearchCommonConstants.TIME_OUT_ERROR + " :: " + id);
                    gbGetCallCounts(collectionName, count, false);
                } else if (StringUtils.isNotEmpty(response) || !StringUtils.equalsIgnoreCase(response, "[]")) {
                    gbGetCallCounts(collectionName, count, true);
                    List<DomainObject> domainObjectList = JsonUtil.toDomainObjectList(response);
                    if (!CollectionUtils.isEmpty(domainObjectList)) {
                        domainObject = domainObjectList.get(0);
                    }
                } else {
                    LOGGER.info("No data found in GB in Deleted " + collectionName + " for id :: " + id);
                }

                if (domainObject == null && gbRetryLogic && retryCount < gbRetryCount) {
                    retryCount++;
                } else {
                    break;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Exception while getting single JSON document from deleted collection :: " + collectionName + " for id :: " + id, ex);
            throw ex;
        }

        return domainObject;
    }

    /**
     * This methods is to get the document for a single id from the provided collection name, host , port and
     * convert to the provided classType T
     *
     * @param id
     * @param collectionName
     * @return
     * @throws Throwable
     */
    public <T> T getJsonDomainObject(String id, String collectionName, String hostName, int port, String getUrl, Class<T> classType, String noResponseData) throws Exception {
        T domainObject = null;
        int retryCount = 0;
        try {
            Boolean gbRetryLogic = Boolean.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_FLAG));
            Integer gbRetryCount = Integer.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_COUNT));

            while (true) {
                int count = 1;
                String response = webServiceUtil.getByAltKey(collectionName, getUrl, id, hostName, port);

                if (StringUtils.equalsIgnoreCase(response, SearchCommonConstants.TIME_OUT_ERROR)) {
                    LOGGER.error(SearchCommonConstants.TIME_OUT_ERROR + " :: " + id);
                    gbGetCallCounts(collectionName, count, false);
                } else if (StringUtils.isNotEmpty(response) || !StringUtils.equalsIgnoreCase(response, noResponseData)) {
                    gbGetCallCounts(collectionName, count, true);// TODO:: CHANGE THE COUNTER
                    domainObject = JsonUtil.toDomainObject(response, classType);
                } else {
                    LOGGER.info("No data found in " + hostName + " in  " + collectionName + " for id :: " + id);
                }

                if (domainObject == null && gbRetryLogic && retryCount < gbRetryCount) {
                    retryCount++;
                } else {
                    break;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Exception while getting single JSON document from collection :: " + collectionName + " for id :: " + id, ex);
            throw ex;
        }

        return domainObject;
    }

    /**
     * This methods is to get the pricing grid data for a single partnumber
     *
     * @return
     * @throws Throwable
     */
    public Map<String, Object> getPricingGridData(String urlParam) throws Exception {
        Map<String, Object> pricingData = null;
        int retryCount = 0;
        try {

            Boolean gbRetryLogic = Boolean.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_FLAG));
            Integer gbRetryCount = Integer.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_COUNT));
            while (true) {

                String response = webServiceUtil.getPriceGrid(urlParam);

                if (StringUtils.equalsIgnoreCase(response, SearchCommonConstants.TIME_OUT_ERROR)) {
                    LOGGER.error(SearchCommonConstants.TIME_OUT_ERROR + " :: " + urlParam);
                } else if (StringUtils.isNotEmpty(response) || !StringUtils.equalsIgnoreCase(response, "[]")) {
                    pricingData = JsonUtil.getObjectMapper().readValue(response, JsonUtil.getObjectMapper().getTypeFactory().constructMapType(Map.class, String.class, Object.class));
                } else {
                    LOGGER.info("No data found in Pricing Gird for urlParam :: " + urlParam);
                }

                if (pricingData == null && gbRetryLogic && retryCount < gbRetryCount) {
                    retryCount++;
                } else {
                    break;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Exception while getting pricing Grid data for urlparama :: " + urlParam, ex);
            throw ex;
        }

        return pricingData;
    }

    /**
     * This methods is to get the pricing grid data for multiple partnumbers using post pricing grid api
     *
     * @return
     * @throws Throwable
     */
    public Map<String, Object> getPricingGridPostData(String jsonString) throws Exception {
        Map<String, Object> pricingData = null;
        int retryCount = 0;
        try {

            Boolean gbRetryLogic = Boolean.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_FLAG));
            Integer gbRetryCount = Integer.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_COUNT));
            while (true) {

                String response = webServiceUtil.postPriceGrid(jsonString);

                if (StringUtils.equalsIgnoreCase(response, SearchCommonConstants.TIME_OUT_ERROR)) {
                    LOGGER.error(SearchCommonConstants.TIME_OUT_ERROR + " ::  for pricingGrid post method with req ::" + jsonString);
                } else if (StringUtils.isNotEmpty(response) || !StringUtils.equalsIgnoreCase(response, "[]")) {
                    pricingData = JsonUtil.getObjectMapper().readValue(response, JsonUtil.getObjectMapper().getTypeFactory().constructMapType(Map.class, String.class, Object.class));
                } else {
                    LOGGER.info("No data found in Pricing Gird for urlParam :: " + jsonString);
                }

                if (pricingData == null && gbRetryLogic && retryCount < gbRetryCount) {
                    retryCount++;
                } else {
                    break;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Exception while getting pricing Grid post data for urlparam :: " + jsonString, ex);
            throw ex;
        }

        return pricingData;
    }

    /**
     * expects domainList which is a list of domain objects.
     * returns 200 on success and -1 for any failed response.
     *
     * @param domainList
     * @param collectionName
     * @return int
     * @throws Throwable
     */
    public int putJsonDomainList(List<DomainObject> domainList, String collectionName, String hostName, int port) throws Exception {
        int success = 200;
        int failure = -1;
        int apiRespCode = 0;
        boolean isFailed = false;

        if (domainList == null || domainList.isEmpty()) {
            return success;
        }
        Object excepItem = null;
        String jsonDomainStr = null;
        try {
            Set<List<DomainObject>> domainSet = getDomainSet(domainList);
            if (domainSet == null || domainSet.isEmpty()) {
                return success;
            }

            for (List<DomainObject> domainObjects : domainSet) {
                excepItem = domainObjects;
                int count = domainObjects.size();
                jsonDomainStr = JsonUtil.fromDomainObjectList(domainObjects);

                if (StringUtils.isEmpty(jsonDomainStr)) {
                    return failure;
                }

                apiRespCode = webServiceUtil.putData(collectionName, jsonDomainStr, hostName, port);


                if (apiRespCode == 200) {
                    LOGGER.debug("PUT SUCCESS:" + jsonDomainStr);
                    gbPutCallCounts(collectionName, count, true);
                } else {
                    LOGGER.info("PUT FAILED:" + jsonDomainStr);
                    gbPutCallCounts(collectionName, count, false);
                    isFailed = true;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Exception in getJsonDomainList:: " + excepItem, ex);
            throw ex;
        }
        if (isFailed) {
            return failure;
        }
        return success;
    }

    /**
     * This method puts the single JSON document into provided collection name
     *
     * @param domainObject
     * @param collectionName
     * @return
     * @throws Throwable
     */
    public int putJsonDomainObject(DomainObject domainObject, String collectionName, String hostName, int port) throws Exception {
        int status;
        int retryCount = 0;
        try {
            Boolean gbRetryLogic = Boolean.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_FLAG));
            Integer gbRetryCount = Integer.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_COUNT));
            while (true) {
                List<DomainObject> domainObjectList = new ArrayList<DomainObject>();
                domainObjectList.add(domainObject);
                int count = domainObjectList.size();
                String jsonDomainStr = JsonUtil.fromDomainObjectList(domainObjectList);
                status = webServiceUtil.putData(collectionName, jsonDomainStr, hostName, port);
                if (status == 200) {
                    gbPutCallCounts(collectionName, count, true);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("GB PUT success for id :: " + domainObject.get_id());
                    }
                } else {
                    gbPutCallCounts(collectionName, count, false);
                    LOGGER.error("GB PUT failed for JSON document :: " + jsonDomainStr);
                }
                if (status != 200 && gbRetryLogic && retryCount < gbRetryCount) {
                    retryCount++;
                } else {
                    break;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Exception while putting single JSON document into collection :: " + collectionName + " for ID :: " + domainObject.get_id(), ex);
            throw ex;
        }
        return status;
    }

    /**
     * This method deletes a single JSON document from the provided collection name
     *
     * @param id
     * @param collectionName
     * @param hostName
     * @param port
     * @return
     * @throws Throwable
     */
    public int deleteData(String id, String collectionName, String hostName, int port) throws Exception {
        int status;
        int retryCount = 0;
        try {
            Boolean gbRetryLogic = Boolean.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_FLAG));
            Integer gbRetryCount = Integer.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_RETRY_COUNT));
            while (true) {
                status = webServiceUtil.deleteData(collectionName, id, hostName, port);
                if (status == 200) {
                    gbPutCallCounts(collectionName, 1, true);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("GB DELETE success for collection :: " + collectionName + ", with Id ::" + id);
                    }
                } else {
                    gbPutCallCounts(collectionName, 1, false);
                    LOGGER.error("GB DELETE failed for collection :: " + collectionName + ", with Id ::" + id);
                }
                if (status != 200 && gbRetryLogic && retryCount < gbRetryCount) {
                    retryCount++;
                } else {
                    break;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Exception while deleting single JSON document into collection :: " + collectionName + " for ID :: " + id, ex);
            throw ex;
        }
        return status;
    }

    /**
     * This method deletes a List JSON document from the provided collection name
     *
     * @param ids
     * @param collectionName
     * @param hostName
     * @param port
     * @return
     * @throws Throwable
     */
    public int deleteData(List<String> ids, String collectionName, String hostName, int port) throws Exception {
        int status = 200;
        boolean isFailed = false;
        try {

            if (ids == null || ids.isEmpty()) {
                return status;
            }

            Set<List<String>> idSet = getIdSet(ids);
            if (idSet == null || idSet.isEmpty()) {
                return status;
            }

            for (List<String> idList : idSet) {
                String itmKeysStr = getItemKeysStr(idList);
                int count = (idList == null) ? 0 : idList.size();

                status = webServiceUtil.deleteData(collectionName, itmKeysStr, hostName, port);

                if (status == 200) {
                    gbDeleteCallCounts(collectionName, count, true);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("GB DELETE success for collection :: " + collectionName + ", with Id ::" + itmKeysStr);
                    }
                } else {
                    gbDeleteCallCounts(collectionName, count, false);
                    LOGGER.error("GB DELETE failed for collection :: " + collectionName + ", with Id ::" + itmKeysStr);
                    isFailed = true;
                }
            }

            if (isFailed) {
                return -1;
            }

        } catch (Exception ex) {
            LOGGER.error("Exception while deleting single JSON document into collection :: " + collectionName + " for ID :: " + ids, ex);
            throw ex;
        }
        return status;
    }

    // ********************************************************************************************************************
    // MISCELLANEOUS METHODS
    // ********************************************************************************************************************

    public Set<List<String>> getIdSet(List<String> idList) throws Exception {
        Set<List<String>> idSet = null;
        try {
            Integer maxItemsPerGet = Integer.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_GET_LIMIT));

            if (idList == null || idList.isEmpty()) {
                return new HashSet<>();
            }
            idSet = new HashSet<List<String>>();
            int counter = 0;
            List<String> ids = null;
            for (String id : idList) {
                if (counter == 0) {
                    ids = new ArrayList<String>();
                    idSet.add(ids);
                }
                if (id != null) {
                    ids.add(id);
                }
                counter++;
                if (counter == maxItemsPerGet) {
                    counter = 0;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Exception while getting a set of list of ids :: " + idList, ex);
            throw ex;
        }
        return idSet;
    }

    private String getItemKeysStr(List<String> pList) throws Exception {
        StringBuilder sb = new StringBuilder(500);
        try {
            for (String pid : pList) {
                sb.append(pid);
                sb.append("/");
            }
        } catch (Exception ex) {
            LOGGER.error("Exception in getItemKeysStr: " + pList, ex);
            throw ex;
        }
        return StringUtils.chop(sb.toString());
    }

    public Set<List<DomainObject>> getDomainSet(List<DomainObject> domainList) throws Exception {
        Set<List<DomainObject>> domaintSet = null;
        try {
            Integer maxItemsPerPut = Integer.valueOf(PropertiesLoader.getProperty(GlobalConstants.GB_API_PUT_LIMIT));

            if (domainList != null && domainList.size() > 0) {
                domaintSet = new HashSet<List<DomainObject>>();
                int counter = 0;
                List<DomainObject> domList = null;
                for (DomainObject domain : domainList) {
                    if (counter == 0) {
                        domList = new ArrayList<DomainObject>();
                        domaintSet.add(domList);
                    }
                    domList.add(domain);
                    counter++;
                    if (counter == maxItemsPerPut) {
                        counter = 0;
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Exception in getDomainSet: " + domainList, ex);
            throw ex;
        }
        return domaintSet;
    }

    // ********************************************************************************************************************
    // COUNTERS REALTED METHODS
    // ********************************************************************************************************************

    public void gbGetCallCounts(String collectionName, int count, Boolean isSuccess) {
        // TODO: is isSuccess do something
    }

    public void gbPutCallCounts(String collectionName, int count, Boolean isSuccess) {
        // TODO: is isSuccess do something
    }

    public void gbDeleteCallCounts(String collectionName, int count, Boolean isSuccess) {
        if (isSuccess) {
            if (StringUtils.equals(collectionName, "promo")) {
            } else if (StringUtils.equals(collectionName, "promorel")) {
            }
        } else {
            if (StringUtils.equals(collectionName, "promo")) {
            } else if (StringUtils.equals(collectionName, "promorel")) {
            }
        }

    }

}
