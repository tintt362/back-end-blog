package com.trongtin.blog.identity.service;

import com.trongtin.blog.identity.event.NotificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Send new post ID to 'new-post' topic
    public void sendNewPostEvent(String postId) {
        kafkaTemplate.send("new-post", postId);
    }

    // Send new comment ID to 'new-comment' topic
    public void sendNewCommentEvent(String postId) {
        kafkaTemplate.send("new-comment", postId);
    }


}
