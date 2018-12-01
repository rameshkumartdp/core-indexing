package com.shc.ecom.search.config;

import org.apache.commons.lang3.StringUtils;

import static com.shc.common.misc.GlobalConstants.BASE_CONFIG_FILE_NAME_SUFFIX;
import static com.shc.common.misc.GlobalConstants.DEFAULT_ENV;
import static com.shc.common.misc.GlobalConstants.SERVICE_ENVIRONMENT;


public class PropertiesLoader {
    public static void load(String fileName) {
        com.shc.common.misc.PropertiesLoader.load(com.shc.common.misc.PropertiesLoader.getBaseProperties(), GlobalConstants.BASE_CONFIG_FILE_NAME);
        com.shc.common.misc.PropertiesLoader.load(com.shc.common.misc.PropertiesLoader.getOverrideProperties(), fileName);
        GlobalConstants.init();
    }

    public static void load() {
        String env = System.getenv(SERVICE_ENVIRONMENT);
        if (StringUtils.isEmpty(env)) {
            env = System.getProperty(SERVICE_ENVIRONMENT, DEFAULT_ENV);
        }
        String overrideFile = GlobalConstants.BASE_CONFIG_FILE_NAME_PREFIX + env + BASE_CONFIG_FILE_NAME_SUFFIX;
        load(overrideFile);
    }
}
