package com.shc.ecom.search.common.zipDDC;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Value object to store ZipDDC Mapping from zipDDCMapping Service
 * Created by hdargah on 11/2/2016.
 */
public class ZipDDCMapping implements Serializable {

    private static final long serialVersionUID = -8576894624489756466L;

    private List<Warehouse> warehousesList;

    public List<Warehouse> getWarehousesList() {
        if (CollectionUtils.isEmpty(warehousesList)) {
            return new ArrayList<>();
        }
        return warehousesList;
    }

    public void setWarehousesList(List<Warehouse> warehousesList) {
        this.warehousesList = warehousesList;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(warehousesList);
    }

}
