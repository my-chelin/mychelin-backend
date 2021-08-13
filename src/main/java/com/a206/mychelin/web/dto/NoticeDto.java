package com.a206.mychelin.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class NoticeDto {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class NoticeComment {
        String type;
        int id;
        int commentId;
        String commentMessage;
        String writerId;
        String writerNickname;
        int postId;
        String postContent;
        boolean isRead;
        String addTime;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class NoticeFollow {
        String type;
        int id;
        String userId;
        String userNickname;
        boolean isRead;
        String addTime;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class NoticePostLike {
        String type;
        int id;
        int postId;
        String postContent;
        Integer placeId;
        Integer placeListId;
        String postLikeUserId;
        String postLikeUserNickname;
        boolean isRead;
        String addTime;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class NoticeRequest {
        @ApiModelProperty(example = "알림 타입")
        String type;

        @ApiModelProperty(example = "아림 id")
        int id;
    }


}