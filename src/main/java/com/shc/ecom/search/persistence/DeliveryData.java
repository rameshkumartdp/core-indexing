package com.shc.ecom.search.persistence;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by jsingar on 12/26/17.
 */
@Component
public class DeliveryData extends FileDataAccessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryData.class);
    private static final String SEPARATOR = "\\|";
    private Map<String, List<String>> delivery = new HashMap<>();
    private Map<String, List<String>> temp = new HashMap<>();

    public List<String> getDeliveryData(String offerId) {
        if (delivery.containsKey(offerId)) {
            return delivery.get(offerId);
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
        String deliveryData = map[1].trim();
        if(deliveryData==null || deliveryData.isEmpty())
        {
            return;
        }
        List<String> deliveryList = Arrays.asList(deliveryData.split("\\s*,\\s*"));
        if (StringUtils.isEmpty(offerId) || CollectionUtils.isEmpty(deliveryList)) {
            return;
        }
        temp.put(offerId, deliveryList);

    }
    private void swap() {
        delivery.putAll(temp);
        temp.clear();
        LOGGER.info("DeliveryData.txt loaded. Size: " + delivery.size());
    }
}
