package com.technoserv.jms.consumer;

import com.technoserv.db.service.objectmodel.api.RequestService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by sergey on 24.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContext.xml")
@Ignore
public class ArmRequestSendMessageTest {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private RequestService requestService;

    @Test
    public void send() throws Exception {
        String request = TestUtils.readFile("request-fullframe-1.json");
        String request2 = TestUtils.readFile("request-preview-1.json");
        for (int i = 0; i < 100; i++) {
            jmsTemplate.convertAndSend("arm.queue", request);
            jmsTemplate.convertAndSend("arm.queue", request2);
        }

        Assert.assertEquals(1, requestService.countAll());

    }
}
