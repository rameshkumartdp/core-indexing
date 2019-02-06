package index;

import docbuilder.SearchDoc;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.TransformJson;


import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

@Component
public class IndexDoc {

    public void sendToSolr() {
       // SearchDoc document = new TransformJson().getObjectFromJson();
        //SolrInputDocument solrInputDocument = convertToSolrInputDocument(document);
        System.out.println("Sending document to Solr Cloud");
        SolrClient solr = new SolrServerInitializer().getSolrClient();
        try {
            InputStream jsonInput = new FileInputStream("C:\\Users\\Test\\Desktop\\custom.json");

            JSONUpdateRequest request = new JSONUpdateRequest(jsonInput);
            request.setSplit("/exams");
            request.addFieldMapping("first", "/first");
            request.addFieldMapping("last", "/last");
            request.addFieldMapping("grade", "/grade");
            request.addFieldMapping("subject", "/exams/subject");
            request.addFieldMapping("test", "/exams/test");
            request.addFieldMapping("marks", "/exams/marks");

            UpdateResponse updateResponse = request.process(solr);
            // UpdateResponse updateResponse = solr.add(solrInputDocument);
            if (updateResponse.getStatus() == 0) {
                System.out.println("Elapsed time in posting document to Solr: "+ updateResponse.getElapsedTime());
                solr.commit();
            } else {
                System.err.println("Failed to post documents to Solr. Response status: "+ updateResponse.getStatus());
            }
        } catch(Exception io){}
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
