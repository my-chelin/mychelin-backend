package com.a206.mychelin.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {
    private String nickname;
    private String bio;
    private String profileImage;
    private int follow;
    private int follower;
    private long like;
    private Boolean isFollowing;

    public void setIsFollower(boolean check) {
        this.isFollowing = check;
    }
}