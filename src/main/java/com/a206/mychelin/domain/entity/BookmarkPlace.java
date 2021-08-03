package com.a206.mychelin.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Table(name = "bookmark_place")
@ToString
@DynamicInsert
@DynamicUpdate
@RequiredArgsConstructor
@Entity
public class BookmarkPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userId;
    private int placeId;
    private String addDate;

    @Builder
    public BookmarkPlace(String userId, int placeId) {
        this.userId = userId;
        this.placeId = placeId;
    }

    @Builder
    public BookmarkPlace(int id, String userId, int placeId, String addDate) {
        this.id = id;
        this.userId = userId;
        this.placeId = placeId;
        this.addDate = addDate;
    }
}
