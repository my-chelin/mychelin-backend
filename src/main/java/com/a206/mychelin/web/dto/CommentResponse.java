package com.a206.mychelin.web.dto;

import lombok.*;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private int id;
    private String writerId;
    private String message;
    private String createDate;
}