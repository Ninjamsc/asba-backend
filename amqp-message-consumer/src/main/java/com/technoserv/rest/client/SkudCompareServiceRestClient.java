package com.technoserv.rest.client;


import com.technoserv.rest.exception.CompareServiceException;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.rest.model.SkudCompareRequest;
import com.technoserv.rest.model.SkudCompareResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Log log = LogFactory.getLog(SkudCompareServiceRestClient.class);

    private RestTemplate rest = new RestTemplate();

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    public String getUrl() {
        return systemSettingsBean.get(SystemSettingsType.COMPARE_SERVICE_URL);
    }

    public String getStopListUrl() {
        return systemSettingsBean.get(SystemSettingsType.COMPARE_SERVICE_STOP_LIST_URL);
    }

    public SkudCompareResponse compare(SkudCompareRequest request) {

        String url = "http://localhost:9080/compare/api/skud"; //TODO HARDCODE

        if(log.isInfoEnabled()) {
            log.info(url + " COMPARING  (SKUD) TEMPLATE: '" + request + "'");
        }
        try {
            //todo request -> json with jackson
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            //HttpEntity<SkudCompareRequest> requestEntity = new HttpEntity<SkudCompareRequest>(request,requestHeaders);
            HttpEntity<SkudCompareRequest> requestEntity = new HttpEntity<SkudCompareRequest>(request,requestHeaders);
            ResponseEntity<SkudCompareResponse> response = rest.exchange(URI.create(url), HttpMethod.PUT, requestEntity, SkudCompareResponse.class);
            if(log.isInfoEnabled()) {
                log.info("COMPARING TEMPLATE: DONE");
            }
            return response.getBody();
        } catch (RestClientResponseException e) {
            e.printStackTrace();
            log.error("COMPARING TEMPLATE ERROR CODE " + e.getRawStatusCode());
            switch (    e.getRawStatusCode()){
                /*Стандартные названия ошибок не совпадают с нашей документацией только коды */
                case 400:log.error("BAD_REQUEST");throw new CompareServiceException(e.getResponseBodyAsString());//BAD_REQUEST:
                case 500:log.error("INTERNAL_SERVER_ERROR");throw new CompareServiceException(e.getResponseBodyAsString());//INTERNAL_SERVER_ERROR:
                default: throw new RuntimeException(e.getResponseBodyAsString());
            }
        }
    }

}