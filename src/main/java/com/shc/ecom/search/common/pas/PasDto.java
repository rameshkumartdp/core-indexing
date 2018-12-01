package com.shc.ecom.search.common.pas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pchauha
 */
public class PasDto implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8128529916815666019L;

    private List<Products> products;

    public List<Products> getProducts() {
        if (products == null) {
            products = new ArrayList<>();
            products.add(new Products());
        }
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

}
