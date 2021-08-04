package com.a206.mychelin.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PlaceDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlaceResponse {
        private int id;
        private String name;
        private String description;
        private double latitude;
        private double longitude;
        private String phone;
        private String location;
        private String operationHours;
        private int categoryId;
        private String image;
        private Object starRate;
    }
}