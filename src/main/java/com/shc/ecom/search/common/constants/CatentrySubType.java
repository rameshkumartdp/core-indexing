package com.shc.ecom.search.common.constants;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author pchauha
 */
public enum CatentrySubType {

    NON_VARIATION("NV"),
    NON_VARIATION_UVD("NV-UVD"),
    VARIATION("V"),
    VARIATION_UVD("V-UVD"),
    BUNDLE("B"),
    COLLECTION("C"),
    OUTFIT("O"),
    HOME_SERVICES("HS"),
    STORE_ONLY("S"),
    UNKNOWN(null);

    private final String catentrySubTypeName;

    private CatentrySubType(String name) {
        catentrySubTypeName = name;
    }

    public static CatentrySubType get(String name) {
        for (CatentrySubType catentrySubType : CatentrySubType.values()) {
            if (name.equalsIgnoreCase(catentrySubType.getName())) {
                return catentrySubType;
            }
        }
        throw new IllegalArgumentException("Unknown CatentrySubType");
    }

    public static CatentrySubType findCatentrySubType(List<String> types) {
        if (types.size() == 1) {
            return get(types.get(0));
        }
        if (types.contains("UVD") && types.contains("NV")) {
            return NON_VARIATION_UVD;
        }
        if (types.contains("UVD") && types.contains("V")) {
            return VARIATION_UVD;
        }
        // THIS TYPE IS NOT INDEXED CURRENTLY, and SAFE TO BE USED AS DEFAULT
        return UNKNOWN;
    }

    public String getName() {
        return catentrySubTypeName;
    }

    public boolean matches(String catentrySubType) {
        return StringUtils.equalsIgnoreCase(catentrySubType, this.catentrySubTypeName);
    }
}
