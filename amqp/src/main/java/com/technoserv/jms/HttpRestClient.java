package com.technoserv.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by Adrey on 20.11.2016.
 */
@Service
public class HttpRestClient {

    private static final Log log = LogFactory.getLog(HttpRestClient.class);

    //TODO Найти другое место для URL
    public static final String URL = "http://www.google.ru/";


    private RestTemplate rest = new RestTemplate();

    public void put(String message) {
        if(log.isInfoEnabled()) {
            log.info("SENDING MESSAGE: '" + message + "'");
        }

        try {
            rest.put(URI.create(URL), message);

        }catch (RestClientException e) {

            //todo сделать обработку исключений
            e.printStackTrace();

        }


        if(log.isInfoEnabled()) {
            log.info("SENDING MESSAGE: '" + message + "' DONE");
        }
    }
}