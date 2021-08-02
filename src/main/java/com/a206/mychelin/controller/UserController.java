package com.a206.mychelin.controller;

import com.a206.mychelin.web.dto.*;
import com.a206.mychelin.service.UserService;
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

    @PutMapping("/changeinfo")
    public ResponseEntity<CustomResponseEntity> updateInfo(@RequestBody UserUpdateRequest requestDto, HttpServletRequest httpRequest) {
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

    @PostMapping("/check/email")
    public ResponseEntity checkEmail(@RequestBody EmailRequest emailRequest){
        return userService.checkEmail(emailRequest);
    }

    @PostMapping("/check/emailToken")
    public ResponseEntity checkEmailToken(@RequestBody EmailTokenRequest emailTokenRequest){
        return userService.checkEmailToken(emailTokenRequest);
    }

}