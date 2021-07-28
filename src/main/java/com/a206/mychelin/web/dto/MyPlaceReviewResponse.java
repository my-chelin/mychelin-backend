package com.a206.mychelin.web.dto;

import lombok.*;

import java.util.Date;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPlaceReviewResponse {
    private int review_id;
    private float star_rate;
    private String content;
    private String user_id;
    private String craete_date;
    private String review_image;
    private int place_id;
    private String place_name;
    private String place_image;
}