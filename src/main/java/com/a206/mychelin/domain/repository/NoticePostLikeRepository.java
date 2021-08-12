package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.NoticePostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticePostLikeRepository extends JpaRepository<NoticePostLike,Integer> {

    Optional<NoticePostLike> findByPostIdAndUserId(int postId,String userId);

    List<NoticePostLike> findByUserIdAndIsRead(String userId,boolean isRead);

    List<Object[]> getPostLikeByUserId(String userId);

}
