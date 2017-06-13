package com.technoserv.rest.client;


import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.rest.exception.CompareServiceException;
import com.technoserv.rest.request.CompareServiceRequest;
import com.technoserv.rest.request.StopListElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by VBasakov on 22.11.2016.
 */
@Service
public class CompareServiceRestClient {

    private static final Logger log = LoggerFactory.getLogger(CompareServiceRestClient.class);

    private RestTemplate rest = new RestTemplate();

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    public String getUrl() {
        return systemSettingsBean.get(SystemSettingsType.COMPARE_SERVICE_URL);
    }

    public String getStopListUrl() {
        return systemSettingsBean.get(SystemSettingsType.COMPARE_SERVICE_STOP_LIST_URL);
    }

    public String compare(CompareServiceRequest request) {
        log.debug("compare request: {} URL: {}", request, getUrl());

        try {
            //todo request -> json with jackson
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CompareServiceRequest> requestEntity = new HttpEntity<>(request, requestHeaders);
            ResponseEntity<String> response = rest.exchange(URI.create(getUrl()), HttpMethod.PUT, requestEntity, String.class);
            log.debug("Compare response: {}", response.getBody());
            return response.getBody();

        } catch (RestClientResponseException e) {
            log.error(String.format("Can't compare template. Original error code: %d", e.getRawStatusCode()), e);

            switch (e.getRawStatusCode()) {
                // стандартные названия ошибок не совпадают с нашей документацией только коды
                case OurErrorCodes.BAD_REQUEST:
                    log.error("Bad request for: {} URL: {} ", request, getUrl());
                    throw new CompareServiceException(e.getResponseBodyAsString());

                case OurErrorCodes.INTERNAL_SERVER_ERROR:
                    log.error("Internal server error for: {} URL: {}", request, getUrl());
                    throw new CompareServiceException(e.getResponseBodyAsString());

                default:
                    throw new RuntimeException(e.getResponseBodyAsString());
            }
        }
    }

    public void importImage(Long id, String photo) {
        StopListElement element = new StopListElement();
        element.setPhoto(photo);

        String url = getStopListUrl() + "/" + id + "/entry";
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<StopListElement> requestEntity = new HttpEntity<>(element, requestHeaders);
            rest.exchange(URI.create(url), HttpMethod.PUT, requestEntity, String.class);

        } catch (RestClientResponseException e) {
            log.error("IMPORT IMAGES ERROR CODE " + e.getRawStatusCode(), e);
            throw new CompareServiceException("IMPORT IMAGES ERROR CODE: " + e.getResponseBodyAsString());
        }
    }
}