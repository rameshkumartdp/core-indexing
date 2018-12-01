package com.shc.ecom.search.indexer;

import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.search.config.GlobalConstants;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;

public enum SolrServerInitializer {
    INSTANCE;
    private SolrClient solrClient;

    SolrServerInitializer() {
        String solr_zk_host = PropertiesLoader.getProperty(GlobalConstants.SOLR_ZK_HOST);
        String solr_cloud_collection = PropertiesLoader.getProperty(GlobalConstants.SOLR_CLOUD_COLLECTION);
        int cloud_zk_timeout = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.CLOUD_ZK_TIMEOUT));
        CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder()
                .withZkHost(solr_zk_host)
                .build();
        cloudSolrClient.setDefaultCollection(solr_cloud_collection);
        cloudSolrClient.setZkConnectTimeout(cloud_zk_timeout);
        solrClient = cloudSolrClient;

    }

    public SolrClient getSolrClient() {
        return solrClient;
    }
}
