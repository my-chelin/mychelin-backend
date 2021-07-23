package com.a206.mychelin.controller;

import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.web.dto.PasswordChangeRequest;
import com.a206.mychelin.service.UserService;
import com.a206.mychelin.web.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        return userService.signUp(user);
    }

    @PutMapping("/changepwd")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest, HttpServletRequest request) {
        return userService.changePassword(passwordChangeRequest, request);
    }

    @PutMapping("/changeInfo/{id}")
    public User updateInfo(@PathVariable String id, @RequestBody UserUpdateRequest requestDto){
        return userService.update(id, requestDto);
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        return new ResponseEntity<String>("test", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        return userService.deleteUser(request);
    }
}