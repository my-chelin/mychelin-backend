  package com.a206.mychelin.controller;

import com.a206.mychelin.exception.PageIndexLessThanZeroException;
import com.a206.mychelin.service.PostService;
import com.a206.mychelin.web.dto.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

    @ApiOperation(value = "포스트를 업로드한다.")
    @PostMapping("/upload")
    public ResponseEntity<Response> uploadTextPost(@RequestBody PostDto.PostUploadRequest postUploadRequest, HttpServletRequest request) {
        return postService.addPost(postUploadRequest, request);
    }

    @ApiOperation(value = "선택한 포스트를 수정한다.")
    @ApiImplicitParam(name = "id", value = "포스트 고유 id")
    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable int id, @RequestBody PostDto.PostUpdateRequest postUpdateRequest, HttpServletRequest httpRequest) {
        return postService.update(id, postUpdateRequest, httpRequest);
    }

    @ApiOperation(value = "선택한 포스트를 가져온다.")
    @ApiImplicitParam(name = "id", value = "포스트 고유 id")
    @GetMapping("/{id}")
    public ResponseEntity<Response> load(@PathVariable int id, HttpServletRequest httpRequest) {
        return postService.getPostByPostId(id, httpRequest);
    }

    @ApiOperation(value = "특정 사용자가 작성한 모든 포스트를 최신순으로 가져온다.")
    @ApiImplicitParam(name = "userNickname", value = "사용자 닉네임")
    @GetMapping("/list/{userNickname}")
    public ResponseEntity<Response> findPostsByUserId(@PathVariable String userNickname, HttpServletRequest httpRequest) {
        return postService.findPostsByUserNickname(userNickname, httpRequest);
    }

    @ApiOperation(value = "선택한 포스트를 삭제한다.")
    @ApiImplicitParam(name = "id", value = "포스트 고유 id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable int id, HttpServletRequest httpRequest) {
        return postService.delete(id, httpRequest);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", required = false, dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", required = false, dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("/main")
    public ResponseEntity<Response> getPosts(HttpServletRequest request, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pagesize) throws PageIndexLessThanZeroException {
        try {
            return postService.findAllPosts(request, page, pagesize);
        } catch (IllegalArgumentException e) {
            throw new PageIndexLessThanZeroException();
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", required = false, dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", required = false, dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("/following")
    public ResponseEntity<Response> findPostsByFollowingUsers(HttpServletRequest httpServletRequest, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pagesize) throws PageIndexLessThanZeroException {
        return postService.findPostsByFollowingUsersOrderByCreateDateDesc(httpServletRequest, page, pagesize);
    }

    @ApiOperation(value = "선택한 포스트에 좋아요 표시를 한다.")
    @ApiImplicitParam(name = "id", value = "포스트 고유 id")
    @PutMapping("/like")
    public ResponseEntity<Response> likePost(@RequestBody PostDto.PostLikeRequest postLikeRequest, HttpServletRequest httpRequest) {
        return postService.likePost(postLikeRequest, httpRequest);
    }

    @ApiOperation(value = "키워드를 포함한 포스트를 검색한다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "검색할 키워드"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", required = false, dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", required = false, dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("/search")
    public ResponseEntity searchPostByKeyword(@RequestParam String keyword, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize, HttpServletRequest httpServletRequest) {
        return postService.findPostsByKeyword(keyword, page, pageSize, httpServletRequest);
    }
}