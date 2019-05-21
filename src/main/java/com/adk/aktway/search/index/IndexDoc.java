package com.adk.aktway.search.index;

import com.adk.aktway.search.config.GlobalConstants;
import com.adk.aktway.search.db.SpringMongoClient;
import com.adk.aktway.search.docbuilder.SearchDoc;
import com.adk.aktway.search.solr.SolrServerInitializer;
import com.adk.aktway.search.transform.BaseFieldTransformations;
import com.mongodb.client.MongoCursor;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.bson.Document;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class IndexDoc {

    public void sendToSolr() {
        String indexTime = new SimpleDateFormat(GlobalConstants.INDEX_TIME_FORMAT).format(new Date());
        List<SearchDoc> searchDocList = new ArrayList<>();
        MongoCursor<Document> cursor = SpringMongoClient.INSTANCE.getMongoCursor();

        while (cursor.hasNext()) {
            Document doc = cursor.next();

            SearchDoc document = new BaseFieldTransformations().transformDoc(doc, indexTime);
            searchDocList.add(document);
        }
        postToSolr(searchDocList);
    }

    public void postToSolr(List<SearchDoc> searchDocList) {
        System.out.println("Sending documents to Solr Cloud");
        List<SolrInputDocument> solrInputDocuments = convertToSolrInputDocuments(new ArrayList<>(searchDocList));
        SolrClient solr = SolrServerInitializer.INSTANCE.getSolrClient();
        UpdateRequest req = new UpdateRequest();
        req.setAction(UpdateRequest.ACTION.COMMIT, false, false);
        req.add(solrInputDocuments);

        try {
            //UpdateResponse updateResponse = solr.add(solrInputDocuments);
            UpdateResponse updateResponse = req.process(solr);
            if (updateResponse.getStatus() == 0) {
                System.out.println("Elapsed time in posting " + solrInputDocuments.size() + " documents to Solr: " + updateResponse.getElapsedTime());
                solr.commit();
            } else {
                System.err.println("Failed to post " + solrInputDocuments.size() + " documents to Solr. Response status: " + updateResponse.getStatus());
            }
        } catch(IOException ie) {
        } catch (SolrServerException sse) {
        }
    }

    public static List<SolrInputDocument> convertToSolrInputDocuments(Collection<?> docs) {
        List<SolrInputDocument> solrInputDocuments = new ArrayList<>();
        for (Object doc : docs) {
            SolrInputDocument solrInputDocument = convertToSolrInputDocument(doc);
            solrInputDocuments.add(solrInputDocument);
        }
        return solrInputDocuments;
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
