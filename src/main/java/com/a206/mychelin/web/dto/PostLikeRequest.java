package com.a206.mychelin.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostLikeRequest {
    private int postId;
    private String userId;

    @Builder
    private PostLikeRequest(int postId, String userId) {
        this.postId = postId;
        this.userId = userId;
    }
}