package com.trongtin.blog.identity.service;

import com.trongtin.blog.identity.dto.request.CommentRequest;
import com.trongtin.blog.identity.dto.response.CommentResponse;
import com.trongtin.blog.identity.entity.Comment;
import com.trongtin.blog.identity.entity.Post;
import com.trongtin.blog.identity.entity.User;
import com.trongtin.blog.identity.exception.AppException;
import com.trongtin.blog.identity.exception.ErrorCode;
import com.trongtin.blog.identity.mapper.CommentMapper;
import com.trongtin.blog.identity.repository.CommentRepository;
import com.trongtin.blog.identity.repository.PostRepository;
import com.trongtin.blog.identity.repository.RoleRepository;
import com.trongtin.blog.identity.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private RoleRepository roleRepository;

    public CommentResponse createComment(CommentRequest commentRequest, String postId) {
        // tim bai post co ton tai ?
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_FOUND)
        );
        // tim user co ton tai hay khong
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Comment comment = Comment.builder()
                .createDate(Instant.now())
                .modifiedDate(Instant.now())
                .content(commentRequest.getComment())
                .post(post)

                .user(user)
                .build();
        log.info("Comment created : " + comment);
        commentRepository.save(comment);

        // Gửi sự kiện comment mới tới Kafka topic 'new-comment'
        kafkaProducer.sendNewCommentEvent(post.getId());
        return commentMapper.toCommentResponse(comment);
    }

    public CommentResponse updateComment(CommentRequest commentRequest, String commentId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        // Only the comment owner can update the comment
        if (!comment.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Update the comment
        comment.setContent(commentRequest.getComment());
        comment.setModifiedDate(Instant.now());
        commentRepository.save(comment);

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .modifiedDate(comment.getModifiedDate())
                .userId(comment.getUser().getId())
                .postId(comment.getPost().getId())
                .build();
    }

    public void deleteComment(String commentId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        var authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        // Check if the user is the owner of the comment or has the admin role
        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        boolean isCommentOwner = comment.getUser().getId().equals(userId);

        // Only the comment owner or a user with admin rights can delete the comment
        if (!isCommentOwner && !isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        commentRepository.delete(comment);
    }
}
