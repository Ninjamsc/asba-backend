package com.technoserv.rest.model;

public class CountByDateObject {

    public CountByDateObject(Long startDate, Long endDate, Long requestCount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.requestCount = requestCount;
    }

    private Long startDate;
    private Long endDate;
    private Long requestCount;

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }
}
