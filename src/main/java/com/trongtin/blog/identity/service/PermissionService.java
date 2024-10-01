package com.trongtin.blog.identity.service;

import com.trongtin.blog.identity.dto.request.PermissionRequest;
import com.trongtin.blog.identity.dto.response.PermissionResponse;
import com.trongtin.blog.identity.entity.Permission;
import com.trongtin.blog.identity.mapper.PermissionMapper;
import com.trongtin.blog.identity.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionRequest permissionRequest) {
        Permission permission = permissionMapper.toPermission(permissionRequest);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getALl() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(
                permission -> permissionMapper.toPermissionResponse(permission)
        ).toList();
    }

    public void deletePermission(String permissionId) {
        permissionRepository.deleteById(permissionId);
    }
}
