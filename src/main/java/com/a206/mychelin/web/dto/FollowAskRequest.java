package com.a206.mychelin.web.dto;

import lombok.Getter;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FollowAskRequest {
    private String userNickname;

    @Builder
    public FollowAskRequest(String userNickname) {
        this.userNickname = userNickname;
    }
}