package com.technoserv.rest.request;

import com.google.common.base.MoreObjects;

import java.util.Date;

/**
 * Created by sergey on 28.12.2016.
 */
public class RequestSearchCriteria {

    private Long requestId;
    private Long iin;
    private Date from;
    private Date to;
    private Integer page;
    private Integer size;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getIin() {
        return iin;
    }

    public void setIin(Long iin) {
        this.iin = iin;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("requestId", requestId)
                .add("iin", iin)
                .add("from", from)
                .add("to", to)
                .add("page", page)
                .add("size", size)
                .toString();
    }
}
