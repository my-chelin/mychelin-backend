package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.Follow;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FollowAcceptRequest {
    private String userId;
    private String followingId;
    private boolean accept;

    @Builder
    public FollowAcceptRequest(String userId, String followingId, boolean accept) {
        this.userId = userId;
        this.followingId = followingId;
        this.accept = accept;
    }

    public Follow toEntity(){
        return Follow.builder()
                .userId(userId)
                .followingId(followingId)
                .accept(accept)
                .build();
    }

}
