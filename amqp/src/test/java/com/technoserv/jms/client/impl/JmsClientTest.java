package com.technoserv.jms.client.impl;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import java.util.Arrays;

/**
* Created by Adrey on 20.11.2016.
*/

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class JmsClientTest {
    public static void main(String[] args) {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.convertAndSend("external.queue","Test message");
    }

    public static ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("tcp://localhost:61616");
        return connectionFactory;
    }


}

