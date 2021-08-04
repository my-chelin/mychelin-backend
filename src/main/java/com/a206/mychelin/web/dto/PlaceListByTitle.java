package com.a206.mychelin.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceListByTitle {
    private int id;
    private String title;
    private String createDate;
    private String userId;
    private String nickname;
    private BigInteger totalItemCnt;
}