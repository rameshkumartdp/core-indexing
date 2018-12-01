package com.shc.ecom.search.config;


public class GlobalConstants {
    public static final String BASE_CONFIG_FILE_NAME_PREFIX = "Core_Indexing_";
    public static final String BASE_CONFIG_FILE_NAME_SUFFIX = ".properties";
    public static final String BASE_CONFIG_FILE_NAME = BASE_CONFIG_FILE_NAME_PREFIX + "BASE" + BASE_CONFIG_FILE_NAME_SUFFIX;


    //Default Http Properties
    public static final String REQUEST_CONNECTION_TIMEOUT = "request.connection.timeout";
    public static final String REQUEST_SOCKET_TIMEOUT = "request.socket.timeout";
    public static final String REQUEST_ACCEPTS = "request.accepts";
    public static final String REQUEST_CONTENT_TYPE = "request.content.type";
    public static final String RESPONSE_CONTENT_TYPE = "response.content.type";
    public static final String RESPONSE_USER_AGENT = "response.user.agent";

    //Greenbox (CATALOG) host
    public static final String GB_HOST = "gb.host";
    public static final String GB_PORT = "gb.port";

    //Greenbox (SZIP) host
    public static final String SZIP_HOST = "szip.host";
    public static final String SZIP_PORT = "szip.port";

    
    //Greenbox API configurations
    public static final String GB_API_PUT_URL = "gb.api.put.url";
    public static final String GB_API_GET_URL = "gb.api.get.url";
    public static final String GB_API_DEL_URL = "gb.api.del.url";
    public static final String GB_API_BUCKET_URL = "gb.api.bucket.url";
    public static final String GB_API_GET_IDS_URL = "gb.api.get-ids.url";
    public static final String GB_API_GET_FILT_URL = "gb.api.get.filt.url";
    public static final String GB_API_GET_ALT_URL = "gb.api.get.alt.url";
    public static final String GB_API_GET_DEL_IDS_URL = "gb.api.get-del-ids.url";
    public static final String GB_API_GET_DELETED_DATA_URL = "gb.api.get-deleted-data.url";


    //Greenbox collection names
    public static final String GB_SELLER_COLLECTION_NAME = "gb.seller.collection.name";
    public static final String GB_PRICE_COLLECTION_NAME = "gb.price.collection.name";
    public static final String GB_CONTENT_COLLECTION_NAME = "gb.content.collection.name";
    public static final String GB_OFFER_COLLECTION_NAME = "gb.offer.collection.name";
    public static final String GB_ATTRIBUTE_COLLECTION_NAME = "gb.attribute.collection.name";
    public static final String GB_VARATTR_COLLECTION_NAME = "gb.varattr.collection.name";
    public static final String GB_ATTRIBUTE_VALUE_COLLECTION_NAME = "gb.attribute.value.collection.name";
    public static final String GB_ITEMCLASS_HIERARCHY_COLLECTION_NAME = "gb.itemclass.hierarchy.collection.name";
    public static final String GB_WEB_HIERARCHY_COLLECTION_NAME = "gb.web.hierarchy.collection.name";
    public static final String GB_STORE_HIERARCHY_COLLECTION_NAME = "gb.store.hierarchy.collection.name";
    public static final String GB_PROMO_COLLECTION_NAME = "gb.promo.collection.name";
    public static final String GB_PROMOREL_COLLECTION_NAME = "gb.promorel.collection.name";
    public static final String GB_PREVIEWPROMO_COLLECTION_NAME = "gb.previewpromo.collection.name";
    public static final String GB_PREVIEWPROMOREL_COLLECTION_NAME = "gb.previewpromorel.collection.name";
    public static final String GB_OFFERATTRS_COLLECTION_NAME = "gb.offerattrs.collection.name";
    public static final String GB_VENDOR_COLLECTION_NAME = "gb.vendor.collection.name";
    public static final String GB_SHC_PRICING_COLLECTION_NAME = "gb.shc.pricing.collection.name";
    public static final String GB_STORE_PRICING_COLLECTION_NAME = "gb.store.pricing.collection.name";
    public static final String GB_REGIONAL_PRICING_COLLECTION_NAME = "gb.regional.pricing.collection.name";
    public static final String GB_LOCALADS_COLLECTION_NAME = "gb.localads.collection.name";
    public static final String GB_LOCALADS_PAGE_COLLECTION_NAME = "gb.localads.page.collection.name";
    public static final String GB_STORE_COLLECTION_NAME = "gb.store.collection.name";
    //Retry config
    public static final String GB_API_GET_LIMIT = "gb.api.get.limit";
    public static final String GB_API_PUT_LIMIT = "gb.api.put.limit";
    public static final String GB_API_RETRY_FLAG = "gb.api.retry.flag";
    public static final String GB_API_RETRY_COUNT = "gb.api.retry.count";

    // GB IDs grouping count (partitioning the list of offers)
    public static final String PARTITION_COUNT = "partitionCount";

    //Seller service properties
    public static final String SELLER_HOSTNAME = "seller.hostname";
    public static final String SELLER_PORTNO = "seller.portno";
    public static final String SELLER_PATH = "seller.path";

    //Promo Service properties
    public static final String PROMO_HOSTNAME = "promo.hostname";
    public static final String PROMO_PORTNO = "promo.portno";
    public static final String PROMO_PATH = "promo.path";
    public static final String PROMO_SITE = "promo.site";
    public static final String PROMO_FLAVOR = "promo.flavor";
    public static final String PROMO_FLAVOR_VALUES = "promo.flavor_values";

    //GB Params
    public static final String GB_CLIENT_PARAMETER_NAME = "gb.client.parameter.name";
    public static final String GB_CLIENT_ID = "gb.client.id";

    //BuyBox API configuration
    public static final String BUYBOX_HOST_URL = "buybox.host.url";
    public static final String BUYBOX_PORT = "buybox.port";
    public static final String BUYBOX_RANK_COLLECTION = "buybox.rank.collection";
    public static final String BUYBOX_RANK_GET_URL = "buybox.rank.get.url";

    //IAS API configuration
    public static final String IAS_HOST_ROUTE_URL = "ias.host.route.url";
    public static final String IAS_PORT = "ias.port";
    public static final String IAS_API_GET_URL = "ias.api.get.url";
    public static final String IAS_API_GET_KSN_POSTFIX = "ias.api.get.ksn.postfix";

    //ProdStats API configuration
    public static final String RETAIL_SERVICE_HOST_URL = "retail.service.host.url";
    public static final String RETAIL_SERVICE_PORT = "retail.service.port";
    public static final String RETAIL_SERVICE_SELECT_URL = "retail.service.select.url";

    //Behavioral Service Properties
    public static final String BEHAVIORAL_HOSTNAME = "behavioral.hostname";
    public static final String BEHAVIORAL_PORTNO = "behavioral.portno";
    public static final String BEHAVIORAL_PATH = "behavioral.path";

    //Pas Service properties
    public static final String PAS_HOSTNAME = "pas.hostname";
    public static final String PAS_PORTNO = "pas.portno";
    public static final String PAS_SEARS_PATH = "pas.sears.path";
    public static final String PAS_KMART_PATH = "pas.kmart.path";

    //UAS Service properties
    public static final String UAS_HOSTNAME = "uas.hostname";
    public static final String UAS_PORTNO = "uas.portno";
    public static final String UAS_PATH = "uas.path";
    public static final String UAS_PATH_SEARSPR = "uas.path.searspr";

    //ZipDDCService properties
    public static final String ZIPDDC_HOSTNAME = "zipddc.hostname";
    public static final String ZIPDDC_PORTNO = "zipddc.portno";
    public static final String ZIPDDC_PATH = "zipddc.path";

    //Autofitment Service properties
    public static final String AUTOFITMENT_HOST = "autoFitment.host";
    public static final String AUTOFITMENT_PORT = "autoFitment.port";
    public static final String AUTOFITMENT_PATH = "autoFitment.path";
    public static final String AUTOFITMENT_BRANDS_PATH = "autoFitment.brands.path";
    public static final String AUTOFITMENT_PARTS_PATH = "autoFitment.parts.path";

    //Monitoring server peoperties
    public static final String MONITORING_SERVER_PORT = "monitoring.port";
    public static final String ENABLE_MONITORING = "enable.monitoring";

    //Bundle pricing PDP URL
    public static final String BUNDLEPRICE_PDP_URL = "bundlePrice.pdp.url";


    //Pricing Grid Service Properties
    public static final String PRICE_GRID_HOST = "priceGrid.host";
    public static final String PRICE_GRID_PORT = "priceGrid.port";
    public static final String PRICE_GRID_PATH = "priceGrid.path";
    public static final String PRICE_GRID_OFFER = "priceGrid.offer";
    public static final String PRICE_GRID_SSIN = "priceGrid.ssin";
    public static final String PRICE_GRID_SITE = "priceGrid.site";
    public static final String PRICE_GRID_STOREUNIT = "priceGrid.storeUnit";
    public static final String PRICE_GRID_ZIPCODE = "priceGrid.zipcode";
    public static final String PRICE_GRID_AUTHID = "priceGrid.authId";

    //Data files Configuration
    public static final String DATAFILES = "datafiles";

    public static final String JSONPATH = "jsonPath";
    public static final String JSONFILEPREFIX = "jsonFilePrefix";

    public static final String GROUPBYPATH = "gbJsonPath";
    public static final String GROUPBYFILEPREFIX = "gbFilePrefix";

    //Solr Zookeeper configuration
    public static final String SOLR_ZK_HOST = "solr_zk_host";
    public static final String SOLR_CLOUD_COLLECTION = "solr_cloud_collection";
    public static final String CLOUD_ZK_TIMEOUT = "cloud_zk_timeOut";

    //Fallback pricing
    public static final String FALLBACK_PRICING_GRID = "featFallbackPricingGrid";
    public static final String FEAT_FALLBACK_SOLR_PRICE = "featFallbackSolrPrice";
    public static final String FALLBACK_PRICING_SOLR_QUERY = "fallback_pricing_solr_query";
    public static final String SOLR_ACTIVE_CLOUD_COLLECTION = "solr_active_cloud_collection";
    public static final String SOLR_LIVE_NODE_SUFFIX = "solr_live_node_suffix";

    public static final String QUICKADD_FILE_NAME = "quickadd-file-name";

    public static final String INCR_FILE_NAME = "incr-file-name";

    public static final String BUCKET_THREAD_POOL_COUNT = "bucket.thread.pool.count";
    //Full mode for all stores thread pool count
    public static final String EXTRACTOR_THREAD_POOL_COUNT = "extractor.thread.pool.count";
    //document indexer to solr thread configuration
    public static final String INDEXER_THREAD_POOL_COUNT = "indexer.thread.pool.count";
    //store price task thread configuration
    public static final String STOREPRICE_THREAD_POOL_COUNT = "storeprice.thread.pool.count";

    public static final String TOTAL_INDEXER_SERVERS_SEARS = "total.indexer.servers.sears";
    public static final String TOTAL_INDEXER_SERVERS_FBM = "total.indexer.servers.fbm";
    public static final String TOTAL_INDEXER_SERVERS_KMART = "total.indexer.servers.kmart";

    public static final String ENABLE_GROUPBY_FILES = "enable.groupby.files";
    public static final String INDEXTOSOLR = "indexToSolr";
    public static final String ENABLE_JSON_FILES = "enable.json.files";
    public static final String ENABLE_POST_JSON_FILES_TO_KAFKA = "enable.post.json.files.to.kafka";
    public static final String ENABLE_POST_JSON_FILES_TO_MEMCACHE = "enable.post.json.files.to.memcache";
    public static final String TRANSLATE_QUEUE_SIZE = "translate.queue.size";

    public static final String SOLR_DOCS_BATCH_SIZE = "solr.docs.batch.size";


    //TopicModeling
    public static final String NAME_SEARCHABLE = "nameSearchable";
    public static final String NAME_SEARCHABLE_AND_DESCRIPTION = "nameSearchableAndDescription";
    public static final String NAME_SEARCHABLE_AND_TRUNCATED_DESCRIPTION = "nameSearchableAndTruncatedDescription";
    public static final String THRESHOLD_POBABILITY = "thresholdProbability";
    public static final String REPLICATION_FACTOR_FOR_TM = "replicationFactor";
    public static final String NUMBER_OF_TOPICS = "numberOfTopics";
    public static final String FILE_PATH_FOR_INFERENCER = "filePathForInferencer";
    public static final String FILE_PATH_FOR_INSTANCES = "filePathForInstances";

    
    //NorthStar
    public static final String FETCH_STORE_IDS_FROM_FILE = "storeZipFromFile";
    public static final String SEARS_STOREZIP_STORE_IDS = "searsStoreZipStoreIds";
    public static final String KMART_STOREZIP_STORE_IDS = "kmartStoreZipStoreIds";
    

    public static final String ACCESSORY_IDENTIFIERS = "accessory.identifiers";

    //Exluded category from primaryLnames and lnames field
    public static final String EXCLUDED_CATEGORY = "excluded.category";

    public static final String TIME_DELTA_IN_HOURS = "time.delta.hours";

    public static final String MAPPING_THREAD_POOL_COUNT = "mapping.thread.pool.count";
    public static final String LOCALAD_THREAD_COUNT = "localad.thread.count";
    public static final String LOCALAD_REQUEST_THREAD_TIMEOUT = "localad.request.thread.timeout";

    //Kafka Producer
    public static final String INDEX_FAIL_TOPIC_NAME = "index.fail.topic.name";
    public static final String INDEXING_METRICS_TOPIC_NAME = "indexing.metrics.topic.name";
    public static final String JSON_FILES_TOPIC_NAME = "json.files.topic.name";
    public static final String BOOTSTRAP_SERVER_LIST = "bootstrap.server.list";
    public static final String MEMCACHE_SERVER_LIST = "memcache.server.list";
    public static final String ZOOKEEPER_KAFKA_LIST = "zookeeper.list";
    public static final String ACKS = "acks";
    public static final String RETRIES = "retries";
    public static final String KEY_SERIALIZER = "key.serializer";
    public static final String MAX_BLOCK_MS = "max.block.ms";
    public static final String REQUEST_TIMEOUT_MS = "request.timeout.ms";
    public static final String KAFKA_THREAD_POOL_COUNT = "kafka.thread.pool.count";
    public static final String KAFKA_MESSAGE_QUEUE_SIZE = "kafka.message.queue.size";

    /*******************************
     * Feature Flags
     ******************************/
    public static final String FEAT_DROP_OOS_PRODUCTS = "featDropOOSProducts";
    public static final String FEAT_RELEVANCY_TEST_OOS_SUSPEND = "featRelevancyTestOOSSuspend";
    public static final String MAX_HTTP_CONNECTIONS = "maxHttpConnections";
    public static final String MAX_HTTP_CONNECTIONS_PER_ROUTE = "maxHttpConnectionsPerRoute";
    public static final String ENABLE_TOPIC_MODELING = "enableTopicModeling";
    public static final String FEAT_REMOVE_SEARSPR_FIELDS = "featRemoveSearsPRFields";
    public static final String FEAT_REMOVE_OFFER_FIELDS_FROM_PARENT = "featRemoveOfferFieldsFromParent";
    public static final String ENABLE_KAFKA_INDEX_FAIL = "enableKafkaIndexFail";
    public static final String ENABLE_STORE_ZIP = "enableStoreZip";

    // In-store pricing
    public static final String ENABLE_INSTORE_PRICING = "enableInStorePricing";
    public static final String ENABLE_NV_SWATCHES = "enableNVSwatches";

    public static final int N_FILTERS_FILE_SIZE = 200000000;
    public static final String ENABLE_LOCALADS = "enableLocalAds";
    public static final String MYGOFER = "mygofer";
    public static final String ENABLE_CROSSSITE_STORE_PICKUP = "enableCrossSiteStorePickup";
    public static final String VIEW_ONLY_FFM_CHANNEL = "VIEW_ONLY";

    //Rebates
    public static final String MAIL_IN_UTILITY_REBATES = "Mail-In Utility Rebates";
    public static final String INSTANT_UTILITY_REBATES = "Instant Utility Rebates";

    //Commercial filter fields
    public static final String COLOR_FAMILY = "Color Family";
    public static final String WIDTH = "Width";
    public static final String HEIGHT = "Height";
    public static final String DEPTH = "Depth";
    public static final String ENERGYSTAR_COMPLIANT = "ENERGY STAR Compliant";
    public static final String ADA_COMPLIANT = "ADA Compliant";

    //Handling Response Status & errors
    public static final String GET_RETURN_STATUS ="GET returned status code: ";
    public static final String POST_RETURN_STATUS ="POST returned status code: ";
    public static final String FOR_URL = " for URL: ";
    public static final String WITH_STATUS_CODE = " with status code : ";
    public static final String ERROR_WITH_URL = "Error processing the request for the URL: ";
    public static final String RESPONSE_WITHOUT_CONTENT = "Response containing no content for the URL: ";
    public static final String EBAY="eBay";
    public static final String DISABLE_EBAY_OFFERS="disableEBayOffers";

    public static final String PRICEJAR_ERROR = "Due to PriceJAR error, ";
    public static final String PRICEJAR_ZERO_PRICE = "Due to Zero price from PriceJAR,  ";

    public static void init() {

    }

}