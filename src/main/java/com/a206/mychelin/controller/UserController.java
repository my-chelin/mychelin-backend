package com.a206.mychelin.controller;

import com.a206.mychelin.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        return new ResponseEntity<String>("test", HttpStatus.OK);
    }

    @PostMapping("/loginSuccess")
    public String loginSuccess(){
        return "성공!";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess1(){
        return "성공!12";
    }
}