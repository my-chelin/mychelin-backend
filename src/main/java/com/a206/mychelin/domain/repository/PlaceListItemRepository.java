package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.Place;
import com.a206.mychelin.domain.entity.PlaceListItem;
import com.a206.mychelin.domain.entity.PlaceListItemPK;
import com.a206.mychelin.web.dto.PlaceListITemsByNicknameResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaceListItemRepository extends JpaRepository<PlaceListItem, PlaceListItemPK> {
    Optional<PlaceListItem> findByPlaceListItemPK(PlaceListItemPK placeListItemPK);

    Optional<PlaceListItem> findByPlaceListItemPKAndContributorId(PlaceListItemPK placeListItemPK, String ContributorId);

    @Query(value = "SELECT pi.placelist_id, pl.title, pi.place_id, p.name, p.description,p.latitude,p.longitude,p.phone,p.location,p.operation_hours, p.category_id,p.image,pi.contributor_id \n" +
            " FROM placelist_item pi, placelist pl,place p  where pi.contributor_id=:contributorId and pi.placelist_id=pl.id and p.id=pi.place_id order by placelist_id, place_id"
            ,countQuery = "SELECT count(*) FROM placelist_item pi, placelist pl,place p  where pi.contributor_id=:contributorId and pi.placelist_id=pl.id and p.id=pi.place_id"
            , nativeQuery = true)
    List<Object[]> getMyPlacelistByContributorIdOrderByPlaceId(String contributorId, Pageable pageable);

    @Query(value = "SELECT count(*) FROM placelist_item pi, placelist pl,place p  where pi.contributor_id=:contributorId and pi.placelist_id=pl.id and p.id=pi.place_id"
            , nativeQuery = true)
    long getCountByContributorId(String contributorId);
}