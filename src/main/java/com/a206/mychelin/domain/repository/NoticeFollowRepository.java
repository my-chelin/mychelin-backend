package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.NoticeFollow;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeFollowRepository extends JpaRepository<NoticeFollow,Integer> {

    Optional<NoticeFollow> findByUserIdAndFollowingId(String userId, String followingId);

    List<NoticeFollow> findByFollowingIdAndIsRead(String followingId,boolean isRead);

}
