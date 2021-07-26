package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
    Optional<Place> findPlacesById(int id);

    List<Place> findPlacesByNameContains(String name);

    List<Place> findPlacesByLocationContains(String location);

    @Query(value = "SELECT AVG(e.star_rate) FROM review e WHERE e.place_id = ?1", nativeQuery = true)
    Optional<Double> getStartRateById(String id);
}