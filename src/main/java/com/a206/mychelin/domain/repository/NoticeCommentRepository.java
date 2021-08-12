package com.a206.mychelin.domain.repository;


import com.a206.mychelin.domain.entity.NoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeCommentRepository extends JpaRepository<NoticeComment,Integer> {

    /*
        commentid를 바탕으로, 해당 comment가 달려 있는 post를 작성한 유저
     */

//    List<NoticeComment> getNoticeComment(String commentId);

}
