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
public class ReviewDeleteRequest {
    @ApiModelProperty(example = "삭제하려는 리뷰 아이디")
    private int id;
}
