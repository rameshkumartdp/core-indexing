package com.shc.ecom.search.extract.components.promo;

import com.shc.ecom.search.common.constants.PromoType;
import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.expirablefields.ExpirableSolrField;
import com.shc.ecom.search.common.expirablefields.GenerateExpFieldExtractWise;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.promo.*;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.StorePromoExtract;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.util.OfferUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * Promotion extraction is done here. The different types of promotions are fetched and only valid promoIDs are stored.
 *
 * @author vsingh8
 */
@Component
public class PromoExtractComponent extends ExtractComponent<Map<Stores, List<PromoDto>>> implements Serializable {

    private static final long serialVersionUID = 5243891287782524873L;
    private static final int FIRSTELEMENT = 0;
    private static final String ACTIVE_STATUS = "a";
    @Autowired
    private PromoService promoService;
    @Autowired
    private GenerateExpFieldExtractWise generateExpirableField;
    /**
     * Get promotion extract for given product. First offer is used for extracting promotions since promo collection aggregates
     * all the promos
     */
    @Override
    protected Map<Stores, List<PromoDto>> get(WorkingDocument wd, ContextMessage context) {
        Map<Stores, List<PromoDto>> promoDtoMap = new HashMap<Stores, List<PromoDto>>();
        List<PromoDto> promoDtoList;

        String storeName = context.getStoreName().toLowerCase();
        List<Integer> storeIds = Stores.getStoreId(wd, context);

        for (Integer storeId : storeIds) {
            promoDtoList = new ArrayList<>();
            Sites site = Stores.getSite(Stores.getStoreName(storeId));
            if (site.isCrossFormat(wd, context)) {
                Sites crossFormatsite = site.getCrossFormatSite();
                storeName = StringUtils.lowerCase(crossFormatsite.getSiteName());
            }

            Map<String, List<String>> storeOffersMap = wd.getExtracts().getOfferExtract().getStoreOffersMap();
            Map<String, List<String>> availableStoreOffersMap = filterAvailableOffers(storeOffersMap, wd);
            if (availableStoreOffersMap.containsKey(storeName)) {
                List<String> offerIds = availableStoreOffersMap.get(storeName);
                if (CollectionUtils.isNotEmpty(offerIds)) {
                    String offerId = offerIds.get(FIRSTELEMENT);
                    // Promo collection aggregates all the promos. So need to hit it
                    // with only one ID.
                    if (site.matches(Sites.SEARSPR.getSiteName())) {
                        // Remove temp hack
                        promoDtoList = promoService.process("pr", offerId);
                    } else {
                        promoDtoList = promoService.process(site.getSiteName().toLowerCase(), offerId);
                    }
                }
            }

            promoDtoMap.put(Stores.getStore(storeId), promoDtoList);
        }
        return promoDtoMap;
    }

    
    private Map<String, List<String>> filterAvailableOffers(Map<String, List<String>> storesOfferMap, WorkingDocument wd) {
    	Map<String, List<String>> result = new HashMap<>();
    	if(storesOfferMap==null || storesOfferMap.isEmpty()) {
    		return result;
    	}
    	List<String> availableOffers = new ArrayList<>(wd.getExtracts().getUasExtract().getAvailableItems());
    	for(Map.Entry<String, List<String>> entry: storesOfferMap.entrySet()) {
    		availableOffers.retainAll(entry.getValue());
    		result.put(entry.getKey(), availableOffers);
    	}
    	return result;
    }
    /**
     * Extract components from promotion extracted for perticular product.
     */
    @Override
    protected Extracts extract(Map<Stores, List<PromoDto>> promoDtoMap, WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();

        for (Map.Entry<Stores, List<PromoDto>> promoDtoMapEntry : promoDtoMap.entrySet()) {

            Stores store = promoDtoMapEntry.getKey();
            List<PromoDto> promoDtoList = promoDtoMapEntry.getValue();
            StorePromoExtract storePromoExtract;
            if (CollectionUtils.isEmpty(promoDtoList)) {
                continue;
            }
            storePromoExtract = new StorePromoExtract();
            PromoDto promoDto = promoDtoList.get(FIRSTELEMENT);
            Set<String> promoIdSet = new HashSet<>();
            PromoList promoList = promoDto.get_blob().getPromorel().getPromoList();
            List<Misc> miscList = promoList.getMisc();
            List<Regular> regularList = promoList.getRegular();
            List<SywMax> sywMaxList = promoList.getSywMax();
            List<SywMbr> sywMbrList = promoList.getSywMbr();
            List<SywRdmpt> sywRdmptList = promoList.getSywRdmpt();
            List<Financing> financingList = promoList.getFinancing();
            List<InstSavings> instSavingsList = promoList.getInstSavings();

            // Adding valid types of promotions into promoId list
            TypePromo<Misc> miscType = new Misc();
            promoIdSet.addAll(miscType.getValidPromoList(miscList));

            TypePromo<Regular> regularType = new Regular();
            promoIdSet.addAll(regularType.getValidPromoList(regularList));

            TypePromo<SywMbr> sywMbrType = new SywMbr();
            promoIdSet.addAll(sywMbrType.getValidPromoList(sywMbrList));

            TypePromo<SywMax> sywMaxType = new SywMax();
            promoIdSet.addAll(sywMaxType.getValidPromoList(sywMaxList));

            TypePromo<SywRdmpt> sywRdmptType = new SywRdmpt();
            promoIdSet.addAll(sywRdmptType.getValidPromoList(sywRdmptList));

            TypePromo<Financing> financingType = new Financing();
            promoIdSet.addAll(financingType.getValidPromoList(financingList));

            TypePromo<InstSavings> instSavingsType = new InstSavings();
            promoIdSet.addAll(instSavingsType.getValidPromoList(instSavingsList));

            // Extracting other attributes of promotion
            List<Regular> validRegularList = regularType.getValidPromos(regularList);
            List<SywMbr> validSywMbrList = sywMbrType.getValidPromos(sywMbrList);

            Regular regularPromoType = new Regular();
            List<PromoRegular> promoRegulars = regularPromoType.getPromoRegularList(validRegularList);
            List<String> regularPromos = regularType.getValidPromoList(validRegularList);

            SywMbr sywMbrPromoType = new SywMbr();
            List<PromoSywMbr> promoSywMbr = sywMbrPromoType.getPromoSywMbrList(validSywMbrList);

            Financing financingPromoType = new Financing();
            List<String> delayInMonthsSet = financingPromoType.getDelayInMonthsList(financingList);

            storePromoExtract.setPromoRegulars(promoRegulars);
            storePromoExtract.setPromoSywMbr(promoSywMbr);
            storePromoExtract.setPromoIds(promoIdSet);
            storePromoExtract.setRegularPromos(regularPromos);
            storePromoExtract.setFreeShipping(isFreeShipping(sywMaxList));
            storePromoExtract.setFreeDelivery(isFreeDelivery(regularList));
            storePromoExtract.setCompanion(isCompanion(regularList));
            storePromoExtract.setBuyOneGetOne(isBuyOneGetOne(regularList));
            storePromoExtract.setDelayInMonths(getDelayInMonths(delayInMonthsSet));
            extracts.getPromoExtract().getStorePromoExtracts().put(store, storePromoExtract);
        }
        return extracts;
    }

    /**
     * is promotype = shipping
     * is status = "a"
     * is valid dates
     * is free Promo flag
     * If above conditions are all true for even one node in the list then its marked for freeshipping
     *
     * @param sywMaxList
     * @return
     */
    public boolean isFreeShipping(List<SywMax> sywMaxList) {
        for (SywMax sywMax : sywMaxList) {
            boolean isFreeShipping = isFreeShippingSywMax(sywMax);
            if (isFreeShipping) {
                return true;
            }
        }
        return false;
    }

    /**
     * is promotype = shipping
     * is status = "a"
     * is valid dates
     * is free Promo flag
     * If above conditions are all true, then its marked for freeshipping
     *
     * @param sywMax
     * @return
     */
    protected boolean isFreeShippingSywMax(SywMax sywMax) {
        boolean isPromoTypeShipping;
        boolean isStatusA;
        boolean areDatesValid;
        boolean isFreePromoFlag;

        isFreePromoFlag = isFreePromoFlag(sywMax);
        if (!isFreePromoFlag) {
            return false;
        }

        isStatusA = StringUtils.equals(sywMax.getStatus(), ACTIVE_STATUS);
        if (!isStatusA) {
            return false;
        }

        isPromoTypeShipping = StringUtils.equalsIgnoreCase(sywMax.getPromoType(), PromoType.SHIPPING.getPromoType());
        if (!isPromoTypeShipping) {
            return false;
        }

        areDatesValid = checkForDatesValidity(sywMax);
        if (!areDatesValid) {
            return false;
        }

        return true;
    }

    /**
     * Check if the start and end date are valid in the syw max node,
     * start >= current and end <= current
     *
     * @param sywMax
     * @return
     */
    private boolean checkForDatesValidity(SywMax sywMax) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        boolean isStartDateValid = OfferUtil.isDateValid(sywMax.getStartDt(), currentDateTime, false);
        boolean isEndDateValid = OfferUtil.isDateValid(sywMax.getEndDt(), currentDateTime, true);

        if (isStartDateValid && isEndDateValid) {
            return true;
        }
        return false;
    }

    private boolean isFreePromoFlag(SywMax sywMax) {
        return StringUtils.isNotEmpty(sywMax.getFreePromoFlag())
                ? Boolean.valueOf(sywMax.getFreePromoFlag()).booleanValue()
                : false;
    }

    /**
     * Get delay in months from promotions for given product
     *
     * @param delayInMonthsSet
     * @return
     */
    private String getDelayInMonths(List<String> delayInMonthsSet) {
        if (delayInMonthsSet.isEmpty()) {
            return StringUtils.EMPTY;
        }
        return delayInMonthsSet.get(FIRSTELEMENT);
    }

    /**
     * Checks if free delivery is applicable
     *
     * @param regularList
     * @return true if product is applicable for free delivery
     */
    private boolean isFreeDelivery(List<Regular> regularList) {

        for (Regular regular : regularList) {
            String promoType = regular.getPromoType();
            String shipInfo = regular.getShipInfo();
            boolean freePromoFlag = regular.getFreePromoFlag();
            boolean companionPromoTypeFlag = StringUtils.equalsIgnoreCase(promoType, PromoType.COMPANION.getPromoType());
            boolean shipInfoUPSFlag = StringUtils.equalsIgnoreCase(shipInfo, PromoType.SHIPINFOUPS.getPromoType());
            if (freePromoFlag && companionPromoTypeFlag && !shipInfoUPSFlag) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if companion promotion is present for product
     *
     * @param regularList
     * @return true if companion promotion is applicable
     */
    private boolean isCompanion(List<Regular> regularList) {
        for (Regular regular : regularList) {
            String promoType = regular.getPromoType();
            String shipInfo = regular.getShipInfo();
            boolean freePromoFlag = regular.getFreePromoFlag();
            boolean companionPromoTypeFlag = StringUtils.equalsIgnoreCase(promoType, PromoType.COMPANION.getPromoType());
            boolean shipInfoUPSFlag = StringUtils.equalsIgnoreCase(shipInfo, PromoType.SHIPINFOUPS.getPromoType());
            if (companionPromoTypeFlag && (!freePromoFlag || shipInfoUPSFlag || StringUtils.isEmpty(shipInfo))) {
                return true;
            }

        }
        return false;
    }

    /**
     * Checks if buy one get one is applicable for given product
     *
     * @param regularList
     * @return true if BOGO promotion is present
     */
    private boolean isBuyOneGetOne(List<Regular> regularList) {
        for (Regular regular : regularList) {
            String promoType = regular.getPromoType();
            boolean bonusFlag = StringUtils.equalsIgnoreCase(promoType, PromoType.BONUS.getPromoType());
            if (bonusFlag) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Extracts extractExpirableFields(Map<Stores, List<PromoDto>> promoDtoMap, WorkingDocument wd) {

        Extracts extracts = wd.getExtracts();

        for (Map.Entry<Stores, List<PromoDto>> promoDtoMapEntry : promoDtoMap.entrySet()) {
            List<PromoDto> promoDtoList = promoDtoMapEntry.getValue();
            if (CollectionUtils.isEmpty(promoDtoList)) {
                continue;
            }
            PromoDto promoDto = promoDtoList.get(FIRSTELEMENT);
            List<ExpirableSolrField> expireableFields = generateExpirableField
                    .getExpirableFieldsMapping(this.getClass().getSimpleName(), promoDto);
            extracts.addExpirableFieldList(expireableFields);
        }
        return extracts;
    }
}
