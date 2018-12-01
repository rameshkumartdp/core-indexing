package com.shc.ecom.search.common.ias;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rgopala
 */
public class IAS {
    private List<Items> items = new ArrayList<>();

    private Metadata metadata;

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public Metadata getMetadata() {
        if (metadata == null) {
            metadata = new Metadata();
        }
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "ClassPojo [items = " + items + ", metadata = " + metadata + "]";
    }

    /**
     * Checks if the object is empty. This helps eliminate junk objects and not storing them
     *
     * @return
     */
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(this.items);
    }
}
