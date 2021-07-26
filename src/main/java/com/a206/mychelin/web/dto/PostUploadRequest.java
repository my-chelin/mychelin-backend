package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUploadRequest {

    private String userId;
    private String title;
    private String content;

    @Builder
    public PostUploadRequest(String userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Post toEntity() {
        return Post.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .build();
    }
}