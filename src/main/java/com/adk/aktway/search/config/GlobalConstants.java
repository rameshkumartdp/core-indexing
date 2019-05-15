package com.adk.aktway.search.config;

public class GlobalConstants {

    public static final String BASE_CONFIG_FILE_NAME = "Core_Indexing_BASE.properties";
    public static final String SOLR_ZK_HOST = PropertiesLoader.getProperty("solr_zk_host");
    public static final String SOLR_CLOUD_COLLECTION = PropertiesLoader.getProperty("solr_cloud_collection");
    public static final String CLOUD_ZK_TIMEOUT = PropertiesLoader.getProperty("cloud_zk_timeOut");
    public static final String MONGO_HOST = PropertiesLoader.getProperty("mongo_host");
    public static final String MONGO_PORT = PropertiesLoader.getProperty("mongo_port");
    public static final String MONGO_DB = PropertiesLoader.getProperty("mongo_db");
    public static final String MONGO_COLLECTION = PropertiesLoader.getProperty("mongo_collection");
    public static final String INDEX_TIME_FORMAT = "yyyy_MM_dd_HHmmSSS";

    public static void init() { }

}