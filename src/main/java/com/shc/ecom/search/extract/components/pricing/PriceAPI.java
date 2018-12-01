package com.shc.ecom.search.extract.components.pricing;

import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.co.config.core.ConfigException;
import com.shc.ecom.co.config.core.Configuration;
import com.shc.ecom.co.config.manager.ConfigManager;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.exception.SearchCommonException;
import com.shc.ecom.search.common.price.bundle.BundlePrice;
import com.shc.ecom.search.common.price.bundle.Indicators;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.http.ApacheHttpClient;
import com.shc.ecom.search.http.HttpClient;
import com.shc.ecom.search.util.JsonUtil;
import com.shc.ecom.wxs.api.SHCPriceAppClientFactory;
import com.shc.ecom.wxs.api.SHCPriceClient;
import com.shc.ecom.wxs.api.SHCStorePriceAppClientFactory;
import com.shc.ecom.wxs.api.data.SHCPrice;
import com.shc.ecom.wxs.api.data.SHCProductPrice;
import com.shc.ecom.wxs.api.utils.PriceClientRequest;
import com.shc.ecom.wxs.common.exception.SHCWxsException;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

/**
 * @author rgopala
 *         Jul 23, 2015 search-doc-builder
 */
@Service
@Qualifier("priceAPI")
public class PriceAPI implements PricingService {

    private static final long serialVersionUID = -3223397018862411233L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceAPI.class);
    private static final String DEF_CONFIG_PATH = ".";

    static {
        try {
            URL configDirPathUrl = PriceAPI.class.getClassLoader().getResource("");
            String configDirPath = (configDirPathUrl != null ? configDirPathUrl.getPath() : null);
            configDirPath = (configDirPath == null ? DEF_CONFIG_PATH : configDirPath);
            ConfigManager.using(Configuration.PROPERTY_FILE).configDirPath(new File(configDirPath)).configure();
        } catch (ConfigException e) {
            LOGGER.error("Exception while reading the configuration from file due to :: ", e);
        }
    }

    private String bundlePriceUrl = PropertiesLoader.getProperty(GlobalConstants.BUNDLEPRICE_PDP_URL);

    @Override
    public Price getOfferPrice(int storeID, String offerId) throws SHCWxsException {
        SHCPriceClient shcPriceClient = SHCPriceAppClientFactory.getClientInstance();
        if (shcPriceClient == null) {
            LOGGER.error("Unable to get SHCPriceClient for Store ID: " + storeID);
            return null;
        }
        SHCPrice shcPrice = shcPriceClient.getPrice(constructPriceClientRequest(Stores.getStoreName(storeID), null,offerId, null)).get(0);
        if (shcPrice == null) {
            LOGGER.error("Price Client Error: Online SHCPrice object is null for offer-id: " + offerId + ", store-id: " + storeID);
            return null;
        }
        Price price = new Price();
        price.setRegPrice(shcPrice.getRegularPrice());
        price.setSellPrice(shcPrice.getSellPrice());
        price.setWasPrice(shcPrice.getWasPrice());
        price.setNetDownPrice(shcPrice.getNetDownPrice());
        price.setClearance(shcPrice.isClearance());
        price.setSale(shcPrice.isPromotion());
        price.setMapViolated(shcPrice.isMapViolated());
        price.setRebateIds(shcPrice.getRebateIds());
        price.setEverydayGreatPrice(shcPrice.getExceptionalvalue());
        return price;
    }

    @Override
    public Price getProductPrice(int storeID, String ssin) throws SHCWxsException {
        SHCPriceClient shcPriceClient = SHCPriceAppClientFactory.getClientInstance();
        if (shcPriceClient == null) {
            LOGGER.error("Unable to get SHCPriceClient for Store ID: " + storeID);
            return null;
        }
        SHCProductPrice shcProductPrice = shcPriceClient.getProductPrice(constructPriceClientRequest(Stores.getStoreName(storeID), ssin, null,null));
        if (shcProductPrice == null) {
            LOGGER.error("Price Client Error: Online SHCProductPrice object is null for ssin: " + ssin + ", store-id: " + storeID);
            return null;
        }
        Price price = new Price();
        price.setRegPrice(shcProductPrice.getRegularPrice().getMinPrice());
        price.setSellPrice(shcProductPrice.getSellPrice().getMinPrice());
        price.setWasPrice(shcProductPrice.getWasPrice().getMinPrice());
        price.setNetDownPrice(shcProductPrice.getNetDownPrice().getMinPrice());
        price.setClearance(shcProductPrice.getClearanceIndicator() == 1 ? true : false);
        price.setSale(shcProductPrice.getSaleIndicator() == 1 ? true : false);
        price.setMapViolated(shcProductPrice.isMapViolated());
        price.setRebateIds(shcProductPrice.getRebateIds());
        price.setEverydayGreatPrice(shcProductPrice.isExceptionalvalue());
        return price;
    }

    @Override
    public Price getStorePrice(String storeID, String offerId) throws SHCWxsException {
        SHCPriceClient shcPriceClient = SHCStorePriceAppClientFactory.getClientInstance();
        Price price = new Price();
        if (shcPriceClient == null) {
            LOGGER.error("Unable to get SHCPriceClient for Store ID: " + storeID);
            return price;
        }
        SHCPrice shcPrice = shcPriceClient.getStorePrice(String.valueOf(storeID),offerId);
        if (shcPrice == null) {
            LOGGER.error("Price Client Error: In-Store SHCPrice object is null for offer-id: " + offerId + ", store-id: " + storeID);
            return price;
        }
        price.setRegPrice(shcPrice.getRegularPrice());
        price.setSellPrice(shcPrice.getSellPrice());
        price.setWasPrice(shcPrice.getWasPrice());
        price.setNetDownPrice(shcPrice.getNetDownPrice());
        price.setClearance(shcPrice.isClearance());
        price.setSale(shcPrice.isPromotion());
        price.setMapViolated(shcPrice.isMapViolated());
        price.setRebateIds(shcPrice.getRebateIds());
        price.setEverydayGreatPrice(shcPrice.getExceptionalvalue());
        return price;
    }

    @Override
    public Price getBundlePrice(int storeID, String bundleId) throws SearchCommonException {
        URL url = null;
        String pdpUrl = MessageFormat.format(bundlePriceUrl, bundleId, Stores.getSite(Stores.getStoreName(storeID)).getSiteName().toLowerCase());
        try {
            url = new URL(pdpUrl);
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url);
            throw new SearchCommonException("Bundle Price Error: Bundle price unavailable for " + bundleId + ", store: " + storeID);
        }
        HttpClient httpClient = new ApacheHttpClient();
        String jsonResponse = httpClient.httpGet(url);
        BundlePrice bundlePrice = JsonUtil.convertToEntityObject(jsonResponse, BundlePrice.class);
        Price price = new Price();
        if (bundlePrice == null || !StringUtils.equals(bundlePrice.getStatus(), "200")) {
            throw new SearchCommonException("Bundle Price Error: Bundle price unavailable for " + bundleId + ", store: " + storeID);
        }

        String oldPrice = bundlePrice.getData().getOldPrice().getNumericValue();
        String displayPrice = bundlePrice.getData().getDisplayPrice().getNumericValue();
        boolean clearanceIndicator = false;
        boolean saleIndicator = false;
        boolean mapViolating = false;
        for (Indicators indicator : bundlePrice.getData().getIndicators()) {
            String indicatorName = indicator.getName();
            if(StringUtils.isEmpty(indicatorName)) {
            	continue;
            }
			switch (indicatorName) {
                case "clearanceInd":
                    clearanceIndicator = StringUtils.equals(indicator.getExists(), "0") ? false : true;
                    break;
                case "saleInd":
                    saleIndicator = StringUtils.equals(indicator.getExists(), "0") ? false : true;
                    break;
                case "mapViolationMsg":
                    mapViolating = StringUtils.equals(indicator.getExists(), "0") ? false : true;
                    break;
            }
        }
        if (NumberUtils.isNumber(oldPrice)) {
            price.setRegPrice(Double.parseDouble(oldPrice));
            price.setWasPrice(Double.parseDouble(oldPrice));
        }
        if (NumberUtils.isNumber(displayPrice)) {
            price.setSellPrice(Double.parseDouble(displayPrice));
        }
        price.setClearance(clearanceIndicator);
        price.setSale(saleIndicator);
        price.setMapViolated(mapViolating);

        return price;
    }

    public static PriceClientRequest constructPriceClientRequest(String site, String ssin, String offer, String storeUnitNo){
        PriceClientRequest clientReqObj = new PriceClientRequest();
        clientReqObj.setSite(site);
        clientReqObj.setSsin(ssin);
        clientReqObj.addToOffersList(offer);
        clientReqObj.setClient("SOLRX");
        clientReqObj.setStoreUnitNum(storeUnitNo);
        clientReqObj.setCountryCd("US");
        clientReqObj.setCurrencyCd("USD");
        return clientReqObj;
    }

}
