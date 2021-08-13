package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.Place;
import com.a206.mychelin.domain.entity.PlaceList;
import com.a206.mychelin.domain.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> getPostsByUserId(String userId);

    @Query(value = "select p.id, u.nickname, ifnull ( (select count(user_id) from follow f where f.following_id = u.id and f.accept = true),0) , p.content, p.create_date, ifnull ((select count(user_id) from post_like where p.id = post_id and user_id is not null group by post_id), 0) ,(select count(message) from comment where p.id = comment.post_id ), p.place_id, p.placelist_id " +
            " from user u join post p where p.user_id = u.id and u.nickname = :userNickname order by p.create_date desc", nativeQuery = true)
    List<Object[]> findPostsByUserNicknameOrderByCreateDateDesc(@Param("userNickname") String userNickname);

    @Query(value = "select p.id, u.nickname, ifnull ( (select count(user_id) from follow f where f.following_id = u.id and f.accept = true),0) , p.content, p.create_date, ifnull ((select count(user_id) from post_like where p.id = post_id and user_id is not null group by post_id), 0) ,(select count(message) from comment where p.id = comment.post_id ), p.place_id, p.placelist_id " +
            "from user u join post p where p.user_id = u.id and p.id = :postId", nativeQuery = true)
    List<Object[]> findPostInfoByPostId(@Param("postId") int postId);//특정 포스트 정보 가지고오기

    Optional<Post> findPostById(int postId);

    @Query(value = "select p.id, u.nickname, ifnull ( (select count(user_id) from follow f where f.following_id = u.id and f.accept = true),0) , p.content, p.create_date, ifnull ((select count(user_id) from post_like where p.id = post_id and user_id is not null group by post_id), 0) ,(select count(message) from comment where p.id = comment.post_id ), p.place_id, p.placelist_id from user u join post p where p.user_id = u.id  group by p.id order by p.create_date desc", nativeQuery = true)
    List<Object[]> findAllPost(Pageable pageable);

    List<Post> findPostsByContentContains(String content, Pageable pageable);

    int deletePostById(int id);

    @Query(value = "SELECT * FROM place p WHERE p.id = ?1", nativeQuery = true)
    Place getPlaceInfoByPlaceId(int placeId);

    @Query(value = "SELECT * FROM placelist pl WHERE e.place_id = ?1", nativeQuery = true)
    PlaceList getPlaceListInfoByPlaceListId(int placeListId);

    @Query(value = "INSERT INTO post (user_id, content) VALUES (:user_id, :content)", nativeQuery = true)
    Optional<Post> saveText(@Param("user_id") String userId, @Param("content") String content);

    @Query(value = "select p.id, u.nickname, ifnull ( (select count(user_id) from follow f where f.following_id = u.id and f.accept = true),0) , p.content, p.create_date, ifnull ((select count(user_id) from post_like where p.id = post_id and user_id is not null group by post_id), 0) ,(select count(message) from comment where p.id = comment.post_id ), p.place_id, p.placelist_id " +
            "from user u join post p where p.user_id = u.id and u.id in (select following_id from follow where user_id = :userId) group by p.id order by p.create_date desc", nativeQuery = true)
    List<Object[]> findPostsByFollowingUsersOrderByCreateDateDesc(@Param("userId") String userId, Pageable pageable);

    @Query(value = "select COUNT(p.id) from post p where p.user_id in (select f.following_id from follow f where f.user_id = :userId)", nativeQuery = true)
    long countPostsByFollowingUsers(@Param("userId") String userId);
}