package com.a206.mychelin.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@Table(name = "follow")
@NoArgsConstructor
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
}