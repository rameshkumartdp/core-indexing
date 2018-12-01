package com.shc.ecom.search.util;

/**
 * Created by hdargah on 8/10/2016.
 */
public class NumberUtil {

    public static final float EPSILON = 0.00001f;

    private NumberUtil() {
    }

    /**
     * Compares two floats for equality within an error of EPSILON
     *
     * @param a float number 1
     * @param b float number 2
     * @return whether both are equal
     */
    public static boolean floatEquals(float a, float b) {
        if (Math.abs(a - b) < EPSILON) {
            return true;
        }
        return false;
    }
}
