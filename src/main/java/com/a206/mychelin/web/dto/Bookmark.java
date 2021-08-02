package com.a206.mychelin.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class Bookmark {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class Info {
        private int id;
        private String userId;
        private String addDate;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PlaceInfo {
        private Info info;
        private int placeId;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PlaceListInfo {
        private Info info;
        private int placelistId;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PlaceRequest {
        private PlaceInfo info;
    }
}
