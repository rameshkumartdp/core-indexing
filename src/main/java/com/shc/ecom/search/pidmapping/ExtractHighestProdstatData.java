package com.shc.ecom.search.pidmapping;

import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.extract.components.prodstats.ProdStats;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope("prototype")
public class ExtractHighestProdstatData {

    private Map<Sites, ProdStats> sitesData;

    /**
     * Get the sites data for a particular SSIN
     * @param ssin
     * @return
     */
    private Map<Sites, ProdStats> getSitesData(String ssin) {
        if(sitesData == null) {
            return RetailDta.getProdstats(ssin);
        } else {
            return sitesData;
        }
    }
    /**
     * Compare old and new prodStats and return the best one
     * @param ssin
     * @param site
     * @param prodstats
     * @return
     */
    public ProdStats compareProdStats (String ssin, Sites site, ProdStats prodstats) {
        if (StringUtils.isEmpty(ssin) || site == null) {
            return new ProdStats();
        }

        sitesData = getSitesData(ssin);

        ProdStats oldprodstats = sitesData.get(site);

        return getBestProdStats(oldprodstats, prodstats);

    }

    /**
     * Compare old and new prodstats and find the best amongst them
     * @param oldProdStats
     * @param newProdStats
     * @return
     */
    private ProdStats getBestProdStats(ProdStats oldProdStats, ProdStats newProdStats) {
        boolean a = isNull(oldProdStats);
        boolean b = isNull(newProdStats);

        ProdStats bestProdStats = oldProdStats;
        if (a && b) {
            bestProdStats = returnMaxProdstats(oldProdStats, newProdStats);
        } else if (!a && !b) {
            bestProdStats = new ProdStats();
        } else if (b) {
            bestProdStats = newProdStats;
        }
        return bestProdStats;
    }
    public boolean isPresent (String ssin) {
        return RetailDta.isPresent(ssin);
    }

    /**
     * Compare old and new prodstats and get the max amongst them. Not sure why are we getting the max.
     * Logic needs to be explained.
     * @param oldProdstats
     * @param newProdstats
     * @return
     */
    private ProdStats returnMaxProdstats (ProdStats oldProdstats, ProdStats newProdstats) {

        double newConversion = newProdstats.getConversion();
        double newrevenue = newProdstats.getRevenue();
        int newitemsSold = newProdstats.getItemsSold();
        int newviews = newProdstats.getProductViews();

        if (newConversion > oldProdstats.getConversion()) {
            oldProdstats.setConversion(newConversion);
        }
        if (newrevenue > oldProdstats.getRevenue()) {
            oldProdstats.setRevenue(newrevenue);
        }
        if (newviews > oldProdstats.getProductViews()) {
            oldProdstats.setProductViews(newviews);
        }
        if (newitemsSold > oldProdstats.getItemsSold()) {
            oldProdstats.setItemsSold(newitemsSold);
        }

        return oldProdstats;

    }

    private boolean isNull (ProdStats oldprodstats) {
        return oldprodstats != null;
    }

}
