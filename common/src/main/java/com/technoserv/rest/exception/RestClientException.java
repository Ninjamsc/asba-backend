package com.technoserv.rest.exception;

/**
 * Created by VBasakov on 23.11.2016.
 */
public abstract class RestClientException extends RuntimeException{

    private String error;

    public RestClientException(String error) {
        this.error = error;
    }

    public String toJSON() {
        return String.format("{\"%s\":%s}", getServiceName(), error);
    }

    protected abstract String getServiceName();
}
