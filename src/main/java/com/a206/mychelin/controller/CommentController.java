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
    @PostMapping("/{id}")
    public ResponseEntity addComment(@PathVariable int id, @RequestBody CommentInsertRequest commentRequest, HttpServletRequest httpRequest){
        return commentService.addComment(id, commentRequest, httpRequest);
    }
}
