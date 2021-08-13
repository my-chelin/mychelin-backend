package com.a206.mychelin.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigInteger;

public class PlaceListDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlaceListByTitle {
        private int id;
        private String title;
        private String createDate;
        private String userId;
        private String nickname;
        private BigInteger totalItemCnt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class PlaceListCreateRequest {
        @ApiModelProperty(example = "맛집 리스트 제목")
        private String title;
    }
}
