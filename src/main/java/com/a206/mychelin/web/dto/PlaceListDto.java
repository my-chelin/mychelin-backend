package com.a206.mychelin.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

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
        private List<String> contributorProfiles;
        private int contributorCnt;
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
