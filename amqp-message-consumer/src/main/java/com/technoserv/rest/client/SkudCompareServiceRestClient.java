package com.technoserv.rest.client;


import com.technoserv.config.ConfigValues;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.rest.exception.CompareServiceException;
import com.technoserv.rest.model.SkudCompareRequest;
import com.technoserv.rest.model.SkudCompareResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by MLevitin on 22.11.2016.
 */
@Service
public class SkudCompareServiceRestClient {

    private static final Logger log = LoggerFactory.getLogger(SkudCompareServiceRestClient.class);

    private RestTemplate rest = new RestTemplate();

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    @Value(ConfigValues.API_COMPARE)
    private String compareApiUrl;

    public String getUrl() {
        return systemSettingsBean.get(SystemSettingsType.COMPARE_SERVICE_URL);
    }

    public String getStopListUrl() {
        return systemSettingsBean.get(SystemSettingsType.COMPARE_SERVICE_STOP_LIST_URL);
    }

    public SkudCompareResponse compare(SkudCompareRequest request) {
        log.debug("compare skud request: {} URL: {}", request, compareApiUrl);

        try {
            // TODO: request -> json with jackson
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<SkudCompareRequest> requestEntity = new HttpEntity<>(request, requestHeaders);
            ResponseEntity<SkudCompareResponse> response = rest.exchange(URI.create(compareApiUrl),
                    HttpMethod.PUT, requestEntity, SkudCompareResponse.class);

            log.debug("compare skud response code: {} body: {}", response.getStatusCodeValue(), response.getBody());
            return response.getBody();

        } catch (RestClientResponseException e) {
            log.error("Can't process SKUD compare request.", e);
            switch (e.getRawStatusCode()) {
                // Стандартные названия ошибок не совпадают с нашей документацией только коды
                case 400:
                    log.error("BAD_REQUEST");
                    throw new CompareServiceException(e.getResponseBodyAsString());

                case 500:
                    log.error("INTERNAL_SERVER_ERROR");
                    throw new CompareServiceException(e.getResponseBodyAsString());

                default:
                    throw new RuntimeException(e.getResponseBodyAsString());
            }
        }
    }

}