package com.a206.mychelin.web.dto;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPlaceReviewResponse {
    private int reviewId;
    private float starRate;
    private String content;
    private String userId;
    private String craeteDate;
    private String reviewImage;
    private int placeId;
    private String placeName;
    private String placeImage;
}