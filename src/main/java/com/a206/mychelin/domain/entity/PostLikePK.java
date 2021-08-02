package com.a206.mychelin.domain.entity;

import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@RequiredArgsConstructor
public class PostLikePK implements Serializable {
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "post_id", nullable = false)
    private int postId;

    public PostLikePK(int postId, String userId) {
        this.postId = postId;
        this.userId = userId;
    }
}