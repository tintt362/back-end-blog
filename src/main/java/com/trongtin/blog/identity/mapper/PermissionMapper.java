package com.trongtin.blog.identity.mapper;

import com.trongtin.blog.identity.dto.request.PermissionRequest;
import com.trongtin.blog.identity.dto.response.PermissionResponse;
import com.trongtin.blog.identity.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
