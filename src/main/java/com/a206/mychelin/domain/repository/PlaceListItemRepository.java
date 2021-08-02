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

    @Query(value = "SELECT distinct pi.placelist_id, pl.title, pi.contributor_id\n" +
            ", (select count(*) from placelist_item pii where pii.contributor_id = pi.contributor_id and pi.placelist_id= pii.placelist_id) as contrubute_item_cnt\n" +
            ", (select count(*) from placelist_item pii where  pi.placelist_id= pii.placelist_id) as total_item_cnt\n" +
            ", pl.create_date\n" +
            " FROM placelist_item pi, placelist pl where pi.placelist_id = pl.id and pi.contributor_id=:contributorId order by pi.placelist_id"
            ,countQuery = "select count(*) from (SELECT distinct pi.placelist_id, pl.title, pi.contributor_id\n" +
            " FROM placelist_item pi, placelist pl where pi.placelist_id = pl.id and pi.contributor_id=:contributorId ) as data"
            , nativeQuery = true)
    List<Object[]> getMyPlacelistByContributorIdOrderByPlaceId(String contributorId, Pageable pageable);

    @Query(value = "select count(*) from (SELECT distinct pi.placelist_id, pl.title, pi.contributor_id\n" +
            " FROM placelist_item pi, placelist pl where pi.placelist_id = pl.id and pi.contributor_id=:contributorId ) as data"
            , nativeQuery = true)
    long getCountByContributorId(String contributorId);
}