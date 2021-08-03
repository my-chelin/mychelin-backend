package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
    Optional<Place> findPlacesById(int id);

    List<Place> findPlacesByNameContainsOrderById(String name, Pageable pageable);

    List<Place> findPlacesByLocationContainsOrderById(String location, Pageable pageable);

    @Query(value = "SELECT ROUND(AVG(e.star_rate),1) FROM review e WHERE e.place_id = ?1", nativeQuery = true)
    Optional<Double> getStartRateById(String id);

    long countByNameContains(String name);

    long countByLocationContains(String location);

    @Query(value = "SELECT *,(select avg(r.star_rate) from review r where r.place_id = DATA.id) as star_rate  \n" +
            "FROM (" +
            "SELECT ( 6371 * acos( cos( radians( :lat ) ) * cos( radians( place.latitude) ) * cos( radians( place.longitude ) - radians(:lng) ) + sin( radians(:lat) ) * sin( radians(place.latitude) ) ) ) AS distance, place.*\n" +
            "FROM place\n" +
            ") DATA\n" +
            "WHERE DATA.distance < :distance order by DATA.distance", nativeQuery = true)
    List<Object[]> getPlaceByCoordinate(float lat, float lng, float distance);
}