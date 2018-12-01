package com.shc.ecom.search.extract.components.behavioral;

import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.search.common.behavioral.BehavioralDto;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.http.ApacheHttpClient;
import com.shc.ecom.search.http.HttpClient;
import com.shc.ecom.search.util.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pchauha
 */
@Service
public class BehavioralService implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6286419353268889021L;

    private static final Logger LOGGER = LoggerFactory.getLogger(BehavioralService.class);

    private String hostname = PropertiesLoader.getProperty(GlobalConstants.BEHAVIORAL_HOSTNAME);

    private int portno = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.BEHAVIORAL_PORTNO));

    private String path = PropertiesLoader.getProperty(GlobalConstants.BEHAVIORAL_PATH);

    private BehavioralDto call(URL url) {
        HttpClient httpClient = new ApacheHttpClient();
        String jsonResponse = httpClient.httpGet(url);

        if (StringUtils.isEmpty(jsonResponse)) {
            return new BehavioralDto();
        }
        return JsonUtil.convertToEntityObject(jsonResponse, BehavioralDto.class);
    }

    private URL createSolrUrl(List<Sites> sites, String ssin) {

        StringBuilder qParamValue = new StringBuilder();

        for (Sites site : sites) {
            if (sites.indexOf(site) > 0) {
                qParamValue.append(" OR ");
            }
            qParamValue.append("id:(").append(StringUtils.lowerCase(site.getSiteName())).append("_").append(ssin).append(")");
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("q", qParamValue.toString());
        parameters.put("rows", String.valueOf(2));
        parameters.put("fl", "id, behavioral");
        parameters.put("wt", "json");

        List<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        String query = URLEncodedUtils.format(pairs, "UTF-8");

        URL url = null;
        try {
            url = new URL("http://" + hostname + ":" + portno + path + "?" + query);
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url);
        }
        return url;
    }

    public BehavioralDto process(List<Sites> sites, String ssin) {
        if (StringUtils.isEmpty(ssin) || CollectionUtils.isEmpty(sites)) {
            return new BehavioralDto();
        }
        URL url = createSolrUrl(sites, ssin);
        return call(url);
    }
}
