package index;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

public class SolrServerInitializer {
    private SolrClient solrClient;

    public SolrClient getSolrClient() {
        SolrClient solrClient = new HttpSolrClient("http://localhost:8983/solr/collection1");
        this.solrClient = solrClient;
        return solrClient;
    }

}
