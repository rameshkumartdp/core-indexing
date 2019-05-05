package index;

import com.fasterxml.jackson.databind.ObjectMapper;
import docbuilder.SearchDoc;
import docbuilder.TicketDetails;
import org.apache.http.protocol.HTTP;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Component;
import java.io.*;
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
            String indexTime = new SimpleDateFormat("yyyy_MM_dd_HHmmSSS").format(new Date());
            SearchDoc document = null;
            List<SearchDoc> searchDocList = new ArrayList<>();
            File folder = new File("/home/ec2-user/jsonFilesToIndex/");
            //File folder = new File("C:\\Users\\rames\\Desktop\\FIle");
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    ObjectMapper mapper = new ObjectMapper();
                    InputStream is = new FileInputStream(folder+"/"+file.getName());
                    document = mapper.readValue(is, SearchDoc.class);
                    document.setName(document.getName().toLowerCase());
                    document.setIndexTime(indexTime);
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
                }
                searchDocList.add(document);
            }
            List<SolrInputDocument> solrInputDocuments = convertToSolrInputDocuments(searchDocList);

            SolrClient solr = SolrServerInitializer.INSTANCE.getSolrClient();
            UpdateResponse updateResponse = solr.add(solrInputDocuments);
            if (updateResponse.getStatus() == 0) {
                System.out.println("Elapsed time in posting "+solrInputDocuments.size()+" documents to Solr: "+ updateResponse.getElapsedTime());
                solr.commit();
            } else {
                System.err.println("Failed to post "+solrInputDocuments.size()+" documents to Solr. Response status: "+ updateResponse.getStatus());
            }
        } catch(Exception e){
            e.printStackTrace();
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
