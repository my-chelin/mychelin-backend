package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.User;

public class UserResponse {

    private String id;
    private String nickname;
    private String phoneNumber;
    private String bio;
    private String profileImage;

    public UserResponse(User entity) {
        this.id = entity.getId();
        this.nickname = entity.getNickname();
        this.phoneNumber = entity.getPhoneNumber();
        this.bio = entity.getBio();
        this.profileImage = entity.getProfileImage();
    }
}
