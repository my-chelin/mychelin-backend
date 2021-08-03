package com.a206.mychelin.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {
    private String nickname;
    private String phoneNumber;
    private String bio;
    private String profileImage;

    @Builder
    public UserUpdateRequest(String id, String nickname, String phoneNumber, String bio, String profileImage) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.profileImage = profileImage;
    }
}