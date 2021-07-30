package com.a206.mychelin.web.dto;

import lombok.Getter;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FollowAcceptRequest {
    private String userNickname;

    @Builder
    public FollowAcceptRequest(String userNickname) {
        this.userNickname = userNickname;
    }
}