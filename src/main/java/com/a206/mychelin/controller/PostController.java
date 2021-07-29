package com.a206.mychelin.controller;

import com.a206.mychelin.service.PostService;
import com.a206.mychelin.web.dto.*;
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

    @ApiOperation(value = "글만 있는 포스트를 업로드한다.")
    @PostMapping("/upload/text")
    public ResponseEntity<CustomResponseEntity> uploadTextPost(@RequestBody PostUploadRequest postUploadRequest, HttpServletRequest request) {
        return postService.addPostText(postUploadRequest, request);
    }

    @ApiOperation(value = "장소를 포함한 포스트를 업로드한다.")
    @PostMapping("/upload/tap")
    public ResponseEntity<CustomResponseEntity> uploadTextPlace(@RequestBody PostWPlaceUploadRequest postRequest, HttpServletRequest request) {
        return postService.addPostPlace(postRequest, request);
    }

    @ApiOperation(value = "장소 리스트를 포함한 포스트를 업로드한다.")
    @PostMapping("/upload/tapl")
    public ResponseEntity<CustomResponseEntity> uploadTextPlaceList(@RequestBody PostWPlaceListUploadRequest postRequest, HttpServletRequest request) {
        return postService.addPostPlaceList(postRequest, request);
    }

    @ApiOperation(value = "선택한 포스트를 수정한다.")
    @ApiImplicitParam(name = "id", value = "포스트 고유 id")
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable int id, @RequestBody PostUpdateRequest postUpdateRequest, HttpServletRequest httpRequest) {
        return postService.update(id, postUpdateRequest, httpRequest);
    }

    @ApiOperation(value = "선택한 포스트를 가져온다.")
    @ApiImplicitParam(name = "postId", value = "포스트 고유 id")
    @GetMapping("/{postId}")
    public ResponseEntity load(@PathVariable int postId) {
        return postService.findPostByPostId(postId);
    }

    @ApiOperation(value = "특정 사용자가 작성한 모든 포스트를 최신순으로 가져온다.")
    @ApiImplicitParam(name = "userNickname", value = "사용자 닉네임")
    @GetMapping("/list/{userNickname}")
    public ResponseEntity findPostsByUserId(@PathVariable String userNickname) {
        return postService.findPostsByUserNickname(userNickname);
    }

    @ApiOperation(value = "선택한 포스트를 삭제한다.")
    @ApiImplicitParam(name = "id", value = "포스트 고유 id")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable int id, HttpServletRequest httpRequest) {
        return postService.delete(id, httpRequest);
    }

    @GetMapping("/main")
    public ResponseEntity findPostsByFollowingUsers(HttpServletRequest httpServletRequest){
        return postService.findPostsByFollowingUsersOrderByCreateDateDesc(httpServletRequest);
    }
}