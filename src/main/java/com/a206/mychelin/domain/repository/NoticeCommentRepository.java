package com.a206.mychelin.domain.repository;


import com.a206.mychelin.domain.entity.NoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeCommentRepository extends JpaRepository<NoticeComment, Integer> {

    @Query(value = "select nc.id as id, nc.comment_id as commentId, c.message as commentMessage,\n" +
            "u.id as writerId, u.nickname as writerNickname,\n" +
            "p.id as postId, p.content as postContent, nc.is_read as isRead, nc.add_time as addTime\n" +
            "from notice_comment nc, comment c, post p, user u\n" +
            "where nc.is_read=false and p.user_id=:id and nc.comment_id=c.comment_id and p.id=c.post_id and u.id=c.writer_id and c.writer_id != :id", nativeQuery = true)
    List<Object[]> getCommentByUserId(String id);

    /*
        commentid를 바탕으로, 해당 comment가 달려 있는 post를 작성한 유저
    */
    @Query(value = "select u.nickname from user u, post p, comment c where p.id = c.post_id and c.comment_id=:commentId and p.user_id=u.id;", nativeQuery = true)
    String getPostWriterNicknameByCommentId(int commentId);

}