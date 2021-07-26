package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.PostLikePK;
import com.a206.mychelin.domain.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikePK> {
    String query = "select count(u.id) from post p, post_like l, user u where p.id = l.post_id and p.user_id = u.id and u.id = :id";
    @Query(value = "select count(u.id) from Post p, PostLike l, User u where p.id = l.postId and p.userId = u.id and u.id = :id")
    Long getLikes(@Param("id") String id);
}