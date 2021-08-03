package com.a206.mychelin.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlaceRequest {
        private int placeId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlacelistRequest {
        private int placelistId;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PlaceResponse {
        private int placeId;
        private String placeName;
        private String placeDescription;
        private String location;
        private String image;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PlacelistResponse {
        private int placelistId;
        private String userNickname;
        private String placelistName;
        private Object placeCnt;
        private String addDate;
    }
}
