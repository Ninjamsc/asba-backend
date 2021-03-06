package com.technoserv.jms.client.impl;

import com.technoserv.jms.trusted.JsonDeliveryRetryMessage;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.ConnectionFactory;

/**
* Created by Adrey on 20.11.2016.
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContext.xml")
@Ignore
public class JmsClientTest {

//    @Autowired
//    private BrokerService brokerService;

//    @Test
//    @Ignore
//    public void testSendMessage() {
//        JmsTemplate template = new JmsTemplate();
//        template.setConnectionFactory(connectionFactory());
//        template.convertAndSend("external.queue","Test message");
//    }

    @Test
    @Ignore
    public void internalIT() throws Exception {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(internalConnectionFactory());
        template.convertAndSend("internal.queue", new JsonDeliveryRetryMessage("Test message"));

    }

    public static ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("tcp://localhost:61616");
        return connectionFactory;
    }

    public static ConnectionFactory internalConnectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("vm://localhost");
        return connectionFactory;
    }
}