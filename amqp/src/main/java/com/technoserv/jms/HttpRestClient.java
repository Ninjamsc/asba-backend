package com.technoserv.jms;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by Adrey on 20.11.2016.
 */
@Service
public class HttpRestClient {

    public static final String URL = "";

    public void put(String message) {
        System.out.println("SEND");
        RestTemplate rest = new RestTemplate();
        rest.put(URI.create(URL), message);
    }
}