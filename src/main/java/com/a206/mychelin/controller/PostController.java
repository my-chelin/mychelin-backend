package com.a206.mychelin.controller;

import com.a206.mychelin.service.PostService;
import com.a206.mychelin.web.dto.CustomResponseEntity;
import com.a206.mychelin.web.dto.PostByUserRequest;
import com.a206.mychelin.web.dto.PostUpdateRequest;
import com.a206.mychelin.web.dto.PostUploadRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @ApiOperation(value="포스트를 업로드한다.")
    @ApiImplicitParam(name="id", value="식당 고유 id")
    @PostMapping("/upload")
    public ResponseEntity<CustomResponseEntity> save(@RequestBody PostUploadRequest postUploadRequest, HttpServletRequest request){
        return postService.save(postUploadRequest, request);
    }

    @ApiOperation(value="선택한 포스트를 수정한다.")
    @ApiImplicitParam(name="id", value="포스트 고유 id")
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable int id, @RequestBody PostUpdateRequest postUpdateRequest, HttpServletRequest httpRequest) {
        return postService.update(id, postUpdateRequest, httpRequest);
    }

    @ApiOperation(value="선택한 포스트를 가져온다.")
    @ApiImplicitParam(name="id", value="포스트 고유 id")
    @GetMapping("/{id}")
    public ResponseEntity load(@PathVariable int id) {
        return postService.findPostById(id);
    }

    @ApiOperation(value="특정 사용자가 작성한 모든 포스트를 최신순으로 가져온다.")
    @ApiImplicitParam(name = "user_id", value = "사용자 아이디(이메일)")
    @GetMapping("/list")
    public ResponseEntity findPostsByUserId(@RequestBody PostByUserRequest postByUserRequest) {
        return postService.findPostsByUserId(postByUserRequest);
    }

}