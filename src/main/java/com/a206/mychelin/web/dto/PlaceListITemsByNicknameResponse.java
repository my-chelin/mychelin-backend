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
    private int placelist_id;
    private String title;
    private String contributor_id;
    private BigInteger contrubute_item_cnt;
    private BigInteger total_item_cnt;
    private String create_date;
}
