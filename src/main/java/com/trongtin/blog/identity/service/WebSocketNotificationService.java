package com.trongtin.blog.identity.service;

import com.trongtin.blog.identity.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(User user, String message) {
        messagingTemplate.convertAndSendToUser(
                user.getUsername(),
                "/queue/notifications",
                message
        );
    }
}
