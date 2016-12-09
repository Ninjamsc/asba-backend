package com.technoserv.rest.client.exception;

import com.technoserv.rest.client.exception.RestClientException;

/**
 * Created by VBasakov on 23.11.2016.
 */
public class PhotoAnalizerServiceException extends RestClientException {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1114877643669249618L;

	public PhotoAnalizerServiceException(String error) {
        super(error);
    }

    @Override
    protected String getServiceName() {
        return "PhotoAnalizerService";
    }
}
