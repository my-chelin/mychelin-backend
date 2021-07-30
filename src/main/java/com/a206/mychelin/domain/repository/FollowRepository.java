package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.FollowPK;
import com.a206.mychelin.domain.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, FollowPK> {
    int countByUserIdAndAccept(String userId, boolean accept);

    int countByFollowingIdAndAccept(String followingId, boolean accept);

    int countByUserIdAndFollowingIdAndAccept(String userId, String followingId, boolean accept);

    @Query(value = "select u.profile_image , u.nickname, f.following_id, u.bio from user u, follow f where f.following_id = u.id and f.user_id = :user_id and f.accept = true", nativeQuery = true)
    ArrayList<Object[]> findFollowsByUserId(@Param("user_id") String userId); //유저가 팔로우하는 팔로워 목록

    Optional<Follow> findFollowByUserIdAndFollowingId(@Param("user_id") String userId, @Param("following_id") String followingId);

    @Query(value = "select u.profile_image, u.nickname, f.following_id, u.bio from user u, follow f where f.following_id = u.id and f.user_id = ( select id from user where nickname = :nickname) and f.accept = true", nativeQuery = true)
    ArrayList<Object[]> findFollowsByUserNickname(@Param("nickname") String userNickname);
}