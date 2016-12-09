package com.technoserv.rest.client.exception;

import com.technoserv.rest.client.exception.RestClientException;

/**
 * Created by VBasakov on 23.11.2016.
 */
public class TemplateBuilderServiceException extends RestClientException {


    /**
	 * 
	 */
	private static final long serialVersionUID = 2180593147483049442L;

	public TemplateBuilderServiceException(String error) {
        super(error);
    }

    @Override
    protected String getServiceName() {
        return "TemplateBuilderService";
    }
}
