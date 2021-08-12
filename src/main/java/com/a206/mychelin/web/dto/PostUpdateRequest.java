package com.a206.mychelin.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostUpdateRequest {
    private String content;
    private Integer placeId;
    private Integer placelistId;
    private List<String> images;

    @Builder
    public PostUpdateRequest(String content, Integer placeId, Integer placelistId) {
        this.content = content;
        this.placeId = placeId;
        this.placelistId = placelistId;
    }
}