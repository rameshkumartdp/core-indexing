package com.shc.ecom.search.common.promo;

import com.shc.ecom.search.common.constants.DateFormat;
import com.shc.ecom.search.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface to handle all types of Promotions. For any new type of promotion this interface should be implemented
 *
 * @param <T> PromoType in context
 * @author vsingh8
 */
public abstract interface TypePromo<T> {

    /**
     * @param promoList
     * @return
     */
    public default List<String> getValidPromoList(List<T> promoList) {
        List<String> validPromoList = new ArrayList<>();
        if (promoList.isEmpty()) {
            return validPromoList;
        }
        for (T singlePromo : promoList) {
            get(singlePromo);
            if (isValidPromo()) {
                validPromoList.add(getPromoId());
            }
        }
        return validPromoList;
    }

    /**
     * @param promoList
     * @return
     */
    public default List<T> getValidPromos(List<T> promoList) {
        List<T> validPromos = new ArrayList<>();
        if (promoList.isEmpty()) {
            return validPromos;
        }
        for (T singlePromo : promoList) {
            get(singlePromo);
            if (isValidPromo()) {
                validPromos.add(singlePromo);
            }
        }
        return validPromos;
    }

    /**
     * Checks if promo is a valid type of promo at time of indexing
     *
     * @param promo
     * @return
     */
    default boolean isValidPromo() {

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now();

        if (isNotComparablePromo()) {
            return false;
        }
        if (StringUtils.isNotEmpty(getStartDt())) {
            String actionDate = DateUtil.convertToStandardDateTime(getStartDt(), false);
            startDateTime = LocalDateTime.parse(actionDate, DateTimeFormat.forPattern(DateFormat.LONGDATE.getDateFormat()));
        }
        if (StringUtils.isNotEmpty(getEndDt())) {
            String expireDate = DateUtil.convertToStandardDateTime(getEndDt(), true);
            endDateTime = LocalDateTime.parse(expireDate, DateTimeFormat.forPattern(DateFormat.LONGDATE.getDateFormat()));
        }
        if (currentDateTime.compareTo(startDateTime) >= 0 && currentDateTime.compareTo(endDateTime) < 0
                && endDateTime.compareTo(startDateTime) > 0) {
            return true;
        }
        return false;
    }

    /**
     * @return true if promoID or startDate or EndDate is not present in promo.
     */
    default boolean isNotComparablePromo() {
        if (StringUtils.isEmpty(getPromoId()) || StringUtils.isEmpty(getEndDt())
                || StringUtils.isEmpty(getStartDt())) {
            return true;
        }
        return false;
    }

    /**
     * @param promo
     * @return
     */
    public abstract T get(T promo);

    /**
     * @return
     */
    public abstract String getPromoId();

    /**
     * @return
     */
    public default String getStartDt() {
        return StringUtils.EMPTY;
    }

    /**
     * @return
     */
    public default String getEndDt() {
        return StringUtils.EMPTY;
    }
}
