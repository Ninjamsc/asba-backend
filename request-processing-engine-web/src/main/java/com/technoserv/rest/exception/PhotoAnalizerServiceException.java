package com.technoserv.rest.exception;

import com.technoserv.rest.exception.RestClientException;

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
