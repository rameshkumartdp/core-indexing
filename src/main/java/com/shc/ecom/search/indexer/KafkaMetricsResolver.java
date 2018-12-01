package com.shc.ecom.search.indexer;

import java.util.List;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Meter;
import com.shc.ecom.search.jmx.Counters;
import com.shc.ecom.search.jmx.Timers;

/**
 * Created by tgulati on 12/27/16.
 */
public class KafkaMetricsResolver {

    private List<Timers> timers;

    private List<Counters> counters;

    private List<Meter> meters;

    private List<Gauge> gauges;

    public List<Gauge> getGauges() {
        return gauges;
    }

    public void setGauges(List<Gauge> gauges) {
        this.gauges = gauges;
    }

    public List<Meter> getMeters() {
        return meters;
    }

    public void setMeters(List<Meter> meters) {
        this.meters = meters;
    }

    public List<Timers> getTimers() {
        return timers;
    }

    public void setTimers(List<Timers> timers) {
        this.timers = timers;
    }

    public List<Counters> getCounters() {
        return counters;
    }

    public void setCounters(List<Counters> counters) {
        this.counters = counters;
    }
}
