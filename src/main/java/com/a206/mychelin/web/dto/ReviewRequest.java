package com.a206.mychelin.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewRequest {
    @ApiModelProperty(example = "평점")
    private float star_rate;

    @ApiModelProperty(example = "리뷰 내용")
    private String content;

    @ApiModelProperty(example = "맛집 아이디")
    private int place_id;

    @ApiModelProperty(example = "이미지, 없으면 null")
    private String image;
}