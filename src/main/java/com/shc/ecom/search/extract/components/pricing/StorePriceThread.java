package com.shc.ecom.search.extract.components.pricing;

import com.codahale.metrics.Timer;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.search.common.exception.ErrorCode;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.wxs.common.exception.SHCWxsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * @author rgopala
 */

@Component
@Scope("prototype")
public class StorePriceThread implements Callable<Price> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorePriceThread.class.getName());
    @Autowired
    protected MetricManager metricManager;
    @SuppressWarnings("unused")
    private String threadId;
    private String offerId;
    private String storeUnit;
    @Autowired
    @Qualifier("priceAPI")
    private PricingService pricingService;

    public StorePriceThread(String threadId, String offerId, String storeUnit) {
        this.threadId = threadId;
        this.offerId = offerId;
        this.storeUnit = storeUnit;
    }

    @Override
    public Price call() throws Exception {
        Price price = null;
        try {
            metricManager.incrementCounter(Metrics.COUNTER_STORE_PRICE_CALLS);
            Timer.Context timerContext = metricManager.startTiming(Metrics.TIMER_STORE_PRICE);
            try {
                price = pricingService.getStorePrice(storeUnit, offerId);
            } finally {
                metricManager.stopTiming(timerContext);
            }
        } catch (Exception | SHCWxsException e) {
            LOGGER.error(ErrorCode.PRICING_ERROR.name() + ": " + e.getMessage());
        }
        return price;
    }

}
