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

    @Getter
    @AllArgsConstructor
    @Builder
    public static class UserProfileResponse {
        private String nickname;
        private String bio;
        private String profileImage;
        private String phoneNumber;
        private int follow;
        private int follower;
        private long like;
        private int isFollowing;
    }

    @Getter
    @Builder
    public static class UserSearchResponse {
        private String nickname;
        private String profileImage;
        private String bio;
    }

    @Getter
    @NoArgsConstructor
    public static class UserSaveRequest {
        private String id;
        private String nickname;
        private String password;
        private String phoneNumber;

        @Builder
        public UserSaveRequest(String id, String nickname, String password, String phoneNumber) {
            this.id = id;
            this.nickname = nickname;
            this.password = password;
            this.phoneNumber = phoneNumber;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PasswordChangeRequest {
        String password;
        String newPassword;
    }
}