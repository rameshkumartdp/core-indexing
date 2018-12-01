package com.shc.ecom.search.jmx;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

/**
 * @author rgopala
 */
public class SystemStatNotificationListener implements NotificationListener, NotificationFilter {

    private static final long serialVersionUID = -1728317092686898114L;

    @Override
    public boolean isNotificationEnabled(Notification notification) {
        return AttributeChangeNotification.class.isAssignableFrom(notification.getClass());
    }

    @Override
    public void handleNotification(Notification notification, Object handback) {
        //EMAIL
    }

}
