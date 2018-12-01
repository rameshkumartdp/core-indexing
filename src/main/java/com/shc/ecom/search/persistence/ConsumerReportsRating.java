/**
 *
 */
package com.shc.ecom.search.persistence;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rgopala
 */
@Component
public class ConsumerReportsRating extends FileDataAccessor {

    /**
     *
     */
    private static final long serialVersionUID = -2641311632319929848L;

    private static final String SEPARATOR = "\\|";

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerReportsRating.class);

    private Map<String, String> consumerReportRatings = new HashMap<>();
    private Map<String, String> temp = new HashMap<>();

    public String getConsumerReportRating(String ssin) {
        if (consumerReportRatings.containsKey(ssin)) {
            return consumerReportRatings.get(ssin);
        }
        return StringUtils.EMPTY;
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
        String ssin = map[0].trim();
        String rating = map[1].trim();
        if (StringUtils.isEmpty(ssin) || StringUtils.isEmpty(rating)) {
            return;
        }
        temp.put(ssin, rating);

    }

    private void swap() {
        consumerReportRatings.putAll(temp);
        temp.clear();
        LOGGER.info("ConsumerReportsRating.txt loaded. Size: " + consumerReportRatings.size());
    }
}
