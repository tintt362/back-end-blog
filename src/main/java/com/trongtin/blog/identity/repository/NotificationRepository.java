package com.trongtin.blog.identity.repository;

import com.trongtin.blog.identity.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, String> {
}
