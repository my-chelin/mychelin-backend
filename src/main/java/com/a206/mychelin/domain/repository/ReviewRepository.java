package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    int countAllByPlaceId(@Param("placeId") int placeId);
}
