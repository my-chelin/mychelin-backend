package com.a206.mychelin.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUploadRequest {
    private String content;
    private Integer placeId;
    private Integer placelistId;
}