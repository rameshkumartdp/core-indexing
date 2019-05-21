package com.adk.aktway.search.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by rames on 21-05-2019.
 */

public class BookingEntries {
    private String startTime;
    private String endTime;
    private String noOfTickets;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNoOfTickets() {
        return noOfTickets;
    }

    public void setNoOfTickets(String noOfTickets) {
        this.noOfTickets = noOfTickets;
    }
}
