package index;

import com.fasterxml.jackson.databind.ObjectMapper;
import docbuilder.SearchDoc;
import docbuilder.TicketDetails;
import org.apache.http.protocol.HTTP;
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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class IndexDoc {

    public void sendToSolr() {
        System.out.println("Sending document to Solr Cloud");
        try {
            /*RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            //headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            //headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentType(new MediaType("application","json"));*/
             /*HttpEntity<String> entity = new HttpEntity<>(requestJson.toString(), headers);
            restTemplate.exchange("http://localhost:8983/solr/collection3/update/json/docs?split=/&f=*//**&commit=true", HttpMethod.POST, entity, String.class);
             */

            ObjectMapper mapper = new ObjectMapper();
            InputStream is = new FileInputStream("I:\\Ramesh\\core-indexing\\src\\main\\resources\\activity_sample.json");
            SearchDoc document = mapper.readValue(is, SearchDoc.class);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmSSS");
            document.setIndexTime(sdf.format(new Date()));
            document.setAboutInfo(document.getAdditionalInfo().getAboutInfo().getAbout());
            document.setMiscellaneous(document.getAdditionalInfo().getMiscellaneous());
            List<String> accList = new ArrayList<>();
            accList.add("type:"+document.getAccessoryInfo().get(0).getType());
            accList.add("description:"+document.getAccessoryInfo().get(0).getDescription());
            document.setAccessoryInfos(accList);
            List<TicketDetails> ticketList = document.getTicketInfo();
            Iterator<TicketDetails> itr = ticketList.iterator();
            while(itr.hasNext()) {
                TicketDetails details = itr.next();
                List<String> list = new ArrayList<>();
                list.add("ticketCategory:"+details.getTicketCategory());
                list.add("price:"+details.getPrice());
                list.add("totalPrice:"+details.getTotalPrice());
                list.add("fromAge:"+details.getFromAge());
                list.add("noOfTotalTickets:"+details.getNoOfTotalTickets());
                list.add("toAge:"+details.getToAge());
                if(details.getTicketCategory().equals("Adult")) {
                    document.setChildTicketInfo(list);
                } else {
                    document.setAdultTicketInfo(list);
                }
            }
            SolrClient solr = SolrServerInitializer.INSTANCE.getSolrClient();
            SolrInputDocument solrInputDocument = convertToSolrInputDocument(document);
            UpdateResponse updateResponse = solr.add(solrInputDocument);
            // UpdateResponse updateResponse = solr.add(solrInputDocument);
            if (updateResponse.getStatus() == 0) {
                System.out.println("Elapsed time in posting document to Solr: "+ updateResponse.getElapsedTime());
                solr.commit();
            } else {
                System.err.println("Failed to post documents to Solr. Response status: "+ updateResponse.getStatus());
            }
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
