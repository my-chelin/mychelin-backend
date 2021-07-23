package com.a206.mychelin.controller;

import com.a206.mychelin.service.PostService;
import com.a206.mychelin.web.dto.PostUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping("/upload")
    public ResponseEntity<String> save(@RequestBody PostUploadRequest postUploadRequest, HttpServletRequest request){
        return postService.save(postUploadRequest, request);
    }

}