package com.a206.mychelin.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigInteger;

public class PlaceListItemDto {
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlaceListItemDetail {
        private int placeListId;
        private int placeId;
        private String nickname;
        private String name;
        private String description;
        private double latitude;
        private double longitude;
        private String phone;
        private String location;
        private String opertaionHours;
        private int categoryId;
        private String image;
        private Double starRate;
        private int reviewCnt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaceListItemRequest {
        @ApiModelProperty(example = "맛집 id")
        int placeId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlaceListITemsByNicknameResponse {
        private int placeListId;
        private String title;
        private String nickname;
        private BigInteger contrubuteItemCnt;
        private BigInteger totalItemCnt;
        private String createDate;
    }
}