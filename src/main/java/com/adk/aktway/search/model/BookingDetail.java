package com.adk.aktway.search.model;

import java.util.List;

/**
 * Created by rames on 21-05-2019.
 */
public class BookingDetail {
    private String typeOfBooking;
    private String duration;
    private String departurePoint;
    private List<BookingDetailEntries> bookingDetailEntries;

    public String getTypeOfBooking() {
        return typeOfBooking;
    }

    public void setTypeOfBooking(String typeOfBooking) {
        this.typeOfBooking = typeOfBooking;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDeparturePoint() {
        return departurePoint;
    }

    public void setDeparturePoint(String departurePoint) {
        this.departurePoint = departurePoint;
    }

    public List<BookingDetailEntries> getBookingDetailEntries() {
        return bookingDetailEntries;
    }

    public void setBookingDetailEntries(List<BookingDetailEntries> bookingDetailEntries) {
        this.bookingDetailEntries = bookingDetailEntries;
    }
}
