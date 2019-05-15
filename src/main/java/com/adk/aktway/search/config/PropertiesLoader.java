package com.adk.aktway.search.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by rames on 15-05-2019.
 */
public class PropertiesLoader {

    private static Logger logger = Logger.getLogger(PropertiesLoader.class.getName());
    private static Properties baseProperties = new Properties();
    private static Properties overrideProperties = new Properties();

    public PropertiesLoader() {
    }

    public static void load(Properties properties, String fileName) {
        InputStream input = null;

        try {
            logger.info("filename is " + fileName);
            input = PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName);
            properties.load(input);
        } catch (IOException var12) {
            var12.printStackTrace();
        } finally {
            if(input != null) {
                try {
                    input.close();
                } catch (IOException var11) {
                    var11.printStackTrace();
                }
            }

        }

    }

    public static String getProperty(String key) {
        String property = System.getProperty(key);
        if(StringUtils.isNotEmpty(property)) {
            return property;
        } else {
            property = overrideProperties.getProperty(key);
            return StringUtils.isNotEmpty(property)?property:baseProperties.getProperty(key);
        }
    }

    public static Properties getBaseProperties() {
        return baseProperties;
    }

    public static Properties getOverrideProperties() {
        return overrideProperties;
    }

    public static List<String> getPropertyAsList(String key, String delimiter) {
        String property = getProperty(key);
        return StringUtils.isEmpty(property)?null:Arrays.asList(property.split(delimiter));
    }
    public static void load(String fileName) {
        load(getBaseProperties(), GlobalConstants.BASE_CONFIG_FILE_NAME);
        load(getOverrideProperties(), fileName);
        GlobalConstants.init();
    }

}
