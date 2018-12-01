package com.shc.ecom.search.extract.components.fitments;

import com.codahale.metrics.Timer;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.search.common.fitment.FitmentDTO;
import com.shc.ecom.search.config.GlobalConstants;
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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class FitmentService implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8797751587002727066L;

    private static final Logger LOGGER = LoggerFactory.getLogger(FitmentService.class);
    @Autowired
    protected MetricManager metricManager;
    //    @Value("${autoFitment.host}")
    private String hostname = PropertiesLoader.getProperty(GlobalConstants.AUTOFITMENT_HOST);
    //    @Value("${autoFitment.port}")
    private String portno = PropertiesLoader.getProperty(GlobalConstants.AUTOFITMENT_PORT);
    //    @Value("${autoFitment.path}")
    private String path = PropertiesLoader.getProperty(GlobalConstants.AUTOFITMENT_PATH);
    //    @Value("${autoFitment.brands.path}")
    private String brands = PropertiesLoader.getProperty(GlobalConstants.AUTOFITMENT_BRANDS_PATH);
    //    @Value("${autoFitment.parts.path}")
    private String parts = PropertiesLoader.getProperty(GlobalConstants.AUTOFITMENT_PARTS_PATH);

    private FitmentDTO call(URL url) {
        HttpClient httpClient = new ApacheHttpClient();
        metricManager.incrementCounter(Metrics.COUNTER_FITMENT_SERVICE_CALLS);
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_FTIMENT_SERVICE);
        String jsonResponse = null;
        try {
            jsonResponse = httpClient.httpGet(url);
        } finally {
            metricManager.stopTiming(timerContext);
        }
        if (StringUtils.isEmpty(jsonResponse)) {
            return new FitmentDTO();
        }
        return JsonUtil.convertToEntityObject(jsonResponse, FitmentDTO.class);
    }

    private URL getUrl(String brandCodeId, String mfrPartNo) {
        URL url = null;
        try {
            String brandCodeIdEncoded = URLEncoder.encode(brandCodeId, "UTF-8");
            url = new URL("http://" + hostname + ":" + portno + path + brands + brandCodeIdEncoded + parts + mfrPartNo);
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e + "");
            LOGGER.error("Unable to encode " + brandCodeId);
        }
        return url;
    }

    public FitmentDTO process(String brandCodeId, String mfrPartNo) {
        if (StringUtils.isEmpty(brandCodeId) || StringUtils.isEmpty(mfrPartNo)) {
            return new FitmentDTO();
        }
        return call(getUrl(brandCodeId, mfrPartNo));
    }
}
