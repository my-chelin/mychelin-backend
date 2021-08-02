package com.a206.mychelin.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Table(name = "bookmark_placelist")
@ToString
@DynamicUpdate
@NoArgsConstructor
@Entity
public class BookmarkPlacelist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userId;
    private int placelistId;
    private String addDate;
}
