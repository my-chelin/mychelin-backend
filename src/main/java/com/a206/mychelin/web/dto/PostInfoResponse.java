package com.a206.mychelin.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class PostInfoResponse {
    private int postId;
    private String userNickname;
    private Object userFollowerCnt;
    private String content;
    private String createDate;
    private boolean liked;
    private Object likeCnt;
    private Object commentCnt;
    private Object placeId;
    private Object placeListId;
    private List<String> images;
    private ArrayList<CommentResponse> comments;
    private String profileImage;
}