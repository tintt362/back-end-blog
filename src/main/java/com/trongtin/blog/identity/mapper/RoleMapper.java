package com.trongtin.blog.identity.mapper;


import com.trongtin.blog.identity.dto.request.RoleRequest;
import com.trongtin.blog.identity.dto.response.RoleResponse;
import com.trongtin.blog.identity.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
