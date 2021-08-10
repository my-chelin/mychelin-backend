package com.a206.mychelin.domain.repository;


import com.a206.mychelin.domain.entity.NoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeCommentRepository extends JpaRepository<NoticeComment,Integer> {
}
