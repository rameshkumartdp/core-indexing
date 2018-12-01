package com.shc.ecom.search.extract.extracts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author rgopala
 */
public class IASExtract implements Serializable {

    private static final long serialVersionUID = -3191350122765697082L;
    private Map<String, Boolean> offerIdShipAvailMap = new HashMap<>();
    private Map<String, List<String>> offerIdZoneMap = new HashMap<>();
    private Map<String, List<String>> offerIdFacilityIdsMap = new HashMap<>();
    private Map<String, Set<String>> offerIdRevervationMap = new HashMap<>();
    private Map<String, Set<String>> offerIdDDCMap = new HashMap<>();
    private Map<String, Set<String>> offerIdReservableStoreMap = new HashMap<>();
    private Map<String, Boolean> offerIdReservableAllStoresMap = new HashMap<>();
    private Map<String, Set<String>> offerIdStoreUnits = new HashMap<>();

    public Map<String, Set<String>> getOfferIdStoreUnits() {
        return offerIdStoreUnits;
    }

    public void setOfferIdStoreUnits(Map<String, Set<String>> offerIdStoreUnits) {
        this.offerIdStoreUnits = offerIdStoreUnits;
    }

    public Map<String, Boolean> getOfferIdReservableAllStoresMap() {
        return offerIdReservableAllStoresMap;
    }

    public void setOfferIdReservableAllStoresMap(Map<String, Boolean> offerIdReservableAllStoresMap) {
        this.offerIdReservableAllStoresMap = offerIdReservableAllStoresMap;
    }

    public Map<String, Set<String>> getOfferIdRevervationMap() {
        return offerIdRevervationMap;
    }

    public void setOfferIdRevervationMap(Map<String, Set<String>> offerIdRevervationMap) {
        this.offerIdRevervationMap = offerIdRevervationMap;
    }

    public Map<String, Boolean> getOfferIdShipAvailMap() {
        return offerIdShipAvailMap;
    }

    public void setOfferIdShipAvailMap(Map<String, Boolean> offerIdShipAvailMap) {
        this.offerIdShipAvailMap = offerIdShipAvailMap;
    }

    public Map<String, List<String>> getOfferIdZoneMap() {
        return offerIdZoneMap;
    }

    public void setOfferIdZoneMap(Map<String, List<String>> offerIdZoneMap) {
        this.offerIdZoneMap = offerIdZoneMap;
    }

    public Map<String, List<String>> getOfferIdFacilityIdsMap() {
        return offerIdFacilityIdsMap;
    }

    public void setOfferIdFacilityIdsMap(Map<String, List<String>> offerIdFacilityIdsMap) {
        this.offerIdFacilityIdsMap = offerIdFacilityIdsMap;
    }

    public Map<String, Set<String>> getOfferIdDDCMap() {
        return offerIdDDCMap;
    }

    public void setOfferIdDDCMap(Map<String, Set<String>> offerIdDDCMap) {
        this.offerIdDDCMap = offerIdDDCMap;
    }

    public Map<String, Set<String>> getOfferIdReservableStoreMap() {
        return offerIdReservableStoreMap;
    }

    public void setOfferIdReservableStoreMap(Map<String, Set<String>> offerIdReservableStoreMap) {
        this.offerIdReservableStoreMap = offerIdReservableStoreMap;
    }
}
