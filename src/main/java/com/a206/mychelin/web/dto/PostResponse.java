package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.Post;
import lombok.Getter;

@Getter
public class PostResponse {
    private int id;
    private String userId;
    private String content;

    public PostResponse(Post entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.content = entity.getContent();
    }
}