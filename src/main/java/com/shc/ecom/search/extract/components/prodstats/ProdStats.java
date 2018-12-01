package com.shc.ecom.search.extract.components.prodstats;

public class ProdStats {
    private double revenue;
    private int itemsSold;
    private int productViews;
    private double conversion;

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public int getItemsSold() {
        return itemsSold;
    }

    public void setItemsSold(int itemsSold) {
        this.itemsSold = itemsSold;
    }

    public int getProductViews() {
        return productViews;
    }

    public void setProductViews(int productViews) {
        this.productViews = productViews;
    }

    public double getConversion() {
        return conversion;
    }

    public void setConversion(double conversion) {
        this.conversion = conversion;
    }
}
