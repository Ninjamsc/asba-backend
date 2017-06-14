package com.technoserv.jms.trusted;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * Created by sergey on 20.11.2016.
 */
public class ArmRequestRetryMessage implements Serializable {
    /**
     * Тело сообщения
     */
    private String message;

    /**
     * Кол-во ретраев сохранения.
     */
    private int tryCount;

    public ArmRequestRetryMessage(String message) {
        this.message = message;
        tryCount = 1;
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
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("message", message)
                .add("tryCount", tryCount)
                .toString();
    }
}
