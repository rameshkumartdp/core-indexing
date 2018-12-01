package com.shc.ecom.search.extract.components.uas;

import com.codahale.metrics.Timer;
import com.google.common.collect.Lists;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.uas.UasDto;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.http.ApacheHttpClient;
import com.shc.ecom.search.http.HttpClient;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UasService implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3742526367212130352L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UasService.class);
    private static final int OFFER_PARITION_COUNT = 2;
    @Autowired
    protected MetricManager metricManager;

    private String hostname = PropertiesLoader.getProperty(GlobalConstants.UAS_HOSTNAME);
    private int portno = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.UAS_PORTNO));
    private String path = PropertiesLoader.getProperty(GlobalConstants.UAS_PATH);

    private List<JSONObject> getJsonObject(List<String> offerIds, int partitionCount) {
        List<List<String>> partitionedOfferIdsList = Lists.partition(offerIds, partitionCount);
        List<JSONObject> partionedOfferIdsJson = new ArrayList<>();

        for (List<String> partionedOfferIds : partitionedOfferIdsList) {
            JSONObject paritionedOfferIdsObject = new JSONObject();
            paritionedOfferIdsObject.put("items", partionedOfferIds);
            partionedOfferIdsJson.add(paritionedOfferIdsObject);
        }

        return partionedOfferIdsJson;
    }

    private UasDto call(JSONObject offerIdsJson, URL url) {
        HttpClient httpClient = new ApacheHttpClient();
        metricManager.incrementCounter(Metrics.COUNTER_UAS_SERVICE_CALLS);
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_UAS_SERVICE);
        String jsonResponse = null;
        try {
            jsonResponse = httpClient.httpPost(url, offerIdsJson.toString());
        } finally {
            metricManager.stopTiming(timerContext);
        }
        if (StringUtils.isEmpty(jsonResponse)) {
            return new UasDto();
        }
        return JsonUtil.convertToEntityObject(jsonResponse, UasDto.class);
    }

    private URL getURL(ContextMessage context) {
        URL url = null;
        try {
            if (Stores.SEARSPR.matches(context.getStoreName())) {
                path = PropertiesLoader.getProperty(GlobalConstants.UAS_PATH_SEARSPR);
            }
            url = new URL("http://" + hostname + ":" + portno + path);
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url);
        }
        return url;
    }

    public UasDto process(List<String> offerIds, ContextMessage context) {
        List<JSONObject> partionedOfferIds = getJsonObject(offerIds, UasService.OFFER_PARITION_COUNT);
        URL url = getURL(context);

        Map<String, Map<String, Boolean>> itemMap = new HashMap<>();

        for (JSONObject offerIdsJson : partionedOfferIds) {
            UasDto uasDto = call(offerIdsJson, url);
            itemMap.putAll(uasDto.getItemMap());
        }

        UasDto uasDto = new UasDto();
        uasDto.setItemMap(itemMap);

        return uasDto;
    }

}
