package com.shc.ecom.search.docbuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.solr.client.solrj.beans.Field;

import java.util.ArrayList;
import java.util.List;

public class OfferDoc {

    @Field("id")
    private String offerDocId;

    @Field("product_type_s")
    private String productType;

    @Field("searchableAttributesOffer_ss")
    private List<String> searchableAttributes;

    @Field("searchableAttributesSearchableOffer_ss")
    private List<String> searchableAttributesSearchable;

    @Field("freeShippingOffer_ss")
    private List<String> freeShipping;

    @Field
    private String opsUuid_s;

    @Field("opsCurrentServer_i")
    private String opsCurrentServer;

    @Field
    private String partnumber;

    @Field
    private String sellerId;

    @Field("deliveryArea_ss")
    private List<String> deliveryAreaList;

    @Field("reservableStores_ss")
    private List<String> reservableStores;

    @Field("reservableAllStores_b")
    private boolean reservableStoresAll;

    @Field("storeUnit_ss")
    private List<String> storeUnits;

    @Field("zones_ss")
    private List<String> zones;
    
    @Field("shippingAvailable_b")
    private boolean shippingAvailable;

    public OfferDoc(OfferDocBuilder offerDocBuilder) {
        this.offerDocId = offerDocBuilder.getOfferDocId();
        this.opsUuid_s = offerDocBuilder.getOpsUuid_s();
        this.productType = offerDocBuilder.getProductType();
        this.searchableAttributes = offerDocBuilder.getSearchableAttributes();
        this.searchableAttributesSearchable = offerDocBuilder.getSearchableAttributesSearchable();
        this.freeShipping = offerDocBuilder.getFreeShipping();
        this.opsCurrentServer = offerDocBuilder.getOpsCurrentServer();
        this.sellerId = offerDocBuilder.getSellerId();
        this.storeUnits = offerDocBuilder.getStoreUnits();
        this.partnumber = offerDocBuilder.getPartnumber();
        this.deliveryAreaList = offerDocBuilder.getDeliveryAreaList();
        this.reservableStores = offerDocBuilder.getReservableStores();
        this.reservableStoresAll = offerDocBuilder.isReservableAllStores();
        this.zones = offerDocBuilder.getZoneList();
        this.shippingAvailable = offerDocBuilder.isShipAvaialable();
    }

    @JsonProperty("zones_ss")
    public List<String> getZones() {
        return zones;
    }

    public void setZones(List<String> zones) {
        this.zones = zones;
    }

    @JsonProperty("storeUnit_ss")
    public List<String> getStoreUnits() {
        return storeUnits;
    }

    public void setStoreUnits(List<String> storeUnits) {
        this.storeUnits = storeUnits;
    }

    @JsonProperty("id")
    public String getOfferDocId() {
        return offerDocId;
    }

    public void setOfferDocId(String offerDocId) {
        this.offerDocId = offerDocId;
    }

    @JsonProperty("product_type_s")
    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @JsonProperty("searchableAttributesOffer_ss")
    public List<String> getSearchableAttributes() {
        return searchableAttributes;
    }

    public void setSearchableAttributes(List<String> searchableAttributes) {
        this.searchableAttributes = searchableAttributes;
    }

    @JsonProperty("searchableAttributesSearchableOffer_ss")
    public List<String> getSearchableAttributesSearchable() {
        return searchableAttributesSearchable;
    }

    @JsonProperty("freeShippingOffer_ss")
    public List<String> getFreeShipping() {
        if (freeShipping == null) {
            return new ArrayList<>();
        }
        return freeShipping;
    }

    public void setFreeShipping(List<String> freeShipping) {
        this.freeShipping = freeShipping;
    }

    @JsonProperty("opsUuid_s")
    public String getOpsUuid_s() {
        return opsUuid_s;
    }

    public void setOpsUuid_s(String opsUuid_s) {
        this.opsUuid_s = opsUuid_s;
    }

    @JsonProperty("opsCurrentServer_i")
    public String getOpsCurrentServer() {
        return opsCurrentServer;
    }

    public void setOpsCurrentServer(String opsCurrentServer) {
        this.opsCurrentServer = opsCurrentServer;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public void setPartnumber(String partnumber) {
        this.partnumber = partnumber;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    @JsonProperty("deliveryArea_ss")
    public List<String> getDeliveryAreaList() {
        return deliveryAreaList;
    }

    public void setDeliveryAreaList(List<String> deliveryAreaList) {
        this.deliveryAreaList = deliveryAreaList;
    }

    @JsonProperty("reservableStores_ss")
    public List<String> getReservableStores() {
        return reservableStores;
    }

    @JsonProperty("reservableAllStores_b")
    public boolean isReservableStoresAll() {
        return reservableStoresAll;
    }
    
    @JsonProperty("shippingAvailable_b")
    public boolean isShippingAvailable() {
        return shippingAvailable;
    }
}
