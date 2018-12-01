package com.shc.ecom.search.common.constants;

import com.shc.ecom.search.persistence.*;
import org.apache.commons.lang.StringUtils;

public enum DataFile {

    /**
     * 
     */
    QUICKADD("quickAdd.txt", QuickAdd.class),

    /**
     * 
     */
    INCR("incr.txt", Incremental.class),
    /**
     * 
     */
    BROWSEBOOST_PROPERTIES("browseBoost.properties", BrowseBoost.class),
    /**
     * 
     */
    BROWSEBOOSTSCALE_PROPERTIES("browseBoostScale.properties", BrowseBoost.class),
    /**
     * 
     */
    AUTORANK("autoRank.txt", AutoRank.class),
    /**
     * 
     */
    CUSTOMER_RATING("CustomerRating.txt", CustomerRating.class),
    /**
         * 
         */
    COSUMER_REPORT_RATING("ConsumerReportRating.txt", ConsumerReportsRating.class),
    /**
     * 
     */
    LEVEL3CATS("level3Cats.txt", Level3CatsData.class),
    /**
     * 
     */
    NFILTERSDATA("nFiltersData.txt", NFiltersData.class), REBATES("rebates.txt", Rebates.class),
    /**
     * 
     */
    LOCALAD("LocalAd", ProductLocalAd.class),
    /**
     * 
     */
    EXPIRABLEFIELDS("expirablefields.txt", ExpirableFieldMapping.class),
    /**
     * 
     */
    SERVICEMAPPING("serviceMapping.txt", ServiceMapping.class),
    /**
     * 
     */
    PID_MAPPING("PidMappingNew.txt", PidMapping.class);

    private final String filename;
    private final Class<?> beanname;

    DataFile(String name, Class<?> beanName) {
        filename = name;
        beanname = beanName;
    }

    public static DataFile get (String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        for (DataFile datafile : DataFile.values()) {
            if (name.equalsIgnoreCase(datafile.getFilename())) {
                return datafile;
            }
        }
        return null;
    }

    public static Class<?> getBeanClass (String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        for (DataFile datafile : DataFile.values()) {
            if (name.equalsIgnoreCase(datafile.getFilename())) {
                return datafile.getBeanName();
            }
        }
        return null;
    }

    public String getFilename () {
        return filename;
    }

    public Class<?> getBeanName () {
        return beanname;
    }

}
