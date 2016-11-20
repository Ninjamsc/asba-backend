package com.technoserv.jms.client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technoserv.jms.client.JmsClient;
import com.technoserv.jms.consumer.JmsConsumer;
import com.technoserv.jms.producer.JmsProducer;

@Service
public class JmsClientImpl implements JmsClient{

	@Autowired
	JmsConsumer jmsConsumer;
	
	@Autowired
	JmsProducer jmsProducer;
	
	public void send(String msg) {
		jmsProducer.send(msg);
	}

	public String receive() {
		return jmsConsumer.receive();
	}

}
