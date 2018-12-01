package com.shc.ecom.search.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.shc.ecom.gb.doc.offer.Free;
import com.shc.ecom.gb.doc.offer.Offer;
import com.shc.ecom.search.common.constants.DateFormat;
import com.shc.ecom.search.common.messages.ContextMessage;
import com.shc.ecom.search.docbuilder.OfferDocBuilder;
import com.shc.ecom.search.extract.extracts.WorkingDocument;

/**
 * Created by hdargah on 6/1/2016.
 */
public class OfferUtil {

    /**
     * If you modify this class, make sure you add more tests to OfferUtilTest
     */
    private static final String OFFERID_SUFFIX = "_o_";
    private static final String PRODUCT_TYPE = "child";
    private static final String SEPARATOR = "_";
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormat.forPattern(DateFormat.LONGDATE.getDateFormat());

    private OfferUtil() {
    }

    /**
     * Returns the set of OfferIds that are part of that product.
     *
     * @param wd
     * @return
     */
    public static Set<String> getOfferIdsFromWorkingDocument(WorkingDocument wd) {

        //Get only available offerIds
        return new HashSet<>(wd.getExtracts().getUasExtract().getAvailableItems());
    }

    /**
     * Creates a map of offerId - OfferDocBuilder for all the offers in a wd
     *
     * @param wd      - WorkingDocument with all the offerIds
     * @param context - Context of the current run
     * @return Map with offerId - OfferDocBuilder pairings
     */
    public static Map<String, OfferDocBuilder> createOfferDocBuilders(WorkingDocument wd, ContextMessage context) {

        Set<String> offerIds = getOfferIdsFromWorkingDocument(wd);
        String store = context.getStoreName();
        Map<String, OfferDocBuilder> offerDocBuilderMap = new HashMap<>();
        for (String offerId : offerIds) {
            offerDocBuilderMap.put(offerId, new OfferDocBuilder()
                    .offerDocId(wd.getExtracts().getSsin() + SEPARATOR + offerId + OFFERID_SUFFIX + store.toLowerCase())
                    .productType(PRODUCT_TYPE));

        }
        return offerDocBuilderMap;
    }

    /**
     * Get map of offerIDs and isFreeShipping eligible for that offer.
     *
     * @param offers
     * @return map
     */
    public static Map<String, Boolean> getFreeShippingMap(List<Offer> offers) {
        Map<String, Boolean> freeShippingMap = new HashMap<>();

        for (Offer offer : offers) {
            String soldBy = offer.getFfm().getSoldBy();
            if (isFreeShippingAvailable(offer, soldBy)) {
                freeShippingMap.put(offer.getId(), true);
            } else {
                freeShippingMap.put(offer.getId(), false);
            }
        }
        return freeShippingMap;
    }

    /**
     * Create a 1:1 map of offerID to KSN.
     * This is required because we store child documents in terms of offers and not ksn
     *
     * @param offers
     * @return
     */
    public static Map<String, String> getOfferIdKsnMap(List<Offer> offers) {
        Map<String, String> offerIdKsnMap = new HashMap<>();

        for (Offer offer : offers) {
            if (StringUtils.isNotEmpty(offer.getAltIds().getKsn())) {
                offerIdKsnMap.put(offer.getId(), offer.getAltIds().getKsn());
            }
        }
        return offerIdKsnMap;
    }

    /**
     * If offer has free ship available and is valid at time of indexing. For Kmart and Sears we are just checking isFreeShipping
     * flag in offer.
     *
     * @param offer
     * @param soldBy
     * @return
     */
    private static boolean isFreeShippingAvailable(Offer offer, String soldBy) {

        boolean areDatesValid = false;
        boolean isPriceValid = false;

        if (isPriceNodePresent(offer)) {
            isPriceValid = isPriceReturnedValid(offer);
        }

        if (isPriceValid) {
            return true;
        }

        if (areFreeNodePresent(offer)) {
            areDatesValid = areDatesValid(offer);
        }

        if (areDatesValid) {
            return true;
        }

        return false;

    }

    private static boolean isPriceReturnedValid(Offer offer) {
        double price = offer.getShipping().getMode().getGnd().getPrice();
        double difference = Math.abs(0.00 - price);
        if (difference == 0.0) {
            return true;
        }
        return false;
    }

    private static boolean areDatesValid(Offer offer) {

        Free free = offer.getShipping().getMode().getGnd().getFree();
        String endDate = free.getEndDt();
        String startDate = free.getStartDt();

        // start and end dates not present
        if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {
            return false;
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        boolean isStartDateValid = isDateValid(startDate, currentDateTime, false);
        boolean isEndDateValid = isDateValid(endDate, currentDateTime, true);

        if (isStartDateValid && isEndDateValid) {
            return true;
        }

        return false;
    }

    public static boolean isDateValid(String date, LocalDateTime currentDateTime, boolean isExpiryDate) {
        if (StringUtils.isNotEmpty(date)) {
            String startDateStandard = DateUtil.convertToStandardDateTime(date, isExpiryDate);
            LocalDateTime dateConverted = LocalDateTime.parse(startDateStandard, DATE_TIME_FORMAT);
            int compare = currentDateTime.compareTo(dateConverted);
            if (isExpiryDate && compare > 0) {
                return false;
            } else if (isExpiryDate && compare <= 0) {
                return true;
            } else if (!isExpiryDate && compare >= 0) {
                return true;
            } else if (!isExpiryDate && compare < 0) {
                return false;
            }
        }
        // default, true if start or end date not present
        return true;
    }

    private static boolean isPriceNodePresent(Offer offer) {
        if (isGndNodePresent(offer)) {
            if (offer.getShipping().getMode().getGnd().getPrice() != null) {
                return true;
            }
        }
        return false;
    }

    private static boolean areFreeNodePresent(Offer offer) {
        if (isGndNodePresent(offer)) {
            if (offer.getShipping().getMode().getGnd().getFree() != null) {
                return true;
            }
        }
        return false;
    }

    private static boolean isGndNodePresent(Offer offer) {
        if (offer.getShipping() != null) {
            if (offer.getShipping().getMode() != null) {
                if (offer.getShipping().getMode().getGnd() != null) {
                    return true;
                }
            }
        }
        return false;
    }

}