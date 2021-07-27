package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostByUserRequest {
    private String userId;

    @Builder
    public PostByUserRequest(String userId) {
        this.userId = userId;
    }

    public Post toEntity() {
        return Post.builder().build();
    }
}