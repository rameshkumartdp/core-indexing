/**
 *
 */
package com.shc.ecom.search.persistence;

import com.shc.ecom.search.common.level3CatData.Level3CatDataDto;
import com.shc.ecom.search.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Level3CatsData extends FileDataAccessor {

    private static final long serialVersionUID = -8766708566758338715L;

    private static final String SEPARATOR = "=";

    private static final Logger LOGGER = LoggerFactory.getLogger(Level3CatsData.class);

    private Map<String, Level3CatDataDto> level3Data = new HashMap<>();
    private Map<String, Level3CatDataDto> temp = new HashMap<>();

    public Map<String, Level3CatDataDto> getLevel3Data() {
        return level3Data;
    }

    public void setLevel3Data(Map<String, Level3CatDataDto> level3Data) {
        this.level3Data = level3Data;
    }

    @Override
    public void save(String value) {
        if (StringUtils.equalsIgnoreCase(value, "EOF")) {
            swap();
        }
        String[] data = value.split(SEPARATOR);
        if (data.length != 2) {
            return;
        }
        Level3CatDataDto level3CatsDto = JsonUtil.convertToEntityObject(data[1], Level3CatDataDto.class);
        temp.put(data[0], level3CatsDto);
    }

    private void swap() {
        level3Data.putAll(temp);
        temp.clear();
        LOGGER.info("level3Cats loaded. Size: " + level3Data.size());
    }

}
