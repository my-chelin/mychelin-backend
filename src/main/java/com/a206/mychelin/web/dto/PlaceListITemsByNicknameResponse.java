package com.a206.mychelin.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceListITemsByNicknameResponse {
    private int placelist_id;
    private String title;
    private  int place_id;
    private String name;
    private String description;
    private Float lattitude;
    private Float longitude;
    private String phone;
    private String location;
    private String operation_hours;
    private int category_id;
    private String image;
    private String contributor_id;
}
