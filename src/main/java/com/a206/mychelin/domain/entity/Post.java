package com.a206.mychelin.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Table(name = "post")
@ToString
@DynamicUpdate
@RequiredArgsConstructor
@Entity
public class Post {

    @Builder
    public Post(String userId, String content, Integer placeId, Integer placeListId) {
        this.userId = userId;
        this.content = content;
        this.placeId = placeId;
        this.placeListId = placeListId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "create_date", insertable = false, updatable = false)
    private Date createDate;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "place_id", nullable = true)
    private Integer placeId;

    @Column(name = "placelist_id", nullable = true)
    private Integer placeListId;

    public void update(String content) {
        this.content = content;
    }
}