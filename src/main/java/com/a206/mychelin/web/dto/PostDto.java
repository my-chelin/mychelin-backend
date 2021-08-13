package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class PostDto {
    @Getter
    @Builder
    public static class PostInfoResponse {
        private int postId;
        private String userNickname;
        private Object userFollowerCnt;
        private String content;
        private String createDate;
        private boolean liked;
        private Object likeCnt;
        private Object commentCnt;
        private Object placeId;
        private Object placeListId;
        private List<String> images;
        private ArrayList<CommentDto.CommentResponse> comments;
        private String profileImage;
    }

    @Getter
    @RequiredArgsConstructor
    public static class PostLikeRequest {
        private int postId;
        private String userId;

        @Builder
        private PostLikeRequest(int postId, String userId) {
            this.postId = postId;
            this.userId = userId;
        }
    }

    @Getter
    public static class PostResponse {
        private int id;
        private String userId;
        private String content;

        public PostResponse(Post entity) {
            this.id = entity.getId();
            this.userId = entity.getUserId();
            this.content = entity.getContent();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PostUpdateRequest {
        private String content;
        private Integer placeId;
        private Integer placelistId;
        private List<String> images;

        @Builder
        public PostUpdateRequest(String content, Integer placeId, Integer placelistId) {
            this.content = content;
            this.placeId = placeId;
            this.placelistId = placelistId;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PostUploadRequest {
        private String content;
        private Integer placeId;
        private Integer placeListId;
        private List<String> images;

        public void checkPlaceIdOrPlaceListId() {
            if (this.placeId != null && this.placeId <= 0) {
                this.placeId = null;
            }

            if (this.placeListId != null && this.placeListId <= 0) {
                this.placeListId = null;
            }
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PostByUserRequest {
        private String userId;

        @Builder
        public PostByUserRequest(String userId) {
            this.userId = userId;
        }

        public Post toEntity() {
            return Post.builder().build();
        }
    }
}
