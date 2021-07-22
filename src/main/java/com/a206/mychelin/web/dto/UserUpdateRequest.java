package com.a206.mychelin.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {
    private String id;
    private String nickname;
    private String phone_number;
    private String bio;
    private String profile_image;

    @Builder
    public UserUpdateRequest(String id, String nickname, String phone_number, String bio, String profile_image) {
        this.id = id;
        this.nickname = nickname;
        this.phone_number = phone_number;
        this.bio = bio;
        this.profile_image = profile_image;
    }
}