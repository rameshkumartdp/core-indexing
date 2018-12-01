package com.shc.ecom.search.extract.components.seller;

import com.codahale.metrics.Timer;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.search.common.seller.SellerDto;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.http.ApacheHttpClient;
import com.shc.ecom.search.http.HttpClient;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pchauha
 */
// TODO: Can we group more than one seller Ids at a time
@Service
public class SellerService implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3767587433321218199L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SellerService.class);
    @Autowired
    protected MetricManager metricManager;

    private String hostname = PropertiesLoader.getProperty(GlobalConstants.SELLER_HOSTNAME);

    private int portno = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.SELLER_PORTNO));

    private String path = PropertiesLoader.getProperty(GlobalConstants.SELLER_PATH);

    //Caches seller-id to it's response to avoid calling the seller service for the same seller again and again. Maps seller-id to seller service response (SellerDTO).
    private ConcurrentHashMap<Integer, SellerDto> sellerResponses = new ConcurrentHashMap<>();

    private SellerDto call(URL url) {

        HttpClient httpClient = new ApacheHttpClient();
        metricManager.incrementCounter(Metrics.COUNTER_SELLER_SERVICE_CALLS);
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_SELLER_SERVICE);
        String jsonResponse = null;
        try {
            jsonResponse = httpClient.httpGet(url);
        } finally {
            metricManager.stopTiming(timerContext);
        }

        if (StringUtils.isEmpty(jsonResponse)) {
            return new SellerDto();
        }

        List<SellerDto> sellerDtoList = JsonUtil.convertToEntityObjectList(jsonResponse, SellerDto.class);
        // TODO: we need to fix this. The below is to avoid indexoutofbound
        return sellerDtoList.isEmpty() ? new SellerDto() : sellerDtoList.get(0);
    }

    private URL getUrl(int sellerId) {
        URL url = null;
        try {
            url = new URL("http://" + hostname + ":" + portno + path + String.valueOf(sellerId));
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url);
        }
        return url;
    }

    public Map<String, SellerDto> process(Map<String, Integer> offerToSellerIdMap) {
        Map<String, SellerDto> offerToSellerDTO = new HashMap<>();

        for (Map.Entry<String, Integer> entry : offerToSellerIdMap.entrySet()) {
            Integer sellerId = entry.getValue();
            if (!sellerResponses.containsKey(sellerId)) {
                URL url = getUrl(sellerId);
                SellerDto sellerDTO = call(url);
                sellerResponses.put(sellerId, sellerDTO);
            }
            offerToSellerDTO.put(entry.getKey(), sellerResponses.get(sellerId));
        }
        return offerToSellerDTO;
    }
}
