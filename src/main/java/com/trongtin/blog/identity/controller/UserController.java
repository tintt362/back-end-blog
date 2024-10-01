package com.trongtin.blog.identity.controller;

import com.trongtin.blog.identity.dto.request.UserCreationRequest;
import com.trongtin.blog.identity.dto.request.UserUpdateRequest;
import com.trongtin.blog.identity.dto.response.ApiResponse;
import com.trongtin.blog.identity.dto.response.UserResponse;
import com.trongtin.blog.identity.mapper.UserMapper;
import com.trongtin.blog.identity.service.FollowService;
import com.trongtin.blog.identity.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private FollowService followService;

    @PostMapping("/registration")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) throws SQLIntegrityConstraintViolationException {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .message("Create User Successfully")
                .build();
    }

    @GetMapping("/{userId}")
    private ApiResponse<UserResponse> getUsers(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .message("Get User with UserID " + userId + " Successfully")
                .build();
    }

    @GetMapping()
    public ApiResponse<List<UserResponse>> getAllUsers() {

         return ApiResponse.<List<UserResponse>>builder()
                 .result(userService.getUser()).build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId,
                                   @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId,userUpdateRequest))
                .message("Update User Successfully")
                .build();
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User deleted";
    }

    @PostMapping("/{followeeId}/follow")
    public ResponseEntity<ApiResponse<String>> followUser(@PathVariable String followeeId) {
        followService.followUser(followeeId);
        return ResponseEntity.ok(ApiResponse.<String>builder().result("Followed successfully").build());
    }

    @DeleteMapping("/{followeeId}/follow")
    public ResponseEntity<ApiResponse<String>> unfollowUser(@PathVariable String followeeId) {
        // Implement unfollow logic
        return ResponseEntity.ok(ApiResponse.<String>builder().result("Unfollowed successfully").build());
    }

    @GetMapping("/{foloweeId}/follow")
    public ApiResponse<List<String>> getAllFollowers(@PathVariable String foloweeId) {
        return ApiResponse.<List<String>>builder()
                .result(followService.getAllFollower(foloweeId)).build();
    }

    @GetMapping("/{folowerId}/followee")
    public ApiResponse<List<String>> getAllFollowees(@PathVariable String folowerId) {
        return ApiResponse.<List<String>>builder()
                .result(followService.getAllFollowee(folowerId)).build();
    }
}
