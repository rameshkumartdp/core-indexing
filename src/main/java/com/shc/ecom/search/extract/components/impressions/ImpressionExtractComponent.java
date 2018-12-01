package com.shc.ecom.search.extract.components.impressions;

import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class ImpressionExtractComponent extends ExtractComponent<List<ProductImpressionSummary>> {

    private static final long serialVersionUID = 1346036889418781320L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ImpressionExtractComponent.class.getName());

    long runNumber;

    @Autowired
    private ImpressionsMongoService impressionsMongoService;

    @PostConstruct
    public void init() {
        List<ProductImpressionRuns> productImpressionRuns = new ArrayList<>();
        try {
            productImpressionRuns = impressionsMongoService.getLatestRunNumberImpressionSummary();
        } catch (Exception e) {
            LOGGER.info("Latest impressions run number summary couldn't be fetched");
        }
        if (!CollectionUtils.isEmpty(productImpressionRuns)) {
            runNumber = productImpressionRuns.get(0).getRunNumber();
        }
        // If runNumber=0, handle here what the fall back runNumber should be.
        LOGGER.info("Mongo latest run number for fetching impression data: " + runNumber);

    }

    @Override
    protected List<ProductImpressionSummary> get(WorkingDocument wd,
                                                 ContextMessage context) {
        List<ProductImpressionSummary> productImpressionSummary = null;
        try {
            productImpressionSummary = impressionsMongoService.getImpressionSummary(
                    wd.getExtracts().getSsin(), this.runNumber);
        } catch (Exception e) {
            LOGGER.info("Failed to fetch impressions data. Product: {}, Runnumber: {}", wd.getExtracts().getSsin(), this.runNumber);
            LOGGER.error(e.getMessage());
        }
        return productImpressionSummary;
    }

    @Override
    protected Extracts extract(List<ProductImpressionSummary> productImpressionSummary,
                               WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();
        if (!CollectionUtils.isEmpty(productImpressionSummary)) {
            extracts.getImpressionExtract().setImpressions(productImpressionSummary.get(0).getImpressionCount());
        }
        return extracts;
    }

}
