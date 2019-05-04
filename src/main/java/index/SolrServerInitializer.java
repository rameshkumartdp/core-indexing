package index;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

public enum SolrServerInitializer {

    INSTANCE;
    private SolrClient solrClient;

    SolrServerInitializer() {

        /*CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder()
                .withZkHost("")
                .build();
        cloudSolrClient.setDefaultCollection("");
        cloudSolrClient.setZkConnectTimeout(100);
        solrClient = cloudSolrClient;*/

        solrClient = new HttpSolrClient("http://localhost:8983/solr/collection2");
    }

    public SolrClient getSolrClient() {
        return solrClient;
    }

}
