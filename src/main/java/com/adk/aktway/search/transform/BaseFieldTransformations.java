package com.adk.aktway.search.transform;

import com.adk.aktway.search.docbuilder.SearchDoc;
import com.adk.aktway.search.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;

import java.io.IOException;
import java.util.*;

/**
 * Created by rames on 22-05-2019.
 */
public class BaseFieldTransformations {
    public SearchDoc transformDoc(Document doc, String indexTime) {
        doc.remove("displayImages");
        doc.remove("aboutImages");
        doc.remove("highlightsImages");
        doc.put("_id", doc.get("_id"));
        System.out.println(doc.get("_id"));
        String json = com.mongodb.util.JSON.serialize(doc);
        json = json.replace("{ \"$oid\" :","").replace("} , \"serviceProviderId\""," , \"serviceProviderId\"").replace("} , \"serviceId\""," , \"serviceId\"");
        json = json.replace("\"$date\"","").replace(" : {  :",":").replace("Z\"}","Z\"");

        System.out.println("JSON serialized Document: " + json);
        SearchDoc document = null;
        ObjectMapper mapper1 = new ObjectMapper();
        // mapper1.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            document = mapper1.readValue(json, SearchDoc.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.setName(Optional.ofNullable(document.getName()).orElse(""));
        document.setIndexTime(indexTime);
        document.setAboutInfo(document.getAdditionalInfo().getAboutInfo().getAbout());
        List<TicketDetails> ticketList = document.getTicketInfo();
        Iterator<TicketDetails> itr = ticketList.iterator();
        while(itr.hasNext()) {
            TicketDetails details = itr.next();
            List<String> list = new ArrayList<>();
            String ticketCategory = details.getTicketCategory();
            list.add("ticketCategory:"+ticketCategory);
            list.add("price:"+details.getPrice());
            list.add("totalPrice:"+details.getTotalPrice());
            list.add("fromAge:"+details.getFromAge());
            list.add("noOfTotalTickets:"+details.getNoOfTotalTickets());
            list.add("toAge:"+details.getToAge());
            if(ticketCategory != null) {
                switch (details.getTicketCategory()) {
                    case "Adult":
                        document.setAdultTicketInfo(list);
                    case "Infant":
                        document.setInfantTicketInfo(list);
                    case "Child":
                        document.setChildTicketInfo(list);
                    case "SeniorCitizen":
                        document.setSeniorCitizenTicketInfo(list);
                }
            }
        }
        BookingDetail bookingDetail = document.getBookingDetail();
        if(bookingDetail != null) {
            List<String> bookingInfo = new ArrayList<>();
            bookingInfo.add("departurePoint:" + Optional.ofNullable(bookingDetail.getDeparturePoint()).orElse(""));
            bookingInfo.add("duration:" + Optional.ofNullable(bookingDetail.getDuration()).orElse(""));
            bookingInfo.add("typeOfBooking:" + Optional.ofNullable(bookingDetail.getTypeOfBooking()).orElse(""));
            List<BookingDetailEntries> bookingDetailEntries = bookingDetail.getBookingDetailEntries();
            List<Map<String, String>> list = new ArrayList<>();
            Iterator<BookingDetailEntries> itr1 = bookingDetailEntries.iterator();
            while (itr1.hasNext()) {
                BookingDetailEntries details = itr1.next();
                Map<String, String> map = new LinkedHashMap<>();
                map.put("startTime:", details.getStartTime());
                map.put("endTime:", details.getEndTime());
                map.put("noOfTickets:", details.getNoOfTickets());
                list.add(map);
            }
            bookingInfo.add("bookingEntries:" + list);
            document.setBookingInfo(bookingInfo);
        }
        List<String> accessoryDetailInfo = new ArrayList<>();
        AccessoryDetail accessoryDetails = document.getAccessoryDetail();
        if(accessoryDetails != null) {
            List<AccessoryDetailEntries> accessoryDetailEntries = accessoryDetails.getAccessoryDetailEntries();
            Iterator<AccessoryDetailEntries> itr2 = accessoryDetailEntries.iterator();
            while (itr2.hasNext()) {
                AccessoryDetailEntries accessoryDetailEntries1 = itr2.next();
                accessoryDetailInfo.add("description:" + accessoryDetailEntries1.getDescription());
                accessoryDetailInfo.add("type:" + accessoryDetailEntries1.getType());
                List<String> measurementList = new ArrayList<>();
                List<Measurements> measurementsList = accessoryDetailEntries1.getMeasurements();
                Iterator<Measurements> itr3 = measurementsList.iterator();
                while (itr3.hasNext()) {
                    Measurements measurements = itr3.next();
                    measurementList.add("size:" + measurements.getSize());
                    measurementList.add("units:" + measurements.getUnits());
                }
                accessoryDetailInfo.add("measurements:" + measurementList);
            }
            document.setAccessoryDetailInfo(accessoryDetailInfo);
        }

        List<String> highlightDesc = new ArrayList<>();
        List<Highlights> highlights = document.getAdditionalInfo().getHighlightsInfo().getHighlights();
        Iterator<Highlights> hItr = highlights.iterator();
        while(hItr.hasNext()) {
            highlightDesc.add(hItr.next().getDescription());
        }
        document.setHighlights(highlightDesc);
        return document;
    }
}
