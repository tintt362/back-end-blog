package com.trongtin.blog.identity.repository;

import com.trongtin.blog.identity.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
}
