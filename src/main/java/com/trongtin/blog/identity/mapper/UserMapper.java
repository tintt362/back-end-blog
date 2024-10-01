package com.trongtin.blog.identity.mapper;

import com.trongtin.blog.identity.dto.request.UserCreationRequest;
import com.trongtin.blog.identity.dto.request.UserUpdateRequest;
import com.trongtin.blog.identity.dto.response.UserResponse;
import com.trongtin.blog.identity.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest creationRequest);

    UserResponse toUserResponse(User user);
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
