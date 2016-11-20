package com.technoserv.jms;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by Adrey on 20.11.2016.
 */
@Service
public class HttpRestClient {

    public static final String URL = "http://www.google.ru/";
    private RestTemplate rest = new RestTemplate();

    public void put(String message) {
        System.out.println("SEND");

        try {
            rest.put(URI.create(URL), message);

        }catch (RestClientException e) {

            //todo сделать обработку исключений
            e.printStackTrace();

        }
    }
}