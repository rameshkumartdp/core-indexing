/**
 *
 */
package com.shc.ecom.search.extract.extracts;

import com.shc.ecom.search.extract.components.pricing.Price;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rgopala
 */
public class PriceExtract implements Serializable {

    private static final long serialVersionUID = -3275486970706971494L;

    private Map<Integer, Price> onlinePrice;
    private Map<String, Price> storePrice;

    /**
     * @return the onlinePrice
     */
    public Map<Integer, Price> getOnlinePrice() {
        if (onlinePrice == null) {
            onlinePrice = new HashMap<>();
        }
        return onlinePrice;
    }

    /**
     * @param onlinePrice the onlinePrice to set
     */
    public void setOnlinePrice(Map<Integer, Price> onlinePrice) {
        this.onlinePrice = onlinePrice;
    }

    /**
     * @return the storePrice
     */
    public Map<String, Price> getStorePrice() {
        if (storePrice == null) {
            storePrice = new HashMap<>();
        }
        return storePrice;
    }

    /**
     * @param storePrice the storePrice to set
     */
    public void setStorePrice(Map<String, Price> storePrice) {
        this.storePrice = storePrice;
    }
}
