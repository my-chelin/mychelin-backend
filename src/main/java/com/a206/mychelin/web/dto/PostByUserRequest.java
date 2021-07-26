package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostByUserRequest {
    private String user_id;

    @Builder
    public PostByUserRequest(String user_id) {
        this.user_id = user_id;
    }

    public Post toEntity(){
        return Post.builder().build();
    }

}
