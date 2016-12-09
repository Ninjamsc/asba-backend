package com.technoserv.rest.client.exception;

import com.technoserv.rest.client.exception.RestClientException;

/**
 * Created by VBasakov on 23.11.2016.
 */
public class CompareServiceException extends RestClientException {


    /**
	 * 
	 */
	private static final long serialVersionUID = -2052202223618282431L;

	public CompareServiceException(String error) {
        super(error);
    }

    @Override
    protected String getServiceName() {
        return "CompareService";
    }
}
