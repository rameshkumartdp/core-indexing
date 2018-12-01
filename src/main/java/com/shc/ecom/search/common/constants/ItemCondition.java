package com.shc.ecom.search.common.constants;

import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Item Condition  - a filter returned to the front end to enable the user to
 * filter the results on the condition on the item
 * <p>
 * Existing classification and aggregation:
 * New: NEW, NEW_OTHER, NEW_BLEMISHED
 * Used: USED_LIKE_NEW, USED_VERY_GOOD, USED_GOOD, USED_ACCEPTABLE, USED_POOR
 * Refurbished: REFURBISHED, REFURBISHED_SELLER, REFURBISHED_MANUFACTURER_AUTHORIZED
 * <p>
 * New classification and aggregation:
 * New: New, New - Other, New - Blemished
 * Used: Used - Like New, Used – Very Good, Used - Good, Used - Acceptable, Used - Poor
 * Refurbished: Refurbished, Refurbished – Mfr Authorized, Refurbished – Seller Refurbished
 */
public enum ItemCondition {

    // Parent item condition, rank, list containing all child item conditions
    NEW("New", 1, Arrays.asList("NEW",
            "NEW_OTHER",
            "NEW_BLEMISHED",
            "New",
            "New - Other",
            "New - Blemished")),
    USED("Used", 2, Arrays.asList("USED",
            "USED_GENERAL",
            "USED_LIKE_NEW",
            "USED_VERY_GOOD",
            "USED_GOOD",
            "USED_ACCEPTABLE",
            "USED_POOR",
            "Used",
            "Used - Like New",
            "Used - Very Good",
            "Used - Good",
            "Used - Acceptable",
            "Used - Poor")),
    REFURBISHED("Refurbished", 3, Arrays.asList("REFURBISHED",
            "REFURBISHED_SELLER",
            "REFURBISHED_MANUFACTURER_AUTHORIZED",
            "Refurbished",
            "Refurbished - Mfr Authorized",
            "Refurbished - Seller Refurbished"));

    // Item condition to sub-condition mapping
    private static Map<String, Integer> itemCondRankMap = Maps.newHashMap();

    // Sub-condition to filter map
    private static Map<String, String> itemConditionFilterMap = Maps.newHashMap();

    static {
        for (ItemCondition itemCondition : ItemCondition.values()) {
            for (String subItemCondition : itemCondition.subStatus) {
                itemCondRankMap.put(subItemCondition, itemCondition.rank);
                itemConditionFilterMap.put(subItemCondition, itemCondition.status);
            }
        }
    }

    private String status;
    private int rank;
    private List<String> subStatus;

    ItemCondition(String status, int rank, List<String> subStatus) {
        this.status = status;
        this.rank = rank;
        this.subStatus = subStatus;
    }

    public static String getItemCondition(String itemCondition) {
        return itemConditionFilterMap.get(itemCondition);
    }

    public String getStatus() {
        return status;
    }

    public int getRank() {
        return rank;
    }

}