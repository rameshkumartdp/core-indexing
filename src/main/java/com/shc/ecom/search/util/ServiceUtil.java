package com.shc.ecom.search.util;

import com.shc.ecom.rtc.connector.PersistentHttpAdapter;
import com.shc.ecom.search.common.exception.ErrorCode;
import com.shc.ecom.search.common.exception.SearchCommonException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.net.URL;

/**
 * @author pchauha
 */
public class ServiceUtil extends PersistentHttpAdapter implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1265050586863365183L;
    @Autowired
    private PersistentHttpAdapter persistentHttpAdapter;

    public ServiceUtil() throws Exception {
        super();
    }

    // TODO: Need to look into how to apply retry logic
    public <Entity> String getServiceResponse(URL url, Class<Entity> classType) throws SearchCommonException {
        String jsonResponse = persistentHttpAdapter.processGet(url.toString(), url.getHost(), url.getPort());
        if (StringUtils.isEmpty(jsonResponse) || StringUtils.equalsIgnoreCase(jsonResponse, "TIME OUT ERROR")) {
            throw new SearchCommonException(ErrorCode.NO_RESPONSE_FROM_SERVICE, "URL - " + url);
        }
        return jsonResponse;
    }
}
