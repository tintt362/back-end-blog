package com.trongtin.blog.identity.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    User follower; // Người dùng theo dõi( người sẽ theo dỗi người khác

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id")
    User followee; // Người dùng bị theo dõi ( người được theo dỗi)

    Instant createdDate;
}