package com.shc.ecom.search.common.seller;

import java.util.ArrayList;
import java.util.List;

public class StoreFront {
    List<Offers> offers;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Offers> getOffers() {
        if (offers == null) {
            offers = new ArrayList<>();
        }
        return offers;
    }

    public void setOffers(List<Offers> offers) {
        this.offers = offers;
    }

}
