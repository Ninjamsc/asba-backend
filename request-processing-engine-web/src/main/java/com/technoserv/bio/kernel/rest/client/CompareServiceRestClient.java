package com.technoserv.bio.kernel.rest.client;


import com.technoserv.bio.kernel.rest.exception.CompareServiceException;
import com.technoserv.bio.kernel.rest.request.CompareServiceRequest;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final Log log = LogFactory.getLog(CompareServiceRestClient.class);

    private RestTemplate rest = new RestTemplate();

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    public String getUrl() {
        return systemSettingsBean.get(SystemSettingsType.COMPARE_SERVICE_URL);
    }

    public String compare(CompareServiceRequest request) {

        String url = getUrl();

        if(log.isInfoEnabled()) {
            log.info(url + " COMPARING TEMPLATE: '" + request + "'");
        }
        try {
            //todo request -> json with jackson
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CompareServiceRequest> requestEntity = new HttpEntity<CompareServiceRequest>(request,requestHeaders);
            ResponseEntity<String> response = rest.exchange(URI.create(url), HttpMethod.PUT, requestEntity, String.class);
            if(log.isInfoEnabled()) {
                log.info("COMPARING TEMPLATE: DONE");
            }
            return response.getBody();
        } catch (RestClientResponseException e) {
            e.printStackTrace();
            log.error("COMPARING TEMPLATE ERROR CODE " + e.getRawStatusCode());
            switch (e.getRawStatusCode()){
                /*Стандартные названия ошибок не совпадают с нашей документацией только коды */
                case 400:log.error("BAD_REQUEST");throw new CompareServiceException(e.getResponseBodyAsString());//BAD_REQUEST:
                case 500:log.error("INTERNAL_SERVER_ERROR");throw new CompareServiceException(e.getResponseBodyAsString());//INTERNAL_SERVER_ERROR:
                default: throw new RuntimeException(e.getResponseBodyAsString());
            }
        }
    }

    public static void main(String[] args) {
        CompareServiceRestClient restClient = new CompareServiceRestClient(){
            public String getUrl() {
                return "http://sdorohov.ru/rpe/rest/api/compare-stub/template";
            }
        };
        restClient.compare(new CompareServiceRequest());

    }
}