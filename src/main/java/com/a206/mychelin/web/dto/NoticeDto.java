package com.a206.mychelin.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class NoticeDto {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class NoticeComment{
        String type;
        int id;
        String comment_id;
        boolean isRead;
        String addTime;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class NoticeFollow{
        String type;
        int id;
        String userId;
        String followingId;
        boolean isRead;
        String addTime;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class NoticePostLike{
        String type;
        int id;
        int postId;
        String postContent;
        int placeId;
        int placeListId;
        String postLikeUserId;
        boolean isRead;
        String addTime;
    }


}
