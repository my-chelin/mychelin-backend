package com.a206.mychelin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PlaceListItemPK implements Serializable {
    @Column(name = "placelist_id")
    private int placelistId;

    @Column(name = "place_id")
    private int placeId;
}