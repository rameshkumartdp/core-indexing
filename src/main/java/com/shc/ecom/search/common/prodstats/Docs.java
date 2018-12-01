package com.shc.ecom.search.common.prodstats;

public class Docs {
    private String id;

    private String conversion;

    private String itemsSold;

    private String productViews;

    private String revenue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConversion() {
        return conversion;
    }

    public void setConversion(String conversion) {
        this.conversion = conversion;
    }

    public String getItemsSold() {
        return itemsSold;
    }

    public void setItemsSold(String itemsSold) {
        this.itemsSold = itemsSold;
    }

    public String getProductViews() {
        return productViews;
    }

    public void setProductViews(String productViews) {
        this.productViews = productViews;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", conversion = " + conversion + ", itemsSold = " + itemsSold + ", productViews = " + productViews + ", revenue = " + revenue + "]";
    }
}
