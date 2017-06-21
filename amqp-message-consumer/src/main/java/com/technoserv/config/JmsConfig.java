package com.technoserv.config;

import com.google.common.collect.Lists;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@Configuration("classpath:application.properties")
public class JmsConfig {

    private String brokerUrl;

    private String notificationQueue;

    @Autowired
    public JmsConfig(@Value(ConfigValues.BROKER_URL) String brokerUrl,
                     @Value(ConfigValues.SKUD_NOTIFICATION_QUEUE) String notificationQueue) {

        this.brokerUrl = brokerUrl;
        this.notificationQueue = notificationQueue;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        return converter;
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setTrustedPackages(Lists.newArrayList("com.technoserv"));
        return connectionFactory;
    }

    @Bean
    public JmsListenerContainerFactory<?> containerFactory(ConnectionFactory connectionFactory) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setDefaultDestinationName(notificationQueue);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}