package com.a206.mychelin.controller;

import com.a206.mychelin.service.FollowService;
import com.a206.mychelin.web.dto.CustomResponseEntity;
import com.a206.mychelin.web.dto.FollowAcceptRequest;
import com.a206.mychelin.web.dto.FollowAskRequest;
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
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;

    @ApiOperation(value = "특정 사용자 팔로우 요청 보내기")
    @ApiImplicitParam(name = "userNickname", value = "팔로우하고자 하는 사용자 닉네임")
    @PostMapping("/request")
    public ResponseEntity<Response> addFollowingUser(@RequestBody FollowAskRequest followAskRequest, HttpServletRequest httpRequest) {
        return followService.follow(followAskRequest, httpRequest);
    }

    @ApiOperation(value = "언팔로우 요청 보내기")
    @ApiImplicitParam(name = "userNickname", value = "언팔로우하고자 하는 사용자 닉네임")
    @DeleteMapping("/request")
    public ResponseEntity<Response> unfollow(@RequestBody FollowAskRequest followAskRequest, HttpServletRequest request) {
        return followService.unfollow(followAskRequest, request);
    }

    @ApiOperation(value = "팔로우 요청을 수락한다.")
    @ApiImplicitParam(name = "userNickname", value = "사용자 닉네임")
    @PutMapping("/accept")
    public ResponseEntity<Response> acceptFollowingUser(@RequestBody FollowAcceptRequest followAcceptRequest, HttpServletRequest httpRequest) {
        return followService.acceptFollowing(followAcceptRequest, httpRequest);
    }

    @ApiOperation(value = "사용자의 팔로잉 목록을 확인한다.")
    @GetMapping("/following/{userNickname}")
    public ResponseEntity<Response> findFollowingList(@PathVariable String userNickname) {
        return followService.findFollowingList(userNickname);
    }

    @ApiOperation(value = "사용자의 팔로워 목록을 확인한다.")
    @GetMapping("/follower/{userNickname}")
    public ResponseEntity<Response> findFollowerList(@PathVariable String userNickname) {
        return followService.findFollowerList(userNickname);
    }

    @ApiOperation(value = "팔로우 요청을 확인한다.")
    @GetMapping("/requests")
    public ResponseEntity<Response> findFollowerList(HttpServletRequest request) {
        return followService.getFollowRequest(request);
    }
}