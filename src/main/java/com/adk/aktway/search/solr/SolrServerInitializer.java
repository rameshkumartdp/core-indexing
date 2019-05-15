package com.adk.aktway.search.solr;

import com.adk.aktway.search.config.GlobalConstants;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;

public enum SolrServerInitializer {

    INSTANCE;
    private SolrClient solrClient;

    SolrServerInitializer() {
        CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder().withZkHost(GlobalConstants.SOLR_ZK_HOST).build();
        cloudSolrClient.setDefaultCollection(GlobalConstants.SOLR_CLOUD_COLLECTION);
        cloudSolrClient.setZkConnectTimeout(Integer.parseInt(GlobalConstants.CLOUD_ZK_TIMEOUT));
        solrClient = cloudSolrClient;
    }

    public SolrClient getSolrClient() {
        return solrClient;
    }

}
