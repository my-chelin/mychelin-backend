package com.a206.mychelin.web.dto;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceListItemDetail {
    private int placeListId;
    private int placeId;
    private String placeListTitle;
    private String contributorId;
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