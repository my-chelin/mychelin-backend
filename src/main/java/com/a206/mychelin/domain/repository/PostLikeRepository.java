package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.PostLikePK;
import com.a206.mychelin.domain.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikePK> {
    @Query(value = "select count(u.id) from Post p, PostLike l, User u where p.id = l.postId and p.userId = u.id and u.id = :id")
    Long getLikes(@Param("id") String id);

    int countPostLikesByPostId(int postId);

    Optional<PostLike> findPostLikeByPostIdAndUserId(int postId, String userId);
}