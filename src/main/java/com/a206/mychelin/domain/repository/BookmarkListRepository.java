package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.BookmarkPlacelist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkListRepository extends JpaRepository<BookmarkPlacelist, Integer> {
    Optional<BookmarkPlacelist> findBookmarkPlacelistByUserIdAndPlacelistId(@Param("user_id") String userId, @Param("placelist_id") int placelistId);

    @Query(value = "select distinct bp.placelist_id, (select nickname from user u where u.id = p.user_id) , p.title, (select count(pli.place_id) from placelist_item pli where pli.placelist_id = p.id) , p.create_date from placelist p, bookmark_placelist bp where bp.placelist_id = p.id and bp.user_id = :user_id", nativeQuery = true)
    List<Object[]> findBookmarkPlacelistsByUserIdOrderByAddDate(@Param("user_id") String userId);

    @Query(value = "select p.id, p.name, p.description, p.location, p.image from bookmark_place bp join place p where bp.place_id = p.id and bp.user_id = :userId", nativeQuery = true)
    List<Object[]> findBookmarkPlacelistsByUserId(String userId);


}
