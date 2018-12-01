package com.shc.ecom.search.persistence;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
/**
 * Factory for different types of Files read and extracted.
 * @author vsingh8
 *
 */
@Component
public class PersistenceFactory implements Serializable {

    private static final long serialVersionUID = -2234786157990250916L;

    @Autowired
    private NFiltersData nFiltersData;

    @Autowired
    private Level3CatsData level3CatsData;

    @Autowired
    private BrowseBoost browseBoost;

    @Autowired
    private AutoRank autoRank;

    @Autowired
    private QuickAdd quickAdd;

    @Autowired
    private Incremental incremental;

    @Autowired
    private CustomerRating customerRating;

    @Autowired
    private ConsumerReportsRating consumerReportsRating;

    @Autowired
    private Rebates rebates;
    
    @Autowired 
    private PidMapping pidMapping;

    @Autowired
    private ProductLocalAd productLocalAd;
    
    @Autowired
    private ExpirableFieldMapping expirableFields;
    
    @Autowired
    private ServiceMapping serviceMapping;

    @Autowired
    private DeliveryData deliveryData;
    @Autowired
    private ZipDDCData zipDDCData;

    public DataAccessor obtain(String resourceName) {
        if (StringUtils.isEmpty(resourceName)) {
            return null;
        }
        switch (resourceName) {
            case "quickAdd.txt":
                return quickAdd;
            case "incr.txt":
                return incremental;
            case "browseBoost.properties":
                return browseBoost;
            case "browseBoostScale.properties":
                return browseBoost;
            case "autoRank.txt":
                return autoRank;
            case "CustomerRating.txt":
                return customerRating;
            case "ConsumerReportRating.txt":
                return consumerReportsRating;
            case "level3Cats.txt":
                return level3CatsData;
            case "nFiltersData.txt":
                return nFiltersData;
            case "rebates.txt":
                return rebates;
            case "localAdsService":
                return productLocalAd;
            case "PidMappingNew.txt":
                return pidMapping;
            case "zipDDCMappingService":
                return zipDDCData;
            case "expirablefields.txt":
                return expirableFields;
            case "serviceMapping.txt":
                return serviceMapping;
            case "deliveryData.txt":
                return deliveryData;
            default:
                return nFiltersData;
        }
    }

}
