package com.shc.ecom.search.extract.components.ias;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codahale.metrics.Timer;
import com.google.common.base.Strings;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.ias.IAS;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.http.ApacheHttpClient;
import com.shc.ecom.search.http.HttpClient;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.util.JsonUtil;


@Service
public class IasService implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7813102888767400296L;
    private static final Logger LOGGER = LoggerFactory.getLogger(IasService.class);
    @Autowired
    protected MetricManager metricManager;
    private String hostname = PropertiesLoader.getProperty(GlobalConstants.IAS_HOST_ROUTE_URL);
    private int portno = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.IAS_PORT));
    private String path = PropertiesLoader.getProperty(GlobalConstants.IAS_API_GET_URL);
    private String kmartKsnPostfix = PropertiesLoader.getProperty(GlobalConstants.IAS_API_GET_KSN_POSTFIX);

    /**
     * Gets the common IAS URL for all stores. Useless for Kmart.
     *
     * @param offerId
     * @return
     */
    protected URL getUrl(String offerId) {
        URL url = null;
        try {
            url = new URL("http://" + hostname + ":" + portno + path + offerId);
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url);
        }
        return url;
    }

    /**
     *
     * @param offerIdKsnMap
     * @param offerId
     * @return
     */
    protected URL getKmartUrl(Map<String, String> offerIdKsnMap, String offerId) {
        URL url = null;
        //Assuming we won't have ksns for all offers, we have to place this check
        Optional ksnOptional = Optional.ofNullable(offerIdKsnMap.get(offerId));
        if (ksnOptional.isPresent()) {
            String ksn = getPaddedKSN(ksnOptional.get().toString());
            try {
                url = new URL(getUrl(offerId) + kmartKsnPostfix + ksn);
            } catch (MalformedURLException e) {
                LOGGER.error("URL formation malformed. URL - " + url);
            }
        }
        return url;
    }

    /**
     * Make the http call to the IAS service with the constructed URL and return the IAS DTO
     *
     * @param url
     * @return
     */
    protected IAS call(URL url) {
        HttpClient httpClient = new ApacheHttpClient();
        metricManager.incrementCounter(Metrics.COUNTER_IAS_SERVICE_CALLS);
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_IAS_SERVICE);
        String jsonResponse = null;
        try {
            jsonResponse = httpClient.httpGet(url);
        } finally {
            metricManager.stopTiming(timerContext);
        }
        if (StringUtils.isEmpty(jsonResponse)) {
            return new IAS();
        }
        return JsonUtil.convertToEntityObject(jsonResponse, IAS.class);
    }

    /**
     * Added as a hack to pad KSN to 9 digits because the inventory team won't do so.
     * We should not be modifying content. This is madness.
     * @param ksn
     * @return
     */
    protected String getPaddedKSN(String ksn) {
        return Strings.padStart(ksn, 9, '0');
    }

    /**
     * Process the IAS data by making calls to the IAS service
     * The code here has gotten messy. Clean it up.
     * @param wd
     * @param context
     * @return
     */
    public Map<String, IAS> process(WorkingDocument wd, ContextMessage context) {


        //It only makes sense to make calls for available items. #Optimization
        List<String> availableOfferIds = wd.getExtracts().getUasExtract().getAvailableItems();
        Map<String, String> offerIdKsnMap = wd.getExtracts().getOfferExtract().getOfferIdKsnMap();
        Map<String, IAS> offerIdIasMap= new HashMap<>();

        for (String offerId : availableOfferIds) {
            String storeName = context.getStoreName();
            Sites site = Stores.getSite(storeName);
            boolean isCrossFormat = site.isCrossFormat(wd, context);
            URL url = getUrl(offerId); //Get URL for all stores
            //Override the url in case of kmart & mygofer, we use ksns for kmart availability
            if ((Stores.KMART.matches(context.getStoreName()) || Stores.MYGOFER.matches(context.getStoreName()))) {
                if (!isCrossFormat) {
                    url = getKmartUrl(offerIdKsnMap, offerId);
                }
            } else if (isCrossFormat) {
            	url = getKmartUrl(offerIdKsnMap, offerId);
            }
            if (url == null) {
                continue;
            }
            offerIdIasMap.put(offerId, call(url));
        }
        return offerIdIasMap;
    }


	/**
     * This method explicitly handles cross site IAS data for Sears and Kmart. If Sears then returns Kmart IAS data and vice versa
     *
     * @param wd
     * @param context
     */
    public Optional<IAS> getCrossSiteIASData(String offerId, WorkingDocument wd, ContextMessage context) {

        if (Stores.KMART.matches(context.getStoreName())) {
            return Optional.ofNullable(call(getUrl(offerId)));
        } else if (Stores.SEARS.matches(context.getStoreName())) {
            Optional ksnOptional = Optional.ofNullable(wd.getExtracts().getOfferExtract().getOfferIdKsnMap().get(offerId));
            if (ksnOptional.isPresent()) {
                return Optional.ofNullable(call(getKmartUrl(wd.getExtracts().getOfferExtract().getOfferIdKsnMap(), offerId)));
            }
        }
        return Optional.empty();
    }
}
