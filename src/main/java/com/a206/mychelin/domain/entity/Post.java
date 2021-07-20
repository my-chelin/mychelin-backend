package com.a206.mychelin.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
@Table(name="post")
@ToString
@DynamicUpdate
public class Post {
    @Id
    @Column(name="id", nullable = true)
    private int Id;
    @Column(name="user_id", nullable = false)
    private String userId;
    @Column(name="create_date", nullable = false)
    private Date createDate;
    @Column(name="title", nullable = true)
    private String title;
    @Column(name="create_date", nullable = false)
    private String content;
    @Column(name="place_id", nullable = true)
    private String placeId;
    @Column(name="placelist_id", nullable = true)
    private String placeListId;
}
