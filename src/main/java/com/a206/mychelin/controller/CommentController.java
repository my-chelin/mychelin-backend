package com.a206.mychelin.controller;

import com.a206.mychelin.service.CommentService;
import com.a206.mychelin.web.dto.CommentInsertRequest;
import com.a206.mychelin.web.dto.CustomResponseEntity;
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
    @ApiImplicitParam(name="id", value="포스트 고유 id")
    @GetMapping("/{id}")
    public ResponseEntity<CustomResponseEntity> findAllCommentsByPostId(@PathVariable int id, HttpServletRequest httpRequest){
        return commentService.findCommentsByPostId(id, httpRequest);
    }

    @ApiOperation(value = "특정 포스트에 댓글을 단다.")
    @ApiImplicitParam(name="id", value="포스트 고유 id")
    @PostMapping("/{postId}")
    public ResponseEntity addComment(@PathVariable int postId, @RequestBody CommentInsertRequest commentRequest, HttpServletRequest httpRequest){
        return commentService.addComment(postId, commentRequest, httpRequest);
    }

    @ApiOperation(value = "특정 포스트에 남긴 특정 댓글을 삭제한다.")
    @ApiImplicitParam(name="comment_id", value="댓글 고유 id")
    @DeleteMapping("/{comment_id}")
    public ResponseEntity deleteComment(@PathVariable int comment_id, HttpServletRequest httpRequest){
        return commentService.deleteComment(comment_id, httpRequest);
    }
}
