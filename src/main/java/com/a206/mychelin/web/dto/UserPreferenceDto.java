package com.a206.mychelin.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserPreferenceDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class UserPreferenceRequest {
        public int sweet;
        public int salty;
        public int sour;
        public int oily;
        public int spicy;
        public boolean challenging;
        public boolean planning;
        public boolean sociable;
        public boolean sensitivity;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class TastePreferenceResponse {
        public int sweet;
        public int salty;
        public int sour;
        public int oily;
        public int spicy;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class PlacePreferenceResponse {
        public boolean challenging;
        public boolean planning;
        public boolean sociable;
        public boolean sensitivity;
    }
}