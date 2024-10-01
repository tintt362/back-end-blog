package com.trongtin.blog.identity.mapper;

import com.trongtin.blog.identity.dto.request.CommentRequest;
import com.trongtin.blog.identity.dto.response.CommentResponse;
import com.trongtin.blog.identity.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentRequest request);
    CommentResponse toCommentResponse(Comment comment);
}
