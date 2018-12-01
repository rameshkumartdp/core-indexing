package com.shc.ecom.search.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.shc.ecom.search.common.constants.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pchauha
 */
public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    public static ObjectMapper mapper = new ObjectMapper();

    public static <Entity> Entity convertToEntityObject(String jsonResponse, Class<Entity> classType) {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        Entity entityObject = null;
        try {
            entityObject = mapper.readValue(jsonResponse, classType);
        } catch (IOException e) {
            LOGGER.error("Error while mapping the json response to entity object.  Input: {}. Exception: {}", jsonResponse, e);
        }
        return entityObject;
    }

    public static <Entity> List<Entity> convertToEntityObjectList(String jsonResponse, Class<Entity> classType) {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        List<Entity> entityObjectList = null;
        try {
            entityObjectList = mapper.readValue(jsonResponse, TypeFactory.defaultInstance().constructCollectionType(List.class, classType));
        } catch (IOException e) {
            LOGGER.error("Error while mapping the json response to entity object list" + e.getMessage());
        }
        return entityObjectList;
    }

    public static <KeyEntity, ValueEntity> HashMap<KeyEntity, ValueEntity> convertToHashMap(String jsonResponse, Class<KeyEntity> keyType, Class<ValueEntity> valueType) {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        HashMap<KeyEntity, ValueEntity> hashMap = null;
        try {
            hashMap = mapper.readValue(jsonResponse, mapper.getTypeFactory().constructMapType(HashMap.class, keyType, valueType));
        } catch (IOException e) {
            LOGGER.error("Error while mapping the json response to entity object.  Input: {}. Exception: {}", jsonResponse, e);
        }
        return hashMap;
    }

    /**
     * Parses the JSON response from Solr to get the prices of the docs returned.
     *
     * @param jsonResponse
     * @param priceMap
     * @return
     */
    public static Map<Integer, Float> parseJsonToPrice(String jsonResponse, Map<Integer, Float> priceMap) {
        Map<Integer, Float> processedPriceMap = new HashMap<>();
        try {
            JsonNode root = mapper.readTree(jsonResponse);
            JsonNode docs = root.path("response").path("docs");
            if (docs.isMissingNode()) {
                return priceMap;
            }

            //Loop through all the docs
            for (JsonNode doc : docs) {
                //Get the price array, which may be empty
                JsonNode priceList = doc.path("price");
                if (priceList.isMissingNode()) {
                    continue;
                }

                processedPriceMap = getPriceFromSolrDocPriceNode(priceMap, priceList);
            }
            return processedPriceMap;
        } catch (IOException ioE) {
            LOGGER.error("Error mapping solr fallback pricing response to Map. Price will remain empty", ioE);
            return new HashMap<Integer, Float>();
        }
    }

    private static Map<Integer, Float> getPriceFromSolrDocPriceNode(Map<Integer, Float> priceMap, JsonNode priceList) {
        for (JsonNode jsonNode : priceList) {
            //Parse the raw string
            String[] tokens = jsonNode.asText().split("_");
            try {
                int storeId = Integer.parseInt(tokens[0]);
                //Only add to map if it is a valid online store, ignore instore prices
                if (Stores.isValidStoreId(storeId)) {
                    float price = Float.parseFloat(tokens[1]);
                    priceMap.put(storeId, price);
                }
            } catch (NumberFormatException e) {
                LOGGER.error("NumberFormatException while parsing " + jsonNode.asText(), e);
                continue;
            }
        }
        return priceMap;
    }
}
