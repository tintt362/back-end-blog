package com.trongtin.blog.identity.controller;

import com.trongtin.blog.identity.dto.request.CommentRequest;
import com.trongtin.blog.identity.dto.response.ApiResponse;
import com.trongtin.blog.identity.dto.response.CommentResponse;
import com.trongtin.blog.identity.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/posts/{postId}")
    public ApiResponse<CommentResponse> createComment(@RequestBody CommentRequest commentRequest, @PathVariable String postId) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.createComment(commentRequest, postId)).build();
    }

    @PutMapping("/{commentId}")
    public ApiResponse<CommentResponse> updateComment(@PathVariable String commentId, @RequestBody CommentRequest commentRequest) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.updateComment(commentRequest, commentId)).build();
    }
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.<Void>builder()
                .message("Comment deleted successfully")
                .build();
    }
}
