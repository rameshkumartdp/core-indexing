package com.shc.ecom.search.extract.components.pas;

import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.pas.PasDto;
import com.shc.ecom.search.common.pas.Products;
import com.shc.ecom.search.common.pas.StoreList;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Product availability of product in each physical store is extracted here.
 *
 * @author vsingh8
 */
@Component
public class PasExtractComponent extends ExtractComponent<PasDto> implements Serializable {

    private static final long serialVersionUID = 4134892457350999627L;

    @Autowired
    private PasService pasService;

    @Override
    protected PasDto get(WorkingDocument wd, ContextMessage context) {

        String storeName = context.getStoreName();
        Sites site = Stores.getSite(storeName);
        String offerParentID = wd.getExtracts().getSsin();
        String offerId = context.getOfferId();
        boolean isCrossFormat = site.isCrossFormat(wd, context);

        // SEARCH:1185 If ssin is cross format item then PAS call should be made using parentID of offer in context.
        if (!StringUtils.isEmpty(offerId) && (Stores.KMART.matches(storeName) || Stores.SEARS.matches(storeName))) {
            offerParentID = wd.getExtracts().getOfferExtract().getOfferIdParentId().get(offerId);
        }

        if (isCrossFormat) {
            site = site.getCrossFormatSite();
        }
        //Returning Empty PAS DTO to avoid making a PAS call and decommissioning the same. SEARCH-3773
        return new PasDto();
        //return pasService.process(offerParentID, site);

    }

    @Override
    protected Extracts extract(PasDto pasDomainObject, WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();

        if (CollectionUtils.isEmpty(pasDomainObject.getProducts())) {
            return extracts;
        }

        // SEARCH:1185 If ssin is cross format item then PAS call should be made using parentID of offer in context.
        Products product = pasDomainObject.getProducts().get(0);
        List<StoreList> storeAvailabilityList = product.getStoreList();
        List<String> showRoomList = new ArrayList<>();
        List<String> storeList = new ArrayList<>();
        for (StoreList storeAvailability : storeAvailabilityList) {
            if (StringUtils.equalsIgnoreCase(storeAvailability.getAvailable(), "Y") && StringUtils.equalsIgnoreCase(storeAvailability.getType(), "STORE")) {
                storeList.add(storeAvailability.getStoreId());
            }
            if (StringUtils.equalsIgnoreCase(storeAvailability.getIsDisplayAvailable(), "Y") && StringUtils.equalsIgnoreCase(storeAvailability.getType(), "STORE")) {
                showRoomList.add(storeAvailability.getStoreId());
            }
        }
        extracts.getPasExtract().setAvailableStores(storeList);
        extracts.getPasExtract().setAvailableShowRoomUnits(showRoomList);
        extracts.getPasExtract().setAvailableZones(product.getZoneList());
        return extracts;
    }

}
