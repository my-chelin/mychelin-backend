package com.a206.mychelin.web.dto;

import lombok.*;

public class UserDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateRequest {
        private String nickname;
        private String phoneNumber;
        private String bio;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ProfileUpdateRequest {
        private String profileImage;
    }
}