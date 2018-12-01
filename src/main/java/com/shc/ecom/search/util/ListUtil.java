package com.shc.ecom.search.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ListUtil {

    public static boolean containsCaseInsensitive(List<String> list, String searchTerm) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }

        for (String listItem : list) {
            if (StringUtils.equalsIgnoreCase(listItem, searchTerm)) {
                return true;
            }
        }
        return false;
    }
}