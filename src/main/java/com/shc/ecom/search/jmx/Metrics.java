/**
 *
 */
package com.shc.ecom.search.jmx;

/**
 * @author rgopala
 */
public class Metrics {

    public static final String INBOUND_ID_COUNTER = "Counter-InboundIDs";

    public static final String DUPLICATE_ID_COUNTER = "Counter-DuplicateIDs";

    public static final String DIFF_SERVER_ID_COUNTER = "Counter-DiffServerIDs";

    public static final String REJECT_ID_COUNTER = "Counter-RejectedIDs";

    public static final String PROCESSED_ID_COUNTER = "Counter-ProcessedIDs";

    public static final String INDEXED_ID_COUNTER = "Counter-IndexedIDs";

    public static final String DOCBUILDING_EXCEPTIONS_COUNTER = "Counter-DocBuildingExceptions";

    public static final String INDEXING_EXCEPTIONS_COUNTER = "Counter-SolrIndexingExceptions";

    public static final String JSONFILEWRITE_EXCEPTIONS_COUNTER = "Counter-JsonDocWritingExceptions";

    public static final String KAFKA_PUSH_INDEX_FAIL_COUNTER = "Counter-KafkaPushIndexFailCounter";

    public static final String KAFKA_PUSH_INDEX_FAIL_EXCEPTIONS_COUNTER = "Counter-KafkaPushIndexFailExceptionsCounter";

    public static final String KAFKA_PUSH_JSON_FAIL_COUNTER = "Counter-KafkaPushJsonFailCounter";

    public static final String KAFKA_PUSH_JSON_FAIL_EXCEPTIONS_COUNTER = "Counter-KafkaPushJsonFailExceptionsCounter";

    public static final String KAFKA_PUSH_INDEXING_METRICS_COUNTER = "Counter-KafkaPushIndexingMetricsCounter";

    public static final String KAFKA_PUSH_INDEXING_METRICS_EXCEPTIONS_COUNTER = "Counter-KafkaPushIndexingMetricsExceptionsCounter";

    public static final String TIMER_EACH_PREFIX = "Timer-Each";

    public static final String REQUEST = "Request";

    public static final String CALLS = "Calls";

    public static final String COUNTER_PREFIX = "Counter-";

    public static final String OVERALL_ID_PROCESSING_TIME = "Timer-OverallIDProcessing";

    public static final String STATUS_BUCKET_EXPORT_COMPLETE = "Status-BucketExportComplete";

    public static final String STATUS_EXTRACT_COMPLETE = "Status-ExtractComplete";

    public static final String STATUS_INDEXING_COMPLETE = "Status-IndexingComplete";

    public static final String TIMER_INDEX_TO_SOLR = "Timer-IndexDocsToSolr";

    public static final String TIMER_POST_JSON_TO_KAFKA = "Timer-PostJsonToKafka";

    public static final String TIMER_POST_JSON_TO_MEMCACHE = "Timer-PostJsonToMemcache";

    public static final String TIMER_STORE_PRICE = "Timer-EachStorePriceRequest";

    public static final String COUNTER_STORE_PRICE_CALLS = "Counter-StorePriceRequest";

    public static final String COUNTER_IAS_SERVICE_CALLS = "Counter-IASCalls";

    public static final String TIMER_IAS_SERVICE = "Timer-EachIASRequest";

    public static final String COUNTER_UAS_SERVICE_CALLS = "Counter-UASCalls";

    public static final String TIMER_UAS_SERVICE = "Timer-EachUASRequest";

    public static final String COUNTER_PAS_SERVICE_CALLS = "Counter-PASCalls";

    public static final String TIMER_PAS_SERVICE = "Timer-EachPASRequest";

    public static final String COUNTER_FITMENT_SERVICE_CALLS = "Counter-FitmentCalls";

    public static final String TIMER_FTIMENT_SERVICE = "Timer-EachFitmentRequest";

    public static final String COUNTER_PROMO_SERVICE_CALLS = "Counter-PromoCalls";

    public static final String TIMER_PROMO_SERVICE = "Timer-EachPromoRequest";

    public static final String COUNTER_SELLER_SERVICE_CALLS = "Counter-SellerCalls";

    public static final String TIMER_SELLER_SERVICE = "Timer-EachSellerRequest";

    public static final String COUNTER_FALLBACK_SOLR_PRICE_CALLS = "Counter-FallBackSolrPriceCalls";

    public static final String TIMER_FALLBACK_SOLR_PRICE_SERVICE = "Timer-EachFallBackSolrPriceRequest";

    public static final String TIMER_GENERATE_OFFER_DOCS = "Timer-GenerateOfferDocForEachProduct";

    public static final String COUNTER_OFFER_DOCS_GENERATED = "Counter-OfferDocsGenerated";

    public static final String COUNTER_OFFER_DOCS_EXCEPTIONS = "Counter-OfferDocExceptions";

	public static final String COUNTER_PRICE_GRID_CALLS = "Counter-PriceGridCalls";

	public static final String TIMER_PRICE_GRID_SERVICE = "Timer-EachPriceGridServiceRequest";

    public static final String COUNTER_ZIPDDC_SERVICE_CALLS = "Counter-ZipDDCCalls";

    public static final String TIMER_ZIPDDC_SERVICE = "Timer-EachZipDDCRequest";

    public static final String COUNTER_OFFER_CALLS_FOR_GETTING_KSN = "Counter-OfferCallsForGettingKSN";

    public static final String COUNTER_BUNDLE_DISPLAY_ELIGIBILE_RULE = "Counter-BundleDisplayEligibleRule";

    public static final String COUNTER_BUNDLE_HIERARCHY_EXISTENCE_RULE = "Counter-BundleHierarchyExistenceRule";

    public static final String COUNTER_BUNDLE_MARK_FOR_DELETE_RULE = "Counter-BundleMarkForDeleteRule";

    public static final String COUNTER_BUNDLE_SITES_RULE = "Counter-BundleSitesRule";

    public static final String COUNTER_CONTENT_DISPLAY_ELIGIBILE_RULE = "Counter-ContentDisplayEligibleRule";

    public static final String COUNTER_FITMENT_RULE = "Counter-FitmentRule";

    public static final String COUNTER_HIERARCHY_EXISTENCE_RULE = "Counter-HierarchyExistenceRule";

    public static final String COUNTER_IDENTITY_EXISTENCE_RULE = "Counter-IdentityExistenceRule";

    public static final String COUNTER_IS_KMARTPR_DROP_RULE = "Counter-IdentityExistenceRule";

    public static final String COUNTER_MARK_FOR_DELETE_RULE = "Counter-MarkForDeleteRule";

    public static final String COUNTER_OFFER_AUTOMOTIVE_RULE = "Counter-OfferAutomotiveRule";

    public static final String COUNTER_OFFER_DISPLAY_ELIGIBLE_RULE = "Counter-OfferDisplayEligibleRule";

    public static final String NO_VALID_OFFERS_FOR_SSIN_RULE = "NoValidOffersForSsinRule";

    public static final String EMPTY_PRICE_RULE = "EmptyPriceRule";

    public static final String COUNTER_OFFER_MARKETPLACE_RULE = "Counter-OfferMarketPlaceRule";

    public static final String COUNTER_OFFER_TYPE_EXISTENCE_RULE = "Counter-OfferTypeExistenceRule";

    public static final String COUNTER_OPERATIONAL_SITES_EXISTENCE_RULE = "Counter-OpertionalSitesExistenceRule";

    public static final String OUT_OF_STOCK_PRODUCT_RULE = "OutOfStockProductRule";

    public static final String COUNTER_OUT_OF_STOCK_OFFER_RULE = "Counter-OutOfStockOfferRule";

    public static final String COUNTER_PRICE_EXISTENCE_RULE = "Counter-PriceExistenceRule";

    public static final String COUNTER_PRODUCT_STORE_ELIGIBLE_RULE = "Counter-ProductStoreEligibleRule";

    public static final String COUNTER_RANKING_RULE = "Counter-RankingRule";

    public static final String COUNTER_RETIRED_TAXONOMY_RULE = "Counter-RetiredTaxonomyRule";

    public static final String COUNTER_SEARCH_SUPRESSION_RULE = "Counter-SearchSupressionRule";

    public static final String COUNTER_SITE_ELIG_RULE = "Counter-SiteEligRule";

    public static final String COUNTER_SITES_EXISTENCE_RULE = "Counter-SitesExistenceRule";

    public static final String COUNTER_SSIN_EXISTENCE_RULE = "Counter-SsinExistenceRule";

    public static final String COUNTER_SYW_EXCLUSIVE_RULE = "Counter-SywExclusiveRule";

    public static final String COUNTER_UID_EXISTENCE_RULE = "Counter-UidExistenceRule";

}
