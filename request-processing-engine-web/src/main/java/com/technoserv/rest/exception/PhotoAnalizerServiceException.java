package com.technoserv.rest.exception;

/**
 * Created by VBasakov on 23.11.2016.
 */
public class PhotoAnalizerServiceException extends RestClientException {


    public PhotoAnalizerServiceException(String error) {
        super(error);
    }

    @Override
    protected String getServiceName() {
        return "PhotoAnalizerService";
    }
}
