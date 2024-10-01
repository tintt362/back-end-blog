package com.trongtin.blog.identity.service;

import com.trongtin.blog.identity.constant.PredefinedRole;
import com.trongtin.blog.identity.dto.request.UserCreationRequest;
import com.trongtin.blog.identity.dto.request.UserUpdateRequest;
import com.trongtin.blog.identity.dto.response.UserResponse;
import com.trongtin.blog.identity.entity.Role;
import com.trongtin.blog.identity.entity.User;
import com.trongtin.blog.identity.event.NotificationEvent;
import com.trongtin.blog.identity.exception.AppException;
import com.trongtin.blog.identity.exception.ErrorCode;
import com.trongtin.blog.identity.mapper.UserMapper;
import com.trongtin.blog.identity.repository.RoleRepository;
import com.trongtin.blog.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public UserResponse createUser(UserCreationRequest request) throws SQLIntegrityConstraintViolationException {
        User user = userMapper.toUser(request);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        } else if (userRepository.existsByEmail(user.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);


        // gui email khi tao user thanh cong
        NotificationEvent notificationEvent = NotificationEvent
                .builder()
                .channel("EMAIL")
                .recipient(request.getEmail())
                .subject("Welcome to bookteria")
                .body("Hello " + request.getFirstName() + " " + request.getLastName())
                .build();
        kafkaTemplate.send("notification-delivery", notificationEvent);
        // Add role to the user


     //   roleRepository.findByName(PredefinedRole.USER_ROLE).ifPresent(roles::add);


        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User not found")
        ));
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('APPROVE_POST')")
    public List<UserResponse> getUser() {
        var context = SecurityContextHolder.getContext();
        log.info("User logged in: {}", context.getAuthentication().getName());
        log.info("User ROLE in: {}", context.getAuthentication().getAuthorities());
        return userRepository.findAll().stream().map(
                user -> userMapper.toUserResponse(user)
        ).toList();
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id).get();
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
