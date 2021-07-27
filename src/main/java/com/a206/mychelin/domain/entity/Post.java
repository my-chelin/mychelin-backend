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
    public Post(String userId, String title, String content, Integer placeId, Integer placelistId) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.placeId = placeId;
        this.placelistId = placelistId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "create_date", insertable = false, updatable = false)
    private Date createDate;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "place_id", nullable = true)
    private Integer placeId;

    @Column(name = "placelist_id", nullable = true)
    private Integer placelistId;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}