package com.technoserv.bio.kernel.rest.client;


import com.technoserv.bio.kernel.rest.exception.CompareServiceException;
import com.technoserv.bio.kernel.rest.request.CompareServiceRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by VBasakov on 22.11.2016.
 */
@Service
//@PropertySource("classpath:compareServiceRestClient.properties")
public class CompareServiceRestClient {

    private static final Log log = LogFactory.getLog(CompareServiceRestClient.class);

//    @Value("${http.compare.service.client.url}")
    //TODO fix it
    private String url;
    private RestTemplate rest = new RestTemplate();

    public String compare(CompareServiceRequest request) {
        if(log.isInfoEnabled()) {
            log.info("REQUESTING TEMPLATE: '" + request + "'");
        }
        try {
            //todo request -> json with jackson
            ResponseEntity<String> response = rest.exchange(URI.create(url), HttpMethod.PUT, new HttpEntity<>(request), String.class);
            if(log.isInfoEnabled()) {
                log.info("REQUESTING TEMPLATE: DONE");
            }
            return response.getBody();
        } catch (RestClientResponseException e) {
            switch (e.getRawStatusCode()){
                /*Стандартные названия ошибок не совпадают с нашей документацией только коды */
                case 400://BAD_REQUEST:
                case 500://INTERNAL_SERVER_ERROR:
                default: throw new CompareServiceException(e.getResponseBodyAsString());
            }
        }
    }
}