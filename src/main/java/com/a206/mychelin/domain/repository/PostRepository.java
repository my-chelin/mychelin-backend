package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findPostsByUserIdOrderByCreateDateDesc(String user_id);

    Optional<Post> findPostById(int id);

    List<Post> findPostsByContentContains(String content);

    int deletePostById(int id);
}