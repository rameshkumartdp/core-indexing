package com.shc.ecom.search.docbuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregates all the information required to build the offerDoc
 */
public class OfferDocBuilder {

    private String offerDocId; //Unique identifier
    private String productType; //Always "child" for offer docs
    private List<String> searchableAttributes;
    private List<String> searchableAttributesSearchable;
    private List<String> freeShipping; //
    private String opsUuid_s; //Timestamp
    private String partnumber;
    private String sellerId;
    private String opsCurrentServer;


    //Ensure Availability
    private List<String> deliveryAreaList; //DDC regions where the item can be delivered
    private List<String> reservableStores; //Stores where the item is reservable
    private boolean reservableAllStores; //The product is reservable in all stores (VRES, CRES, DRES)
    private List<String> storeUnits; //Added to decommission PAS. All info will come from IAS
    private List<String> zoneList;
    private boolean shippingAvailable;

    public OfferDocBuilder zoneList(List<String> zoneList) {
        this.zoneList = zoneList;
        return this;
    }

    public boolean isReservableAllStores() {
        return reservableAllStores;
    }

    public OfferDocBuilder storeUnits(List<String> storeUnits) {
        this.storeUnits = storeUnits;
        return this;
    }

    public OfferDocBuilder reservableAllStores(boolean reservableAllStores) {
        this.reservableAllStores = reservableAllStores;
        return this;
    }

    public OfferDocBuilder sellerId(String sellerId) {
        this.sellerId = sellerId;
        return this;
    }

    public OfferDocBuilder partnumber(String partnumber) {
        this.partnumber = partnumber;
        return this;
    }

    public OfferDocBuilder opsCurrentServer(String opsCurrentServer) {
        this.opsCurrentServer = opsCurrentServer;
        return this;
    }

    public OfferDocBuilder opsUuid_s(String opsUuid_s) {
        this.opsUuid_s = opsUuid_s;
        return this;
    }

    public OfferDocBuilder offerDocId(String offerDocId) {
        this.offerDocId = offerDocId;
        return this;
    }

    public OfferDocBuilder productType(String productType) {
        this.productType = productType;
        return this;
    }

    public OfferDocBuilder searchableAttributes(List<String> searchableAttributes) {
        this.searchableAttributes = searchableAttributes;
        return this;
    }

    public OfferDocBuilder searchableAttributesSearchable(List<String> searchableAttributesSearchable) {
        this.searchableAttributesSearchable = searchableAttributesSearchable;
        return this;
    }

    public OfferDocBuilder freeShipping(List<String> freeShipping) {
        this.freeShipping = freeShipping;
        return this;
    }

    public OfferDocBuilder deliveryAreaList(List<String> deliveryAreaList) {
        this.deliveryAreaList = deliveryAreaList;
        return this;
    }

    public OfferDocBuilder reservableStores(List<String> reservableStores) {
        this.reservableStores = reservableStores;
        return this;
    }
    
    public OfferDocBuilder shippingAvailable(boolean shippingAvailable) {
        this.shippingAvailable = shippingAvailable;
        return this;
    }
    public String getOfferDocId() {
        return offerDocId;
    }

    public String getProductType() {
        return productType;
    }

    public List<String> getSearchableAttributes() {
        return searchableAttributes;
    }

    public List<String> getSearchableAttributesSearchable() {
        return searchableAttributesSearchable;
    }

    public String getOpsUuid_s() {
        return opsUuid_s;
    }

    public List<String> getFreeShipping() {
        if (freeShipping == null) {
            return new ArrayList<>();
        }
        return freeShipping;
    }

    public String getOpsCurrentServer() {
        return opsCurrentServer;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public List<String> getDeliveryAreaList() {
        return deliveryAreaList;
    }

    public List<String> getReservableStores() {
        return reservableStores;
    }

    public List<String> getStoreUnits() {
        return storeUnits;
    }
    public List<String> getZoneList() {
        return zoneList;
    }
    public boolean isShipAvaialable() {
        return shippingAvailable;
    }
}
