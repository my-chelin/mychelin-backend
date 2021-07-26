package com.a206.mychelin.web.dto;

import lombok.*;

import java.util.Date;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private int id;
    private String writerNickname;
    private String message;
    private Date createDate;
}