package com.technoserv.queue;

import com.google.common.base.MoreObjects;

import java.util.Date;

/**
 *
 */
public class RequestRetry {

    private long requestId;

    private int retryCount;

    private Date retriedAt;

    public RequestRetry(long requestId, int retryCount, Date retriedAt) {
        this.requestId = requestId;
        this.retryCount = retryCount;
        this.retriedAt = retriedAt;
    }

    public RequestRetry() {
        this(0L, 0, new Date());
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public Date getRetriedAt() {
        return retriedAt;
    }

    public void setRetriedAt(Date retriedAt) {
        this.retriedAt = retriedAt;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("requestId", requestId)
                .add("retryCount", retryCount)
                .add("retriedAt", retriedAt)
                .toString();
    }
}
