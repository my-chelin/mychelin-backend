package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentInsertRequest {

    private String writerId;
    private String message;
    private int postId;

    @Builder
    public CommentInsertRequest(String writerId, String message, int postId) {
        this.writerId = writerId;
        this.message = message;
        this.postId = postId;
    }

    public Comment toEntity(){
        return Comment.builder()
                .writerId(writerId)
                .message(message)
                .postId(postId)
                .build();
    }

    public void setWriterId(String writerId){
        this.writerId = writerId;
    }
}
