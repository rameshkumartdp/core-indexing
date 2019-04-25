package index;

import com.fasterxml.jackson.databind.ObjectMapper;
import docbuilder.SearchDoc;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import util.TransformJson;


import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collections;

@Component
public class IndexDoc {

    public void sendToSolr() {
       // SearchDoc document = new TransformJson().getObjectFromJson();
        //SolrInputDocument solrInputDocument = convertToSolrInputDocument(document);
        System.out.println("Sending document to Solr Cloud");
        SolrClient solr = new SolrServerInitializer().getSolrClient();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            ObjectMapper mapper = new ObjectMapper();
            InputStream is = new FileInputStream("D:\\Ramesh\\core-indexing\\src\\main\\resources\\activity_sample.json");
            Object requestJson = mapper.readValue(is, Object.class);

            HttpEntity<String> entity = new HttpEntity<>(requestJson.toString(), headers);
            restTemplate.exchange("http://localhost:8983/solr/collection2/update/json/docs?split=/&f=/**&commit=true", HttpMethod.POST, entity, Object.class);

            /*pdateResponse updateResponse = request.process(solr);
            // UpdateResponse updateResponse = solr.add(solrInputDocument);
            if (updateResponse.getStatus() == 0) {
                System.out.println("Elapsed time in posting document to Solr: "+ updateResponse.getElapsedTime());
                solr.commit();
            } else {
                System.err.println("Failed to post documents to Solr. Response status: "+ updateResponse.getStatus());
            }*/
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static SolrInputDocument convertToSolrInputDocument(Object obj) {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        for (Field f : obj.getClass().getDeclaredFields()) {
            f.setAccessible(true); //Mandatory
            try {
                solrInputDocument.addField(getFieldName(f), f.get(obj));
            } catch (IllegalAccessException iaE) {
                System.err.println("Error in converting SearchDoc to SolrInputDoc "+ iaE);
            }
        }
        return solrInputDocument;
    }

    private static String getFieldName(Field f) {

        org.apache.solr.client.solrj.beans.Field field = f.getAnnotation(org.apache.solr.client.solrj.beans.Field.class);
        if (field != null && !"#default".equals(field.value())) {
            return field.value();
        } else {
            return f.getName();
        }

    }
}
