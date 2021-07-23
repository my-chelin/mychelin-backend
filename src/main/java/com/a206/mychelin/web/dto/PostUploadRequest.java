package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUploadRequest {

    private String user_id;
    private String title;
    private String content;

    @Builder
    public PostUploadRequest(String user_id, String title, String content) {
        this.user_id = user_id;
        this.title = title;
        this.content = content;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Post toEntity(){
        return Post.builder()
                    .userId(user_id)
                    .title(title)
                    .content(content)
                    .build();
    }
}
