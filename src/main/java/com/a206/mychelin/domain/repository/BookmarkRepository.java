package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.BookmarkPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookmarkPlace, Integer> {
    @Query(value = "select p.id, p.name, p.description, p.location, p.image from bookmark_place bp join place p where bp.place_id = p.id and bp.user_id = :userId", nativeQuery = true)
    List<Object[]> findBookmarkPlacesByUserId(String userId);

    Optional<BookmarkPlace> findBookmarkPlaceByUserIdAndPlaceId(@Param("user_id") String userId, @Param("place_id") int placeId);
}