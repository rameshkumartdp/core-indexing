package com.shc.ecom.search.persistence;

import com.shc.ecom.search.common.customerrating.CustomerRatingDto;
import org.apache.commons.lang.math.NumberUtils;
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
public class CustomerRating extends FileDataAccessor {

    private static final long serialVersionUID = -5510272273974039455L;

    private static final String SEPARATOR = "\t";

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRating.class);

    private Map<String, Map<Integer, CustomerRatingDto>> customerRating = new HashMap<>();
    private Map<String, Map<Integer, CustomerRatingDto>> temp = new HashMap<>();

    public Map<String, Map<Integer, CustomerRatingDto>> getCustomerRating() {
        if (customerRating == null) {
            new HashMap<>();
        }
        return customerRating;
    }

    public void setCustomerRating(Map<String, Map<Integer, CustomerRatingDto>> customerRating) {
        this.customerRating = customerRating;
    }

    @Override
    public void save(String value) {
        if (StringUtils.equalsIgnoreCase(value, "EOF")) {
            swap();
            return;
        }

        String[] data = value.split(SEPARATOR);
        if (data.length != 4) {
            return;
        }

        CustomerRatingDto customerRatingDto = new CustomerRatingDto();
        customerRatingDto.setRating(NumberUtils.toDouble(data[1]));
        customerRatingDto.setReviewCount(NumberUtils.toInt(data[2]));

        Map<Integer, CustomerRatingDto> ratingReviewStoreIdMap = null;
        if (temp.containsKey(data[0])) {
            ratingReviewStoreIdMap = temp.get(data[0]);
        } else {
            ratingReviewStoreIdMap = new HashMap<>();
        }
        ratingReviewStoreIdMap.put(Integer.valueOf(data[3]), customerRatingDto);
        temp.put(data[0], ratingReviewStoreIdMap);
    }

    private void swap() {
        customerRating.putAll(temp);
        temp.clear();
        LOGGER.info("CustomerRating.txt loaded. Size: " + customerRating.size());
    }

}
