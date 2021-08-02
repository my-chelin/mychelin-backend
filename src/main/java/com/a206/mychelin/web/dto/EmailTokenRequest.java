package com.a206.mychelin.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailTokenRequest {
    @ApiModelProperty(example = "이메일")
    private String email;
    @ApiModelProperty(example = "인증 번호")
    private String token;
}
