package com.technoserv.service;

import com.technoserv.dto.Notification;

/**
 *
 */
public interface NotificationService {

    boolean send(Notification notification);

}
