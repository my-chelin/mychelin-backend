package com.a206.mychelin.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import org.hibernate.annotations.DynamicInsert;
import com.a206.mychelin.domain.entity.Post;

@Getter
@DynamicInsert
@NoArgsConstructor
public class PostWPlaceUploadRequest {
    @Setter
    private String userId;
    private String content;
    private int placeId;

    @Builder
    public PostWPlaceUploadRequest(String userId, String content, int placeId) {
        this.userId = userId;
        this.content = content;
        this.placeId = placeId;
    }

    public Post toEntity() {
        return Post.builder()
                .userId(userId)
                .content(content)
                .placeId(placeId)
                .build();
    }
}