package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.NoticePostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticePostLikeRepository extends JpaRepository<NoticePostLike, Integer> {

    Optional<NoticePostLike> findByPostIdAndUserId(int postId, String userId);

    List<NoticePostLike> findByUserIdAndIsRead(String userId, boolean isRead);

    @Query(value = "select npl.id as id, npl.post_id as postId, p.content as postContent,\n" +
            "p.place_id as placeId, p.placelist_id as placeListId,\n" +
            " npl.user_id as postLikeUserId, (select nickname from user where id = npl.user_id) as postLikeUserNickname, npl.is_read as isRead, npl.add_time as addTime\n" +
            " from notice_post_like npl, post p where p.user_id=:userId and npl.post_id=p.id and npl.is_read=false and npl.user_id != :userId", nativeQuery = true)
    List<Object[]> getPostLikeByUserId(String userId);

}