package com.adk.aktway.search.solr;

import com.adk.aktway.search.config.GlobalConstants;
import com.adk.aktway.search.config.PropertiesLoader;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;

public enum SolrServerInitializer {

    INSTANCE;
    private SolrClient solrClient;

    SolrServerInitializer() {
        CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder().withZkHost(PropertiesLoader.getProperty(GlobalConstants.SOLR_ZK_HOST)).build();
        cloudSolrClient.setDefaultCollection(PropertiesLoader.getProperty(GlobalConstants.SOLR_CLOUD_COLLECTION));
        cloudSolrClient.setZkConnectTimeout(Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.CLOUD_ZK_TIMEOUT)));
        solrClient = cloudSolrClient;
    }

    public SolrClient getSolrClient() {
        return solrClient;
    }

}
