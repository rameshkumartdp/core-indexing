/**
 *
 */
package com.shc.ecom.search.common.vo.buybox;

import java.io.Serializable;

/**
 * @author djohn0
 */
public class Offers implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -9109754130978156592L;

    private String id;
    private Integer rank;
    private Integer transitDays;
    private Integer totalTransitDays;
    private Double totalPrice;
    private Double shippingPrice;
    private String storeName;
    private String conditionString;
    private Boolean conditionDetails;
    private String sellerId;
    private String sellerName;
    private String catalog;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getTransitDays() {
        return transitDays;
    }

    public void setTransitDays(Integer transitDays) {
        this.transitDays = transitDays;
    }

    public Integer getTotalTransitDays() {
        return totalTransitDays;
    }

    public void setTotalTransitDays(Integer totalTransitDays) {
        this.totalTransitDays = totalTransitDays;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(Double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getConditionString() {
        return conditionString;
    }

    public void setConditionString(String conditionString) {
        this.conditionString = conditionString;
    }

    public Boolean getConditionDetails() {
        return conditionDetails;
    }

    public void setConditionDetails(Boolean conditionDetails) {
        this.conditionDetails = conditionDetails;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }


}
