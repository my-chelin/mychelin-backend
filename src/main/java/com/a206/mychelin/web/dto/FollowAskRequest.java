package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.Follow;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FollowAskRequest {
    private String userId;
    private String followingId;

    @Builder
    public FollowAskRequest(String userId, String followingId) {
        this.userId = userId;
        this.followingId = followingId;
    }

    public Follow toEntity(){
        return Follow.builder()
                .userId(userId)
                .followingId(followingId)
                .build();
    }
}
