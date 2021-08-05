package com.a206.mychelin.domain.repository;


import com.a206.mychelin.domain.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage,Integer> {
    @Query(value="select image from post_image where post_id = :post_id order by post_image.order" , nativeQuery = true)
    List<String> findPostsByPostIdOrderByOrder(@Param("post_id") int postId);
}
