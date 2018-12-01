package com.shc.ecom.search.extract.components.ias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shc.ecom.search.common.ias.Facilities;
import com.shc.ecom.search.common.ias.IAS;
import com.shc.ecom.search.common.ias.Items;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.persistence.ZipDDCData;

/**
 * @author rgopala
 */
@Component
public class IASExtractComponent extends ExtractComponent<Map<String, IAS>> {

    private static final long serialVersionUID = 3249186673574798347L;

    @Autowired
    private IasService iasService;

    @Autowired
    private ZipDDCData zipDDCData;

    @Override
    protected Map<String, IAS> get(WorkingDocument wd, ContextMessage context) {
        return iasService.process(wd, context);
    }

    @Override
    protected Extracts extract(Map<String, IAS> offerIdIasMap, WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();
        Map<String, Boolean> offerIdShipAvailMap = new HashMap<>();
        Map<String, List<String>> offerIdZoneMap = new HashMap<>();
        Map<String, List<String>> offerIdFacilityIdsMap = new HashMap<>();
        Map<String, Set<String>> offerIdResvMap = new HashMap<>();
        Map<String, Set<String>> offerIdDDCMap = new HashMap<>();
        Map<String, Set<String>> offerIdReservableStoresMap = new HashMap<>();
        Map<String, Boolean> offerIdReservableAllStoresMap = new HashMap<>();
        Map<String, Set<String>> offerIdStoreUnitsMap = new HashMap<>();

        for (Map.Entry<String, IAS> entry : offerIdIasMap.entrySet()) {
            String offerId = entry.getKey();
            IAS iasDto = entry.getValue();

            List<Items> items = iasDto.getItems();
            List<String> facilities = new ArrayList<>();
            Set<String> reservations = new HashSet<>();
            Set<String> reservableStores = new HashSet<>();
            Set<String> ddcList = new HashSet<>();
            Set<String> storeUnits = new HashSet<>();

            if (CollectionUtils.isEmpty(items)) {
                continue;
            }
            for(Items item : items) {
	            for (Facilities facility : item.getFacilities()) {
	                /**Business logic for each facility*/
	                facilities.addAll(getFacilities(facility));
	                reservations.addAll(getReservations(facility));
	                reservableStores.addAll(getReservableStores(facility));
	                ddcList.addAll(getDDCList(facility));
	                storeUnits.addAll(getStoreUnits(facility));
	            }
            }

            offerIdFacilityIdsMap.put(offerId, facilities);
            offerIdShipAvailMap.put(offerId, items.get(0).isAvailableforShip());
            offerIdZoneMap.put(offerId, items.get(0).getZoneList());
            offerIdResvMap.put(offerId, reservations);
            offerIdDDCMap.put(offerId, ddcList);
            offerIdReservableStoresMap.put(offerId, reservableStores);
            offerIdReservableAllStoresMap.put(offerId, isGloballyResAvailable(items, offerId, wd, context));
            offerIdStoreUnitsMap.put(offerId, storeUnits);
        }
        extracts.getIasExtract().setOfferIdShipAvailMap(offerIdShipAvailMap);
        extracts.getIasExtract().setOfferIdZoneMap(offerIdZoneMap);
        extracts.getIasExtract().setOfferIdFacilityIdsMap(offerIdFacilityIdsMap);
        extracts.getIasExtract().setOfferIdRevervationMap(offerIdResvMap);
        extracts.getIasExtract().setOfferIdDDCMap(offerIdDDCMap);
        extracts.getIasExtract().setOfferIdReservableStoreMap(offerIdReservableStoresMap);
        extracts.getIasExtract().setOfferIdStoreUnits(offerIdStoreUnitsMap);
        extracts.getIasExtract().setOfferIdReservableAllStoresMap(offerIdReservableAllStoresMap);

        return extracts;
    }
    
    
    /**
     * For KMART items, the call includes PID and KSN, hence two items nodes in response.
     * For KMART items that is cross-formatted to SEARS, only take global status from KSN items node.  PID will provide availability for online warehouse and we make facility level check.
     * @param items
     * @return
     */
    private boolean isGloballyResAvailable(List<Items> items, String offerId, WorkingDocument wd, ContextMessage context) {
    	if(CollectionUtils.isEmpty(items)) {
    		return false;
    	}
    	boolean resAvailableAcrossNodes = true;
    	for(Items item : items) {
    		resAvailableAcrossNodes &= item.isResAvailable();
    	}
    	return resAvailableAcrossNodes;
    }

    private List<String> getFacilities(Facilities facility) {
        List<String> result = new ArrayList<>();

        if (StringUtils.contains(facility.getId(), "urn:facility:sears:")) {
            int onHand = facility.getDispositions().getUrnDispositionOnhand();
            int threshold = facility.getDispositions().getUrnDispositionThreshold();

            if (onHand > threshold) {
                result.add(facility.getId().split("urn:facility:sears:")[1]);
            }
        }

        return result;
    }

    private Set<String> getReservations(Facilities facility) {
        Set<String> result = new HashSet<>();

        String resvDate = facility.getResv().getDate();
        if (StringUtils.equalsIgnoreCase(resvDate, "3-5 Days")) {
            result.add(facility.getId() + "_1");
        } else if (StringUtils.equalsIgnoreCase(resvDate, "7-9 Days")) {
            result.add(facility.getId() + "_2");
        }

        return result;
    }

    private Set<String> getStoreUnits(Facilities facility) {
        Set<String> result = new HashSet<>();

        /**
         * This was the logic approved by Dasari in the Availability team
         */
        if(facility.getDispositions().getUrnDispositionOnhand() > facility.getDispositions().getUrnDispositionThreshold()) {
            result.add(facility.getId().replaceAll("\\D+", ""));
        }

        return result;
    }

    private Set<String> getReservableStores(Facilities facility) {
        Set<String> result = new HashSet<>();

        if (facility.isResAvailable()) {
            /*
            Getting the digits only from the facility id
            Letting the trailing 0's be there because storeUnit field also has it like that
             */
            result.add(facility.getId().replaceAll("\\D+", ""));
        }

        return result;
    }

    private Set<String> getDDCList(Facilities facility) {
        Set<String> result = new HashSet<>();

        //DDC delivery logic #EnsureAvailability #PutInSeperateMethod #SEARCH-1765
        // This will work for sears DC and kmart facilities
        if (StringUtils.contains(facility.getId(), "urn:facility:searsdc:")
                || StringUtils.contains(facility.getId(), "urn:facility:kmart:")
                || StringUtils.contains(facility.getId(), "urn:facility:kmartdc:")) {
            result.addAll(zipDDCData.getDdcSet()
                    .stream()
                    .filter(ddc -> StringUtils.contains(facility.getId(), ddc))
                    .filter(ddc -> facility.getDispositions().getUrnDispositionOnhandReserved() > 0)
                    .collect(Collectors.toSet()));
        }

        return result;
    }

}
