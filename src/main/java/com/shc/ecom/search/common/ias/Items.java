package com.shc.ecom.search.common.ias;

import java.util.List;

/**
 * @author rgopala
 */
public class Items {
    private String id;

    private String facility_count;

    private String giftWrapEnabled;

    private String giftReceiptEnabled;

    private List<String> zoneList;

    private List<Facilities> facilities;

    private boolean resAvailable;

    private String giftMessageEnabled;

    private boolean isAvailableforShip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFacility_count() {
        return facility_count;
    }

    public void setFacility_count(String facility_count) {
        this.facility_count = facility_count;
    }

    public String getGiftWrapEnabled() {
        return giftWrapEnabled;
    }

    public void setGiftWrapEnabled(String giftWrapEnabled) {
        this.giftWrapEnabled = giftWrapEnabled;
    }

    public String getGiftReceiptEnabled() {
        return giftReceiptEnabled;
    }

    public void setGiftReceiptEnabled(String giftReceiptEnabled) {
        this.giftReceiptEnabled = giftReceiptEnabled;
    }

    public List<String> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<String> zoneList) {
        this.zoneList = zoneList;
    }

    public List<Facilities> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facilities> facilities) {
        this.facilities = facilities;
    }

    public String getGiftMessageEnabled() {
        return giftMessageEnabled;
    }

    public void setGiftMessageEnabled(String giftMessageEnabled) {
        this.giftMessageEnabled = giftMessageEnabled;
    }

    public boolean isAvailableforShip() {
        return isAvailableforShip;
    }

    public void setAvailableforShip(boolean isAvailableforShip) {
        this.isAvailableforShip = isAvailableforShip;
    }

    public boolean isResAvailable() {
        return resAvailable;
    }

    public void setResAvailable(boolean resAvailable) {
        this.resAvailable = resAvailable;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id +
                ", facility_count = " + facility_count +
                ", giftWrapEnabled = " + giftWrapEnabled +
                ", giftReceiptEnabled = " + giftReceiptEnabled +
                ", zoneList = " + zoneList +
                ", facilities = " + facilities +
                ", giftMessageEnabled = " + giftMessageEnabled +
                ", reservableAllStores = " + resAvailable +
                "]";
    }
}
