package com.a206.mychelin.domain.entity;

import com.a206.mychelin.domain.PostLikePK;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@Table(name = "post_like")
@NoArgsConstructor
@IdClass(PostLikePK.class)
public class PostLike implements Serializable {
    @Id
    @Column(name = "post_id", nullable = false)
    private int postId;

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Builder
    public PostLike(int postId, String user_id) {
        this.postId = postId;
        this.userId = user_id;
    }
}