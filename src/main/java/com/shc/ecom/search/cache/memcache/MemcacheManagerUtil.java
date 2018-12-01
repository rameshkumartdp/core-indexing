package com.shc.ecom.search.cache.memcache;

import com.shc.common.memcache.MemcachedClientInitializer;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.search.config.GlobalConstants;
import com.shc.ecom.search.jmx.Metrics;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPOutputStream;


@Component
public class MemcacheManagerUtil {

    private static final String MEMCACHE_SERVER_LIST = PropertiesLoader.getProperty(GlobalConstants.MEMCACHE_SERVER_LIST);
    private static final boolean POST_TO_MEMCACHE = StringUtils.equalsIgnoreCase(PropertiesLoader.getProperty(GlobalConstants.ENABLE_POST_JSON_FILES_TO_MEMCACHE), "true");
    private MemcachedClient memcachedClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(MemcacheManagerUtil.class);

    @Autowired
    private MetricManager metricManager;

    public MemcacheManagerUtil() {
        memcachedClient = MemcachedClientInitializer.getInstance(MEMCACHE_SERVER_LIST);
    }

    public void sendJsonToMemcacheTimed(String key, String value) {
        if (POST_TO_MEMCACHE) {
            String compressedString = compress(value);
            metricManager.timed(Metrics.TIMER_POST_JSON_TO_MEMCACHE, () -> this.put(key, 0, compressedString));
        }
    }

    public void put(String key, int ttl, Object value) {
        if (memcachedClient == null || value == null) {
            metricManager.incrementCounter("Counter-NullValueNotPushedToMemcache");
            return;
        }
        OperationFuture<Boolean> result = null;
        try {
            result = memcachedClient.set(key, ttl, value);
        } catch (IllegalStateException ex) {
            LOGGER.error("id:" + key + " MemcacheQueueFullException\n" + ex);
            metricManager.incrementCounter("Counter-MemcacheIdsIllegalStateException");
            return;
        } catch (Exception e) {
            LOGGER.error("id:" + key + " MemcacheException\n" + e);
            metricManager.incrementCounter("Counter-MemcachePushException");
            return;
        }
        trackFuture(result,key);
    }

    public void trackFuture(OperationFuture<Boolean> future,String id){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit((Runnable) () -> {
            try {
                if(future != null){
                    if(future.get()){
                        metricManager.incrementCounter("Counter-MemcachedIds");
                    } else{
                        LOGGER.error("id: " + id + "Memcache Return Value False");
                        metricManager.incrementCounter("Counter-MemcachePushFalse");
                    }
                }
            } catch (InterruptedException e) {
                LOGGER.error("id: " + id + "InterruptedException during Memcache Push\n" + e);
                metricManager.incrementCounter("Counter-MemcachePushException");
            } catch (ExecutionException e) {
                LOGGER.error("id: " + id + "ExecutionException during Memcache Push\n" + e);
                metricManager.incrementCounter("Counter-MemcachePushException");
            }
        });
        executorService.shutdown();
    }




    public static String compress(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        String result = "";
        GZIPOutputStream gzipOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(str.getBytes(StandardCharsets.UTF_8));
        } catch(UnsupportedEncodingException ex){
            LOGGER.error("UnsupportedEncodingException while compressing docs and pushing to Memcache", ex);
        } catch(IOException ex){
            LOGGER.error("IOException while encoding docs and pushing to Memcache", ex);
        } finally {
            IOUtils.closeQuietly(gzipOutputStream);
            IOUtils.closeQuietly(byteArrayOutputStream);
        }
        result = Base64.encodeBase64String(byteArrayOutputStream.toByteArray());
        return result;
    }

}
