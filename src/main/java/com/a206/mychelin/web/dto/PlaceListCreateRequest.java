package com.a206.mychelin.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlaceListCreateRequest {
    @ApiModelProperty(example = "맛집 리스트 제목")
    private String title;
}