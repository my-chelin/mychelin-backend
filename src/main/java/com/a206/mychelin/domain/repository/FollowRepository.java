package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.FollowPK;
import com.a206.mychelin.domain.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, FollowPK> {
    int countByUserIdAndAccept(String userId, boolean accept);

    int countByFollowingIdAndAccept(String followingId, boolean accept);

    int countByUserIdAndFollowingIdAndAccept(String userId, String followingId, boolean accept);

    Optional<Follow> findFollowByUserIdAndFollowingId(String userId, String followingId);

    @Query(value = "select u.profile_image, u.nickname, u.bio from user u, follow f where f.following_id = u.id and f.user_id = ( select id from user where nickname = :nickname) and f.accept = true", nativeQuery = true)
    ArrayList<Object[]> findFollowingByUserNickname(@Param("nickname") String userNickname);

    @Query(value = "select u.profile_image, u.nickname, u.bio from user u, follow f where f.user_id = u.id and f.following_id = ( select id from user where nickname = :nickname) and f.accept = true", nativeQuery = true)
    ArrayList<Object[]> findFollowerByUserNickname(@Param("nickname") String userNickname);

    @Query(value = "select nickname, profile_image from user where id in (select user_id from follow f where following_id = :following_id and accept = false);", nativeQuery = true)
    ArrayList<String[]> findUserIdByUserId(@Param("following_id") String followingId);

    @Transactional
    void deleteAllByUserIdAndFollowingIdAndAccept(String userId, String followingId, boolean accept);
}