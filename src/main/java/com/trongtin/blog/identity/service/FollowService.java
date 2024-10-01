package com.trongtin.blog.identity.service;

import com.trongtin.blog.identity.entity.Follow;
import com.trongtin.blog.identity.entity.User;
import com.trongtin.blog.identity.exception.AppException;
import com.trongtin.blog.identity.exception.ErrorCode;
import com.trongtin.blog.identity.repository.FollowRepository;
import com.trongtin.blog.identity.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class FollowService {


    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    public void followUser(String followeeId) {
        String followerId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (followerId.equals(followeeId)) {
            throw new RuntimeException("Error");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Follow follow = Follow.builder()
                .follower(follower)
                .followee(followee)
                .createdDate(Instant.now())
                .build();

        followRepository.save(follow);
    }

    // lay danh sach ma user khac dang dang theo doi minh
    public List<String> getAllFollower(String followeeId) {
        return followRepository.findFollowersUsernames(followeeId);
    }

    // xem minh dang follow nhung ai
    public List<String> getAllFollowee(String followerId) {
        return followRepository.findAllFolloweeUsernames(followerId);
    }

    //
}
