package com.a206.mychelin.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {
    private String nickname;
    private String bio;
    private String profileImage;
    private String phoneNumber;
    private int follow;
    private int follower;
    private long like;
    private int isFollowing;
}