package com.a206.mychelin.web.dto;

import lombok.*;

public class FollowDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowListResponse {
        private String profileImage;
        private String nickname;
        private String bio;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowRequestResponse {
        private String nickname;
        private String profileImage;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class FollowAskRequest {
        private String userNickname;
    }

    @Getter
    @RequiredArgsConstructor
    public static class FollowAcceptRequest {
        private String userNickname;

        @Builder
        public FollowAcceptRequest(String userNickname) {
            this.userNickname = userNickname;
        }
    }
}