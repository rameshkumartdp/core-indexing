package com.shc.ecom.search.common.jmx;

public interface SearchCommonJmxMBean {

    String getTotalContentGetFailCount();

    String getTotalOfferGetFailCount();

    String getTotalPromoGetFailCount();

    String getTotalPromorelGetFailCount();

    String getTotalContentGetCount();

    String getTotalOfferGetCount();

    String getTotalPromoGetCount();

    String getTotalPromorelGetCount();

    String getTotalContentPutFailCount();

    String getTotalOfferPutFailCount();

    String getTotalPromoPutFailCount();

    String getTotalPromorelPutFailCount();

    String getTotalContentPutCount();

    String getTotalOfferPutCount();

    String getTotalPromoPutCount();

    String getTotalPromorelPutCount();

    String getTotalPromoDeleteCount();

    String getTotalPromorelDeleteCount();

    String getTotalPromoDeleteFailCount();

    String getTotalPromorelDeleteFailCount();

    String getGbAPIWriteAvgCallTime();

    String getGbAPIReadAvgCallTime();

    String getBucketIdQcount();

    String getPromoIdQcount();

    String getPromoQcount();

    String getTotalOfferCount();

    String getTotalValidPromoRelCount();

    String getPricingGridCount();

}
