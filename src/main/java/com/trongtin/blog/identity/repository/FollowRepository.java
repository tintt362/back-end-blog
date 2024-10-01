package com.trongtin.blog.identity.repository;

import com.trongtin.blog.identity.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, String> {

    @Query("SELECT f.follower.username FROM Follow f WHERE f.followee.id = :followeeId")
    List<String> findFollowersUsernames(@Param("followeeId") String followeeId);

    @Query("SELECT f.followee.username FROM Follow f WHERE f.follower.id = :followerId")
    List<String> findAllFolloweeUsernames(@Param("followerId") String followerId);
}
