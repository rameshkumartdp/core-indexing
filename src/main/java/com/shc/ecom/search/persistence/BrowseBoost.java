/**
 *
 */
package com.shc.ecom.search.persistence;

import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.constants.Stores;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.extract.components.prodstats.ProdStats;
import com.shc.ecom.search.extract.extracts.ProdstatsExtract;
import com.shc.ecom.search.extract.extracts.WorkingDocument;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author rgopala
 */
@Component
public class BrowseBoost extends FileDataAccessor {

    private static final long serialVersionUID = -2241523701343534132L;

    private static final String SEPARATOR = "=";

    private int maxItemsSold = 0;
    private double maxRevenue = 0;
    private int maxProductViews = 0;
    private double maxConversion = 0;
    private double itemsSoldScale = 1;
    private double revenueScale = 1;
    private double productViewsScale = 1;
    private double conversionScale = 1;
    private double impressionsScale = 1;
    private Map<String, Stats> browseStats = new HashMap<>();

    @Autowired
    private AutoRank autoRank;

    @Override
    public void save(String value) {
        if (StringUtils.equalsIgnoreCase(value, "EOF")) {
            build();
        }
        String[] data = value.split(SEPARATOR);
        if (data.length != 2) {
            return;
        }
        if (!NumberUtils.isNumber(data[1])) {
            return;
        }
        switch (data[0]) {
            case "max.itemsSold":
                maxItemsSold = Integer.parseInt(data[1]);
                break;
            case "max.revenue":
                maxRevenue = Double.parseDouble(data[1]);
                break;
            case "max.productViews":
                maxProductViews = Integer.parseInt(data[1]);
                break;
            case "max.conversion":
                maxConversion = Double.parseDouble(data[1]);
                break;
            case "max.itemsSold.scale":
                itemsSoldScale = Double.parseDouble(data[1]);
                break;
            case "max.revenue.scale":
                revenueScale = Double.parseDouble(data[1]);
                break;
            case "max.productViews.scale":
                productViewsScale = Double.parseDouble(data[1]);
                break;
            case "max.conversion.scale":
                conversionScale = Double.parseDouble(data[1]);
                break;
            case "max.impressions.scale":
                impressionsScale = Double.parseDouble(data[1]);
                break;
        }
    }

    private void build() {
        browseStats.put("itemsSold", new Stats(itemsSoldScale, maxItemsSold));
        browseStats.put("revenue", new Stats(revenueScale, maxRevenue));
        browseStats.put("productViews", new Stats(productViewsScale, maxProductViews));
        browseStats.put("conversion", new Stats(conversionScale, maxConversion));
    }

    private double getBrowseBoostPerStat(double statsValue, double autoRankValue, double maxValue, double scale) {
        if (maxValue <= 0) {
            return 0;
        }
        return (statsValue / maxValue) * autoRankValue * scale;
    }

    public double computeBrowseBoost(WorkingDocument wd, ContextMessage context, Sites site) {
        double browseBoost = 0.0;

        // Key prod-stats that will contribute to browseboost score are the ones
        // listed in AutoRank text for the product's associated catalog.

        Set<String> keyStatsSet = autoRank.getRankings(site.getCatalogId()).keySet();
        for (String stats : keyStatsSet) {
            double statsValue = getStatsValue(wd, context, stats, site);
            double statsCatalogRank = autoRank.getRank(site.getCatalogId(), stats);
            double statsMaxValue = getMaxValue(stats);
            double statsScale = getScale(stats);

            // Compute BrowseBoost
            browseBoost += getBrowseBoostPerStat(statsValue, statsCatalogRank, statsMaxValue, statsScale);
        }
        return browseBoost;
    }

    public double computeBrowseBoostWithImpressions(WorkingDocument wd, ContextMessage context, Sites site) {
        long impressions = getBaseImpressions(wd, site);
        double browseBoost = computeBrowseBoost(wd, context, site);
        double browseBoostImpr = browseBoost / (1 + impressions * impressionsScale);
        return browseBoostImpr;

    }

    /**
     * There are situations where some products have more views or items sold
     * count than impressions count. Usually this could happen due to ads and
     * campaigns on these products.
     * <p>
     * This method takes care of setting a base limit for impressions. There is
     * a lower bound computed first and then it is multiplied by a factor called
     * minimum impression coefficient. This cooefficient tells that to get one
     * productView or an itemsSold for this product, it should have had atleast
     * minImprCoefficient impressions.
     * <p>
     * The data for minImprCoefficient is highly arbitrarily set based on
     * current products having low customer interactions on search results page.
     *
     * @param wd
     * @return modified impressions
     */
    public long getBaseImpressions(WorkingDocument wd, Sites site) {
        if (Sites.COMMERCIAL.matches(site.getSiteName())){
            return 0;
        }
        Map<Sites, ProdStats> sitesProdStatsMap = wd.getExtracts().getProdstatsExtract().getSitesProdStatsMap();
        ProdStats prodStats = sitesProdStatsMap.get(site);

        long impressions = wd.getExtracts().getImpressionExtract().getImpressions();
        long productViews = prodStats.getProductViews();
        long itemsSold = prodStats.getItemsSold();
        long minImpressions = Math.max(productViews, itemsSold);
        int minImprCoefficient = 10000;
        if (impressions == 0 || impressions < minImpressions) {
            impressions = minImpressions * minImprCoefficient;
        }
        return impressions;
    }

    private double getScale(String stats) {
        if (browseStats.containsKey(stats)) {
            return browseStats.get(stats).scale;
        }
        return 1;
    }

    private double getMaxValue(String stats) {
        if (browseStats.containsKey(stats)) {
            return browseStats.get(stats).maxValue;
        }
        return 1;
    }

    private double getStatsValue(WorkingDocument wd, ContextMessage context, String stats, Sites site) {
        if (StringUtils.equalsIgnoreCase(context.getStoreName(), Stores.COMMERCIAL.getStoreName())) {
            return 0;
        }
        ProdstatsExtract prodstatsExtract = wd.getExtracts().getProdstatsExtract();
        Map<Sites, ProdStats> sitesProdStatsMap = prodstatsExtract.getSitesProdStatsMap();
        ProdStats prodStats = sitesProdStatsMap.get(site);
        switch (stats) {
            case "revenue":
                return prodStats.getRevenue();
            case "itemsSold":
                return prodStats.getItemsSold();
            case "productViews":
                return prodStats.getProductViews();
            case "conversion":
                return prodStats.getProductViews() == 0 ? 0 : prodStats.getItemsSold() / prodStats.getProductViews();
            case "bidValue":
                return 0; // TODO: get bid value
            case "clearance":
                return wd.getExtracts().getPriceExtract().getOnlinePrice().get(Stores.getStoreId(context.getStoreName())).isClearance() ? 1 : 0;
            case "sale":
                return wd.getExtracts().getPriceExtract().getOnlinePrice().get(Stores.getStoreId(context.getStoreName())).isSale() ? 1 : 0;
            case "cpcFlag":
                return 0;
        }
        return 0;
    }

    public int getMaxItemsSold() {
        return maxItemsSold;
    }

    public double getMaxRevenue() {
        return maxRevenue;
    }

    public int getMaxProductViews() {
        return maxProductViews;
    }

    public double getMaxConversion() {
        return maxConversion;
    }

    private class Stats {
        double scale = 0;
        double maxValue = 0;

        Stats(double scale, double maxValue) {
            this.scale = scale;
            this.maxValue = maxValue;
        }
    }
}
