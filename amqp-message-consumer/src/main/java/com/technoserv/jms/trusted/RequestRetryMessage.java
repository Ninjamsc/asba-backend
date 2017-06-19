package com.technoserv.jms.trusted;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * Created by sergey on 20.11.2016.
 */
public class RequestRetryMessage implements Serializable {
    /**
     * Тело сообщения
     */
    private String message;

    /**
     * Кол-во ретраев сохранения.
     */
    private int tryCount;

    public RequestRetryMessage(String message) {
        this.message = message;
        this.tryCount = 1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTryCount() {
        return tryCount;
    }

    public void setTryCount(int tryCount) {
        this.tryCount = tryCount;
    }

    public void incTryCount() {
        tryCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestRetryMessage that = (RequestRetryMessage) o;

        if (tryCount != that.tryCount) return false;
        return message.equals(that.message);
    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + tryCount;
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("message", message)
                .add("tryCount", tryCount)
                .toString();
    }
}
