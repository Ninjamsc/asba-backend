package com.technoserv.rest.exception;

/**
 * Created by VBasakov on 24.11.2016.
 */
public class PhotoPersistServiceException extends RestClientException {

    public PhotoPersistServiceException(String error) {
        super(error);
    }

    protected String getServiceName() {
        return "PhotoPersistService";
    }
}
