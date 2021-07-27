package com.a206.mychelin.domain.entity;

import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@RequiredArgsConstructor
public class FollowPK implements Serializable {
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "following_id", nullable = false)
    private String followingId;

    public FollowPK(String userId, String followingId) {
        this.userId = userId;
        this.followingId = followingId;
    }
}