package com.a206.mychelin.domain;

import javax.persistence.Column;
import java.io.Serializable;

public class FollowPK implements Serializable {
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "following_id", nullable = false)
    private String followingId;
}