package com.a206.mychelin.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostUploadRequest {
    private String content;
    private Integer placeId;
    private Integer placeListId;
    private List<String> images;
}