package com.a206.mychelin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/loginSuccess")
    public String loginSuccess(){
        return "성공!";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess1(){
        return "성공!12";
    }
}
