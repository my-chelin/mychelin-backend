package com.a206.mychelin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="placelist_item")
@DynamicInsert
public class PlaceListItem {

    @EmbeddedId
    private PlaceListItemPK placeListItemPK;

    private String contributorId;

}
