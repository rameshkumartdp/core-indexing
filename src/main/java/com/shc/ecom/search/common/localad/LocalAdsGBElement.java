package com.shc.ecom.search.common.localad;

import java.io.Serializable;
import java.util.List;

public class LocalAdsGBElement implements Serializable {
    private String coords;
    private List<Products> products;

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }
}
