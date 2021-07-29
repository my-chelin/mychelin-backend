package com.a206.mychelin.controller;

import com.a206.mychelin.service.FollowService;
import com.a206.mychelin.web.dto.FollowAcceptRequest;
import com.a206.mychelin.web.dto.FollowAskRequest;
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
    @ApiImplicitParam(name = "user_id", value = "")
    @PostMapping("request/{userNickname}")
    public ResponseEntity addFollowingUser(@PathVariable String userNickname, HttpServletRequest httpRequest) {
        return followService.addFollowingUser(userNickname, httpRequest);
    }

    @ApiOperation(value = "팔로우 요청을 수락한다.")
    @PutMapping("accept/{userNickname}")
    public ResponseEntity acceptFollowingUser(@PathVariable String userNickname, HttpServletRequest httpRequest) {
        return followService.acceptFollowing(userNickname, httpRequest);
    }

    @ApiOperation(value = "사용자의 팔로잉 목록을 확인한다.")
    @GetMapping("list/{userNickname}")
    public ResponseEntity findFollowingList(@PathVariable String userNickname){
        return followService.findFollowingList(userNickname);
    }
}
