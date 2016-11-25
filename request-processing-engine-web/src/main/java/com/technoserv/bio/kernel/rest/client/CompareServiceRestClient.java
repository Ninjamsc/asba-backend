package com.technoserv.bio.kernel.rest.client;


import com.technoserv.bio.kernel.rest.exception.CompareServiceException;
import com.technoserv.bio.kernel.rest.request.CompareServiceRequest;
import com.technoserv.rest.request.Base64Photo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by VBasakov on 22.11.2016.
 */
@Service
@PropertySource("classpath:compareServiceRestClient.properties")
public class CompareServiceRestClient {

    private static final Log log = LogFactory.getLog(CompareServiceRestClient.class);

    @Value("${http.compare.service.client.url}")
    private String url;
    private RestTemplate rest = new RestTemplate();

    public String compare(CompareServiceRequest request) {
        if(log.isInfoEnabled()) {
            log.info("COMPARING TEMPLATE: '" + request + "'");
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
            log.error("COMPARING TEMPLATE ERROR CODE " + e.getRawStatusCode());
            switch (e.getRawStatusCode()){
                /*Стандартные названия ошибок не совпадают с нашей документацией только коды */
                case 400:log.error("BAD_REQUEST");//BAD_REQUEST:
                case 500:log.error("INTERNAL_SERVER_ERROR");//INTERNAL_SERVER_ERROR:
                default: throw new CompareServiceException(e.getResponseBodyAsString());
            }
        }
    }
}