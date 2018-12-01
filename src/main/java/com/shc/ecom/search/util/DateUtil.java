package com.shc.ecom.search.util;

import com.shc.ecom.search.common.constants.DateFormat;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Util methods related to dates.
 *
 * @author vsingh8
 */
public final class DateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    /**
     * Hiding public constructor
     */
    private DateUtil() {
    }

    /**
     * Converts the given dateInput to standard date format like "yyyy-MM-dd'T'HH:mm:ss.SSSZ". If the dateInput is ExpDate then
     * by default time is set to 23:59:59.000Z. If the dateInput is ActDate then by default time is set to 00:00:00.000Z.
     *
     * @param dateInput date in context not in correct format
     * @param isExpDate if date in context is expDate for vocTag
     * @return
     */
    public static String convertToStandardDateTime(String dateInput, boolean isExpDate) {
        String dateFormatRegEx = "([0-9]{2})/([0-9]{2})/([0-9]{4})";
        String dateFormatRegEx2 = "([0-9]{4})/([0-9]{2})/([0-9]{2})";
        String dateFormatRegEx3 = "([0-9]{4})-([0-9]{2})-([0-9]{2})";
        String dateFormatRegEx4 = "([0-9]{4})-([0-9]{2})-([0-9]{2})T([0-9]{2}):([0-9]{2}):([0-9]{2}).([0-9]){3}[Z]*";
        String dateFormatRegEx5 = "([0-9]{4})-([0-9]{2})-([0-9]{2})T([0-9]{2}):([0-9]{2}):([0-9]{2})[Z]*";
        
        LocalDateTime actualDateTime;
        if (dateInput.matches(dateFormatRegEx4)) {
            return dateInput;
        }
        if (dateInput.matches(dateFormatRegEx)) {
            actualDateTime = LocalDateTime.parse(dateInput, DateTimeFormat.forPattern(DateFormat.MMDDYYYSLASH.getDateFormat()));
        } else if (dateInput.matches(dateFormatRegEx2)) {
            actualDateTime = LocalDateTime.parse(dateInput, DateTimeFormat.forPattern(DateFormat.YYYYMMDDSLASH.getDateFormat()));
        } else if (dateInput.matches(dateFormatRegEx3)) {
            actualDateTime = LocalDateTime.parse(dateInput, DateTimeFormat.forPattern(DateFormat.YYYYMMDDDASH.getDateFormat()));
        } else if (dateInput.matches(dateFormatRegEx5)) {
            actualDateTime = LocalDateTime.parse(dateInput, DateTimeFormat.forPattern(DateFormat.LONGDATEWITHOUTMILLISEC.getDateFormat()));
        } else {
            LOGGER.debug("New type of date format found not supported right now {}", dateInput);
            // In case of unknown format return past date to in-validate tag.
            return DateFormat.PASTDATE.getDateFormat();
        }
        return getFinalDateTime(actualDateTime ,isExpDate);
    }
    
    /**
     * Adding 23:59:59 in ExpDate because invalidate Tag at the end of day
     * @param actualDateTime
     * @param isExpDate
     * @return
     */
    private static String getFinalDateTime(LocalDateTime actualDateTime , boolean isExpDate){
       LocalDateTime dateTime = actualDateTime;
       if (isExpDate) {
          dateTime = actualDateTime.plusHours(23).plusMinutes(59).plusSeconds(59);
          return dateTime.toString() + "Z";
       } else {
          return dateTime.toString() + "Z";
       }
    }

    /**
     * To get the date from long datetime format input.
     *
     * @param date in long format
     * @return date part of input
     */
    public static LocalDate getLocalDate(String date) {

        LocalDate localDate = null;

        if (StringUtils.isNotEmpty(date) && date.length() >= 10) {
            try {
                localDate = LocalDate.parse(date, DateTimeFormat.forPattern(DateFormat.LONGDATE.getDateFormat()));
            } catch (IllegalArgumentException e) {
                LOGGER.error("Illegal Date Time " + date, e);
                LOGGER.debug("Illegal Date Time {}. Mismatch with {}.{}", date, DateFormat.LONGDATE.getDateFormat());
                localDate = LocalDate.parse(date.substring(0, DateFormat.LONGDATE.getDateFormat().indexOf("'T'")));
            }
        }
        return localDate;
    }

}
