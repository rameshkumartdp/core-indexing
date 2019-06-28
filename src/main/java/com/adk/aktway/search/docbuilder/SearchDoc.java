package com.adk.aktway.search.docbuilder;

import com.adk.aktway.search.model.AccessoryDetail;
import com.adk.aktway.search.model.AdditionalInfo;
import com.adk.aktway.search.model.BookingDetail;
import com.adk.aktway.search.model.TicketDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.solr.client.solrj.beans.Field;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchDoc {

    @Field("id")
    private String _id;

    @Field
    private String serviceProviderId;

    @Field
    private String serviceId;

    @Field
    private String searchText;

    @Field
    private String name;

    @Field
    private String description;

    @Field
    private String location;

    @Field
    private String category;

    @Field
    private String price;

    @Field
    private String contact;

    private AccessoryDetail accessoryDetail;

    @JsonIgnore
    private List<String> accessoryDetailInfo = new ArrayList<>();

    @Ignore
    private BookingDetail bookingDetail;

    @Field
    private List<String> bookingInfo = new ArrayList<>();

    private  List<TicketDetails> ticketInfo = new ArrayList<>();

    @Field
    private  List<String> adultTicketInfo = new ArrayList<>();

    @Field
    @JsonIgnore
    private  List<String> childTicketInfo ;

    @Field
    @JsonIgnore
    private  List<String> infantTicketInfo ;

    @Field
    private  String activityStartDate ;

    @Field
    private  String activityEndDate ;

    @Field
    @JsonIgnore
    private  List<String> seniorCitizenTicketInfo ;

    private AdditionalInfo additionalInfo;

    @Field
    private  String aboutInfo = "";

    @Field
    private  List<String> highlights = new ArrayList<>();

    //@Field
    private  List<String> highlightsInfo = new ArrayList<>();

    @Field
    private  String _class;

    @Field
    private  String indexTime = "";

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<TicketDetails> getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(List<TicketDetails> ticketInfo) {
        this.ticketInfo = ticketInfo;
    }

    public String getAboutInfo() {
        return aboutInfo;
    }

    public void setAboutInfo(String aboutInfo) {
        this.aboutInfo = aboutInfo;
    }

    public List<String> getHighlightsInfo() {
        return highlightsInfo;
    }

    public void setHighlightsInfo(List<String> highlightsInfo) {
        this.highlightsInfo = highlightsInfo;
    }

    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public List<String> getAdultTicketInfo() {
        return adultTicketInfo;
    }

    public void setAdultTicketInfo(List<String> adultTicketInfo) {
        this.adultTicketInfo = adultTicketInfo;
    }

    public List<String> getChildTicketInfo() {
        return childTicketInfo;
    }

    public void setChildTicketInfo(List<String> childTicketInfo) {
        this.childTicketInfo = childTicketInfo;
    }

    public String getIndexTime() {
        return indexTime;
    }

    public void setIndexTime(String indexTime) {
        this.indexTime = indexTime;
    }

    public List<String> getHighlights() {
        return highlights;
    }

    public void setHighlights(List<String> highlights) {
        this.highlights = highlights;
    }

    public List<String> getInfantTicketInfo() {
        return infantTicketInfo;
    }

    public void setInfantTicketInfo(List<String> infantTicketInfo) {
        this.infantTicketInfo = infantTicketInfo;
    }

    public List<String> getSeniorCitizenTicketInfo() {
        return seniorCitizenTicketInfo;
    }

    public void setSeniorCitizenTicketInfo(List<String> seniorCitizenTicketInfo) {
        this.seniorCitizenTicketInfo = seniorCitizenTicketInfo;
    }

    public String getActivityStartDate() {
        return activityStartDate;
    }

    public void setActivityStartDate(String activityStartDate) {
        this.activityStartDate = activityStartDate;
    }

    public String getActivityEndDate() {
        return activityEndDate;
    }

    public void setActivityEndDate(String activityEndDate) {
        this.activityEndDate = activityEndDate;
    }

    public BookingDetail getBookingDetail() {
        return bookingDetail;
    }

    public void setBookingDetail(BookingDetail bookingDetail) {
        this.bookingDetail = bookingDetail;
    }

    public List<String> getBookingInfo() {
        return bookingInfo;
    }

    public void setBookingInfo(List<String> bookingInfo) {
        this.bookingInfo = bookingInfo;
    }

    public AccessoryDetail getAccessoryDetail() {
        return accessoryDetail;
    }

    public void setAccessoryDetail(AccessoryDetail accessoryDetail) {
        this.accessoryDetail = accessoryDetail;
    }

    public List<String> getAccessoryDetailInfo() {
        return accessoryDetailInfo;
    }

    public void setAccessoryDetailInfo(List<String> accessoryDetailInfo) {
        this.accessoryDetailInfo = accessoryDetailInfo;
    }
}