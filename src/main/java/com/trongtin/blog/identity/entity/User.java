package com.trongtin.blog.identity.entity;

import com.trongtin.blog.identity.validator.DobConstraint;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;
    String password;

    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String email;

    @Column(name = "email_verified", nullable = false, columnDefinition = "boolean default false")
    boolean emailVerified;

    LocalDate dob;
    @ManyToMany
    Set<Role> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    List<Post> posts = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    Set<Post> likedPosts = new HashSet<>(); // Danh sách các bài viết mà người dùng đã thích

    //comment
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    Set<Comment> comments = new HashSet<>();


    @OneToMany(mappedBy = "follower")
    Set<Follow> following = new HashSet<>(); // Những người mà người dùng đang theo dõi

    @OneToMany(mappedBy = "followee")
    Set<Follow> followers = new HashSet<>(); // Những người theo dõi người dùng

}
