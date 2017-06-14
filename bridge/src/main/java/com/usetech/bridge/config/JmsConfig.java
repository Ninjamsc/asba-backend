package com.usetech.bridge.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {

    private CommonConfig config;

    @Autowired
    public JmsConfig(CommonConfig config) {
        this.config = config;
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
        connectionFactory.setBrokerURL(config.getBrokerUrl());
        connectionFactory.setTrustedPackages(Lists.newArrayList("com.usetech.bridge"));
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setDefaultDestinationName(config.getQueueName());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}