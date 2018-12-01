package com.shc.ecom.search.extract.components.solr;

import com.codahale.metrics.Timer;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.extract.components.pricing.Price;
import com.shc.ecom.search.http.ApacheHttpClient;
import com.shc.ecom.search.http.HttpClient;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.util.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * This class should be used for all communication with the Solr ZK ensemble
 * <p>
 * Created by hdargah
 */

@Service
public class SolrService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolrService.class.getName());
    @Autowired
    protected MetricManager metricManager;
    private CloudSolrClient cloudSolrClient;
    private String randomLiveNode;
    private String fallbackSolrQuery;
    private String solrActiveCloudCollection;
    private Set<String> liveNodes;

    @PostConstruct
    public void init() {

        String solrZkHost = PropertiesLoader.getProperty(GlobalConstants.SOLR_ZK_HOST);
        int cloudZkTimeout = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.CLOUD_ZK_TIMEOUT));
        fallbackSolrQuery = PropertiesLoader.getProperty(GlobalConstants.FALLBACK_PRICING_SOLR_QUERY);
        solrActiveCloudCollection = PropertiesLoader.getProperty(GlobalConstants.SOLR_ACTIVE_CLOUD_COLLECTION);

        cloudSolrClient = new CloudSolrClient.Builder().withZkHost(solrZkHost).build();
        cloudSolrClient.setDefaultCollection(solrActiveCloudCollection);
        cloudSolrClient.setZkConnectTimeout(cloudZkTimeout);
        //Fails execution if zk path is incorrect
        cloudSolrClient.connect();
        liveNodes = cloudSolrClient.getZkStateReader().getClusterState().getLiveNodes();
        try {
            //Assuming the state of the cluster will not change for the duration of indexing
            //Closing the zk connection
            cloudSolrClient.close();
        } catch (IOException ioE) {
            LOGGER.error("Error Closing connection to ZK " + solrZkHost, ioE);
        }
        try {
            //Select and preprocess random node from the collection
            randomLiveNode = preProcessLiveNode(selectRandomLiveNode(liveNodes).orElse(""));

        } catch (Exception e) {
            LOGGER.error("!! Solr Active Collection is down !!", e);
            randomLiveNode = "";
        }
    }


    /**
     * This method queries production Solr to get the last known price of the product.
     * The price can be from the same store or a different store. The purpose is to get a ballpark number
     * to mitigate incorrect sorting due to absent prices.
     * <p>
     * Routes queries to the solr endpoint for the respective environment.
     *
     * @param price         Empty price Element
     * @param partnumber    OfferID or PartNumber can be used interchangeably
     * @param onlineStoreId Store for which we are indexing
     * @return
     * @author Huzefa
     */
    public Price getFallbackPriceFromSolr(Price price, String partnumber, int onlineStoreId) {

        metricManager.incrementCounter(Metrics.COUNTER_FALLBACK_SOLR_PRICE_CALLS);

        if (StringUtils.isEmpty(partnumber)) {
            return price;
        }
        //Future optimization possible using cloudSolrClient API
        String solrFallBackQuery = "http://" + randomLiveNode + fallbackSolrQuery + partnumber + "*";
        Map<Integer, Float> pricingMap = new HashMap<>();
        String solrResponse = null;
        Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_FALLBACK_SOLR_PRICE_SERVICE);
        try {
            HttpClient httpClient = new ApacheHttpClient();
            URL solrFallbackURL = new URL(solrFallBackQuery);
            solrResponse = httpClient.httpGet(solrFallbackURL);
        } catch (MalformedURLException e) {
            LOGGER.error("Malformed URL in SolrService for " + partnumber, e);
            return price;
        } catch (Exception e) {
            LOGGER.error("Unknown Exception in SolrService for " + partnumber, e);
            return price;
        } finally {
            metricManager.stopTiming(timerContext);
        }

        pricingMap = JsonUtil.parseJsonToPrice(solrResponse, pricingMap);

        Optional fallbackPrice = getBestPrice(pricingMap, onlineStoreId);
        if (!fallbackPrice.isPresent()) {
            LOGGER.info("Fallback price for " + partnumber + " not found !");
            return price;
        }
        LOGGER.info("Fallback price for " + partnumber + " : " + fallbackPrice.get());
        //Setting the wasPrice since that is the last resort in PricingAlgorithm
        price.setWasPrice(((Float) fallbackPrice.get()).floatValue());

        return price;
    }

    /**
     * Heuristic to select the best price from the prices returned from Solr
     *
     * @param pricingMap    Returned from JsonUtil
     * @param onlineStoreId The current storeId
     * @return The best price based on the heuristic chosen
     */
    private Optional<Float> getBestPrice(Map<Integer, Float> pricingMap, int onlineStoreId) {

        //No fallback price found. Can't do much
        if (pricingMap.isEmpty() || !Stores.isValidStoreId(onlineStoreId)) {
            LOGGER.error("EmptyPriceMap or Invalid storeId " + onlineStoreId + " passed in SolrService");
            return Optional.empty();
        }

        //Return the only value if only one
        if (pricingMap.size() == 1) {
            return Optional.of(new Float((float) pricingMap.values().toArray()[0]));
        }

        //Below will work well for sears and fbm, since they share the same onlineStoreId
        if (pricingMap.containsKey(onlineStoreId)) {
            return Optional.of(new Float(pricingMap.get(onlineStoreId)));
        }

        //When we have multiple values, return one at random
        Random random = new Random();
        return Optional.of(new Float((float) pricingMap.values().toArray()[random.nextInt(pricingMap.size())]));
    }


    /**
     * Out of a Set of Live nodes selects one at random
     *
     * @param liveNodes
     * @return
     */
    private Optional<String> selectRandomLiveNode(Set<String> liveNodes) throws Exception {
        if (CollectionUtils.isEmpty(liveNodes)) {
            //This scenario should never occur in PROD, if it does, so help us God !!
            throw new SolrActiveCollectionDownException("!!! Solr Active Collection Down !!!");
        }
        if (liveNodes.size() == 1) {
            return Optional.ofNullable(liveNodes.iterator().next());
        } else {
            int random = new Random().nextInt(liveNodes.size());
            int i = 0;
            for (String node : liveNodes) {
                if (i == random) {
                    return Optional.ofNullable(node);
                }
                i++;
            }
        }
        return Optional.empty();
    }

    /**
     * The livenode returned from zookeeper have a suffix "_solr" which needs to be removed before forming the URL
     * One could have retained the existing "solr" suffix and replaced _ with / to shorten the query.
     * But since the number of operations remain the same, let's keep everything explicit.
     * * @param liveNode
     *
     * @return
     */
    private String preProcessLiveNode(String liveNode) {
        String liveNodeSuffix = PropertiesLoader.getProperty(GlobalConstants.SOLR_LIVE_NODE_SUFFIX);
        return liveNode.replace(liveNodeSuffix, StringUtils.EMPTY);

    }

    public String getRandomLiveNode() {
        return randomLiveNode;
    }

    public String getFallbackSolrQuery() {
        return fallbackSolrQuery;
    }

    public Set<String> getLiveNodes() {
        return liveNodes;
    }

    public String getSolrActiveCloudCollection() {
        return solrActiveCloudCollection;
    }

    public class SolrActiveCollectionDownException extends Exception {
        public SolrActiveCollectionDownException(String message) {
            super(message);
        }
    }
}
