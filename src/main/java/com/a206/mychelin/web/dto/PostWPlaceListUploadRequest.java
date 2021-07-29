package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Getter
@DynamicInsert
@NoArgsConstructor
public class PostWPlaceListUploadRequest {
    @Setter
    private String userId;
    private String content;
    private int placelistId;

    @Builder
    public PostWPlaceListUploadRequest(String userId, String content, int placelistId) {
        this.userId = userId;
        this.content = content;
        this.placelistId = placelistId;
    }

    public Post toEntity() {
        return Post.builder()
                .userId(userId)
                .content(content)
                .placelistId(placelistId)
                .build();
    }
}