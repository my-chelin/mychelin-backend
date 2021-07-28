package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, String> {
    @Query(value = "SELECT c.comment_id, u.nickname, c.message, c.create_date FROM post p, comment c, user u WHERE p.id = c.post_id and p.id = :post_id and u.id = c.writer_id", nativeQuery = true)
    List<Object[]> findCommentsByPostId(@Param("post_id") int postId);

    Optional<Comment> findCommentByCommentId(@Param("comment_id") int commentId);

    int deleteCommentByCommentId(int commentId);
}