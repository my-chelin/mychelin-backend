package com.a206.mychelin.controller;

import com.a206.mychelin.web.dto.*;
import com.a206.mychelin.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/signup")
    public ResponseEntity<CustomResponseEntity> signUp(@RequestBody UserSaveRequest userSaveRequest) {
        return userService.signUp(userSaveRequest);
    }

    @PutMapping("/changepwd")
    public ResponseEntity<CustomResponseEntity> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest, HttpServletRequest request) {
        return userService.changePassword(passwordChangeRequest, request);
    }

    @PutMapping("/profile")
    public ResponseEntity<CustomResponseEntity> updateInfo(@RequestBody UserDto.UpdateRequest requestDTO, HttpServletRequest httpRequest) {
        return userService.updateInfo(requestDTO, httpRequest);
    }

    @GetMapping("/profile/{nickname}")
    public ResponseEntity<CustomResponseEntity> getProfile(@PathVariable String nickname, HttpServletRequest request) {
        return userService.getProfile(nickname, request);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponseEntity> deleteUser(HttpServletRequest request) {
        return userService.deleteUser(request);
    }

    @ApiOperation(value = "이메일 확인로 인증 메일 전송")
    @PostMapping("/check/email")
    public ResponseEntity<CustomResponseEntity> checkEmail(@RequestBody EmailRequest emailRequest) {
        return userService.checkEmail(emailRequest);
    }

    @ApiOperation(value = "이메일로 전송된 인증 토큰 확인")
    @PostMapping("/check/emailToken")
    public ResponseEntity<CustomResponseEntity> checkEmailToken(@RequestBody EmailTokenRequest emailTokenRequest) {
        return userService.checkEmailToken(emailTokenRequest);
    }

    @ApiOperation(value = "유저 프로필 이미지 저장")
    @PostMapping("/profile/image")
    public ResponseEntity<CustomResponseEntity> saveUserProfileImage(@RequestBody ImageRequest imageRequest, HttpServletRequest request) {
        return userService.saveUserProfileImage(imageRequest, request);
    }
}