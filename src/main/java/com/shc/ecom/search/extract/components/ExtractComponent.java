package com.shc.ecom.search.extract.components;

import java.io.Serializable;

import com.shc.common.index.rules.Decision;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.codahale.metrics.Timer;
import com.shc.ecom.search.common.kafka.SearchKafkaProducer;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.util.DecisionUtil;

/**
 * @author rgopala Jul 20, 2015 search-doc-builder
 */
public abstract class ExtractComponent<T> implements Serializable, Validatable<T>, Expirable<T> {

    private static final long serialVersionUID = -7290489493675533872L;

    public static final String INDEX_FAIL_TOPIC_NAME = PropertiesLoader.getProperty(GlobalConstants.INDEX_FAIL_TOPIC_NAME);

    protected static final Logger LOGGER = LoggerFactory.getLogger("debuglog");

    @Autowired
    protected MetricManager metricManager;

    @Autowired
    private SearchKafkaProducer kafkaProducer;


    public WorkingDocument process(WorkingDocument wd, ContextMessage context) {
        Timer.Context timerContext = null;
        T source = null;
        try {
            timerContext = _startTimer();
            source = get(wd, context);
        } finally {
            _stopTimer(timerContext);
        }
        Decision decision = validate(source, wd, context);
        wd.setDecision(decision);

        if (!decision.isRejected()) {
            wd.setExtracts(extract(source, wd, context));
            wd.setExtracts(extractExpirableFields(source, wd));
        } else {
            DecisionUtil.incrementCounterAddToKafka(metricManager, kafkaProducer, decision, decision.getValidationResults().getRejectedRule());
            LOGGER.info("Product dropped: {} {}", wd.getExtracts().getSsin(), wd.getDecision().toString());
        }

        return wd;
    }

    protected abstract T get(WorkingDocument wd, ContextMessage context);

    protected abstract Extracts extract(T source, WorkingDocument wd, ContextMessage context);

    /**
     * Validation is done here for different extracts.
     * @return Decision
     * @param source
     * @param wd
     * @param context
     */
    public Decision validate(T source, WorkingDocument wd, ContextMessage context) {
        return wd.getDecision();
    }

	/* APPLICATION METRICS */

    protected Timer.Context _startTimer() {
        return metricManager.startTiming("Service-" + this.getClass().getSimpleName());
    }

    protected void _stopTimer(Timer.Context timerContext) {
        metricManager.stopTiming(timerContext);
    }

}
