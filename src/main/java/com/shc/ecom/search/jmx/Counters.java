package com.shc.ecom.search.jmx;

/**
 * @author rgopala
 */
public class Counters {
    private String count;

    private String name;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return " \n" + name + " [count = " + count + "]";
    }
}