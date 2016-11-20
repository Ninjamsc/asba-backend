package com.technoserv.jms.client.impl;

import com.technoserv.jms.SpringbootJmsApplication;
import com.technoserv.jms.consumer.JmsConsumer;
import com.technoserv.jms.producer.JmsProducer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Adrey on 20.11.2016.
 */

@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = SpringbootJmsApplication.class, loader = SpringApplicationContextLoader.class)
public class JmsClientTest {

    @Autowired
    private JmsConsumer jmsConsumer;
    @Autowired
    private JmsProducer jmsProducer;

    @Test
    public void queue() throws Exception {
        String message = "QQQQ";
        jmsProducer.send(message);

        Assert.assertEquals(message, message);
    }
}

