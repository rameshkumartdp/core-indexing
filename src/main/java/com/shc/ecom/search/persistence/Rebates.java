/**
 *
 */
package com.shc.ecom.search.persistence;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author rgopala
 */
@Component
public class Rebates extends FileDataAccessor {

    /**
     *
     */
    private static final long serialVersionUID = -2641311632319929848L;

    private static final String SEPARATOR = "\\|";

    private static final Logger LOGGER = LoggerFactory.getLogger(Rebates.class);

    private Map<String, List<String>> rebates = new HashMap<>();
    private Map<String, List<String>> temp = new HashMap<>();

    public List<String> getRebates(String offerId) {
        if (rebates.containsKey(offerId)) {
            return rebates.get(offerId);
        }
        return new ArrayList<String>();
    }

    @Override
    public void save(String value) {
        if (StringUtils.equalsIgnoreCase(value, "EOF")) {
            swap();
            return;
        }
        String[] map = value.split(SEPARATOR);
        if (map.length != 2) {
            return;
        }
        String offerId = map[0].trim();
        String rebates = map[1].trim();
        List<String> rebateList = Arrays.asList(rebates.split("\\s*,\\s*"));
        if (StringUtils.isEmpty(offerId) || CollectionUtils.isEmpty(rebateList)) {
            return;
        }
        temp.put(offerId, rebateList);

    }

    private void swap() {
        rebates.putAll(temp);
        temp.clear();
        LOGGER.info("Rebates.txt loaded. Size: " + rebates.size());
    }
}
