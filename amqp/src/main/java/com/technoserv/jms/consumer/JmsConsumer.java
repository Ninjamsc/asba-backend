package com.technoserv.jms.consumer;

import com.technoserv.jms.HttpRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsConsumer {

    @Autowired
    private HttpRestClient httpRestClient;

    @JmsListener(destination = "internal.queue")
    public void onReceive(String message) {
        httpRestClient.put(message);
    }
}