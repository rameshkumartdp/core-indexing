package com.shc.ecom.search.extract.components.prodstats;

import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.common.prodstats.Retail;
import com.shc.ecom.search.extract.components.ExtractComponent;
import com.shc.ecom.search.extract.extracts.Extracts;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import com.shc.ecom.search.indexer.SpringContext;
import com.shc.ecom.search.pidmapping.ExtractHighestProdstatData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ProdStatsExtractComponent extends ExtractComponent<Retail> {

    private static final long serialVersionUID = -522485023866775270L;

    @Autowired
    private ProdStatsService prodStatsService;

    @Override
    protected Retail get (WorkingDocument wd, ContextMessage context) {
        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);

        Retail retail = prodStatsService.process(wd.getExtracts().getSsin(), context.getOfferId(), eligibleSites);

        // Fall-back retry for KMART if there is no BSO/Prodstats history with
        // SSIN. Try with Kmart offer's Parent-ID.
        if (Stores.KMART.matches(context.getStoreName()) && retail.getResponse().getNumFound() == 0) {
            String kmartOfferParentId = wd.getExtracts().getOfferExtract().getKmartOfferParentId();
            if (!StringUtils.equalsIgnoreCase(wd.getExtracts().getSsin(), kmartOfferParentId)) {
                retail = prodStatsService.process(kmartOfferParentId, context.getOfferId(), eligibleSites);
            }
        }
        return retail;
    }

    @Override
    protected Extracts extract (Retail retail, WorkingDocument wd, ContextMessage context) {
        Extracts extracts = wd.getExtracts();

        String ssin = extracts.getSsin();

        List<Sites> eligibleSites = Stores.getStore(context.getStoreName()).getEligibleSites(wd, context);

        Map<Sites, ProdStats> sitesProdStatsMap = new EnumMap<>(Sites.class);
        ExtractHighestProdstatData getHighestProdStat = SpringContext.getApplicationContext().getBean(
            ExtractHighestProdstatData.class);

        for (Sites site : eligibleSites) {
            double revenue = 0.0;
            int itemsSold = 0;
            int views = 0;
            double conversion = 0.0;

            for (int i = 0; i < retail.getResponse().getNumFound(); i++) {
                String id = retail.getResponse().getDocs().get(i).getId();
                if (id.split("_").length >= 2) {
                    String sitePrefix = id.split("_")[0];
                    if (StringUtils.equalsIgnoreCase(sitePrefix, site.getSiteName())
                        || StringUtils.equalsIgnoreCase(sitePrefix, "mkt")) {
                        revenue += Double.parseDouble(retail.getResponse().getDocs().get(i).getRevenue());
                        itemsSold += Integer.parseInt(retail.getResponse().getDocs().get(i).getItemsSold());
                        views += Integer.parseInt(retail.getResponse().getDocs().get(i).getProductViews());
                        conversion += Double.parseDouble(retail.getResponse().getDocs().get(i).getConversion());
                    }
                }
            }

            ProdStats prodStats = new ProdStats();
            prodStats.setRevenue(revenue);
            prodStats.setItemsSold(itemsSold);
            prodStats.setProductViews(views);
            prodStats.setConversion(conversion);

            if (getHighestProdStat.isPresent(ssin)) {
                prodStats = getHighestProdStat.compareProdStats(ssin, site, prodStats);
            }
            sitesProdStatsMap.put(site, prodStats);
        }

        extracts.getProdstatsExtract().setSitesProdStatsMap(sitesProdStatsMap);
        return extracts;
    }
}
