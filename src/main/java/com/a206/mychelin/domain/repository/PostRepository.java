package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.Place;
import com.a206.mychelin.domain.entity.PlaceList;
import com.a206.mychelin.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> getPostsByUserId(String user_id);

    List<Post> findPostsByUserIdOrderByCreateDateDesc(String user_id);

    Optional<Post> findPostById(int id);//특정 포스트 정보 가지고오기

    List<Post> findPostsByTitleOrContentContains(String title, String content);

    List<Post> findPostByTitleContains(String title);

    List<Post> findPostsByContentContains(String content);

    int deletePostById(int id);

    @Query(value = "SELECT * FROM place p WHERE p.id = ?1", nativeQuery = true)
    Place getPlaceInfoByPlaceId(int place_id);

    @Query(value = "SELECT * FROM placelist pl WHERE e.place_id = ?1", nativeQuery = true)
    PlaceList getPlaceListInfoByPlaceListId(int placelist_id);

    @Query(value = "INSERT INTO post (user_id, title, content) VALUES (:user_id, :title, :content)",nativeQuery = true)
    Optional<Post> saveText(@Param("user_id") String user_id, @Param("title") String title,@Param("content") String content);
}
