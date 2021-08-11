package com.a206.mychelin.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceListITemsByNicknameResponse {
    private int placeListId;
    private String title;
    private String nickname;
    private BigInteger contrubuteItemCnt;
    private BigInteger totalItemCnt;
    private String createDate;
}