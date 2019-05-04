package docbuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.solr.client.solrj.beans.Field;

import java.util.ArrayList;
import java.util.List;

public class SearchDoc {

    @Field
    private String id;

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
    private String duration;

    @Field
    private int totalNoOfTickets;

    @Field
    private String itemnumber = "";

    @Field
    private String contact;

    @Field
    private String activityStartDate;

    @Field
    private String activityEndDate;

    @Field
    @JsonIgnore
    private String displayImages = "";

    @Field
    @JsonIgnore
    private  String aboutImages = "";

    private  List<TicketDetails> ticketInfo = new ArrayList<>();

    @Field
    private  List<String> adultTicketInfo = new ArrayList<>();

    @Field
    @JsonIgnore
    private  List<String> childTicketInfo ;

    private  List<Accessory> accessoryInfo  = new ArrayList<>();

    @Field
    private List<String> accessoryInfos;

    private  AdditionalInfo additionalInfo;

    @Field
    private  String accessoryType = "";

    @Field
    private  String accessoryDescription = "";

    @Field
    private  String accessoryMeasurements = "";

    @Field
    private  String aboutInfo = "";

    @Field
    private  List<String> highlightsInfo = new ArrayList<>();

    @Field
    private  String _class;

    @Field
    private  String miscellaneous = "";

    @Field
    private  String indexTime = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getTotalNoOfTickets() {
        return totalNoOfTickets;
    }

    public void setTotalNoOfTickets(int totalNoOfTickets) {
        this.totalNoOfTickets = totalNoOfTickets;
    }

    public String getItemnumber() {
        return itemnumber;
    }

    public void setItemnumber(String itemnumber) {
        this.itemnumber = itemnumber;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getDisplayImages() {
        return displayImages;
    }

    public void setDisplayImages(String displayImages) {
        this.displayImages = displayImages;
    }

    public String getAboutImages() {
        return aboutImages;
    }

    public void setAboutImages(String aboutImages) {
        this.aboutImages = aboutImages;
    }

    public List<TicketDetails> getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(List<TicketDetails> ticketInfo) {
        this.ticketInfo = ticketInfo;
    }


    public String getAccessoryType() {
        return accessoryType;
    }

    public void setAccessoryType(String accessoryType) {
        this.accessoryType = accessoryType;
    }

    public String getAccessoryDescription() {
        return accessoryDescription;
    }

    public void setAccessoryDescription(String accessoryDescription) {
        this.accessoryDescription = accessoryDescription;
    }

    public String getAccessoryMeasurements() {
        return accessoryMeasurements;
    }

    public void setAccessoryMeasurements(String accessoryMeasurements) {
        this.accessoryMeasurements = accessoryMeasurements;
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

    public String getMiscellaneous() {
        return miscellaneous;
    }

    public void setMiscellaneous(String miscellaneous) {
        this.miscellaneous = miscellaneous;
    }

    public List<Accessory> getAccessoryInfo() {
        return accessoryInfo;
    }

    public void setAccessoryInfo(List<Accessory> accessoryInfo) {
        this.accessoryInfo = accessoryInfo;
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

    public List<String> getAccessoryInfos() {
        return accessoryInfos;
    }

    public void setAccessoryInfos(List<String> accessoryInfos) {
        this.accessoryInfos = accessoryInfos;
    }
}