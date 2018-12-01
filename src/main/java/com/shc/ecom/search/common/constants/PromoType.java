package com.shc.ecom.search.common.constants;

/**
 * Enum for different types of promotions.
 *
 * @author vsingh8
 */
public enum PromoType {

    // Shipping promotion type
    SHIPPING("Shipping"),

    // Companion promotion type
    COMPANION("Companion"),

    // Ship To Store Ground,UPS promotion type
    SHIPINFOUPS("Ship To Store Ground,UPS"),

    // Bonus promotion type
    BONUS("Bonus");

    private final String promo;

    PromoType(String promoType) {
        this.promo = promoType;
    }

    /**
     * Get the selected promotion type
     *
     * @return the promotion type in context.
     */
    public String getPromoType() {
        return promo;
    }
}
