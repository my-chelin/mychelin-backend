package com.a206.mychelin.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@ToString
@Getter
@Entity
@Table(name = "follow")
@RequiredArgsConstructor
@IdClass(FollowPK.class)
public class Follow implements Serializable {
    @Id
    @Column(name = "user_id")
    String userId;

    @Id
    @Column(name = "following_id")
    String followingId;

    @Column(name = "accept")
    boolean accept;

    @Builder
    public Follow(String userId, String followingId, boolean accept) {
        this.userId = userId;
        this.followingId = followingId;
        this.accept = accept;
    }

    public void update(String userId, String followingId) {
        this.userId = userId;
        this.followingId = followingId;
        this.accept = !accept; //입력하면 아무튼 뒤집어줌
    }
}