package com.a206.mychelin.controller;

import com.a206.mychelin.util.RealTimeDataBase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    final private RealTimeDataBase realTimeDataBase;

    @GetMapping("{nickname}")
    public void test(@PathVariable String nickname){

        realTimeDataBase.setNotice("123@naver.com");

    }

}
