package com.trongtin.blog.identity.repository;

import com.trongtin.blog.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, String> {
   boolean existsByUsername(String username);
   boolean existsByEmail(String email);

   Optional<User> findByUsername(String username);
}
