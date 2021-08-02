package com.a206.mychelin.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@Builder
public class PostInfoResponse {
    private int postId;
    private String userNickname;
    private Object userFollowerCnt;
    private String content;
    private String createDate;
    private Object likeCnt;
    private Object commentCnt;
    private Object placeId;
    private Object placelistId;
    private String image;
    private ArrayList<CommentResponse> comments;
}