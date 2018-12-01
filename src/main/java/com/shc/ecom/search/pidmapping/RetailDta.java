package com.shc.ecom.search.pidmapping;


import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.shc.ecom.search.common.constants.Sites;
import com.shc.ecom.search.common.prodstats.Retail;
import com.shc.ecom.search.extract.components.prodstats.ProdStats;
import com.shc.ecom.search.extract.components.prodstats.ProdStatsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RetailDta implements Callable<String> {

    private static ConcurrentMap<String, Map<Sites, ProdStats>> prodStatsBasedOnSites = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, String> pidMaps = new ConcurrentHashMap<>();
    @Autowired
    private ProdStatsService prodStatsService;
    private String oldSSIN;
    private String newSSIN;
    private String threadId;

    public static boolean isPresent(String ssin) {
        return pidMaps.containsKey(ssin);
    }

    public static String getOldPid(String ssin) {
        return pidMaps.get(ssin);
    }

    public static Map getProdstats(String ssin) {

        return !StringUtils.isEmpty(ssin) && prodStatsBasedOnSites.get(pidMaps.get(ssin)) != null ? prodStatsBasedOnSites
                .get(pidMaps.get(ssin)) : new HashMap<>();
    }

    @Override
    public String call () throws Exception {

        List<Sites> sites = new ArrayList<>();
        sites.addAll(Arrays.asList(Sites.values()));

        if (!pidMaps.containsKey(oldSSIN)) {
            Retail retail = prodStatsService.process(oldSSIN, sites);
            fillInfo(oldSSIN, retail, sites);
        }
        pidMaps.put(newSSIN, oldSSIN);
        return threadId;
    }

    public void configure (String threadId, String oldId, String newId) {
        this.threadId = threadId;
        this.newSSIN = newId;
        this.oldSSIN = oldId;
    }

    private void fillInfo (String oldSSIN, Retail retail, List<Sites> sites) {

        Map<Sites, ProdStats> sitesProdStatsMap = new EnumMap<>(Sites.class);
        for (Sites site : sites) {

            ProdStats prodStats = filterProdstat(site, retail);
            sitesProdStatsMap.put(site, prodStats);

        }
        prodStatsBasedOnSites.put(oldSSIN, sitesProdStatsMap);
    }

    private ProdStats filterProdstat (Sites site, Retail ret) {
        double rev = 0.0;
        int itemsSld = 0;
        int prodviews = 0;
        double conversions = 0.0;
        for (int i = 0; i < ret.getResponse().getNumFound(); i++) {
            String id = ret.getResponse().getDocs().get(i).getId();
            if (id.split("_").length >= 2) {
                String sitePrefix = id.split("_")[0];

                /** any how its working code but still for the optimization need to have discussion with ravi */
                if (StringUtils.equalsIgnoreCase(sitePrefix, "mkt")
                    || StringUtils.equalsIgnoreCase(sitePrefix, site.getSiteName())) {
                    rev += Double.parseDouble(ret.getResponse().getDocs().get(i).getRevenue());
                    itemsSld += Integer.parseInt(ret.getResponse().getDocs().get(i).getItemsSold());
                    prodviews += Integer.parseInt(ret.getResponse().getDocs().get(i).getProductViews());
                    conversions += Double.parseDouble(ret.getResponse().getDocs().get(i).getConversion());
                }
            }
        }
        return getProdstat(rev, itemsSld, prodviews, conversions);
    }

    private ProdStats getProdstat (double rev, int itemsSld, int prodviews, double conversions) {
        ProdStats prodStats = new ProdStats();
        prodStats.setRevenue(rev);
        prodStats.setItemsSold(itemsSld);
        prodStats.setProductViews(prodviews);
        prodStats.setConversion(conversions);
        return prodStats;
    }
}
