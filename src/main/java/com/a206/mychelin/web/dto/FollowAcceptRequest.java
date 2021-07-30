package com.a206.mychelin.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class FollowAcceptRequest {
    private String userNickname;
}
