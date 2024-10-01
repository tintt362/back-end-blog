package com.trongtin.blog.identity.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String title;
    String content;
    Instant createdDate;
    Instant modifiedDate;
    Long likeCount;
    @Lob
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;


    @ManyToMany(mappedBy = "likedPosts")
    Set<User> likedByUsers = new HashSet<>(); // Người dùng đã thích bài viết

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
    Set<Comment> comments = new HashSet<>();
}
