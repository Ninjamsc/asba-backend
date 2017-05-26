package com.technoserv.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by Adrey on 20.11.2016.
 */
@Service
@PropertySource("classpath:httpRestClient.properties")
public class HttpRestClient {

    private static final Log log = LogFactory.getLog(HttpRestClient.class);

    @Value("${http.rest.client.url}")
    private String url;

    private RestTemplate rest = new RestTemplate();

    public boolean put(String message) {
        if (log.isInfoEnabled()) {
            log.info("SENDING MESSAGE: '" + message + "'");
        }
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<String>(message, requestHeaders);
            rest.exchange(URI.create(url), HttpMethod.PUT, requestEntity, String.class);
//            rest.put(URI.create(url), message);
            /* Вариант использования
            ResponseEntity<String> response = rest.exchange(URI.create(url), HttpMethod.POST, new HttpEntity<String>(message), String.class);*/
            if (log.isInfoEnabled()) {
                log.info("SENDING MESSAGE: '" + message + "' DONE");
            }
            return true;
        } catch (HttpClientErrorException e) {
            switch (e.getStatusCode()) {
                case INTERNAL_SERVER_ERROR://some buisness logic will be here
                case NOT_IMPLEMENTED://some buisness logic will be here
                case BAD_GATEWAY://some buisness logic will be here
                default:
                    if (e.getStatusCode() != null) {
                        log.error(String.format("%s:  %s", e.getStatusCode(), e.getMessage()), e);
                    } else {
                        log.error(e.getMessage(), e);
                    }
            }

        }
        return false;
    }
}