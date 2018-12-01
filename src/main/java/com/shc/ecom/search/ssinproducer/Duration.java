package com.shc.ecom.search.ssinproducer;

import java.io.Serializable;

/**
 * Class represents "time" duration for the IDs to be processed by indexer.
 * Basically used in delta/incremental mode which fetches only IDs that were
 * changed (in Content) in the set duration, and indexes them.
 *
 * @author rgopala
 */
public class Duration implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1523720904508029906L;

    private long fromEpoch;

    private long toEpoch;

    /**
     * @return Epoch start time (from time) in seconds.
     */
    public long getFromEpoch() {
        return fromEpoch;
    }

    public void setFromEpoch(long fromEpoch) {
        this.fromEpoch = fromEpoch;
    }

    /**
     * @return Epoch end time (to time) in seconds.
     */
    public long getToEpoch() {
        return toEpoch;
    }

    public void setToEpoch(long toEpoch) {
        this.toEpoch = toEpoch;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Duration [from=")
                .append(fromEpoch)
                .append(" to=")
                .append(toEpoch)
                .append("]");
        return builder.toString();
    }

}
