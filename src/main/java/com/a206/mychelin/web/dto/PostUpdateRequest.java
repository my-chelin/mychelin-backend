package com.a206.mychelin.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequest {
    private String content;
    private Integer placeId;
    private Integer placelistId;

    @Builder
    public PostUpdateRequest(String content, Integer placeId, Integer placelistId) {
        this.content = content;
        this.placeId = placeId;
        this.placelistId = placelistId;
    }
}