package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.PlaceListItem;
import com.a206.mychelin.domain.entity.PlaceListItemPK;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceListItemRepository extends JpaRepository<PlaceListItem, PlaceListItemPK> {
    Optional<PlaceListItem> findByPlaceListItemPK(PlaceListItemPK placeListItemPK);

    Optional<PlaceListItem> findByPlaceListItemPKAndContributorId(PlaceListItemPK placeListItemPK, String ContributorId);

    @Query(value = "select * \n" +
            "from ((SELECT distinct pi.placelist_id, pl.title, u.nickname, (select count(*) from placelist_item pii where pii.contributor_id = pi.contributor_id and pi.placelist_id= pii.placelist_id) as contrubute_item_cnt , \n" +
            "(select count(*) from placelist_item pii where pi.placelist_id= pii.placelist_id) as total_item_cnt , pl.create_date \n" +
            "FROM placelist_item pi, placelist pl, user u \n" +
            "where pi.placelist_id = pl.id and pi.contributor_id = :contributor_id and u.id = pi.contributor_id\n" +
            "order by pi.placelist_id)\n" +
            "UNION\n" +
            "(SELECT distinct pl.id, pl.title, u.nickname, (select count(*) from placelist_item pii where pii.contributor_id = pi.contributor_id and pi.placelist_id= pii.placelist_id) as contrubute_item_cnt , \n" +
            "(select count(*) from placelist_item pii where pi.placelist_id= pii.placelist_id) as total_item_cnt , pl.create_date \n" +
            "FROM user u join placelist_item pi \n" +
            "on u.id = pi.contributor_id\n" +
            "right outer join placelist pl \n" +
            "on pi.placelist_id = pl.id\n" +
            "where pl.user_id = :contributor_id\n" +
            "order by pl.id)) a"
            , countQuery = "select count(*) from (SELECT distinct pi.placelist_id, pl.title, pi.contributor_id\n" +
            " FROM placelist_item pi, placelist pl where pi.placelist_id = pl.id and pi.contributor_id=:contributorId ) as data"
            , nativeQuery = true)
    List<Object[]> getMyPlacelistByContributorIdOrderByPlaceId(@Param("contributor_id") String contributorId, Pageable pageable);

    @Query(value = "select count(*) from (SELECT distinct pi.placelist_id, pl.title, pi.contributor_id\n" +
            " FROM placelist_item pi, placelist pl where pi.placelist_id = pl.id and pi.contributor_id=:contributorId ) as data"
            , nativeQuery = true)
    long getCountByContributorId(String contributorId);

    @Query(value = "select count(*)\n" +
            "from placelist pl, placelist_item pi\n" +
            "where pl.id = pi.placelist_id \n" +
            "and pl.user_id = :user_id\n" +
            "and pi.contributor_id = :user_id", nativeQuery = true)
    long getIntersectionCount(String user_id);

    @Query(value = "select count(*)\n" +
            "from (select distinct contributor_id\n" +
            "from placelist_item\n" +
            "where placelist_id = :placeId) tmp", nativeQuery = true)
    int countContributorByPlaceId(@Param("placeId") int placeId);
}