package com.technoserv.rest.client.exception;

/**
 * Created by VBasakov on 23.11.2016.
 */
public abstract class RestClientException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4964509678924596748L;
	private String error;

    public RestClientException(String error) {
        this.error = error;
    }

    public String toJSON() {
        return String.format("{\"%s\":%s}", getServiceName(), error);
    }

    protected abstract String getServiceName();
}
