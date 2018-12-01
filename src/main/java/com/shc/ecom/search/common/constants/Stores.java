package com.shc.ecom.search.common.constants;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.extracts.WorkingDocument;


/**
 * @author rgopala Jul 17, 2015 search-extract-common
 */
public enum Stores {

    SEARS("SEARS", 10153, Sites.SEARS, BucketFilters.SEARS),
    SEARSPR("SEARSPR", 10165, Sites.SEARSPR, BucketFilters.SEARSPR),
    FBM("FBM", 10153, Sites.SEARS, BucketFilters.FBM),
    CPC("CPC", 10153, Sites.SEARS, BucketFilters.CPC),
    C2C("C2C", 10153, Sites.SEARS, BucketFilters.NOFILTERS),
    KMART("KMART", 10151, Sites.KMART, BucketFilters.KMART),
    KMARTPR("KMARTPR", 10351, Sites.KMART, BucketFilters.NOFILTERS),
    MYGOFER("MYGOFER", 10175, Sites.MYGOFER, BucketFilters.MYGOFER),
    TABLET("TABLET", 10153, Sites.SEARS, BucketFilters.SEARS),
    AUTO("AUTO", 10153, Sites.SEARS, BucketFilters.AUTO),
    INSURANCE("INSURANCE", 11201, Sites.SEARS, BucketFilters.NOFILTERS),
    KENMORE("KENMORE", 10154, Sites.KENMORE, BucketFilters.NOFILTERS),
    CRAFTSMAN("CRAFTSMAN", 10155, Sites.CRAFTSMAN, BucketFilters.NOFILTERS),
    COMMERCIAL("COMMERCIAL", 10153, Sites.COMMERCIAL, BucketFilters.COMMERCIAL);

    private final String storeName;
    private final int storeId;
    private final Sites site;
    private final BucketFilters bucketFilters;

    Stores(String storeName, int storeId, Sites site, BucketFilters bucketFilters) {
        this.storeName = storeName;
        this.storeId = storeId;
        this.site = site;
        this.bucketFilters = bucketFilters;
    }

    public static boolean isStoreValid(String storeName) {
        for (Stores store : Stores.values()) {
            if (StringUtils.equalsIgnoreCase(storeName, store.name())) {
                return true;
            }
        }
        return false;
    }

    public static List<Integer> getStoreId(WorkingDocument wd, ContextMessage context) {
        Stores store = getStore(context.getStoreName());
        if (store == null) {
            return Collections.emptyList();
        }

        List<Integer> storeIds = new ArrayList<>();
        if (store.matches(FBM.getStoreName()) || store.matches(AUTO.getStoreName())) {
            List<Sites> eligibleSites = store.getEligibleSites(wd, context);
            if (eligibleSites.contains(Sites.SEARS)) {
                storeIds.add(Stores.SEARS.getStoreId());
            }

            if (eligibleSites.contains(Sites.KMART)) {
                storeIds.add(Stores.KMART.getStoreId());
            }
            return storeIds;
        }

        storeIds.add(store.getStoreId());
        return storeIds;
    }

    public static Integer getStoreId(String storeName) {
        for (Stores store : Stores.values()) {
            if (StringUtils.equalsIgnoreCase(store.getStoreName(), storeName)) {
                return store.storeId;
            }
        }

        if (Sites.SEARSPR.matches(storeName)) {
            return SEARSPR.getStoreId();
        }
        return null;
    }

    public static String getStoreName(String storeId) {
        int storeIdInt = Integer.parseInt(storeId);
        for (Stores store : Stores.values()) {
            if (store.getStoreId() == storeIdInt) {
                return store.storeName;
            }
        }
        return null;
    }

    public static String getStoreName(int storeId) {
        for (Stores store : Stores.values()) {
            if (store.getStoreId() == storeId) {
                return store.storeName;
            }
        }
        return null;
    }

    public static Stores getStore(String storeName) {
        for (Stores store : values()) {
            if (StringUtils.equalsIgnoreCase(store.getStoreName(), storeName)) {
                return store;
            }
        }
        return null;
    }

    public static Stores getStore(int storeId) {
        return getStore(getStoreName(storeId));
    }

    public static boolean isValidStoreId(int storeId) {

        for (Stores store : values()) {
            if (store.storeId == storeId) {
                return true;
            }
        }
        return false;
    }

    public static Sites getSite(String storeName) {
        for (Stores store : values()) {
            if (StringUtils.equalsIgnoreCase(store.getStoreName(), storeName)) {
                return store.site;
            }
        }
        return null;
    }

    public static BucketFilters getBucketFilters(String storeName) {
        for (Stores store : values()) {
            if (StringUtils.equalsIgnoreCase(store.getStoreName(), storeName)) {
                return store.bucketFilters;
            }
        }
        return BucketFilters.NOFILTERS;
    }

    public static boolean matchesAll(List<Stores> stores, String storeName) {
        for (Stores store : stores) {
            if (StringUtils.equalsIgnoreCase(store.getStoreName(), storeName)) {
                return true;
            }
        }
        return false;
    }

    public int getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public boolean matches(String storeName) {
        return StringUtils.equalsIgnoreCase(storeName, this.storeName);
    }

    public boolean matchesAll(List<Stores> stores) {
        for (Stores store : stores) {
            if (StringUtils.equalsIgnoreCase(store.getStoreName(), storeName)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(List<String> storeNameList) {
        for (Stores store : values()) {
            for (String storeListElement : storeNameList) {
                if (StringUtils.equalsIgnoreCase(store.getStoreName(), storeListElement)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method allows to determine if a store has association with another store
     * Eg: Craftsman store (site) is associated with Sears in that, the items indexed during Sears run are the ones shown on Craftsman site.
     *
     * @param storeName
     * @return
     */
    public boolean isAssociatedToStore(String storeName) {
        boolean isAssociated = false;
        switch (this) {
            case KENMORE:
            case CRAFTSMAN:
            case SEARS:
                isAssociated = Stores.SEARS.matches(storeName) ? true : false;
                break;
            default:
                isAssociated = Stores.this.matches(storeName) ? true : false;
                break;
        }
        return isAssociated;
    }

    public List<String> getCrossFormatStores() {
        List<String> crossFormatStores = new ArrayList<>();
        switch (this) {
            case SEARS:
                crossFormatStores.add(Stores.KMART.getStoreName());
                break;
            case KMART:
                crossFormatStores.add(Stores.SEARS.getStoreName());
                break;
            default:
                break;
        }
        return crossFormatStores;
    }

    public List<Sites> getEligibleSites(WorkingDocument wd, ContextMessage context) {
        List<Sites> eligibleSites = new ArrayList<>();
        switch (this) {
            case FBM:
            case AUTO:
                Map<Sites, Boolean> contentSitesElig = wd.getExtracts().getContentExtract().getSitesEligibility();
                Map<Sites, Boolean> offerSitesElig = wd.getExtracts().getOfferExtract().getSitesEligibility();
                boolean isSearsSiteElig = false;
                boolean isKmartSiteElig = false;
                if(StringUtils.equalsIgnoreCase(wd.getExtracts().getContentExtract().getCatentrySubType(), "B")) {
                	isSearsSiteElig = BooleanUtils.toBoolean(contentSitesElig.get(Sites.SEARS));
                	isKmartSiteElig = BooleanUtils.toBoolean(contentSitesElig.get(Sites.KMART));
                } else {
                	isSearsSiteElig = BooleanUtils.toBoolean(contentSitesElig.get(Sites.SEARS)) && BooleanUtils.toBoolean(offerSitesElig.get(Sites.SEARS));
                	isKmartSiteElig = BooleanUtils.toBoolean(contentSitesElig.get(Sites.KMART)) && BooleanUtils.toBoolean(offerSitesElig.get(Sites.KMART));
                }
                if (isSearsSiteElig) {
                    eligibleSites.add(Sites.SEARS);
                }

                if (isKmartSiteElig) {
                    eligibleSites.add(Sites.KMART);
                }
                break;
            default:
                eligibleSites.add(Stores.getSite(context.getStoreName()));
        }
        return eligibleSites;
    }
}