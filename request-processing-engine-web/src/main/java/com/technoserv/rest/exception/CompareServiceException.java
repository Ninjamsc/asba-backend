package com.technoserv.rest.exception;

import com.google.common.base.MoreObjects;

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
