package com.technoserv.jms.consumer;

import com.technoserv.jms.producer.JmsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExternalJmsConsumer {

	@Autowired
	private JmsProducer jmsProducer;

	@JmsListener(destination = "external.queue")
	public void receive(String message) {
		System.out.println(message);
		jmsProducer.send(message);
	}
}
