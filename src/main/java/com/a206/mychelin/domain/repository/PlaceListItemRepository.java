package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.PlaceListItem;
import com.a206.mychelin.domain.entity.PlaceListItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceListItemRepository extends JpaRepository<PlaceListItem,PlaceListItemPK> {
    Optional<PlaceListItem> findByPlaceListItemPK(PlaceListItemPK placeListItemPK);
    Optional<PlaceListItem> findByPlaceListItemPKAndContributorId(PlaceListItemPK placeListItemPK,String ContributorId);
}
