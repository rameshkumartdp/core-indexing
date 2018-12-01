package com.shc.ecom.search.ssinconsumer;

import com.codahale.metrics.Timer;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.gb.doc.common.DomainObject;
import com.shc.ecom.search.classifications.Classification;
import com.shc.ecom.search.classifications.ClassificationFactory;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.exception.SearchCommonException;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.messages.MessageProcessor;
import com.shc.ecom.search.common.messages.MessageType;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.docbuilder.Archive;
import com.shc.ecom.search.docbuilder.SearchDoc;
import com.shc.ecom.search.docbuilder.SearchDocBuilder;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.extract.extracts.WorkingDocumentUtility;
import com.shc.ecom.search.indexer.ProcessManager;
import com.shc.ecom.search.indexer.SpringContext;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.transformations.SearsPRTransformation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class Extractor implements Callable<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Extractor.class.getName());
    private static final Logger DEBUGLOGGER = LoggerFactory.getLogger("debuglog");
    private static ConcurrentHashMap<String, Boolean> processedIds = new ConcurrentHashMap<>();
    private static int docBatchSize;

    @Autowired
    @Qualifier("contentExtractComponent")
    protected ExtractComponent<DomainObject> contentExtractComponent;

    @Autowired
    private MessageProcessor messageProcessor;

    @Autowired
    private ClassificationFactory factory;

    @Autowired
    private MetricManager metricManager;

    @Autowired
    private WorkingDocumentUtility workingDocumentUtility;

    @Autowired
    private SearsPRTransformation searsPRTransformation;

    private String threadId;
    private BlockingQueue<String> translateQueue;
    private BlockingQueue<Archive> docQueue;

    public Extractor() {
        // Do nothing
    }

    public void configure(String threadId, BlockingQueue<String> translateQueue, BlockingQueue<Archive> docQueue) {
        this.threadId = threadId;
        this.translateQueue = translateQueue;
        this.docQueue = docQueue;
        docBatchSize = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.SOLR_DOCS_BATCH_SIZE));

    }

    private boolean isSkippable(ContextMessage context) {

		/*
         * Skip the processing of product if it's already processed or being processed by another thread/process.
		 */

        if (processedIds.containsKey(context.getKey())) {
            metricManager.incrementCounter(Metrics.DUPLICATE_ID_COUNTER);
            DEBUGLOGGER.info("Product skipped as duplicate: {}", context.getKey());
            return true;
        }

		/*
         * Skip the processing of product if it needs to be processed on
		 * a different server, for this store.
		 */
        int totalServers = Integer.parseInt(PropertiesLoader.getProperty("total.indexer.servers." + context.getStoreName().toLowerCase()));
        int currentServer = Integer.parseInt(context.getCurrentServer());

        int designatedServer = Math.abs(context.getPid().hashCode()) % totalServers;
        if (designatedServer != currentServer) {
            metricManager.incrementCounter(Metrics.DIFF_SERVER_ID_COUNTER);
            DEBUGLOGGER.info("Product skipped for processing on server instance {}: {}", designatedServer, context.getPid());
            return true;
        }
        return false;
    }

    @Override
    public String call() throws Exception {

        List<SearchDoc> searchDocs = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        while (!translateQueue.isEmpty() || ProcessManager.bucketExportComplete.get() == false) {

            String id = translateQueue.poll(5, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(id)) {
                continue;
            }
            metricManager.incrementCounter(Metrics.INBOUND_ID_COUNTER);

            Timer.Context timerContext = null;
            ContextMessage contextMessage = messageProcessor.getContextMessage(id);

            try {
                timerContext = metricManager.startTiming(Metrics.OVERALL_ID_PROCESSING_TIME);
                SearchDoc sd;

                switch (contextMessage.getStoreName()) {
                    case "C2C":
                        C2CConsumer c2CConsumer = (C2CConsumer) SpringContext.getApplicationContext().getBean("c2CConsumer");

                        sd = c2CConsumer.process(contextMessage.getJsonDocument());
                        break;

                    default:
                        DEBUGLOGGER.info("Processing {}", id);
                        sd = process(contextMessage);

                }

                if (sd == null) {
                    continue;
                }

                searchDocs.add(sd);
                ids.add(sd.getId());
                if (searchDocs.size() >= docBatchSize) {
                    Archive archive = buildArchive(searchDocs, ids);
                    docQueue.add(archive);
                    searchDocs.clear();
                    ids.clear();
                }

                metricManager.incrementCounter(Metrics.PROCESSED_ID_COUNTER);

            } catch (Exception e) {
                LOGGER.error("Exception in " + Thread.currentThread().getName() + ". Product: " + id + ". ", e);
                DEBUGLOGGER.info("Product dropped due to exception: {}", id);
                metricManager.incrementCounter(Metrics.DOCBUILDING_EXCEPTIONS_COUNTER);
            } finally {
                metricManager.stopTiming(timerContext);
            }

        }
        LOGGER.info("Translate Queue Size: " + translateQueue.size() + " Bucket Export? " + ProcessManager.bucketExportComplete.get());
        if (!searchDocs.isEmpty() && CollectionUtils.isNotEmpty(ids)) {
            Archive archive = buildArchive(searchDocs, ids);
            docQueue.add(archive);
            searchDocs.clear();
        }
        return threadId;
    }

    private Archive buildArchive(List<SearchDoc> searchDocs, List<String> ids) {
        Archive archive = new Archive();
        archive.setSearchDocuments(new ArrayList<>(searchDocs));
        archive.setIds(new ArrayList<>(ids));
        return archive;
    }

    private SearchDoc process(ContextMessage context) throws SearchCommonException {

        SearchDoc sd = null;

        if (isSkippable(context)) {
            return sd;
        }

        /*
         * Processed IDs "false" denote the products picked up for processing but not processed completely yet.
		 */
        processedIds.put(context.getKey(), false);
        WorkingDocument wd = workingDocumentUtility.prepareWorkingDocument(context);

		/*
         * Extract GB Content first to determine the rest of the flow for product
    	 */
        wd = contentExtractComponent.process(wd, context);
        if (wd.getDecision().isRejected()) {
            processedIds.put(context.getKey(), true);
            metricManager.incrementCounter(Metrics.REJECT_ID_COUNTER);
            return sd;
        }

        String catentrySubType = wd.getExtracts().getContentExtract().getCatentrySubType();
        Classification classification = factory.classify(catentrySubType);
        wd = classification.extract(wd, context);

        /**
         If we are running in the DRYRUN mode, just log the results without indexing the document
         */
        if (MessageType.getMessageType(context.getMessageId()) == MessageType.DRYRUN) {
            logValidationResults(wd);
            return sd; //Returning null is part of the contract
        }

        if (wd.getDecision().isRejected()) {
            processedIds.put(context.getKey(), true);
            metricManager.incrementCounter(Metrics.REJECT_ID_COUNTER);
            return sd;
        }

        SearchDocBuilder sb = classification.build(wd, context);

        boolean removeSearsPRFields = BooleanUtils.toBoolean(PropertiesLoader.getProperty(GlobalConstants.FEAT_REMOVE_SEARSPR_FIELDS));
        if (removeSearsPRFields && Stores.SEARSPR.matches(context.getStoreName())) {
            sb = searsPRTransformation.removeFields(sb);
        }
        sd = sb.build(context);

        /*
         * Processed IDs "true" denote the products that have been successfully processed.
		 */
        processedIds.put(context.getKey(), true);
        return sd;

    }

    /**
     * Logs the ValidationResults
     *
     * @param wd
     */
    private void logValidationResults(WorkingDocument wd) {
        StringBuilder sb = new StringBuilder();
        sb.append(wd.getExtracts().getSsin());
        sb.append("|");
        sb.append(getRuleResultsAsString(wd));
        LOGGER.info(sb.toString());
    }

    /**
     * Constructs a string of the validation results
     *
     * @param wd
     * @return
     */
    private String getRuleResultsAsString(WorkingDocument wd) {
        StringBuilder sb = new StringBuilder();
        Set<String> rules = wd.getDecision().getValidationResults().getDataMap().keySet();
        for (String rule : rules) {
            sb.append(rule + "::");
            sb.append(wd.getDecision().getValidationResults().getDataMap().get(rule).toString() + "::");
        }
        return sb.toString();
    }

}
