package com.shc.ecom.search.http;

import com.codahale.metrics.Timer;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.indexer.SpringContext;
import com.shc.ecom.search.jmx.Metrics;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;


/**
 * @author pchauha
 */
public class ApacheHttpClient implements HttpClient {


	private static final Logger LOGGER = LoggerFactory.getLogger(ApacheHttpClient.class);

    private static CloseableHttpClient httpClient;

    private static final String HEADER_AUTH_ID = "AuthID";

    private static int RETRY_ATTEMPTS = 3;
    private static int maxConnections = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.MAX_HTTP_CONNECTIONS));
    private static int maxConnectionsPerRoute = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.MAX_HTTP_CONNECTIONS_PER_ROUTE));
    @SuppressWarnings("unused")
    private String responseContentType = PropertiesLoader.getProperty(GlobalConstants.RESPONSE_CONTENT_TYPE);
    private int socketTimeout = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.REQUEST_SOCKET_TIMEOUT));
    private String requestAccepts = PropertiesLoader.getProperty(GlobalConstants.REQUEST_ACCEPTS);
    private int connectionTimeout = Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.REQUEST_CONNECTION_TIMEOUT));
    private String requestContentType = PropertiesLoader.getProperty(GlobalConstants.REQUEST_CONTENT_TYPE);
    private String responseUserAgent = PropertiesLoader.getProperty(GlobalConstants.RESPONSE_USER_AGENT);
    private String authId = PropertiesLoader.getProperty(GlobalConstants.PRICE_GRID_AUTHID);
    private MetricManager metricManager = (MetricManager) SpringContext.getApplicationContext().getBean("metricsManager");

    public ApacheHttpClient() {
        httpClient = getHttpClientInstance();
    }

    private static CloseableHttpClient getHttpClientInstance() {
        if (httpClient == null) {
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
            poolingHttpClientConnectionManager.setMaxTotal(maxConnections);
            poolingHttpClientConnectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
            httpClient = HttpClients.custom().setRetryHandler(new RetryHandler()).setServiceUnavailableRetryStrategy(new ServiceRetryHandler()).setConnectionManager(poolingHttpClientConnectionManager).build();
        }
        return httpClient;
    }

    private String parseResponse(HttpResponse response) throws IOException {
        String result = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        result = sb.toString();
        reader.close();
        return result;
    }

    public String httpGet(URL url) {
        HttpGet httpGet = new HttpGet(url.toString());
        httpGet.setHeader(HttpHeaders.ACCEPT, requestAccepts);
        httpGet.setHeader(HttpHeaders.CONTENT_TYPE, requestContentType);
        httpGet.addHeader(HttpHeaders.USER_AGENT, responseUserAgent);
        httpGet.addHeader(HEADER_AUTH_ID, authId);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout).setSocketTimeout(socketTimeout).build();
        httpGet.setConfig(requestConfig);

        String stringResponse = null;
        metricManager.incrementCounter(Metrics.COUNTER_PREFIX + url.getHost());
        Timer.Context timerContext = metricManager.startTiming(Metrics.REQUEST + "-" + url.getHost());
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                metricManager.incrementCounter("Exceptions-" + url.getHost() + "-HTTP_" + httpResponse.getStatusLine().getStatusCode());
                LOGGER.debug("GET returned: " + httpResponse.getStatusLine().getStatusCode() + " for URL: " + url);
            }
            stringResponse = parseResponse(httpResponse);
        } catch (IOException e) {
            LOGGER.error("Error processing the request for the URL: " + url, e);
        } finally {
            metricManager.stopTiming(timerContext);
        }
        return stringResponse;
    }

    // TODO: Need to check how different formats should be handled
    public <T> String httpPost(URL url, T postData) {
        HttpPost httpPost = new HttpPost(url.toString());
        httpPost.setHeader(HTTP.CONTENT_TYPE, requestContentType);
        httpPost.addHeader(HttpHeaders.USER_AGENT, responseUserAgent);

        HttpEntity httpEntity = null;
        if (postData instanceof String) {
            httpEntity = EntityBuilder.create().setText((String) postData).build();
        }
        httpPost.setEntity(httpEntity);

        HttpResponse httpResponse;
        String stringRespone = null;
        metricManager.incrementCounter(Metrics.COUNTER_PREFIX + url.getHost());
        Timer.Context timerContext = metricManager.startTiming(Metrics.REQUEST + "-" + url.getHost());
        try {
            httpResponse = httpClient.execute(httpPost);
            stringRespone = parseResponse(httpResponse);
        } catch (ClientProtocolException e) {
            LOGGER.error("Response containg no content for the URL: " + url, e);
        } catch (IOException e) {
            LOGGER.error("Error processing the request for the URL: " + url, e);
        } finally {
            metricManager.stopTiming(timerContext);
        }
        return stringRespone;
    }

    private static class RetryHandler implements HttpRequestRetryHandler {

        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if (executionCount > RETRY_ATTEMPTS) {
                return false;
            }

            if (exception instanceof UnknownHostException) {
                return false;
            }

            // TODO: Get the info from context to log it.
            LOGGER.info("Retrying connection attempt. Retry count: " + executionCount);
            return true;
        }
    }

    /**
     * Since all the external services indexer points to are idempotent, this is safe to use at http transport level.
     *
     * @author rgopala
     */
    private static class ServiceRetryHandler implements ServiceUnavailableRetryStrategy {

        @Override
        public boolean retryRequest(HttpResponse response, int executionCount,
                                    HttpContext context) {
            int statusCode = response.getStatusLine().getStatusCode();

            // Do not retry for successful attempts (200) and not-found errors (404).
            if (!(statusCode == 200 || statusCode == 404) && executionCount < RETRY_ATTEMPTS) {
                LOGGER.info("Retrying service " + context.getAttribute(HttpCoreContext.HTTP_REQUEST) + " due to " + response.getStatusLine().getStatusCode() + " response. Retry count: " + executionCount);
                return true;
            }
            return false;
        }

        @Override
        public long getRetryInterval() {
            return 0;
        }

    }


}
