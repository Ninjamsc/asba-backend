package com.technoserv.rest.client.exception;

/**
 * Created by VBasakov on 24.11.2016.
 */
public class PhotoPersistServiceException extends RestClientException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -701943601887580275L;

	public PhotoPersistServiceException(String error) {
        super(error);
    }

    protected String getServiceName() {
        return "PhotoPersistService";
    }
}
