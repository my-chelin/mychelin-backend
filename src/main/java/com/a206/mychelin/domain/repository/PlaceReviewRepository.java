package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaceReviewRepository extends JpaRepository<Review, Integer> {
    @Query(value = "SELECT r.id as review_id, r.star_rate, r.content, r.user_id, r.create_date, r.image, p.id as place_id, p.name as place_name, p.image as place_image " +
            "FROM review r, place p\n" +
            "where r.place_id = p.id and r.user_id=:id order by review_id desc"
            , countQuery = "SELECT COUNT(*) FROM review r, place p where r.place_id = p.id and r.user_id=:id"
            , nativeQuery = true)
    List<Object[]> getPlaceReviewsById(String id, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM review r, place p where r.place_id = p.id and r.user_id=:id"
            , nativeQuery = true)
    int getPlaceReviewsNumById(String id);

    @Query(value = "SELECT r.id as review_id, r.star_rate, r.content, r.user_id, r.create_date, r.image, u.nickname as user_nickname, u.profile_image as user_profile_image " +
            "FROM review r, user u " +
            "where r.place_id = :placeId and r.user_id = u.id order by review_id desc"
            , countQuery = "SELECT COUNT(*) FROM review r, user u where r.place_id = :placeId and r.user_id = u.id"
            , nativeQuery = true)
    List<Object[]> getPlaceReviewsByPlaceId(int placeId, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM review r, user u where r.place_id = :placeId and r.user_id = u.id"
            , nativeQuery = true)
    int getPlaceReviewsNumByPlaceId(int placeId);

    List<Review> findByPlaceIdOrderByIdDesc(int placeId);

    @Query(value = "SELECT avg(star_rate) FROM review where place_id = :placeId"
            , nativeQuery = true)
    Optional<Float> getPlaceReviewsAVGByPlaceId(int placeId);

    Optional<Review> findById(int id);
}