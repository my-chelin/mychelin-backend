package com.a206.mychelin.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Table(name = "bookmark_placelist")
@ToString
@DynamicUpdate
@RequiredArgsConstructor
@Entity
public class BookmarkPlacelist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userId;
    private int placelistId;
    private String addDate;

    @Builder
    public BookmarkPlacelist(String userId, int placelistId) {
        this.userId = userId;
        this.placelistId = placelistId;
    }
}
