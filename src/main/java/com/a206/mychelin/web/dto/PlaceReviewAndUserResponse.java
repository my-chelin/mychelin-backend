package com.a206.mychelin.web.dto;

import lombok.*;

import java.util.Date;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceReviewAndUserResponse {
    private int review_id;
    private float star_rate;
    private String content;
    private String user_id;
    private String craete_date;
    private String review_image;
    private String user_nickname;
    private String user_profile_image;
}