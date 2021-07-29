package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequest {
    private String content;

    @Builder
    public PostUpdateRequest(String content) {
        this.content = content;
    }

    public Post toEntity() {
        return Post.builder()
                .content(content)
                .build();
    }
}