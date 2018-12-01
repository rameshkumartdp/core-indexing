package com.shc.ecom.search.http;

import java.net.URL;

/**
 * @author pchauha
 */
public interface HttpClient {
    String httpGet(URL url);

    <T> String httpPost(URL url, T postData);
}
