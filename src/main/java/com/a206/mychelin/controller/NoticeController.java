package com.a206.mychelin.controller;

import com.a206.mychelin.domain.repository.NoticeCommentRepository;
import com.a206.mychelin.domain.repository.NoticeFollowRepository;
import com.a206.mychelin.domain.repository.NoticePostLikeRepository;
import com.a206.mychelin.service.NoticeService;
import com.a206.mychelin.util.RealTimeDataBase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    final private RealTimeDataBase realTimeDataBase;
    final private NoticeService noticeService;
    // 나중에 자기 알림만 가능하도록 변경 필요.
    @GetMapping("{nickname}")
    public ResponseEntity getNotice(@PathVariable String nickname){

        return noticeService.getNotice(nickname);

    }

}
