package com.trongtin.blog.identity.controller;

import com.trongtin.blog.identity.dto.request.email.SendEmailRequest;
import com.trongtin.blog.identity.dto.request.email.Sender;
import com.trongtin.blog.identity.dto.request.email.To;
import com.trongtin.blog.identity.entity.Comment;
import com.trongtin.blog.identity.entity.Post;
import com.trongtin.blog.identity.entity.User;
import com.trongtin.blog.identity.event.NotificationEvent;
import com.trongtin.blog.identity.exception.AppException;
import com.trongtin.blog.identity.exception.ErrorCode;
import com.trongtin.blog.identity.repository.PostRepository;
import com.trongtin.blog.identity.service.EmailService;
import com.trongtin.blog.identity.service.NotificationService;
import com.trongtin.blog.identity.service.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {

    private final EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private WebSocketNotificationService webSocketNotificationService;

    @KafkaListener(topics = "notification-delivery", groupId = "group_id")
    public void handleNotification(NotificationEvent event) {
        if ("EMAIL".equals(event.getChannel())) {
            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .sender(new Sender("Thaitrongtindev", "thaitrongtindev@gmail.com"))
                    .to(Collections.singletonList(new To(event.getRecipient(), "User")))
                    .subject(event.getSubject())
                    .htmlContent(event.getBody())
                    .build();

            emailService.sendEmailUsingBrevo(emailRequest)
                    .subscribe(
                            result -> log.info("Email sent: {}", result),
                            error -> log.error("Failed to send email", error)
                    );
        }
    }

    @KafkaListener(topics = "new-post", groupId = "notification-group")
    public void consumeNewPost(String postId) {
        Post post = findPostById(postId);

        // Notify followers
        post.getUser().getFollowers().forEach(follow -> {
            User followerUser = follow.getFollower(); // Get the User from the Follow entity
            notificationService.createNotification(followerUser, "New post from " + post.getUser().getUsername());
            webSocketNotificationService.sendNotification(followerUser, "New post from " + post.getUser().getUsername());
        });

    }

    @KafkaListener(topics = "new-comment", groupId = "notification-group")
    public void consumeNewComment(String postId) {
        Post post = findPostById(postId);
        User postOwner = post.getUser();

        // Notify post owner
        notificationService.createNotification(postOwner, "Your post has a new comment");
        webSocketNotificationService.sendNotification(postOwner, "Your post has a new comment");

        // Notify commenters
        post.getComments().stream()
                .map(Comment::getUser)
                .distinct()
                .forEach(commenter -> {
                    notificationService.createNotification(commenter, "A post you commented on has a new comment");
                    webSocketNotificationService.sendNotification(commenter, "A post you commented on has a new comment");
                });
    }

    private Post findPostById(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
    }
}
