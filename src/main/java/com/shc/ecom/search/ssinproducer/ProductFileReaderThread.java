package com.shc.ecom.search.ssinproducer;

import com.google.gson.Gson;
import com.shc.common.misc.PropertiesLoader;
import com.shc.common.monitoring.MetricManager;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.jmx.Metrics;
import com.shc.ecom.search.util.ProcessUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

@Component
@Scope("prototype")
public class ProductFileReaderThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductFileReaderThread.class.getName());
    private static final String DOC_PREFIX = "DOC";
    private static final String PIPE_SEPARATOR = "|";
    @Autowired
    MetricManager metricManager;
    private String fileName = PropertiesLoader.getProperty("c2c.text.file");
    private String[] headerTokens;
    private BufferedReader reader;
    private BlockingQueue<String> translateQueue;
    private ContextMessage context;
    @Autowired
    private ProcessUtil processUtil;

    public ProductFileReaderThread(BlockingQueue<String> translateQueue, ContextMessage context) {
        this.context = context;
        this.translateQueue = translateQueue;

        CharsetDecoder decoder = Charset.defaultCharset().newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE);

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)), decoder));
            String headerLine = reader.readLine();
            headerTokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(headerLine, PIPE_SEPARATOR);
        } catch (Exception e) {
            LOGGER.error("ERROR reading the file - " + fileName, e);
            processUtil.exitWithError();
        }
        LOGGER.info("Opened C2C Vendor file");
    }

    public void run() {
        String line;
        while ((line = getNextLine()) != null) {
            String jsonString = processInputString(line);
            if (jsonString == null){
                continue;
            }

            StringBuffer message = new StringBuffer();
            message.append(getMessagePrefix());
            message.append(jsonString);
            message.append(getMessagePostfix());

            translateQueue.add(message.toString());
        }
    }

    private String processInputString(String line) {

        String[] lineTokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, "|");
        if (lineTokens.length != headerTokens.length) {
            LOGGER.info("Could not parse this line . " + line);
            LOGGER.info("Expected number of tokens " + headerTokens.length + " Actual number of tokens " + lineTokens.length);
            metricManager.incrementCounter(Metrics.DOCBUILDING_EXCEPTIONS_COUNTER);
            return null;
        }

        Map<String, String> fieldMap = getFieldMap(headerTokens, lineTokens);

        C2CBean bean = new C2CBean();
        bean.setOpsUuid_s(context.getTimestamp());
        bean.setOpsCurrentServer_i(context.getCurrentServer());
        bean.setCatalogs(fieldMap.get("catalogs"));
        bean.setLevel1Cats(fieldMap.get("level1Cats"));
        bean.setLevel2Cats(fieldMap.get("level2Cats"));
        bean.setLevel3Cats(fieldMap.get("level3Cats"));
        bean.setVendorId(fieldMap.get("vendorId"));
        bean.setvNames(fieldMap.get("vNames"));
        bean.setcNames(fieldMap.get("cNames"));
        bean.setsNames(fieldMap.get("sNames"));
        bean.setUrl(fieldMap.get("url"));
        bean.setId(fieldMap.get("id"));
        bean.setPartnumber(fieldMap.get("partnumber"));
        bean.setName(fieldMap.get("name"));
        bean.setImage(fieldMap.get("image"));
        bean.setBrand(fieldMap.get("brand"));
        bean.setPrice(fieldMap.get("price"));
        bean.setRegPrice(fieldMap.get("regPrice"));
        bean.setPriceRangeInd(fieldMap.get("priceRangeInd"));
        bean.setSale(fieldMap.get("sale"));
        bean.setClearance(fieldMap.get("clearance"));
        bean.setStatus(fieldMap.get("status"));
        bean.setAvailDate(fieldMap.get("availDate"));
        bean.setAvgRating(fieldMap.get("avgRating"));
        bean.setNumReviews(fieldMap.get("numReviews"));
        bean.setItemsSold(fieldMap.get("itemsSold"));
        bean.setProductViews(fieldMap.get("productViews"));
        bean.setRevenue(fieldMap.get("revenue"));
        bean.setConversion(fieldMap.get("conversion"));
        bean.setSearch1(fieldMap.get("search1"));
        bean.setSearch2(fieldMap.get("search2"));
        bean.setSearch3(fieldMap.get("search3"));
        bean.setSearch4(fieldMap.get("search4"));
        bean.setSearch5(fieldMap.get("search5"));
        bean.setSearch6(fieldMap.get("search6"));
        bean.setSearch7(fieldMap.get("search7"));
        bean.setSearch8(fieldMap.get("search8"));
        bean.setSearch9(fieldMap.get("search9"));
        bean.setSearch10(fieldMap.get("search10"));
        bean.setStoreOrigin(fieldMap.get("storeOrigin"));
        bean.setSin(fieldMap.get("sin"));
        bean.setSellerCount(fieldMap.get("sellerCount"));
        bean.setMfpartno(fieldMap.get("mfpartno"));
        bean.setClickUrl(fieldMap.get("clickUrl"));
        bean.setSaveStory(fieldMap.get("saveStory"));
        bean.setCpcClickRate(fieldMap.get("cpcClickRate"));
        bean.setCpcFlag(fieldMap.get("cpcFlag"));
        bean.setNewItemFlag(fieldMap.get("newItemFlag"));
        bean.setDaysOnline(fieldMap.get("daysOnline"));
        bean.setShippingMsg(fieldMap.get("shippingMsg"));
        bean.setCountryGroup(fieldMap.get("countryGroup"));
        bean.setShipVantage(fieldMap.get("shipVantage"));
        bean.setFreeShipping(fieldMap.get("freeShipping"));
        bean.setCountryGroupExists(fieldMap.get("countryGroupExists"));
        bean.setCatConfidence(fieldMap.get("catConfidence"));
        bean.setSellers(fieldMap.get("sellers"));
        bean.setBidValue(fieldMap.get("bidValue"));
        bean.setBrowseBoost(fieldMap.get("browseBoost"));
        bean.setMatureContentFlag(fieldMap.get("matureContentFlag"));
        bean.setAccessories(fieldMap.get("accessories"));
        bean.setBehavioral(fieldMap.get("behavioral"));
        bean.setOffer(fieldMap.get("offer"));
        bean.setImageStatus(fieldMap.get("imageStatus"));
        bean.setCategories(fieldMap.get("categories"));
        bean.setSearchableAttributes(fieldMap.get("searchableAttributes"));
        bean.setCategory(fieldMap.get("category"));
        bean.setLnames(fieldMap.get("lnames"));
        bean.setPrimaryVertical(fieldMap.get("primaryVertical"));
        bean.setPrimaryCategory(fieldMap.get("primaryCategory"));
        bean.setPrimaryHierarchy(fieldMap.get("primaryHierarchy"));
        bean.setNameSearchable(fieldMap.get("nameSearchable"));
        bean.setPrimaryLnames(fieldMap.get("primaryLnames"));
        bean.setXref(fieldMap.get("xref"));

        return new Gson().toJson(bean);
    }

    private String getNextLine() {
        String line = null;
        try {
            line = reader.readLine();
        } catch (Exception e) {
            LOGGER.error("ERROR reading the file - " + fileName, e);
        }
        return line;
    }

    private Map<String, String> getFieldMap(String[] headerTokens, String[] lineTokens) {
        Map<String, String> fieldMap = new HashMap<>();

        for (int i = 0; i != headerTokens.length; i++) {
            String value = lineTokens[i];
            String key = headerTokens[i];

            if (StringUtils.isEmpty(value)) {
                continue;
            }
            fieldMap.put(key, value);
        }
        return fieldMap;
    }

    private String getMessagePrefix() {
        return DOC_PREFIX + PIPE_SEPARATOR;
    }

    private String getMessagePostfix() {
        return PIPE_SEPARATOR + context.getStoreName() + PIPE_SEPARATOR + context.getTimestamp() + PIPE_SEPARATOR + context.getCurrentServer();
    }
}
