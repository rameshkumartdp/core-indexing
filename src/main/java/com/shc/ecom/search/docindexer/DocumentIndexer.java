package com.shc.ecom.search.docindexer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shc.ecom.search.common.exception.SearchCommonException;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.messages.MessageProcessor;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.docbuilder.Archive;
import com.shc.ecom.search.indexer.Arguments;
import com.shc.ecom.search.indexer.SolrServerInitializer;
import com.shc.ecom.search.indexer.SpringContext;
import com.shc.ecom.search.jmx.Metrics;

/**
 * @author rgopala
 */


@Component
public class DocumentIndexer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentIndexer.class.getName());
    private static final Logger DEBUGLOGGER = LoggerFactory.getLogger("debuglog");
    protected static final String JSON_PATH = PropertiesLoader.getProperty(GlobalConstants.JSONPATH);
    protected static final String INVERTED_INDEX_DIR = "inverted-index/";
    protected static final String GROUPBY_PATH = PropertiesLoader.getProperty(GlobalConstants.GROUPBYPATH);

    private static final int TASK_TERMINATION_WAIT_MINS = 1;
    @Autowired
    protected MetricManager metricManager;
    private Integer solrIndexerThreadPoolCount = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.INDEXER_THREAD_POOL_COUNT));
    @Autowired
    private MessageProcessor messageProcessor;

    private int getCorePoolSize() {
        return solrIndexerThreadPoolCount;
    }

    public void process(BlockingQueue<Archive> docQueue, Arguments args) throws SearchCommonException {

        ContextMessage contextMessage = createContextMessage(args);

        /** Create/spawn threads to extract data and build search document **/
        int corePoolSize = getCorePoolSize();
        ExecutorService executorService = Executors.newFixedThreadPool(corePoolSize);
        List<Future<String>> futures = new ArrayList<>();
        prepareInvertedIndexDir();
        for (int i = 0; i < corePoolSize; i++) {
            IndexerThread indexerThread = (IndexerThread) SpringContext.getApplicationContext().getBean("indexerThread");
            indexerThread.configure("Indexer-" + i, docQueue, contextMessage);
            Future<String> future = executorService.submit(indexerThread);
            futures.add(future);
        }

        for (Future<String> future : futures) {
            try {
                LOGGER.info("{} complete", future.get());
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("Interrupted while awaiting for indexer tasks to complete. ", e);
            }
        }

        executorService.shutdown();
        LOGGER.info("Indexing Complete");
        DEBUGLOGGER.info("Indexing Complete");

        try {
            LOGGER.info("Awaiting " + TASK_TERMINATION_WAIT_MINS + " " + TimeUnit.MINUTES.name() + " for indexer tasks to complete.");
            executorService.awaitTermination(TASK_TERMINATION_WAIT_MINS, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted while awaiting for indexer tasks to complete. ", e);
        }
        executorService.shutdownNow();

        try {
            LOGGER.info("Closing Solr Client.");
            SolrServerInitializer.INSTANCE.getSolrClient().close();
        } catch (IOException e) {
            LOGGER.error("Unable to close Solr Client. ", e);
        }

        metricManager.incrementCounter(Metrics.STATUS_INDEXING_COMPLETE);

    }

    private boolean prepareInvertedIndexDir() {
        boolean invertedIdxPrepared = false;
        File invertedIdxDir = new File(JSON_PATH + INVERTED_INDEX_DIR);
        File[] listFiles = invertedIdxDir.listFiles();
        if (invertedIdxDir.exists() && listFiles != null) {
            for (int i = 0; i < listFiles.length; i++) {
                invertedIdxPrepared = listFiles[i].delete();
            }
        }
        try {
            Files.createDirectories(Paths.get(invertedIdxDir.getPath()));
        } catch (IOException e) {
            LOGGER.error("Inverted Index Dir IO Error.", e);
        }
        return invertedIdxPrepared;
    }

    public ContextMessage createContextMessage(Arguments args) throws SearchCommonException {
        return messageProcessor.getContextMessage(args);
    }
}
