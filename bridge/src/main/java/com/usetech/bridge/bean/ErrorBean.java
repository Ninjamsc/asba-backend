package com.usetech.bridge.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.io.Serializable;

public class ErrorBean implements Serializable {

    private static final long serialVersionUID = -5054749990970511861L;

    @JsonProperty(value = "message")
    private String message;

    public ErrorBean(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("message", message)
                .toString();
    }
}