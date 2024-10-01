package com.trongtin.blog.identity.repository;

import com.trongtin.blog.identity.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository  extends JpaRepository<Post, String> {

    boolean existsPostById(String id);
    Optional<Post> findByTitle(String title);
}
