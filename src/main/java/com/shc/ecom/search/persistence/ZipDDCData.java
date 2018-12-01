package com.shc.ecom.search.persistence;

import com.codahale.metrics.Timer;
import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.search.common.zipDDC.Warehouse;
import com.shc.ecom.search.common.zipDDC.ZipDDCMapping;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.http.ApacheHttpClient;
import com.shc.ecom.search.http.HttpClient;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hdargah
 */

/**
 * This component is responsible for storing the zipToDDC Mapping as part of ensure availability.
 * Reading from this static file is a temporary solution and in Q4 2016 should be replaced by a microservice provided
 * by the Availability team. Use caching of these key values on the indexer side to avoid unnecessary calls to UAS.
 * Most values will remain same.
 */
@Component
public class ZipDDCData extends ServiceDataAccessor<ZipDDCMapping> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipDDCData.class);
    String hostname;
    int portno;
    String path;
    private Set<String> ddcSet;

    @Override
    public void read(String fileName) {
        if (!hasValidProperties(hostname, portno, path)) {
            LOGGER.warn("Invalid properties !!! Skipping reading ZipDDC from Service !!!");
            return;
        }
        LOGGER.info("Started reading ZipDDC Mapping from Service");
        URL url = getURL(hostname, portno, path);
        LOGGER.info("Zipddc Service URL: " + url.toString());
        ZipDDCMapping zipDDCMapping = call(url);
        save(zipDDCMapping);
        LOGGER.info("Finished reading ZipDDC Data .. Unique DDCs: " + ddcSet.size());
    }

    @PostConstruct
    private void intitializeProperties() {
        ddcSet = new HashSet<>();
        try {
            hostname = PropertiesLoader.getProperty(GlobalConstants.ZIPDDC_HOSTNAME);
            portno = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.ZIPDDC_PORTNO));
            path = PropertiesLoader.getProperty(GlobalConstants.ZIPDDC_PATH);
        } catch (NumberFormatException e) {
            LOGGER.warn("ZipDDC properties missing. Continuing execution of the application ");
        }
    }

    public ZipDDCMapping call(URL url) {
        if (url == null || StringUtils.isEmpty(url.toString())) {
            return new ZipDDCMapping();
        }

        HttpClient httpClient = new ApacheHttpClient();
        metricManager.incrementCounter(Metrics.COUNTER_ZIPDDC_SERVICE_CALLS);
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_ZIPDDC_SERVICE);
        String jsonResponse = null;
        try {
            jsonResponse = httpClient.httpGet(url);
        } finally {
            metricManager.stopTiming(timerContext);
        }
        if (StringUtils.isEmpty(jsonResponse)) {
            return new ZipDDCMapping();
        }
        return JsonUtil.convertToEntityObject(jsonResponse, ZipDDCMapping.class);
    }

    public void save(ZipDDCMapping zipDDCMapping) {

        for (Warehouse warehouse : zipDDCMapping.getWarehousesList()) {
            ddcSet.add(warehouse.getDcUnit());
        }
    }

    public Set<String> getDdcSet() {
        return ddcSet;
    }

    private boolean hasValidProperties(String hostname, int port, String path) {
        return !(StringUtils.isEmpty(hostname) || port == 0 || StringUtils.isEmpty(path));
    }

}
