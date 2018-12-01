package com.shc.ecom.search.common.customerrating;

import java.io.Serializable;

public class CustomerRatingDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1023580997188403370L;

    private int reviewCount;
    private double rating;

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
