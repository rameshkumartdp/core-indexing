package com.shc.ecom.search.extract.components.pricing;

import java.io.Serializable;


/**
 * @author rgopala
 *         Jul 21, 2015 search-doc-builder
 */
public class Price implements Serializable {

    private static final long serialVersionUID = -774486751555453008L;

    private String storeName;

    private double regPrice;

    private double sellPrice;

    private double wasPrice;

    private double netDownPrice;

    private boolean sale;

    private boolean clearance;

    private boolean mapViolated;

    private String rebateIds;
    
    private boolean everydayGreatPrice;

    public boolean hasZeroPrice() {
        if (regPrice <= 0.0 && sellPrice <= 0.0 && wasPrice <= 0.0) {
            return true;
        }
        return false;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getRegPrice() {
        return regPrice;
    }

    public void setRegPrice(double regPrice) {
        this.regPrice = regPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getWasPrice() {
        return wasPrice;
    }

    public void setWasPrice(double wasPrice) {
        this.wasPrice = wasPrice;
    }

    public boolean isMapViolated() {
        return mapViolated;
    }

    public void setMapViolated(boolean mapViolated) {
        this.mapViolated = mapViolated;
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(boolean sale) {
        this.sale = sale;
    }

    public boolean isClearance() {
        return clearance;
    }

    public void setClearance(boolean clearance) {
        this.clearance = clearance;
    }

    public String getRebateIds() {
        return rebateIds;
    }

    public void setRebateIds(String rebateIds) {
        this.rebateIds = rebateIds;
    }

    public double getNetDownPrice() {
        return netDownPrice;
    }

    public void setNetDownPrice(double netDownPrice) {
        this.netDownPrice = netDownPrice;
    }

	/**
	 * @return the everydayGreatPrice
	 */
	public boolean isEverydayGreatPrice() {
		return everydayGreatPrice;
	}

	/**
	 * @param everydayGreatPrice the everydayGreatPrice to set
	 */
	public void setEverydayGreatPrice(boolean everydayGreatPrice) {
		this.everydayGreatPrice = everydayGreatPrice;
	}
}
