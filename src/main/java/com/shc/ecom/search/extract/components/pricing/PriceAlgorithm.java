/**
 *
 */
package com.shc.ecom.search.extract.components.pricing;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author rgopala
 */

@Component
public class PriceAlgorithm implements Serializable {

    private static final long serialVersionUID = 2873651030944605433L;

    public double nonZeroPrice(Price price) {

        if (price.getNetDownPrice() > 0.0) {
            return price.getNetDownPrice();
        } else if (price.getSellPrice() > 0.0) {
            return price.getSellPrice();
        } else if (price.getRegPrice() > 0.0) {
            return price.getRegPrice();
        } else {
            return price.getWasPrice();
        }
    }
}
