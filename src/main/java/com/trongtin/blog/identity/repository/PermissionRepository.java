package com.trongtin.blog.identity.repository;

import com.trongtin.blog.identity.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
