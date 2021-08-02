package com.a206.mychelin.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Table(name = "bookmark_place")
@ToString
@DynamicUpdate
@NoArgsConstructor
@Entity
public class BookmarkPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userId;
    private int placeId;
    private String addDate;
}
