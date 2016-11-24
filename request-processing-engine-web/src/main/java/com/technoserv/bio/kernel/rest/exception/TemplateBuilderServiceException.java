package com.technoserv.bio.kernel.rest.exception;

import com.technoserv.rest.exception.RestClientException;

/**
 * Created by VBasakov on 23.11.2016.
 */
public class TemplateBuilderServiceException extends RestClientException {


    public TemplateBuilderServiceException(String error) {
        super(error);
    }

    @Override
    protected String getServiceName() {
        return "TemplateBuilderService";
    }
}
