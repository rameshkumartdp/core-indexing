package com.shc.ecom.search.common.constants;

/**
 * Enum for different types of date format.
 *
 * @author vsingh8
 */
public enum DateFormat {

    // Dates of type 2016-11-23T15:45:56.000Z
    LONGDATE("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"),
    
    // Dates of type 2016-11-23T15:45:56Z
    LONGDATEWITHOUTMILLISEC("yyyy-MM-dd'T'HH:mm:ssZ"),

    // Dates of type 11/23/2016
    MMDDYYYSLASH("MM/dd/yyyy"),

    // Dates of type 2016/11/23
    YYYYMMDDSLASH("yyyy/MM/dd"),

    // Dates of type 2016-11-23
    YYYYMMDDDASH("yyyy-MM-dd"),

    // Sample date of future of year 2999
    FUTUREDATE("2999-01-01T00:00:00.000Z"),

    // Sample date of past of year 1999
    PASTDATE("1999-01-01T00:00:00.000Z");

    private final String date;

    DateFormat(String date) {
        this.date = date;
    }

    /**
     * Get the selected date format
     *
     * @return the date format of enum in context.
     */
    public String getDateFormat() {
        return date;
    }
}
