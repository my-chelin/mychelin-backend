package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.Comment;
import lombok.*;

public class CommentDto {
    @Getter
    @NoArgsConstructor
    public static class CommentInsertRequest {
        private String writerId;
        private String message;
        private int postId;

        @Builder
        public CommentInsertRequest(String writerId, String message, int postId) {
            this.writerId = writerId;
            this.message = message;
            this.postId = postId;
        }

        public Comment toEntity() {
            return Comment.builder()
                    .writerId(writerId)
                    .message(message)
                    .postId(postId)
                    .build();
        }

        public void setWriterId(String writerId) {
            this.writerId = writerId;
        }

        public void setPostId(int postId) {
            this.postId = postId;
        }
    }

    @ToString
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CommentResponse {
        private int id;
        private String writerId;
        private String message;
        private String createDate;
    }
}
