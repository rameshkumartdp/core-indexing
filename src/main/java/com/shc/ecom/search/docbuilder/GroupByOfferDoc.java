package com.shc.ecom.search.docbuilder;

/**
 * Created by jsingar on 1/24/18.
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public class GroupByOfferDoc {


    private String id;


    private String product_type_s;


    private List<String> searchableAttributesOffer_ss;


    private List<String> searchableAttributesSearchableOffer_ss;


    private List<String> freeShippingOffer_ss;

    private List<Map<String,String>> gbiFreeShippingOffer_ss;


    private String opsUuid_s;


    private String opsCurrentServer_i;


    private String partnumber;


    private String sellerId;


    private List<String> deliveryArea_ss;


    private List<String> reservableStores_ss;


    private boolean reservableAllStores_b;


    private List<String> storeUnit_ss;


    private List<String> zones_ss;


    private boolean shippingAvailable_b;

    private List<Map<String,String>> offerFilters;
    private List<String> storeFilters;

    public String getId() {
        return id;
    }

    public String getProduct_type_s() {
        return product_type_s;
    }

    public List<String> getSearchableAttributesOffer_ss() {
        return searchableAttributesOffer_ss;
    }

    public List<String> getSearchableAttributesSearchableOffer_ss() {
        return searchableAttributesSearchableOffer_ss;
    }

    public List<String> getFreeShippingOffer_ss() {
        if (freeShippingOffer_ss == null) {
            return new ArrayList<>();
        }
        return freeShippingOffer_ss;
    }

    public List<String> getStoreFilters() {
        return storeFilters;
    }

    public void setStoreFilters(List<String> storeFilters) {
        this.storeFilters = storeFilters;
    }

    public String getOpsUuid_s() {
        return opsUuid_s;
    }

    public String getOpsCurrentServer_i() {
        return opsCurrentServer_i;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public String getSellerId() {
        return sellerId;
    }

    public List<String> getDeliveryArea_ss() {
        return deliveryArea_ss;
    }

    public List<String> getReservableStores_ss() {
        return reservableStores_ss;
    }

    public boolean isReservableAllStores_b() {
        return reservableAllStores_b;
    }

    public List<String> getStoreUnit_ss() {
        return storeUnit_ss;
    }

    public List<String> getZones_ss() {
        return zones_ss;
    }

    public boolean isShippingAvailable_b() {
        return shippingAvailable_b;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProduct_type_s(String product_type_s) {
        this.product_type_s = product_type_s;
    }

    public void setSearchableAttributesOffer_ss(List<String> searchableAttributesOffer_ss) {
        this.searchableAttributesOffer_ss = searchableAttributesOffer_ss;
    }

    public void setSearchableAttributesSearchableOffer_ss(List<String> searchableAttributesSearchableOffer_ss) {
        this.searchableAttributesSearchableOffer_ss = searchableAttributesSearchableOffer_ss;
    }

    public void setFreeShippingOffer_ss(List<String> freeShippingOffer_ss) {
        this.freeShippingOffer_ss = freeShippingOffer_ss;
    }

    public void setOpsUuid_s(String opsUuid_s) {
        this.opsUuid_s = opsUuid_s;
    }

    public void setOpsCurrentServer_i(String opsCurrentServer_i) {
        this.opsCurrentServer_i = opsCurrentServer_i;
    }

    public void setPartnumber(String partnumber) {
        this.partnumber = partnumber;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setDeliveryArea_ss(List<String> deliveryArea_ss) {
        this.deliveryArea_ss = deliveryArea_ss;
    }

    public void setReservableStores_ss(List<String> reservableStores_ss) {
        this.reservableStores_ss = reservableStores_ss;
    }

    public void setReservableAllStores_b(boolean reservableAllStores_b) {
        this.reservableAllStores_b = reservableAllStores_b;
    }

    public List<Map<String, String>> getOfferFilters() {
        return offerFilters;
    }

    public void setOfferFilters(List<Map<String, String>> offerFilters) {
        this.offerFilters = offerFilters;
    }

    public void setStoreUnit_ss(List<String> storeUnit_ss) {
        this.storeUnit_ss = storeUnit_ss;
    }

    public void setZones_ss(List<String> zones_ss) {
        this.zones_ss = zones_ss;
    }

    public void setShippingAvailable_b(boolean shippingAvailable_b) {
        this.shippingAvailable_b = shippingAvailable_b;
    }

    public List<Map<String, String>> getGbiFreeShippingOffer_ss() {
        return gbiFreeShippingOffer_ss;
    }

    public void setGbiFreeShippingOffer_ss(List<Map<String, String>> gbiFreeShippingOffer_ss) {
        this.gbiFreeShippingOffer_ss = gbiFreeShippingOffer_ss;
    }

    @Override
    public String toString() {
        return "OfferDoc{" +
                "id='" + id + '\'' +
                ", product_type_s='" + product_type_s + '\'' +
                ", searchableAttributesOffer_ss=" + searchableAttributesOffer_ss +
                ", searchableAttributesSearchableOffer_ss=" + searchableAttributesSearchableOffer_ss +
                ", freeShippingOffer_ss=" + freeShippingOffer_ss +
                ", opsUuid_s='" + opsUuid_s + '\'' +
                ", opsCurrentServer_i='" + opsCurrentServer_i + '\'' +
                ", partnumber='" + partnumber + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", deliveryArea_ss=" + deliveryArea_ss +
                ", reservableStores_ss=" + reservableStores_ss +
                ", reservableAllStores_b=" + reservableAllStores_b +
                ", storeUnit_ss=" + storeUnit_ss +
                ", zones_ss=" + zones_ss +
                ", shippingAvailable_b=" + shippingAvailable_b +
                '}';
    }
}
