package com.shc.ecom.search.extract.components.localad;

import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.search.common.greenbox.GreenboxService;
import com.shc.ecom.search.common.greenbox.bucket.Buckets;
import com.shc.ecom.search.common.localad.*;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.http.ApacheHttpClient;
import com.shc.ecom.search.http.HttpClient;
import com.shc.ecom.search.persistence.SearchFileReader;
import com.shc.ecom.search.persistence.ServiceDataAccessor;
import com.shc.ecom.search.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalAdsDownloadThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchFileReader.class.getName());
    private int bucketstart;
    private int bucketend;
    private ServiceDataAccessor resource;
    private String hostname;

    private int portno;

    @Autowired
    private Buckets bucket;

    @Autowired
    private GreenboxService greenboxService;

    private String localAdsCollectionName;
    private String localAdsPageCollectionName;
    private String localAdsStoreIdsURI;
    private String localAdsPageIdsURI;
    private String localAdsPageURI;

    public LocalAdsDownloadThread(ServiceDataAccessor resource, int start, int end) {
        this.bucketstart = start;
        this.bucketend = end;
        this.resource = resource;
        init();
    }


    @PostConstruct
    public void init() {
        localAdsStoreIdsURI = PropertiesLoader.getProperty(GlobalConstants.GB_API_GET_IDS_URL);
        localAdsPageIdsURI = PropertiesLoader.getProperty(GlobalConstants.GB_API_GET_URL);
        localAdsPageURI = PropertiesLoader.getProperty(GlobalConstants.GB_API_GET_URL);
        localAdsCollectionName = PropertiesLoader.getProperty(GlobalConstants.GB_LOCALADS_COLLECTION_NAME);
        localAdsPageCollectionName = PropertiesLoader.getProperty(GlobalConstants.GB_LOCALADS_PAGE_COLLECTION_NAME);
        hostname = PropertiesLoader.getProperty(GlobalConstants.GB_HOST);
        portno = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.GB_PORT));
    }


    /**
     * Based on the the storeId, get the localAds for it from GB
     * @param id
     * @return
     */
    private List<String> getLocalAdStoreIdsFromGB(String id) {
        HttpClient httpClient = new ApacheHttpClient();
        URL url = null;
        String jsonResponse = "";
        String encodedId = StringUtils.EMPTY;
        try {
            encodedId = URLEncoder.encode(id, "UTF-8");
            url = new URL("http://" + hostname + ":" + portno + localAdsStoreIdsURI + localAdsCollectionName + "/" + encodedId);
            jsonResponse = httpClient.httpGet(url);
        } catch (UnsupportedEncodingException ueE) {
            LOGGER.error("Unable to encode " + encodedId, ueE);
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url, e);
            return new ArrayList<String>();
        }

        List<String> ids = JsonUtil.convertToEntityObjectList(jsonResponse, String.class);
        return ids;
    }

    /**
     * Get all localAds from GB for the bucket id specified
     * @param id Bucket id
     * @return
     */
    private List<LocalAdsGBActivity> getLocalAdsObjectsFromGB(String id) {

        HttpClient httpClient = new ApacheHttpClient();
        String jsonResponse = "";
        String urlString = "http://" + hostname + ":" + portno + localAdsPageURI + localAdsCollectionName + "/" + id;
        List<LocalAdsGBActivity> localAds = new ArrayList<LocalAdsGBActivity>();
        try {
            URL url = new URL(urlString);
            jsonResponse = httpClient.httpGet(url);
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + urlString, e);
            return new ArrayList<LocalAdsGBActivity>();
        }

        List<LocalAdDomainObject> domainObjectList = JsonUtil.convertToEntityObjectList(jsonResponse, LocalAdDomainObject.class);

        for (LocalAdDomainObject obj : domainObjectList) {
            LocalAdsGBCollectionBlob blob = obj.get_blob();
            localAds.addAll(blob.getStorelocalads().getLocalAds());
        }
        return localAds;
    }

    /**
     * Get LocalAd details from GB
     * @param localAdPageid
     * @return
     */
    private List<String> getLocalAdProductIdsFromGB(String localAdPageid) {
        HttpClient httpClient = new ApacheHttpClient();
        List<String> productIds = new ArrayList<String>();
        String encodePageId;
        String jsonResponse = "";

        try {
            encodePageId = URLEncoder.encode(localAdPageid, "UTF-8");
            URL url = new URL("http://" + hostname + ":" + portno + localAdsPageIdsURI + localAdsPageCollectionName + "/" + encodePageId);
            jsonResponse = httpClient.httpGet(url);
        } catch (UnsupportedEncodingException e1) {
            LOGGER.error("Unable to encode " + localAdPageid, e1);
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed", e);
            return new ArrayList<String>();
        }

        List<LocalAdPageDomainObject> domainObjectList = JsonUtil.convertToEntityObjectList(jsonResponse, LocalAdPageDomainObject.class);

        for (LocalAdPageDomainObject obj : domainObjectList) {
            if (obj.get_blob() == null || obj.get_blob().getLocaladpage() == null || obj.get_blob().getLocaladpage().getElements() == null || obj.get_blob().getLocaladpage().getElements().get(0).getProducts() == null) {
                continue;
            }
            List<LocalAdsGBElement> elements = obj.get_blob().getLocaladpage().getElements();
            for (int i = 0; i < elements.size(); i++) {
                for (Products product : elements.get(i).getProducts()) {
                    if (product.getProductId() != null) {
                        productIds.add(product.getProductId());
                    }
                }
            }
        }
        return productIds;

    }

    @Override
    public void run() {
        init();
        for (int i = bucketstart; i <= bucketend; i++) {
            List<String> storeList = getLocalAdStoreIdsFromGB(i + "");
            for (String storeId : storeList) {
                List<LocalAdsGBActivity> localAdsList = getLocalAdsObjectsFromGB(storeId);
                for (LocalAdsGBActivity localads : localAdsList) {
                    String expirationDate = localads.getEndDate();
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    try {
                        if (dateFormatter.parse(expirationDate).before(new Date())) {
                            LOGGER.info("Skipping expired Ad:" + expirationDate + " - ID=" + localads.getActivityId());
                            continue;
                        }
                    } catch (ParseException e) {
                        LOGGER.info("Error: incorrect date format of activity id" + localads.getActivityId());
                    }
                    List<LocalAdsGBPage> pages = localads.getPages();
                    for (LocalAdsGBPage page : pages) {
                        String pageId = page.getPageId();
                        List<String> products = getLocalAdProductIdsFromGB(pageId);
                        for (String productId : products) {
                            resource.save(productId + "\t" + localads.getActivityId());
                        }
                    }
                }
            }
        }
    }

}
