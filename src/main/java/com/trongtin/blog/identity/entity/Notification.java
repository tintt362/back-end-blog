package com.trongtin.blog.identity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String message;

    @ManyToOne
    private User recipient; // Người nhận thông báo

    private boolean read = false; // Trạng thái đã đọc

    private LocalDateTime createdAt = LocalDateTime.now(); // Thời gian tạo thông báo

}
