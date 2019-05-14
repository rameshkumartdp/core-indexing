package solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

public enum SolrServerInitializer {

    INSTANCE;
    private SolrClient solrClient;

    SolrServerInitializer() {

       CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder()
//              .withZkHost("ec2-18-222-165-251.us-east-2.compute.amazonaws.com:2181/solr")
                .withZkHost("localhost:2181")
                .build();
        cloudSolrClient.setDefaultCollection("activity");
        cloudSolrClient.setZkConnectTimeout(100);
        solrClient = cloudSolrClient;

        //solrClient = new HttpSolrClient("http://ec2-18-222-165-251.us-east-2.compute.amazonaws.com:8983/solr/activity");
        //solrClient = new HttpSolrClient("http://localhost:8983/solr/collection2");
    }

    public SolrClient getSolrClient() {
        return solrClient;
    }

}
