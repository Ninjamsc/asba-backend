package com.technoserv.jms.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by VBasakov on 20.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContext.xml")
public class JmsConsumerTest {

    @Autowired
    JsonDeliveryJmsConsumer consumer;

    @Test
    public void good() {
        //todo;
    }
}
