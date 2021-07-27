package com.a206.mychelin.controller;

import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.web.dto.CustomResponseEntity;
import com.a206.mychelin.web.dto.PasswordChangeRequest;
import com.a206.mychelin.service.UserService;
import com.a206.mychelin.web.dto.UserUpdateRequest;
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
    public ResponseEntity<CustomResponseEntity> signUp(@RequestBody User user) {
        return userService.signUp(user);
    }

    @PutMapping("/changepwd")
    public ResponseEntity<CustomResponseEntity> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest, HttpServletRequest request) {
        return userService.changePassword(passwordChangeRequest, request);
    }

    @PutMapping("/changeInfo")
    public ResponseEntity<CustomResponseEntity> updateInfo(@RequestBody UserUpdateRequest requestDto, HttpServletRequest httpRequest) {
        return userService.update(requestDto, httpRequest);
    }

    @GetMapping("/{nickname}")
    public ResponseEntity<CustomResponseEntity> getProfile(@PathVariable String nickname, HttpServletRequest request) {
        return userService.getProfile(nickname, request);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponseEntity> deleteUser(HttpServletRequest request) {
        return userService.deleteUser(request);
    }
}