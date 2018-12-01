package com.shc.ecom.search.jmx;

/**
 * @author rgopala
 */
public class Timers {
    private String min;

    private String max;

    private String count;

    private String fifteenMinuteRate;

    private String meanRate;

    private String name;

    private String oneMinuteRate;

    private String mean;

    private String fiveMinuteRate;

    private String stdDev;

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getFifteenMinuteRate() {
        return fifteenMinuteRate;
    }

    public void setFifteenMinuteRate(String fifteenMinuteRate) {
        this.fifteenMinuteRate = fifteenMinuteRate;
    }

    public String getMeanRate() {
        return meanRate;
    }

    public void setMeanRate(String meanRate) {
        this.meanRate = meanRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOneMinuteRate() {
        return oneMinuteRate;
    }

    public void setOneMinuteRate(String oneMinuteRate) {
        this.oneMinuteRate = oneMinuteRate;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getFiveMinuteRate() {
        return fiveMinuteRate;
    }

    public void setFiveMinuteRate(String fiveMinuteRate) {
        this.fiveMinuteRate = fiveMinuteRate;
    }

    public String getStdDev() {
        return stdDev;
    }

    public void setStdDev(String stdDev) {
        this.stdDev = stdDev;
    }

    @Override
    public String toString() {
        return "\n" + name + " [mean (avg) = " + mean + ", min = " + min + ", max = " + max + ", count = " + count + ", meanRate = " + meanRate + ", oneMinuteRate = " + oneMinuteRate + ", fiveMinuteRate = " + fiveMinuteRate + ", fifteenMinuteRate = " + fifteenMinuteRate + ", stdDev = " + stdDev + "]";
    }
}