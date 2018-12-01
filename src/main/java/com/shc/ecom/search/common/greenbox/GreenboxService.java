package com.shc.ecom.search.common.greenbox;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codahale.metrics.Timer;
import com.shc.ecom.gb.doc.common.DomainObject;
import com.shc.ecom.search.common.store.Store;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.extract.components.uas.UasService;
import com.shc.ecom.search.http.ApacheHttpClient;
import com.shc.ecom.search.http.HttpClient;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.util.JsonUtil;

@Component
public class GreenboxService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UasService.class);

    @Autowired
    protected MetricManager metricManager;
    private String hostname = PropertiesLoader.getProperty(GlobalConstants.GB_HOST);
    private int portno = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.GB_PORT));

    public List<DomainObject> getDomainObject(String path, String collection, String id) {
        HttpClient httpClient = new ApacheHttpClient();
        String encodedId = StringUtils.EMPTY;
        try {
            encodedId = URLEncoder.encode(id, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            LOGGER.error("Unable to encode " + id, e1);
            encodedId = id;
        }
        URL url = null;
        try {
            url = new URL("http://" + hostname + ":" + portno + path + collection + "/" + encodedId + "?" + getClientId());
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url, e);
            return new ArrayList<DomainObject>();
        }
        metricManager.incrementCounter(Metrics.COUNTER_PREFIX + "GB" + collection + Metrics.CALLS);
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_EACH_PREFIX + "GB" + collection + Metrics.REQUEST);
        String jsonResponse = httpClient.httpGet(url);
        metricManager.stopTiming(timerContext);
        List<DomainObject> domainObjects = JsonUtil.convertToEntityObjectList(jsonResponse, DomainObject.class);
        return domainObjects;
    }

    public Map<String, String> getBuckets(String path, String collection) {
        HttpClient httpClient = new ApacheHttpClient();
        URL url = null;
        try {
            url = new URL("http://" + hostname + ":" + portno + path + collection + "?" + getClientId());
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url, e);
            return new HashMap<String, String>();
        }
        String jsonResponse = httpClient.httpGet(url);
        Map<String, String> bucketsMap = JsonUtil.convertToHashMap(jsonResponse, String.class, String.class);
        return bucketsMap;

    }

    public List<DomainObject> getDomainObjects(String path, String collection, List<String> idList) {
        HttpClient httpClient = new ApacheHttpClient();
        List<String> modIds = new ArrayList<>();
        for (String id : idList) {
            try {
                modIds.add(URLEncoder.encode(id, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("Unable to encode " + id, e);
            }
        }
        String ids = StringUtils.join(modIds, "/");
        URL url = null;
        try {
            url = new URL("http://" + hostname + ":" + portno + path + collection + "/" + ids + "?" + getClientId());
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url, e);
            return new ArrayList<DomainObject>();
        }
        metricManager.incrementCounter(Metrics.COUNTER_PREFIX + "GB" + collection + Metrics.CALLS);
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_EACH_PREFIX + "GB" + collection + Metrics.REQUEST);
        String jsonResponse = httpClient.httpGet(url);
        metricManager.stopTiming(timerContext);
        List<DomainObject> domainObjects = JsonUtil.convertToEntityObjectList(jsonResponse, DomainObject.class);
        return domainObjects;
    }

    /**
     * This GB Service API queries greenbox (filter-ids or get-ids) collection
     * to get offer-ids/content-ids for a specific filter-type (eg:
     * pgmType=Sears or offerType=NV). Optionally, we support a data-time that
     * filters ids that were modified within the 'from' and 'to' timestamps Be
     * mindful that there can be only one filter-type sent to this (in addition to
     * date-time) as the GB service currently doesn't support multiple
     * filter-types.
     *
     * @param path            - Path to GB API
     * @param collection      - Collection name - /filter-ids/ or /get-ids/
     * @param bucketId        Bucket number
     * @param filterTypeName  Filter name - pgmType, offerType are examples
     * @param filterTypeValue Filter value - Sears, NV are examples (respectively)
     * @param from            From date-time in UTC format. Eg: 2016-08-10 T 15:34 UTC
     * @param to              To date-time in UTC format. Eg: 2016-08-10 T 18:34 UTC
     * @return List of ids (offer-ids or content-ids) from the bucket number matching the filter criteria.
     */
    public List<String> getIdsFromBuckets(String path, String collection, String bucketId, String filterTypeName, String filterTypeValue, String from, String to) {
        String timeRange = StringUtils.EMPTY;
        //Both from and to should be available to apply this filter
        if (StringUtils.isNotEmpty(from) && StringUtils.isNotEmpty(to)) {
            try {
                timeRange = "&from=" + URLEncoder.encode(from, "UTF-8") + "&to=" + URLEncoder.encode(to, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("Unable to encode from: " + from + " to:" + to, e);
            }
        }

        HttpClient httpClient = new ApacheHttpClient();
        URL url = null;
        try {
            url = new URL("http://" + hostname + ":" + portno + path + collection + "/" + bucketId + "?" + filterTypeName + "=" + filterTypeValue + timeRange + "&" + getClientId());
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url, e);
            return new ArrayList<String>();
        }
        String jsonResponse = httpClient.httpGet(url);
        List<String> ids = JsonUtil.convertToEntityObjectList(jsonResponse, String.class);
        return ids;
    }

    public List<String> getOfferIdsForSSIN(String path, String collection, String ssin) {
        HttpClient httpClient = new ApacheHttpClient();
        URL url = null;
        try {
            url = new URL("http://" + hostname + ":" + portno + path + collection + "?ssin=" + ssin + "&" + getClientId());
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url, e);
            return new ArrayList<String>();
        }
        metricManager.incrementCounter(Metrics.COUNTER_PREFIX + "GB" + collection + Metrics.CALLS);
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_EACH_PREFIX + "GB" + collection + Metrics.REQUEST);
        String jsonResponse = httpClient.httpGet(url);
        metricManager.stopTiming(timerContext);
        List<String> offerIds = JsonUtil.convertToEntityObjectList(jsonResponse, String.class);
        return offerIds;
    }

    public List<String> getOfferIdsForParentID(String path, String collection, String ssin) {
        HttpClient httpClient = new ApacheHttpClient();
        URL url = null;
        try {
            url = new URL("http://" + hostname + ":" + portno + path + collection + "?parentId=" + ssin + "&" + getClientId());
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url, e);
            return new ArrayList<String>();
        }
        metricManager.incrementCounter(Metrics.COUNTER_PREFIX + "GB" + collection + Metrics.CALLS);
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_EACH_PREFIX + "GB" + collection + Metrics.REQUEST);
        String jsonResponse = httpClient.httpGet(url);
        metricManager.stopTiming(timerContext);
        List<String> offerIds = JsonUtil.convertToEntityObjectList(jsonResponse, String.class);
        return offerIds;
    }
    
    
    public List<String> getActiveStoreIds(String path, String collection, String storeName) {
    	HttpClient httpClient = new ApacheHttpClient();
        String szipHost = PropertiesLoader.getProperty(GlobalConstants.SZIP_HOST);
        int szipPort = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.SZIP_PORT));
    	String activeStoresParam = "?strStatus=1";
    	String storeParam = StringUtils.isNotEmpty(storeName) ? "&siteId="+storeName.toLowerCase() : "";
    	URL url = null;
        
        try {
			url = new URL("http://" + szipHost + ":" + szipPort + path + collection + activeStoresParam + storeParam+"&" + getClientId());
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url, e);
            return new ArrayList<String>();
        }
        metricManager.incrementCounter(Metrics.COUNTER_PREFIX + "GB" + collection + Metrics.CALLS);
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_EACH_PREFIX + "GB" + collection + Metrics.REQUEST);
        String jsonResponse = httpClient.httpGet(url);
        metricManager.stopTiming(timerContext);
        List<String> storeIds = JsonUtil.convertToEntityObjectList(jsonResponse, String.class);
        return storeIds;
    }
    
    public List<Store> getStoreDoc(String path, String collection, String storeId) {
    	HttpClient httpClient = new ApacheHttpClient();
    	 String szipHost = PropertiesLoader.getProperty(GlobalConstants.SZIP_HOST);
         int szipPort = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.SZIP_PORT));
        URL url = null;
        try {
            url = new URL("http://" + szipHost + ":" + szipPort + path + collection + "/" + storeId + "?" + getClientId());
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url, e);
            return new ArrayList<Store>();
        }
        metricManager.incrementCounter(Metrics.COUNTER_PREFIX + "GB" + collection + Metrics.CALLS);
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_EACH_PREFIX + "GB" + collection + Metrics.REQUEST);
        String jsonResponse = httpClient.httpGet(url);
        metricManager.stopTiming(timerContext);
        List<Store> storesList = JsonUtil.convertToEntityObjectList(jsonResponse, Store.class);
        return storesList;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPortno() {
        return portno;
    }

    public String getClientId(){
        return PropertiesLoader.getProperty(GlobalConstants.GB_CLIENT_PARAMETER_NAME) + "=" + PropertiesLoader.getProperty(GlobalConstants.GB_CLIENT_ID);
    }
}
