package com.shc.ecom.search.jmx;

public interface SystemStatsMBean {

    int getServiceResponseTime();

    String getSkippedIndexedIDs();

    String getSkippedProcessedIDs();

    String getProcessedIDs();

}
