package com.shc.ecom.search.persistence;

import com.shc.common.monitoring.MetricManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Implementation of DataAccessor that allows for reading data from services.
 *
 * Keeping the class very generalized now. Each service has its own implementation.
 * I wanted this class to have a default implementation of reading from a service ( Like we have for FileDataAccessor )
 * but we do not have many service to read from now.
 * ZipDDC  service is a simple get request, LocalAds is much more complex. We may have a service
 * which has some other specification.
 *
 * Created by hdargah
 */
public abstract class ServiceDataAccessor<K> implements DataAccessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDataAccessor.class.getName());

    @Autowired
    protected MetricManager metricManager;

    /**
     * Use this method to save the data of the VO
     *
     * @param dto VO mapped from the json response of the service
     */
    public abstract void save(K dto);

    /**
     * Can be called by overriding the hostname, portno, and path in the subclass
     * Override if logic is complex and necessary
     *
     * @return
     */
    protected URL getURL(String hostname, int portno, String path) {
        URL url = null;
        try {
            url = new URL("http://" + hostname + ":" + portno + path);
        } catch (MalformedURLException e) {
            LOGGER.error("URL formation malformed. URL - " + url);
        }
        return url;
    }

}
