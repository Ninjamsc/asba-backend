package com.technoserv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private ObjectMapper objectMapper;

    @Autowired
    public NotificationServiceImpl(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean send(Notification notification) {
        log.debug("send notification: {} to queue: {}", notification, jmsTemplate.getDefaultDestinationName());
        try {
            String notificationJson = objectMapper.writeValueAsString(notification);
            log.debug("notificationJson: {}", notificationJson);

            jmsTemplate.convertAndSend(notificationJson);
        } catch (JsonProcessingException | JmsException e) {
            log.error("Failed to send notification: {} to queue: {} using jmsTemplate.",
                    notification, jmsTemplate.getDefaultDestinationName(), e);

            return false;
        }
        return true;
    }

}
