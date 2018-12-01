package com.shc.ecom.search.extract.components.pas;

import com.codahale.metrics.Timer;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.exception.ErrorCode;
import com.shc.ecom.search.common.pas.PasDto;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.http.ApacheHttpClient;
import com.shc.ecom.search.http.HttpClient;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class PasService implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1417775429808344527L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PasService.class);
    @Autowired
    protected MetricManager metricManager;
    //    @Value("${pas.hostname}")
    private String hostname = PropertiesLoader.getProperty(GlobalConstants.PAS_HOSTNAME);
    //    @Value("${pas.portno}")
    private String portno = PropertiesLoader.getProperty(GlobalConstants.PAS_PORTNO);
    //    @Value("${pas.sears.path}")
    private String searPath = PropertiesLoader.getProperty(GlobalConstants.PAS_SEARS_PATH);
    //    @Value("${pas.kmart.path}")
    private String kmartPath = PropertiesLoader.getProperty(GlobalConstants.PAS_KMART_PATH);

    private URL getUrl(String ssin, Sites site) {
        String path = null;

        if (Sites.SEARS == site) {
            path = searPath;
        } else if (Sites.KMART == site || Sites.MYGOFER == site) {
            path = kmartPath;
        }
        if (path == null) {
            LOGGER.error(ErrorCode.PROPERTY_NOT_FOUND.name() + "UAS url not found");
        }

        URL url = null;
        try {
            url = new URL("http://" + hostname + ":" + portno + path + ssin);

        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url);
        }
        return url;
    }

    private PasDto call(URL url) {
        HttpClient httpClient = new ApacheHttpClient();
        metricManager.incrementCounter(Metrics.COUNTER_PAS_SERVICE_CALLS);
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_PAS_SERVICE);
        String jsonResponse = null;
        try {
            jsonResponse = httpClient.httpGet(url);
        } finally {
            metricManager.stopTiming(timerContext);
        }
        PasDto pasDto = JsonUtil.convertToEntityObject(jsonResponse, PasDto.class);

        if (pasDto == null) {
            return new PasDto();
        }
        return pasDto;
    }

    public PasDto process(String ssin, Sites site) {
        URL url = getUrl(ssin, site);
        return call(url);
    }
}