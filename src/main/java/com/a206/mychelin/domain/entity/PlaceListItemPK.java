package com.a206.mychelin.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class PlaceListItemPK implements Serializable {

    @Column(name="placelist_id")
    private int placelistId;

    @Column(name="place_id")
    private int placeId;

}
