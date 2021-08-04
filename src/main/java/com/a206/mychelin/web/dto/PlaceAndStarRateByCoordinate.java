package com.a206.mychelin.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceAndStarRateByCoordinate {
    private double distance;
    private int id;
    private String name;
    private String descrption;
    private float lattitude;
    private float longitude;
    private String phone;
    private String location;
    private String operationHours;
    private int categoryId;
    private String image;
    private Double starRate;
}