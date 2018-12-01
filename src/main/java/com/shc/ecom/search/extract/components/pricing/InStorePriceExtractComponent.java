package com.shc.ecom.search.extract.components.pricing;


import com.shc.common.misc.PropertiesLoader;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.indexer.SpringContext;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author rgopala Jul 29, 2015 search-doc-builder
 *         Store Pricing For Sears and Kmart
 *         <p>
 *         To provide store specific pricing for each product when shop sears or SMS page sends a request to search.
 *         To sort the results on price sort selection based on store price on store page for shop sears and SMS.
 *         To filter the results on store price where price range is selected.
 */
public class InStorePriceExtractComponent extends ExtractComponent<Map<String, Price>> {

    private static final long serialVersionUID = 8561780709824676180L;
    private static final Logger LOGGER = LoggerFactory.getLogger(InStorePriceExtractComponent.class.getName());

    private static ExecutorService executorService = Executors.newFixedThreadPool(Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.STOREPRICE_THREAD_POOL_COUNT)));

    @Autowired
    @Qualifier("priceAPI")
    private PricingService pricingService;

    @Override
    protected Map<String, Price> get(WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();
        List<String> storeUnits = extracts.getPasExtract().getAvailableStores();
        String offerId = extracts.getOfferExtract().getOfferId();

        /**
         * For Kmart and MyGofer remove the padded store-units '0's at the beginning.
         */
        if (Stores.KMART.matches(context.getStoreName()) || Stores.MYGOFER.matches(context.getStoreName())) {
            // If KSN is available, use KSN for in-store price.
            List<String> ksns = extracts.getOfferExtract().getKsn();
            offerId = CollectionUtils.isNotEmpty(ksns) ? ksns.get(0) : offerId;

            for (int i = 0; i < storeUnits.size(); i++) {
                String storeUnit = storeUnits.get(i).replaceFirst("^0+(?!$)", "");
                if (storeUnit.length() < 4) {
                    storeUnit = String.format("%0" + (4 - storeUnit.length()) + "d%s", 0, storeUnit);
                }
                storeUnits.set(i, storeUnit);
            }
        }

        /**
         * Get InStore Price
         */
        return getStorePrice(storeUnits, offerId);
    }

    /**
     * Creates subtask for getting storePrice
     *
     * @param storeUnits
     * @param offerId
     * @return
     */
    private Map<String, Price> getStorePrice(List<String> storeUnits, String offerId) {
        Map<String, Price> storePrice = new HashMap<>();
        if (CollectionUtils.isEmpty(storeUnits)) {
            return storePrice;
        }
        List<StorePriceThread> storePriceThreads = new ArrayList<>();
        int threadId = 0;
        for (String storeUnit : storeUnits) {
            storePriceThreads.add((StorePriceThread) SpringContext.getApplicationContext().getBean("storePriceThread", "StorePriceThread-" + (threadId++), offerId, storeUnit));
        }

        List<Future<Price>> futures = new ArrayList<>();
        for (StorePriceThread storePriceThread : storePriceThreads) {
            futures.add(executorService.submit(storePriceThread));
        }

        int index = -1;
        for (Future<Price> future : futures) {
            try {
                index++;
                Price priceForAStore = future.get(2000, TimeUnit.MILLISECONDS);
                if (priceForAStore != null) {
                    storePrice.put(storeUnits.get(index), priceForAStore);
                }

            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                LOGGER.error("Store Price time out. Offer: ", e);
            }
        }
        return storePrice;
    }

    @Override
    protected Extracts extract(Map<String, Price> storePrice, WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();
        extracts.getPriceExtract().setStorePrice(storePrice);
        return extracts;
    }

    @PreDestroy
    private void cleanUp() {
        try {
            executorService.shutdown();
            executorService.shutdownNow();
        } catch (Exception e) {
            LOGGER.error("Unable to close store price executor service", e);
        }
    }
}
