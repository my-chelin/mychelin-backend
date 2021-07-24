package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> getPostsByUserId(String user_id);

    List<Post> findPostsByUserIdOrderByCreateDateDesc(String user_id);

    Optional<Post> findPostById(int id);//특정 포스트 정보 가지고오기

    List<Post> findPostsByTitleOrContentContains(String title, String content);

    List<Post> findPostByTitleContains(String title);

    List<Post> findPostsByContentContains(String content);
}