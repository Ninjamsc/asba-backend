package com.technoserv.jms.consumer;

import com.technoserv.jms.HttpRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

public class JmsConsumer {

    @Autowired
    private HttpRestClient httpRestClient;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void onReceive(String message) {
        if(!httpRestClient.put(message)) {
            jmsTemplate.convertAndSend(message);
        }
    }
}