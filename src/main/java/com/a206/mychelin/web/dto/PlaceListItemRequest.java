package com.a206.mychelin.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceListItemRequest {
    @ApiModelProperty(example = "맛집 id")
    int placeId;
}