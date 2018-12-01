package com.shc.ecom.search.extract.components.prodstats;

import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.prodstats.Retail;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.http.ApacheHttpClient;
import com.shc.ecom.search.http.HttpClient;
import com.shc.ecom.search.util.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
public class ProdStatsService implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8131128424975781678L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdStatsService.class);

    private static final String SEPARATOR = "_";
    private static final String BLANKSPACE = " ";

    private String hostname = PropertiesLoader.getProperty(GlobalConstants.RETAIL_SERVICE_HOST_URL);

    private int portno = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.RETAIL_SERVICE_PORT));

    private String path = PropertiesLoader.getProperty(GlobalConstants.RETAIL_SERVICE_SELECT_URL);

    private URL getUrl(String ssin, String offerId, List<Sites> sites) {
        StringBuilder queryBuilder = new StringBuilder();

        for (Sites site : sites) {

            String sitePrefix = StringUtils.lowerCase(site.getSiteName()) + SEPARATOR;

            //TODO - SEARSPR Hack, no puertorico store available in retail service, remove once diffs are done
            if (site.matches(Sites.SEARSPR.getSiteName())) {
                sitePrefix = StringUtils.lowerCase(Sites.SEARS.getSiteName()) + SEPARATOR;
            }

            queryBuilder.append(sitePrefix).append(ssin);
            if(StringUtils.isNotBlank(offerId)){
                queryBuilder.append(BLANKSPACE).append("OR").append(BLANKSPACE).append(sitePrefix).append(offerId);
            }
        }

        String query = "id:(" + queryBuilder.toString() + ")";

        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.setRows(8);
        solrQuery.setFields("id,productViews,itemsSold,revenue,conversion");
        solrQuery.set("wt", "json");
        URL url = null;
        try {
            url = new URL("http://" + hostname + ":" + portno + path + "?" + solrQuery.toString());
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url);
        }
        return url;
    }

    private Retail call(URL url) {
        HttpClient httpClient = new ApacheHttpClient();
        String jsonResponse = httpClient.httpGet(url);
        if (StringUtils.isEmpty(jsonResponse)) {
            return new Retail();
        }
        return JsonUtil.convertToEntityObject(jsonResponse, Retail.class);
    }

    public Retail process(String ssin, String offerId, List<Sites> sites) {
        if (StringUtils.isEmpty(ssin) || CollectionUtils.isEmpty(sites)) {
            return new Retail();
        }
        URL url = getUrl(ssin, offerId,  sites);
        Retail retail = call(url);
        if (retail == null) {
            retail = new Retail();
        }
        return retail;
    }

    public Retail process(String ssin,  List<Sites> sites){
        return process(ssin, "", sites);
    }
}
