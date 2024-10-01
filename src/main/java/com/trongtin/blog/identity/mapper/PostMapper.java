package com.trongtin.blog.identity.mapper;


import com.trongtin.blog.identity.dto.request.PostRequest;
import com.trongtin.blog.identity.dto.request.RoleRequest;
import com.trongtin.blog.identity.dto.response.PostResponse;
import com.trongtin.blog.identity.dto.response.RoleResponse;
import com.trongtin.blog.identity.entity.Post;
import com.trongtin.blog.identity.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toPost(PostRequest request);
    PostResponse toPostResponse(Post post);
}
