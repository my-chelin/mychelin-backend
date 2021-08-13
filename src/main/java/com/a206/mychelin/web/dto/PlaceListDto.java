package com.a206.mychelin.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
