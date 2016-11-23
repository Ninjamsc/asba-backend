package com.technoserv.bio.kernel.rest.exception;

import com.technoserv.rest.exception.RestClientException;

/**
 * Created by VBasakov on 23.11.2016.
 */
public class CompareServiceException extends RestClientException {


    public CompareServiceException(String error) {
        super(error);
    }

    @Override
    protected String getServiceName() {
        return "CompareService";
    }
}
