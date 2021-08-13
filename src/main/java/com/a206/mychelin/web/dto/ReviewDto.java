package com.a206.mychelin.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class ReviewDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ReviewDeleteRequest {
        @ApiModelProperty(example = "삭제하려는 리뷰 아이디")
        private int id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ReviewEditRequest {
        @ApiModelProperty(example = "리뷰 아이디")
        private int id;

        @ApiModelProperty(example = "리뷰 별점")
        private float starRate;

        @ApiModelProperty(example = "리뷰 내용")
        private String content;

        @ApiModelProperty(example = "맛집 아이디")
        private int placeId;

        @ApiModelProperty(example = "리뷰 이미지, 없으면 null")
        private String image;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ReviewRequest {
        @ApiModelProperty(example = "평점")
        private float starRate;

        @ApiModelProperty(example = "리뷰 내용")
        private String content;

        @ApiModelProperty(example = "맛집 아이디")
        private int placeId;

        @ApiModelProperty(example = "이미지, 없으면 null")
        private String image;
    }
}
