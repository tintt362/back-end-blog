package com.trongtin.blog.identity.service;

import com.trongtin.blog.identity.entity.Notification;
import com.trongtin.blog.identity.entity.User;
import com.trongtin.blog.identity.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(User recipient, String message) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }
}
