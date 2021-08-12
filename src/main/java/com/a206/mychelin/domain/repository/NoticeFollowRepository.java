package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.NoticeFollow;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticeFollowRepository extends JpaRepository<NoticeFollow,Integer> {

    Optional<NoticeFollow> findByUserIdAndFollowingId(String userId, String followingId);

    List<NoticeFollow> findByFollowingIdAndIsRead(String followingId,boolean isRead);

    @Query(value = "select nf.id as id, nf.user_id as userId, u.nickname as userNickname,\n" +
            "nf.is_read as isRead, nf.add_time as addTime\n" +
            "from notice_follow nf, user u \n" +
            " where nf.is_read=false and nf.user_id = u.id and nf.is_read=false and nf.following_id =:id ", nativeQuery = true)
    List<Object[]> getNoticeFollowById(String id);
}
