/**
 *
 */
package com.shc.ecom.search.common.constants;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author djohn0
 */
public class SearchCommonConstants implements Serializable {

    public static final String TIME_OUT_ERROR = "TIME OUT ERROR";
    public static final String SEARS_STORE_NAME = "SEARS";
    public static final String KMART_STORE_NAME = "KMART";
    public static final String MYGOFER_STORE_NAME = "MYGOFER3";
    public static final String SRSPUERTORICO_STORE_NAME = "SRSPUERTORICO";
    public static final String TGI_STORE_NAME = "TGI";
    public static final Map<String, String> SITE_PGRM_TYPE_MAP;
    public static final Map<String, String> STORENAMES_STOREID_MAP;
    public static final String ONE = "1";
    public static final String FIVE = "5";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String UNDERSCORE ="_";
    public static final String ZERO = "0";


    /**
     *
     */
    private static final long serialVersionUID = 4692894626041022883L;

    static {
        Map<String, String> enumMap = new HashMap<String, String>();
        enumMap.put(SEARS_STORE_NAME, "Sears");
        enumMap.put(KMART_STORE_NAME, "Kmart");
        enumMap.put("FBM", "FBM");
        enumMap.put("BUNDLE", "B");
        enumMap.put("COLLECTIONS", "C");
        enumMap.put("OUTFITS", "O");
        enumMap.put("UVD", "UVD");
        enumMap.put("HS", "HS");
        enumMap.put(MYGOFER_STORE_NAME, "mg3");// TODO:: Confirm the gb filter pgrmType name
        enumMap.put(SRSPUERTORICO_STORE_NAME, "pr");// TODO:: Confirm the gb filter pgrmType name
        enumMap.put(TGI_STORE_NAME, "tgi");// TODO:: Confirm the gb filter pgrmType name

        SITE_PGRM_TYPE_MAP = Collections.unmodifiableMap(enumMap);
    }

    static {
        Map<String, String> storeIdMap = new HashMap<String, String>();
        storeIdMap.put(SEARS_STORE_NAME, "10153");
        storeIdMap.put(KMART_STORE_NAME, "10151");
        storeIdMap.put(MYGOFER_STORE_NAME, "10175");
        storeIdMap.put(TGI_STORE_NAME, "10156");
        storeIdMap.put(SRSPUERTORICO_STORE_NAME, "10165");
        STORENAMES_STOREID_MAP = Collections.unmodifiableMap(storeIdMap);

    }
}
