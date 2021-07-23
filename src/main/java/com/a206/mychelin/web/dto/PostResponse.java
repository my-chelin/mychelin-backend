package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.Post;
import lombok.Getter;

@Getter
public class PostResponse {

    private int id;
    private String user_id;
    private String title;
    private String content;

    public PostResponse(Post entity){
        this.id = entity.getId();
        this.user_id = entity.getUserId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
    }

}
