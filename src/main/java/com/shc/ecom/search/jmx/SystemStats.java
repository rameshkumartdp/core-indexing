package com.shc.ecom.search.jmx;

import javax.management.*;
import java.util.concurrent.atomic.AtomicLong;

public class SystemStats implements SystemStatsMBean, NotificationBroadcaster {

    public static int serviceResponseTime = 0;

    public static AtomicLong skippedIndexedIDs = new AtomicLong(0);
    public static AtomicLong skippedProcessedIDs = new AtomicLong(0);
    public static AtomicLong processedIDs = new AtomicLong(0);

    @Override
    public int getServiceResponseTime() {
        return serviceResponseTime;
    }

    @Override
    public String getSkippedIndexedIDs() {
        return String.valueOf(skippedIndexedIDs);
    }

    @Override
    public String getSkippedProcessedIDs() {
        return String.valueOf(skippedProcessedIDs);
    }

    @Override
    public String getProcessedIDs() {
        return String.valueOf(processedIDs);
    }

    @Override
    public void addNotificationListener(NotificationListener listener,
                                        NotificationFilter filter, Object handback)
            throws IllegalArgumentException {

    }

    @Override
    public void removeNotificationListener(NotificationListener listener)
            throws ListenerNotFoundException {

    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        return null;
    }

}
