package com.shc.ecom.search.jmx;

import java.util.List;

/**
 * @author rgopala
 */
public class Statistics {

    private List<Timers> timers;

    private List<Counters> counters;

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

    @Override
    public String toString() {
        return "\n\nStatistics\n----------\nTimers:\n" + timers + "\nCounters:\n" + counters + "\n";
    }

}
