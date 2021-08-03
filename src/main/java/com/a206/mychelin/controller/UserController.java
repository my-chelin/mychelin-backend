package com.a206.mychelin.controller;

import com.a206.mychelin.web.dto.*;
import com.a206.mychelin.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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

    @PutMapping("/changeinfo")
    public ResponseEntity<CustomResponseEntity> updateInfo(@RequestBody UserDto.UserUpdateRequest requestDto, HttpServletRequest httpRequest) {
        return userService.update(requestDto, httpRequest);
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
    public ResponseEntity checkEmail(@RequestBody EmailRequest emailRequest) {
        return userService.checkEmail(emailRequest);
    }

    @ApiOperation(value = "이메일로 전송된 인증 토큰 확인")
    @PostMapping("/check/emailToken")
    public ResponseEntity checkEmailToken(@RequestBody EmailTokenRequest emailTokenRequest) {
        return userService.checkEmailToken(emailTokenRequest);
    }

    @ApiOperation(value = "유저 프로필 이미지 저장")
    @PostMapping("/profile/image")
    public ResponseEntity saveUserProfileImage(@RequestBody ImageRequest imageRequest, HttpServletRequest request) throws IOException {
        return userService.saveUserProfileImage(imageRequest, request);
    }

    @ApiOperation(value = "유저 닉네임 변경")
    @PutMapping("/profile/nickname")
    public ResponseEntity updateNickname(@RequestBody UserDto.NicknameUpdateRequest updateRequest, HttpServletRequest httpRequest) {
        return userService.updateNickname(updateRequest, httpRequest);
    }

    @ApiOperation(value = "유저 바이오 변경")
    @PutMapping("/profile/bio")
    public ResponseEntity updateBio(@RequestBody UserDto.BioUpdateRequest updateRequest, HttpServletRequest httpRequest) {
        return userService.updateBio(updateRequest, httpRequest);
    }
}