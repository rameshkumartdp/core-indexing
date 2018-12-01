package com.shc.ecom.search.persistence;

import com.google.common.base.Strings;
import com.google.gson.JsonSyntaxException;
import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.search.common.exception.ErrorCode;
import com.shc.ecom.search.common.greenbox.GreenboxService;
import com.shc.ecom.search.common.greenbox.bucket.Buckets;
import com.shc.ecom.search.common.localad.LocalAd;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.extract.components.localad.LocalAdsDownloadThread;
import org.apache.commons.collections.MapUtils;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.*;

/**
 * This component is responsible for loading the local Ad data at the beginning of indexing
 */
@Component
public class ProductLocalAd extends ServiceDataAccessor implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 10000L;
    private static final String SEPARATOR = "\\t";
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductLocalAd.class.getName());

    @Autowired
    private Buckets bucket;

    @Autowired
    private GreenboxService greenboxService;

    private String bucketApiPath;
    private String collectionName;
    private int threadCount;
    private long requestThreadTimeout;

    private Map<String, LocalAd> localAdIds = new ConcurrentHashMap<String, LocalAd>();
    public Map<String, LocalAd> getLocalAdIds() {
        return localAdIds;
    }

    @PostConstruct
    public void init() {
        bucketApiPath = PropertiesLoader.getProperty(GlobalConstants.GB_API_BUCKET_URL);
        collectionName = PropertiesLoader.getProperty(GlobalConstants.GB_LOCALADS_COLLECTION_NAME);
        threadCount = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.LOCALAD_THREAD_COUNT));
        requestThreadTimeout = Long.parseLong(PropertiesLoader.getProperty(GlobalConstants.LOCALAD_REQUEST_THREAD_TIMEOUT));
    }

    @Override
    public void save(Object dto) {
        String dataString = dto.toString();
        if(Strings.isNullOrEmpty(dataString)) {
            return;
        }
        String[] data = dataString.split(SEPARATOR);
        if (data.length < 2) {
            return;
        }

        String productid = data[0];
        LocalAd localAd = new LocalAd();
        ConcurrentHashSet<String> list = new ConcurrentHashSet<>();
        for (int i = 1; i < data.length; i++) {
            list.add(data[i]);
        }
        localAd.setLocalAdIds(list);
        setLocalAdIds(productid, localAd);
    }

    @Override
    public void read(String fileName) throws IOException, JsonSyntaxException {

        //Flag must be present and indexing should stop if not present
        boolean localAdsFlag = Boolean.parseBoolean(PropertiesLoader.getProperty(GlobalConstants.ENABLE_LOCALADS));
        //Exit if the flag is not set
        if (!localAdsFlag) {
            LOGGER.info("Skipping LocalAds. LocalAds flag off !!");
            return;
        }

        bucket = getBuckets(greenboxService, bucketApiPath, collectionName);
        if(bucket.isEmpty()) {
            LOGGER.error("Empty bucket for LocalAds !! Skipping LocalAds");
            return;
        }

        LOGGER.info("Local Ads - bucket start: " + bucket.getStart());
        LOGGER.info("Local Ads - bucket end: " + bucket.getEnd());

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        int chunk = (bucket.getEnd() - bucket.getStart()) / threadCount;
        if (chunk < 1) {
            chunk = 1;
        }
        if (bucket.getEnd() - bucket.getStart() < threadCount) {
            threadCount = 1;
        }

        Future[] tasks = new Future[threadCount];
        int start = 0;

        for (int i = 0; i < threadCount; i++) {
            tasks[i] = executor.submit(new LocalAdsDownloadThread(this, start, start + chunk - 1));
            start = start + chunk;
        }

        for (int i = 0; i < threadCount; i++) {
            try {
                tasks[i].get(requestThreadTimeout, TimeUnit.MILLISECONDS);
            } catch (TimeoutException | InterruptedException | ExecutionException ex) {
                metricManager.incrementCounter("Exceptions-LocalAd-MISC");
                LOGGER.error("Exception in ProductLocalAd : " + ex);
                tasks[i].cancel(true);
            }
        }

        try {
            executor.awaitTermination(180, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.info("Thread interrupted Exception at LocalAdsDataReader");
        }

        executor.shutdown();
        LOGGER.info(this.getLocalAdIds().keySet().size() + " Entries in local ads map");

    }

    /**
     * Given an id, store the localAd into the objects data structure
     * @param id
     * @param localad
     */
    public void setLocalAdIds(String id, LocalAd localad) {
        LocalAd storedLocalAd = this.localAdIds.get(id);
        if (!this.localAdIds.containsKey(id)) {
            this.localAdIds.put(id, localad);
        } else {
            ConcurrentHashSet<String> idList = storedLocalAd.getLocalAdIds();
            for (String lid : localad.getLocalAdIds()) {
                if (!idList.contains(lid)) {
                    idList.add(lid);
                }
            }
        }
    }

    /**
     * Get buckets from GB for localAds
     * @param greenboxService GB Service component bean
     * @param bucketApiPath LocalAd Bucket API path
     * @param collectionName LocalAd Collection name
     * @return
     */
    public Buckets getBuckets(GreenboxService greenboxService, String bucketApiPath, String collectionName) {

        if(Strings.isNullOrEmpty(bucketApiPath) || Strings.isNullOrEmpty(collectionName)) {
            return new Buckets();
        }

        try {
            Map<String, String> bucketIdMap = greenboxService.getBuckets(bucketApiPath, collectionName);

            if (MapUtils.isEmpty(bucketIdMap)) {
                LOGGER.info(ErrorCode.GB_OFFER_BUCKET_EMPTY + "," +
                        greenboxService.getHostname() +":" + greenboxService.getPortno());
                return new Buckets();
            }

            bucket = getBucketFromBucketIdMap(bucketIdMap);

        } catch (Exception e) {
            LOGGER.error("Exception occurred while fetching buckets from GB", e);
        }

        return bucket;
    }

    /**
     * Get the Buckets object by extracting the first key value pair of the bucketIdMap from GB
     * @param bucketIdMap
     * @return
     */
    private Buckets getBucketFromBucketIdMap(Map<String, String> bucketIdMap) {
        Buckets newBuckets = new Buckets();

        //Getting the first key of the map. There is always just one key in this map. :p
        String start = bucketIdMap.entrySet().iterator().next().getKey();
        String end = bucketIdMap.get(start);

        newBuckets.setStart(Integer.parseInt(start)); //I don't understand why GB is sending integers as Strings
        newBuckets.setEnd(Integer.parseInt(end));

        return newBuckets;
    }
}
