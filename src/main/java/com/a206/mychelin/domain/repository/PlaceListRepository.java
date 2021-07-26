package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.PlaceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceListRepository extends JpaRepository<PlaceList, Integer> {
    Optional<PlaceList> findById(int id);

    List<PlaceList> findByTitleContains(String title);

    @Query(value = "select pi.placelist_id, pi.place_id, pi.contributor_id, " +
            "p.name, p.description, p.latitude, p.longitude, p.phone, p.location,operation_hours, p.category_id " +
            "from placelist_item pi, place p where pi.placelist_id=:id and pi.place_id=p.id", nativeQuery = true)
    List<Object[]> getPlaceListItemsById(@Param("id") int id);
}