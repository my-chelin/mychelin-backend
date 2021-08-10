package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.PlaceList;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceListRepository extends JpaRepository<PlaceList, Integer> {
    Optional<PlaceList> findById(int id);

    @Query(value = "SELECT *\n" +
            ",(select u.nickname from user u where u.id=pl.user_id) as nickname \n" +
            ", (select count(*) from placelist_item pi where pi.placelist_id=pl.id) as total_item_cnt \n" +
            "FROM placelist pl where pl.title like :title order by pl.id"
            , countQuery = "SELECT count(*)FROM placelist pl where pl.title like :title"
            , nativeQuery = true)
    List<Object[]> getPlaceListTitleContainsOrderById(String title, Pageable pageable);

    long countByTitleContains(String title);

    @Query(value = "select pi.placelist_id, pi.place_id, pi.contributor_id, " +
            "p.name, p.description, p.latitude, p.longitude, p.phone, p.location,operation_hours, p.category_id, p.image " +
            "from placelist_item pi, place p where pi.placelist_id=:id and pi.place_id=p.id order by p.id"
            , countQuery = "SELECT COUNT(*) from placelist_item pi, place p where pi.placelist_id=:id and pi.place_id=p.id order by p.id"
            , nativeQuery = true)
    List<Object[]> getPlaceListItemsById(@Param("id") int id, Pageable pageable);

    @Query(value = "SELECT COUNT(*) from placelist_item pi, place p where pi.placelist_id=:id and pi.place_id=p.id order by p.id"
            , nativeQuery = true)
    int getPlaceListItemsNumById(int id);

    long countPlaceListsByUserId(@Param("user_id") String userId);
}