package com.shc.ecom.search.extract.components.impressions;

/*
 * Service class to invoke Mongo data store
 *
 * @author Sanjay Chaudhuri
 */

import com.shc.search.MongoDao;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Qualifier("impressionsMongoService")
public class ImpressionsMongoService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private MongoDao mongoDao;

    public void insertImpressionSummary(ProductImpressionSummary impressionSummary) {
        LOG.debug("Entering insertImpressionSummary() method ");

        try {
            mongoDao.insertData(impressionSummary);
            LOG.info("impression summary inserted into DB = " + impressionSummary);
        } catch (Exception ex) {
            LOG.warn("Problem inserting data into Mongo: ", ex);
        }
    }

    public List<ProductImpressionSummary> getImpressionSummary(String pid, long runNumber) {
        LOG.debug("Entering getImpressionSummary() method. PID " + pid + "  Run-number " + runNumber);
        List<ProductImpressionSummary> productImpressionSummaries = mongoDao.findByCriteria(ProductImpressionSummary.class, "runNumber", runNumber, "pid", pid);
        return productImpressionSummaries;
    }

    public List<ProductImpressionRuns> getLatestRunNumberImpressionSummary() {
        LOG.debug("Entering getLatestRunNumbersImpressionSummary() method ");
        return mongoDao.findMaxRecordByCriteria(ProductImpressionRuns.class, "runNumber", "jobName", "SumSearchImpressionForRunNumbers");
    }

    public List<ProductImpressionSummary> getImpressionSummary(DateTime startDateTime, DateTime endDateTime, String... pid) {
        LOG.debug("Entering getImpressionSummary() method ");

        return mongoDao.find(ProductImpressionSummary.class, startDateTime.toDate(), endDateTime.toDate(), "pid", pid);
    }

    public List<ProductImpressionSummary> getImpressionSummary(DateTime now, String... pid) {
        LOG.debug("Entering getImpressionSummary() method ");

        Date startDate = now.withTimeAtStartOfDay().toDate();
        Date endDate = now.millisOfDay().withMaximumValue().toDate();

        return mongoDao.find(ProductImpressionSummary.class, startDate, endDate, "pid", pid);
    }
}
