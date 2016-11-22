package com.technoserv.jms.trusted;

import java.io.Serializable;

/**
 * Created by sergey on 20.11.2016.
 */
public class RetryMessage implements Serializable {
    /**
     * Тело сообщения
     */
    private String message;

    /**
     * Кол-во ретраев отправки.
     */
    private int tryCount;

    public RetryMessage(String message) {
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
        tryCount ++;
    }
}
