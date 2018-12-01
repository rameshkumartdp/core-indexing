package com.shc.ecom.search.ssinconsumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.exception.ErrorCode;
import com.shc.ecom.search.common.exception.SearchCommonException;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.messages.MessageProcessor;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.docbuilder.Archive;
import com.shc.ecom.search.extract.components.store.StoresExtractService;
import com.shc.ecom.search.indexer.Arguments;
import com.shc.ecom.search.indexer.ProcessManager;
import com.shc.ecom.search.indexer.SpringContext;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.persistence.SearchFileReader;

import static com.shc.common.misc.GlobalConstants.COMMA;

@Component
public class SsinConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SsinConsumer.class.getName());

    private static final Logger DEBUGLOGGER = LoggerFactory.getLogger("debuglog");

    private static final int TASK_TERMINATION_WAIT_MINS = 1;

    private List<String> files = PropertiesLoader.getPropertyAsList(GlobalConstants.DATAFILES, COMMA);

    private Integer extractorThreadPoolCount = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.EXTRACTOR_THREAD_POOL_COUNT));

    private AtomicInteger completedThreadsCount = new AtomicInteger(0);
    
    @Autowired
    private StoresExtractService storesExtractService;

    @Autowired
    private SearchFileReader searchFileReader;

    @Autowired
    private MessageProcessor messageProcessor;

    @Autowired
    private MetricManager metricManager;

    private int getCorePoolSize(int qSize) {
        return extractorThreadPoolCount;
    }

    public void process(BlockingQueue<String> translateQueue, BlockingQueue<Archive> docQueue, Arguments args) throws SearchCommonException {

        /** Load the data files first **/
        loadPrerequisiteData(files, searchFileReader);
        
        if(Stores.SEARS.matches(args.getStore()) || Stores.KMART.matches(args.getStore())) {        	
        	storesExtractService.loadCrossFormatEligibleStores();
        }
        /** Create/spawn threads to extract data and build search document **/
        int corePoolSize = getCorePoolSize(translateQueue.size());
        final ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(corePoolSize));
        List<Extractor> extractors = new ArrayList<>();
        for (int i = 0; i < corePoolSize; i++) {
            Extractor extractor = (Extractor) SpringContext.getApplicationContext().getBean("extractor");
            extractor.configure("Extractor-" + i, translateQueue, docQueue);
            extractors.add(extractor);
        }

        // Execute them all.
        for (Extractor extractor : extractors) {
            ListenableFuture<String> future = listeningExecutorService.submit(extractor);
            Futures.addCallback(future, callback(extractors, listeningExecutorService));
        }

    }

    /**
     * A future call back to avoid blocking the main thread from executing.  This will notify upon completion of each callable/runnable.
     * The callback on the success is made use to understand the completion of all threads and notify other threads about completion.
     *
     * @param extractors
     * @param listeningExecutorService
     * @return
     */
    private FutureCallback<String> callback(
            final List<Extractor> extractors,
            final ListeningExecutorService listeningExecutorService) {

        return new FutureCallback<String>() {

            @Override
            public void onSuccess(String callable) {
                completedThreadsCount.getAndIncrement();
                LOGGER.info(callable + " complete. Spawn threads: " + extractors.size() + ", completed threads count: " + completedThreadsCount);
                if (completedThreadsCount.get() == extractors.size()) {
                    shutdown(listeningExecutorService);
                    ProcessManager.extractionComplete.set(true);
                    metricManager.incrementCounter(Metrics.STATUS_EXTRACT_COMPLETE);
                    LOGGER.info("Extraction Complete");
                    DEBUGLOGGER.info("Extraction Complete");
                }
            }

            private void shutdown(final ListeningExecutorService listeningExecutorService) {
                listeningExecutorService.shutdown();
                try {
                    LOGGER.info("Awaiting " + TASK_TERMINATION_WAIT_MINS + " " + TimeUnit.MINUTES.name() + " for consumer tasks to complete.");
                    listeningExecutorService.awaitTermination(TASK_TERMINATION_WAIT_MINS, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    LOGGER.error("Interrupted while awaiting for consumer tasks to complete. ", e);
                }
                listeningExecutorService.shutdownNow();
            }

            @Override
            public void onFailure(Throwable tw) {
                LOGGER.error("failure occured while executing ssin consumer thread", tw);
            }
        };
    }

    /**
     * Loads the dataFiles, data can come from files or a service. We will just call it resource
     * Separated this out to reduce duplication in dry-run mode
     */
    public void loadPrerequisiteData(List<String> files, SearchFileReader searchFileReader) throws SearchCommonException {
        for (String resourceName : files) {
            try {
                searchFileReader.readResource(resourceName);
            } catch (IOException e) {
                LOGGER.error("ERROR reading the file - " + resourceName, e);
                throw new SearchCommonException(ErrorCode.FILE_OPEN_FAILURE, resourceName);
            }
        }
    }

    public ContextMessage createContextMessage(String message) throws SearchCommonException {
        return messageProcessor.getContextMessage(message);
    }

    public ContextMessage createContextMessage(Arguments args) throws SearchCommonException {
        return messageProcessor.getContextMessage(args);
    }

}
