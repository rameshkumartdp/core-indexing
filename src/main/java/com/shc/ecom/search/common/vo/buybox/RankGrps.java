/**
 *
 */
package com.shc.ecom.search.common.vo.buybox;

import java.io.Serializable;
import java.util.List;

/**
 * @author djohn0
 */
public class RankGrps implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3096537674598075028L;

    private String id;
    private Double lowestPrice;
    private Boolean isOpen;
    private Boolean isSearsGroup;
    private List<Offers> offers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Boolean getIsSearsGroup() {
        return isSearsGroup;
    }

    public void setIsSearsGroup(Boolean isSearsGroup) {
        this.isSearsGroup = isSearsGroup;
    }

    public List<Offers> getOffers() {
        return offers;
    }

    public void setOffers(List<Offers> offers) {
        this.offers = offers;
    }

}
