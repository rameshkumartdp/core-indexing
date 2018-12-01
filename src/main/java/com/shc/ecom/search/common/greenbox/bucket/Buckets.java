/**
 *
 */
package com.shc.ecom.search.common.greenbox.bucket;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author rgopala
 *         Jul 17, 2015 ssin-producer
 */

@Component
public class Buckets implements Serializable {

    private static final long serialVersionUID = 1943142918333537412L;

    private int start;

    private int end;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isEmpty() {
        return this.start == 0 && this.end == 0;
    }
}
