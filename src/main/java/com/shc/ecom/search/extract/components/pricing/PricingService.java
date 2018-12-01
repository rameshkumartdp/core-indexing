package com.shc.ecom.search.extract.components.pricing;

import com.shc.ecom.search.common.exception.SearchCommonException;
import com.shc.ecom.wxs.common.exception.SHCWxsException;

import java.io.Serializable;

/**
 * @author rgopala
 *         Jul 23, 2015 search-doc-builder
 */
public interface PricingService extends Serializable {

    long serialVersionUID = 2967388146795226747L;

    Price getOfferPrice(int storeID, String offerId) throws SHCWxsException;

    Price getProductPrice(int storeID, String ssin) throws SHCWxsException;

    Price getBundlePrice(int storeID, String bundleId) throws SearchCommonException;

    /**
     * Get's Store Price for the offer.  Big difference here is the store-id is a String rather than integer in other methods.
     * This is due to trailing zeros in the store-ids cannot be handled/stored as integer/long etc.
     *
     * @param storeID
     * @param offerId
     * @return
     * @throws SHCWxsException
     */
    Price getStorePrice(String storeID, String offerId) throws SHCWxsException;
}
