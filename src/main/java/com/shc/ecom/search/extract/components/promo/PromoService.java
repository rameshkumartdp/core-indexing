package com.shc.ecom.search.extract.components.promo;

import com.codahale.metrics.Timer;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.search.common.promo.PromoDto;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.extract.components.uas.UasService;
import com.shc.ecom.search.http.ApacheHttpClient;
import com.shc.ecom.search.http.HttpClient;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromoService implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8797751587002727066L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UasService.class);
    @Autowired
    protected MetricManager metricManager;
    //    @Value("${promo.hostname}")
    private String hostname = PropertiesLoader.getProperty(GlobalConstants.PROMO_HOSTNAME);
    //    @Value("${promo.portno}")
    private String portno = PropertiesLoader.getProperty(GlobalConstants.PROMO_PORTNO);
    //    @Value("${promo.path}")
    private String path = PropertiesLoader.getProperty(GlobalConstants.PROMO_PATH);
    //    @Value("${promo.site}")
    private String site = PropertiesLoader.getProperty(GlobalConstants.PROMO_SITE);
    //    @Value("${promo.flavor}")
    private String flavor = PropertiesLoader.getProperty(GlobalConstants.PROMO_FLAVOR);
    //    @Value("${promo.flavor_values}")
    private String flavorValues = PropertiesLoader.getProperty(GlobalConstants.PROMO_FLAVOR_VALUES);

    private List<PromoDto> call(URL url) {
        HttpClient httpClient = new ApacheHttpClient();
        metricManager.incrementCounter(Metrics.COUNTER_PROMO_SERVICE_CALLS);
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_PROMO_SERVICE);
        String jsonResponse = null;
        try {
            jsonResponse = httpClient.httpGet(url);
        } finally {
            metricManager.stopTiming(timerContext);
        }
        if (StringUtils.isEmpty(jsonResponse)) {
            return new ArrayList<>();
        }
        return JsonUtil.convertToEntityObjectList(jsonResponse, PromoDto.class);
    }

    private URL getUrl(String storeName, String offerId) {
        URL url = null;
        try {
            url = new URL("http://" + hostname + ":" + portno + path + offerId + "?" + site + "=" + storeName + "&"
                    + flavor + "=" + flavorValues + "&" + getClientId());
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url);
        }
        return url;
    }

    public List<PromoDto> process(String storeName, String offerId) {
        return call(getUrl(storeName, offerId));
    }

    public String getClientId(){
        return PropertiesLoader.getProperty(GlobalConstants.GB_CLIENT_PARAMETER_NAME) + "=" + PropertiesLoader.getProperty(GlobalConstants.GB_CLIENT_ID) ;
    }
}
