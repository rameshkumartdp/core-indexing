package com.shc.ecom.search.extract.extracts;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PASExtract implements Serializable {

    private static final long serialVersionUID = 7534843530695151106L;

    private List<String> availableStores;
    private List<String> availableShowRoomUnits;
    private List<String> availableZones;

    /**
     * @return the availableZones
     */
    public List<String> getAvailableZones() {
        if (CollectionUtils.isEmpty(availableZones)) {
            availableZones = new ArrayList<>();
        }
        return availableZones;
    }

    /**
     * @param availableZones the availableZones to set
     */
    public void setAvailableZones(List<String> availableZones) {
        this.availableZones = availableZones;
    }

    /**
     * @return the availableStores
     */
    public List<String> getAvailableStores() {
        if (CollectionUtils.isEmpty(availableStores)) {
            availableStores = new ArrayList<>();
        }
        return availableStores;
    }

    /**
     * @param availableStores the availableStores to set
     */
    public void setAvailableStores(List<String> availableStores) {
        this.availableStores = availableStores;
    }

    public List<String> getAvailableShowRoomUnits() {
        if (CollectionUtils.isEmpty(availableShowRoomUnits)) {
            availableShowRoomUnits = new ArrayList<>();
        }
        return availableShowRoomUnits;
    }

    public void setAvailableShowRoomUnits(List<String> availableShowRoomUnits) {
        this.availableShowRoomUnits = availableShowRoomUnits;
    }


}
