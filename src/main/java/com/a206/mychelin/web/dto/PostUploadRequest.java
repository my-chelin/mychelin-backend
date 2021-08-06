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

    public void checkPlaceIdOrPlaceListId() {
        if (this.placeId != null && this.placeId <= 0) {
            this.placeId = null;
        }

        if (this.placeListId != null && this.placeListId <= 0) {
            this.placeListId = null;
        }
    }
}