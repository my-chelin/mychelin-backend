package com.a206.mychelin.web.dto;

import lombok.*;

public class UserDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class UserUpdateRequest {
        private String nickname;
        private String phoneNumber;
        private String bio;
        private String profileImage;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ProfileUpdateRequest {
        private String profileImage;
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NicknameUpdateRequest {
        private String nickname;
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BioUpdateRequest {
        private String bio;
    }

}