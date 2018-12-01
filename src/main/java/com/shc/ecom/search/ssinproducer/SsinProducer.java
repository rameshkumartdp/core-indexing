package com.shc.ecom.search.ssinproducer;

import com.google.common.util.concurrent.*;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.gb.doc.common.DomainObject;
import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.constants.CatentrySubType;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.exception.ErrorCode;
import com.shc.ecom.search.common.exception.SearchCommonException;
import com.shc.ecom.search.common.greenbox.GBServiceFacade;
import com.shc.ecom.search.common.greenbox.bucket.Buckets;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.messages.MessageProcessor;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.indexer.Arguments;
import com.shc.ecom.search.indexer.ProcessManager;
import com.shc.ecom.search.indexer.SpringContext;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.persistence.Incremental;
import com.shc.ecom.search.persistence.QuickAdd;
import com.shc.ecom.search.persistence.SearchFileReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class SsinProducer {

    private static final int TASK_TERMINATION_WAIT_MINS = 1;
    private static final Logger LOGGER = LoggerFactory.getLogger(SsinProducer.class);
    private static final Logger DEBUGLOGGER = LoggerFactory.getLogger("debuglog");

    @Autowired
    protected MetricManager metricManager;

    @Autowired
    private MessageProcessor messageProcessor;

    @Autowired
    private GBServiceFacade gbServiceFacade;

    private String quickAddFileName = PropertiesLoader.getProperty(GlobalConstants.QUICKADD_FILE_NAME);

    private String incrFileName = PropertiesLoader.getProperty(GlobalConstants.INCR_FILE_NAME);

    private Integer bucketThreadPoolCount = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.BUCKET_THREAD_POOL_COUNT));

    @Autowired
    private SearchFileReader searchFileReader;

    @Autowired
    private QuickAdd quickAdd;

    @Autowired
    private Incremental incremental;

    private int completedThreadsCount = 0;

    public ContextMessage createContextMessage(String message) throws SearchCommonException {
        return messageProcessor.getContextMessage(message);
    }

    public ContextMessage createContextMessage(Arguments args) throws SearchCommonException {
        return messageProcessor.getContextMessage(args);
    }

    public void process(BlockingQueue<String> translateQueue, Arguments args) throws SearchCommonException {

        ContextMessage contextMessage = createContextMessage(args);
        addMessageToQueue(translateQueue, contextMessage);
    }

    private void addMessageToQueue(BlockingQueue<String> translateQueue,
                                   ContextMessage contextMessage) throws SearchCommonException {
        String runMode = contextMessage.getRunMode();

        switch (runMode) {
            case "FULL":
            case "GROUPED_NV":
                populateQueue(translateQueue, contextMessage);
                break;
            case "INCR":
                contextMessage.setDuration(getDuration(contextMessage.getTimestamp()));
                populateQueue(translateQueue, contextMessage);
                break;
            case "QADD":
                populateQueueFromFile(translateQueue, contextMessage);
                setBucketExportComplete();
                break;
            case "SSIN":
            case "DRYRUN":
                translateQueue.addAll(getSsinFromId(contextMessage));
                setBucketExportComplete();
                break;
            default:
                throw new SearchCommonException(ErrorCode.INVALID_INPUT_MESSAGE, "Run Mode not valid");
        }
    }

    private void populateQueueFromFile(BlockingQueue<String> translateQueue, ContextMessage contextMessage) {
        List<String> tempSsinList = getSsinsFromFile();
        for (String ssin : tempSsinList) {
            ContextMessage context = new ContextMessage();
            context.setMessageId("SSIN");
            context.setPid(ssin);
            context.setOfferId(contextMessage.getOfferId());
            context.setStoreName(contextMessage.getStoreName());
            context.setRunMode(contextMessage.getRunMode());
            context.setTimestamp(contextMessage.getTimestamp());
            context.setCurrentServer(contextMessage.getCurrentServer());
            translateQueue.addAll(getSsinFromId(context));
        }
    }


    public List<String> getSsinFromId(ContextMessage contextMessage) {
        String messageId = contextMessage.getMessageId();
        String id = contextMessage.getPid();

        List<String> ssinList = new ArrayList<>();
        try {
            switch (messageId) {
                case "SSIN":
                case "DRYRUN":
                    DomainObject domainObject = gbServiceFacade.getDomainDoc(id);
                    List<String> typesList = new ArrayList<>();
                    if (domainObject.get_ft() != null) {
                        typesList = domainObject.get_ft().getCatentrySubType();
                    }

                    switch (CatentrySubType.findCatentrySubType(typesList)) {
                        case NON_VARIATION:
                        case NON_VARIATION_UVD:
                            List<String> offerIds = filterDuplicateOfferIds(gbServiceFacade.getOfferIdsByAltKey(id), contextMessage);
                            for (String offerId : offerIds) {
                                switch (messageId) {
                                    case "DRYRUN":
                                        ssinList.add(messageProcessor.constructSSINAndOfferMessageForDryRun(id, offerId, contextMessage));
                                        break;
                                    case "SSIN":
                                        ssinList.add(messageProcessor.constructSSINAndOfferMessage(id, offerId, contextMessage));
                                        break;
                                }
                            }
                            break;
                        default:
                            switch (messageId) {
                                case "DRYRUN":
                                    ssinList.add(messageProcessor.constructSSINAndOfferMessageForDryRun(id, StringUtils.EMPTY, contextMessage));
                                    break;
                                case "SSIN":
                                    ssinList.add(messageProcessor.constructSSINAndOfferMessage(id, StringUtils.EMPTY, contextMessage));
                                    break;
                            }
                    }
                    break;
                case "OFFERID":
                    List<String> tempSsinList = gbServiceFacade.getSsinList(new ArrayList<>(Arrays.asList(id)));
                    for (String ssin : tempSsinList) {
                        ssinList.add("SSIN" + "|" + ssin + "|" + contextMessage.getStoreName() + "|" + contextMessage.getRunMode() + "|" + contextMessage.getCurrentServer());
                    }
                    break;
            }
        } catch (Exception tw) {
            metricManager.incrementCounter("Exceptions-green.prod.global.s.com_"+contextMessage.getStoreName()+"-MISC");
            LOGGER.error("Could not get ssin for the following message - " + contextMessage.toString(), tw);
        }
        return ssinList;
    }

    /**
     * This method prevents indexing the same offer for both sears and FBM QADD for NV
     * This was created as part of search relevancy testing
     * @param offerIds
     * @return
     */
    private List<String> filterDuplicateOfferIds(List<String> offerIds, ContextMessage context) {
        return offerIds.stream()
                .filter(offerId -> filterOffer(offerId, context))
                .collect(Collectors.toList());
    }

    private boolean filterOffer(String offerId, ContextMessage context) {
        Offer offer = gbServiceFacade.getOfferDoc(offerId);
        if (offer == null) {
            return false;
        }
        boolean isDSS = StringUtils.equals(offer.getClassifications().getIsMpPgmType(), "DSS");
        /** There is an edge case for DSS items that are sold by Sears but are actually FBM items
         *  If the offer is FBM, and isSoldBy Sears and is a DSS then it is an actual FBM offer which needs to be indexed
         *  If not dss then skip
         *
         *  Other than that, if it sold by Kmart, just skip it
         * **/

        return !(Stores.FBM.matches(context.getStoreName())
                && ((StringUtils.equalsIgnoreCase(offer.getFfm().getSoldBy(), Stores.SEARS.getStoreName()) && !isDSS)
                || StringUtils.equalsIgnoreCase(offer.getFfm().getSoldBy(), Stores.KMART.getStoreName())));

    }
    private List<String> getSsinsFromFile() {
        try {
            searchFileReader.readResource(quickAddFileName);
            //searchFileReader.readAsResourceStream(quickAddFileName);
        } catch (IOException e) {
            LOGGER.error("ERROR reading the file - " + quickAddFileName, e);
        }
        return new ArrayList<>(quickAdd.getSsins());
    }

    private void populateQueue(BlockingQueue<String> translateQueue, ContextMessage contextMessage) {
        final List<Runnable> producerThreads;

        switch (contextMessage.getStoreName()) {
            case "C2C":
                producerThreads = createFileReaderThreads(translateQueue, contextMessage);
                break;

            default:
                producerThreads = createProducerThreads(translateQueue, contextMessage);
        }
        submitThreads(producerThreads);
    }

    private FutureCallback<Object> callback(
            final List<Runnable> bucketWorkerThreads,
            final ListeningExecutorService listeningExecutorService) {

        return new FutureCallback<Object>() {

            @Override
            public void onSuccess(Object arg0) {
                completedThreadsCount++;
                LOGGER.info("Remaining bucket threads: " + (bucketWorkerThreads.size() - completedThreadsCount));
                if (completedThreadsCount == bucketWorkerThreads.size()) {
                    shutdown(listeningExecutorService);
                    setBucketExportComplete();
                    LOGGER.info("Bucket Export Complete");
                    DEBUGLOGGER.info("Bucket Export Complete");
                }
            }

            private void shutdown(final ListeningExecutorService listeningExecutorService) {
                listeningExecutorService.shutdown();
                try {
                    LOGGER.info("Awaiting " + TASK_TERMINATION_WAIT_MINS + " " + TimeUnit.MINUTES.name() + " for producer tasks to complete.");
                    listeningExecutorService.awaitTermination(TASK_TERMINATION_WAIT_MINS, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    LOGGER.error("Interrupted while awaiting for producer tasks to complete. ", e);
                }
                listeningExecutorService.shutdownNow();
            }

            @Override
            public void onFailure(Throwable tw) {
                LOGGER.error("Failure of ssinProducer callback ", tw);
                // HANDLE FAILURE
            }
        };
    }

    /**
     * Even when there are no bucket export involved, like SSIN and QADD mode,
     * we conventionally call completion of adding SSINs to translate queue
     * using this method.
     */
    private void setBucketExportComplete() {
        ProcessManager.bucketExportComplete.set(true);
        metricManager.incrementCounter(Metrics.STATUS_BUCKET_EXPORT_COMPLETE);
    }

    private int getCorePoolSize(int size) {
        if (size <= 0) {
            return 1;
        }
        if (size <= bucketThreadPoolCount) {
            return size;
        }
        return bucketThreadPoolCount;
    }

    private void submitThreads(List<Runnable> bucketWorkerThreads) {

        final ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(bucketWorkerThreads.size()));

        for (Runnable bucketWorkerThread : bucketWorkerThreads) {
            ListenableFuture<?> future = listeningExecutorService.submit(bucketWorkerThread);
            Futures.addCallback(future, callback(bucketWorkerThreads, listeningExecutorService));
        }

    }

    private List<Runnable> createProducerThreads(BlockingQueue<String> translateQueue, ContextMessage contextMessage) {
        List<Runnable> bucketWorkerThreads = new ArrayList<>();
        Buckets buckets = gbServiceFacade.getBuckets();
        int totalBuckets = buckets.getEnd() - buckets.getStart() + 1;
        int corePoolSize = getCorePoolSize(totalBuckets);
        int end = 0;

        // Divide buckets approximately equally among the threads
        int start = buckets.getStart();
        int range = totalBuckets / corePoolSize;
        for (int i = 0; i < corePoolSize; i++) {
            end = start + range - 1;
            BucketWorkerThread bucketWorkerThread = (BucketWorkerThread) SpringContext.getApplicationContext().getBean("bucketWorkerThread", translateQueue, contextMessage, start, end);
            bucketWorkerThreads.add(bucketWorkerThread);
            start = end + 1;
        }
        end = buckets.getEnd();

        // Assign the remainder buckets to a separate thread. This accounts for the extra thread.
        BucketWorkerThread remainingBucketsWorkerThread = (BucketWorkerThread) SpringContext.getApplicationContext().getBean("bucketWorkerThread", translateQueue, contextMessage, start, end);
        bucketWorkerThreads.add(remainingBucketsWorkerThread);
        return bucketWorkerThreads;

    }

    private List<Runnable> createFileReaderThreads(BlockingQueue<String> translateQueue, ContextMessage contextMessage) {
        List<Runnable> fileReaderThreads = new ArrayList<>();
        ProductFileReaderThread productFileReaderThread = (ProductFileReaderThread) SpringContext.getApplicationContext().getBean("productFileReaderThread", translateQueue, contextMessage);
        fileReaderThreads.add(productFileReaderThread);
        return fileReaderThreads;
    }

    private Duration getDuration(String timestamp) {
        Duration duration = new Duration();
        try {
            searchFileReader.readResource(incrFileName);
            //searchFileReader.readAsResourceStream(incrFileName);
        } catch (IOException e) {
            LOGGER.error("ERROR reading incremental file for startTime. Falling back to delta based incremental. ", e);
            LOGGER.info("Incremental span (delta) in hours: " + Incremental.TIME_DELTA_IN_HOURS);
        }
        long startTime = (incremental.getStartTime() != 0) ? incremental.getStartTime() : incremental.subtractDeltaHours(Long.parseLong(timestamp));
        long endTime = Long.parseLong(timestamp);
        duration.setFromEpoch(startTime);
        duration.setToEpoch(endTime);
        LOGGER.info("Incremental start-time epoch: " + startTime + " (" + incremental.getUTCTime(startTime) + "), end-time epoch: " + endTime + " (" + incremental.getUTCTime(endTime) + ").");
        return duration;
    }

}
