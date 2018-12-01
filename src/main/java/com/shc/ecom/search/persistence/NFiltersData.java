package com.shc.ecom.search.persistence;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shc.ecom.search.common.nFiltersData.NFiltersDataDto;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.util.JsonUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rgopala
 */

@Component
public class NFiltersData extends FileDataAccessor {

    private static final long serialVersionUID = 4065174805142681564L;

    private static final Logger LOGGER = LoggerFactory.getLogger(NFiltersData.class);

    private Map<String, NFiltersDataDto> attachedData = new HashMap<>();

    private StringBuilder file = new StringBuilder(GlobalConstants.N_FILTERS_FILE_SIZE);

    public StringBuilder getFile() {
        return file;
    }

    public void setFile(StringBuilder file) {
        this.file = file;
    }

    public Map<String, NFiltersDataDto> getAttachedData() {
        return attachedData;
    }

    public void setAttachedData(Map<String, NFiltersDataDto> attachedData) {
        this.attachedData = attachedData;
    }

    @Override
    public void save(String value) throws JsonSyntaxException {
        if (StringUtils.equals(value, "EOF")) {
            Map<String, String> filterMap = readFileToMap();
            swap(filterMap);
            return;
        }
        file.append(value);
    }

    public String decodeBase64(String s) {
        byte[] decodedString = Base64.decodeBase64(s.getBytes(Charset.forName("UTF-8")));
        return new String(decodedString, Charset.forName("UTF-8"));
    }

    public Map<String, String> readFileToMap() throws JsonSyntaxException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> filterMap = new HashMap<>();
        try {
            filterMap = gson.fromJson(file.toString(), type);
        } catch (Exception e) {
            LOGGER.error("Error while parsing the nFilters JSON file" + e.toString());
            throw e;
        } finally {
            file = new StringBuilder(GlobalConstants.N_FILTERS_FILE_SIZE);
        }
        return filterMap;
    }

    public void swap(Map<String, String> filterMap) {

        Map<String, NFiltersDataDto> temporaryAttachedData = new HashMap<>();

        for (Map.Entry<String, String> entry : filterMap.entrySet()) {
            try {
                temporaryAttachedData.put(entry.getKey(), JsonUtil.convertToEntityObject(decodeBase64(entry.getValue()), NFiltersDataDto.class));
            } catch (Exception e) {
                LOGGER.error("exception while mapping nfilters data", e);
                LOGGER.info("Error while mapping the json response in nFilters file realized after decoding the Hierarchy -> " + entry.getKey()+e.getMessage());
            }
        }
        attachedData.putAll(temporaryAttachedData);
        temporaryAttachedData.clear();
        LOGGER.info("nFiltersData loaded. Size: " + attachedData.size());

    }
}