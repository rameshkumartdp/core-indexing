package com.shc.ecom.search.common.greenbox;

import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.search.common.vo.buybox.BuyBoxDomain;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.extract.components.uas.UasService;
import com.shc.ecom.search.http.ApacheHttpClient;
import com.shc.ecom.search.http.HttpClient;
import com.shc.ecom.search.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Component
public class RankingService {

    private static final String NO_OFFERS_TO_RANK = "No offers to rank";

    private static final Logger LOGGER = LoggerFactory.getLogger(UasService.class);

    private String hostname = PropertiesLoader.getProperty(GlobalConstants.BUYBOX_HOST_URL);

    private int portno = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.BUYBOX_PORT));

    private String path = PropertiesLoader.getProperty(GlobalConstants.BUYBOX_RANK_GET_URL);

    private String collection = PropertiesLoader.getProperty(GlobalConstants.BUYBOX_RANK_COLLECTION);

    private URL getURL(String uid, String site) {
        URL url = null;
        try {
            url = new URL("http://" + hostname + ":" + portno + path + collection + "?uid=" + uid + (StringUtils.isNotEmpty(site) ? "&site=" + site : StringUtils.EMPTY));
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url);
        }
        return url;
    }

    public BuyBoxDomain getBuyBox(String uid, String site) {
        HttpClient httpClient = new ApacheHttpClient();
        String jsonResponse = null;
        if (GlobalConstants.MYGOFER.equalsIgnoreCase(site)) {
            jsonResponse = httpClient.httpGet(getURL(uid, "kmart"));
        } else {
            jsonResponse = httpClient.httpGet(getURL(uid, site));
        }
        BuyBoxDomain buyboxDomain = null;
        if (!StringUtils.startsWithIgnoreCase(jsonResponse, NO_OFFERS_TO_RANK)) {
            buyboxDomain = JsonUtil.convertToEntityObject(jsonResponse, BuyBoxDomain.class);
        }
        if (buyboxDomain == null) {
            buyboxDomain = new BuyBoxDomain();
        }
        return buyboxDomain;
    }

}
