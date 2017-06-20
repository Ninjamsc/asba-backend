package com.technoserv.rest.model;

public class CountByDateObject {

    public CountByDateObject(Long startDate, Long endDate, Long requestCount,Long biggerCount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.requestCount = requestCount;
        this.biggerCount = biggerCount;
        this.lowerCount = requestCount - biggerCount;
    }

    private Long startDate;
    private Long endDate;
    private Long requestCount;
    private Long biggerCount;
    private Long lowerCount;
    private String text;

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

    public Long getBiggerCount() {
        return biggerCount;
    }

    public void setBiggerCount(Long biggerCount) {
        this.biggerCount = biggerCount;
    }

    public Long getLowerCount() {
        return lowerCount;
    }

    public void setLowerCount(Long lowerCount) {
        this.lowerCount = lowerCount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
