package com.a206.mychelin.controller;

import com.a206.mychelin.service.CommentService;
import com.a206.mychelin.web.dto.CommentInsertRequest;
import com.a206.mychelin.web.dto.CustomResponseEntity;
import com.a206.mychelin.web.dto.Response;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @ApiOperation(value = "특정 포스트의 댓글을 불러온다.")
    @ApiImplicitParam(name = "postId", value = "포스트 고유 id")
    @GetMapping("/{postId}")
    public ResponseEntity<Response> findAllCommentsByPostId(@PathVariable int postId) {
        return commentService.findCommentsByPostId(postId);
    }

    @ApiOperation(value = "특정 포스트에 댓글을 단다.")
    @ApiImplicitParam(name = "postId", value = "포스트 고유 id")
    @PostMapping("/{postId}")
    public ResponseEntity<Response> addComment(@PathVariable int postId, @RequestBody CommentInsertRequest commentRequest, HttpServletRequest request) {
        return commentService.addComment(postId, commentRequest, request);
    }

    @ApiOperation(value = "특정 포스트에 남긴 특정 댓글을 삭제한다.")
    @ApiImplicitParam(name = "commentId", value = "댓글 고유 id")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Response> deleteComment(@PathVariable int commentId, HttpServletRequest httpRequest) {
        return commentService.deleteComment(commentId, httpRequest);
    }
}