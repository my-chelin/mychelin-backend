package com.a206.mychelin.web.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class CustomResponseEntity {
    private int status;
    private String message;
    private Object data;

}
