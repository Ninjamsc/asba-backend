package com.technoserv.rest.exception;

/**
 * Created by VBasakov on 23.11.2016.
 */
public class PhotoAnalyzerServiceException extends RestClientException {

    public PhotoAnalyzerServiceException(String error) {
        super(error);
    }

    @Override
    protected String getServiceName() {
        return "PhotoAnalyzerService";
    }
}
