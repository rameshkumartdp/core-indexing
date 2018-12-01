package com.shc.ecom.search.extract.components.pricing;

import java.net.MalformedURLException;
import java.net.URL;

import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.search.common.constants.SearchCommonConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.codahale.metrics.Timer;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.exception.SearchCommonException;
import com.shc.ecom.search.common.pricinggrid.ItemResponse;
import com.shc.ecom.search.common.pricinggrid.PriceGridResponse;
import com.shc.ecom.search.common.pricinggrid.ProductResponse;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.http.ApacheHttpClient;
import com.shc.ecom.search.http.HttpClient;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.util.JsonUtil;
import com.shc.ecom.wxs.common.exception.SHCWxsException;

/**
 * @author rgopala Jul 23, 2015 search-doc-builder
 */
@Service
@Qualifier("pricingGridService")
public class PricingGridService implements PricingService {

	private static final long serialVersionUID = 10329424294347003L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PricingGridService.class);
	
    @Autowired
    protected MetricManager metricManager;
	
	private String pricingGridHostName = PropertiesLoader.getProperty(GlobalConstants.PRICE_GRID_HOST);

    private int pricingGridPortNo = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.PRICE_GRID_PORT));

    private String pricingGridPath = PropertiesLoader.getProperty(GlobalConstants.PRICE_GRID_PATH);
    
    private String pricingGridOffer = PropertiesLoader.getProperty(GlobalConstants.PRICE_GRID_OFFER);
    
    private String pricingGridSSIN = PropertiesLoader.getProperty(GlobalConstants.PRICE_GRID_SSIN);

    private String pricingGridSite = PropertiesLoader.getProperty(GlobalConstants.PRICE_GRID_SITE);
	 
	@Override
	public Price getOfferPrice(int storeID, String offerId) throws SHCWxsException {
		URL url = getOfferPriceUrl(offerId, Stores.getStoreName(storeID));
		PriceGridResponse priceGridResponse = call(url);
		Price price = new Price();
		if(CollectionUtils.isNotEmpty(priceGridResponse.getPriceResponse().getItemResponse())) {
			ItemResponse itemResponse = priceGridResponse.getPriceResponse().getItemResponse().get(0);
			if(!StringUtils.equals(itemResponse.getStatusCode(), "0")) {
				return price;
			}
			price.setRegPrice(Double.parseDouble(itemResponse.getRegularPrice()));
			price.setSellPrice(Double.parseDouble(itemResponse.getSellPrice().getPrice()));
			price.setWasPrice(Double.parseDouble(itemResponse.getWasPrice()));
			price.setNetDownPrice(Double.parseDouble(itemResponse.getNddPrice()));
			price.setClearance(BooleanUtils.toBoolean(itemResponse.getIndicators().getClearancepriceIndicator()));
			price.setSale(StringUtils.equalsIgnoreCase(itemResponse.getSellPrice().getPriceType(), "P"));
			price.setMapViolated(BooleanUtils.toBoolean(itemResponse.getMapDetails().getViolation()));
			price.setEverydayGreatPrice(BooleanUtils.toBoolean(itemResponse.getIndicators().getExceptionalValue()));
		}
		return price;
	}

	@Override
	public Price getProductPrice(int storeID, String ssin) throws SHCWxsException {
		URL url = getProductPriceUrl(ssin, Stores.getStoreName(storeID));
		PriceGridResponse priceGridResponse = call(url);
		Price price = new Price();
		ProductResponse productResponse = priceGridResponse.getPriceResponse().getProductResponse();
		if(productResponse!=null && StringUtils.equals(productResponse.getStatusCode(), "0")) {
			price.setRegPrice(Double.parseDouble(productResponse.getRegularPrice().getMin()));
			price.setSellPrice(Double.parseDouble(productResponse.getSellPrice().getMin()));
			price.setWasPrice(Double.parseDouble(productResponse.getWasPrice().getMin()));
			price.setNetDownPrice(Double.parseDouble(productResponse.getNddPrice().getMin()));
			price.setClearance(BooleanUtils.toBoolean(checkForVariationIndicatorValueToBeTrue(productResponse.getIndicators().getClearancepriceIndicator())));
			price.setSale(BooleanUtils.toBoolean(productResponse.getIndicators().getSalepriceIndicator()));
			price.setMapViolated(BooleanUtils.toBoolean(productResponse.getMapDetails().getViolation()));
			price.setEverydayGreatPrice(BooleanUtils.toBoolean(productResponse.getIndicators().getExceptionalValue()));
		}
		return price;
	}

	@Override
	public Price getStorePrice(String storeID, String offerId) throws SHCWxsException {
		return new Price();
	}

	@Override
	public Price getBundlePrice(int storeID, String bundleId) throws SearchCommonException {
		return new Price();
	}

	
	private URL getOfferPriceUrl(String offerId, String site) {
        URL url = null;
        try {
            url = new URL("http://" + pricingGridHostName + ":" + pricingGridPortNo + pricingGridPath + pricingGridOffer+"="+offerId+"&"+pricingGridSite+"="+site);
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url);
        }
        return url;
    }
	
	private URL getProductPriceUrl(String ssin, String site) {
        URL url = null;
        try {
            url = new URL("http://" + pricingGridHostName + ":" + pricingGridPortNo + pricingGridPath + pricingGridSSIN+"="+ssin+"&"+pricingGridSite+"="+site);
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url);
        }
        return url;
    }
	private PriceGridResponse call(URL url) {
		HttpClient httpClient = new ApacheHttpClient();
        metricManager.incrementCounter(Metrics.COUNTER_PRICE_GRID_CALLS);
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_PRICE_GRID_SERVICE);
        String jsonResponse = null;
        try {
            jsonResponse = httpClient.httpGet(url);
        } finally {
            metricManager.stopTiming(timerContext);
        }
        if (StringUtils.isEmpty(jsonResponse)) {
            return new PriceGridResponse();
        }
        return JsonUtil.convertToEntityObject(jsonResponse, PriceGridResponse.class);
	}
	private String checkForVariationIndicatorValueToBeTrue(String clearanceIndicator)
	{
		if(SearchCommonConstants.ONE.equals(clearanceIndicator) || SearchCommonConstants.FIVE.equals(clearanceIndicator) || SearchCommonConstants.TRUE.equals(clearanceIndicator)){
			return SearchCommonConstants.TRUE;
		}
		return SearchCommonConstants.FALSE;
	}
}
