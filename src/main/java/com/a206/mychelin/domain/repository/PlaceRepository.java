package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.Place;
import io.lettuce.core.dynamic.annotation.Param;
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
    Optional<Double> getStarRateById(String id);

    long countByNameContains(String name);

    long countByLocationContains(String location);

    @Query(value = "SELECT *,(select avg(r.star_rate) from review r where r.place_id = DATA.id) as star_rate  \n" +
            "FROM (" +
            "SELECT ( 6371 * acos( cos( radians( :lat ) ) * cos( radians( place.latitude) ) * cos( radians( place.longitude ) - radians(:lng) ) + sin( radians(:lat) ) * sin( radians(place.latitude) ) ) ) AS distance, place.*\n" +
            "FROM place\n" +
            ") DATA\n" +
            "WHERE DATA.distance < :distance order by DATA.distance", nativeQuery = true)
    List<Object[]> getPlaceByCoordinate(double lat, double lng, float distance);

    @Query(value = "SELECT p.id, p.name, c.emoji, p.description, p.location, r.content, r.star_rate\n" +
            "FROM category c, review r inner join place p\n" +
            "ON r.place_id = p.id\n" +
            "WHERE r.user_id = :similarUserId\n" +
            "AND p.category_id = c.id\n" +
            "AND r.place_id not in (select r2.place_id\n" +
            "FROM review r2\n" +
            "WHERE r2.user_id = :myId)\n" +
            "ORDER BY star_rate desc\n" +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> findPlacesBySimilarUsersRecommendation(@Param("myId") String myId, @Param("similarUserId") String similarUserId);

    @Query(value = "select p.id, p.name, c.emoji, p.description, p.location, ifnull((select avg(r.star_rate) from review r where r.place_id = p.id group by r.place_id), 0)\n" +
            "from category c, place p\n" +
            "join bookmark_place bp\n" +
            "on bp.place_id = p.id\n" +
            "where p.category_id = c.id\n" +
            "order by bp.add_date desc\n" +
            "limit 10", nativeQuery = true)
    List<Object[]> findRecentlyAddedPlaces();
}