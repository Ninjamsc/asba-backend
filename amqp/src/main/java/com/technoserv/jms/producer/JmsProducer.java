package com.technoserv.jms.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

@Component
public class JmsProducer {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("internal.queue")
    private Queue queue;

    public void send(String msg) {
        jmsTemplate.convertAndSend(queue, msg);
    }
}