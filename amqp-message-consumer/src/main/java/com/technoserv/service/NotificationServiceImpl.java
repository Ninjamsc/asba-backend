package com.technoserv.service;

import com.technoserv.dto.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private final static Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class.getSimpleName());

    private JmsTemplate jmsTemplate;

    @Autowired
    public NotificationServiceImpl(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public boolean send(Notification notification) {
        log.info("sending '{}' to '{}'", notification, jmsTemplate.getDefaultDestinationName());
        try {
            jmsTemplate.convertAndSend(notification);
        } catch (JmsException e) {
            log.error("Failed to send message using jmsTemplate: {}, {}", jmsTemplate, e);
            return false;
        }
        return true;
    }

}
