package com.a206.mychelin.controller;

import com.a206.mychelin.domain.repository.NoticeCommentRepository;
import com.a206.mychelin.domain.repository.NoticeFollowRepository;
import com.a206.mychelin.domain.repository.NoticePostLikeRepository;
import com.a206.mychelin.service.NoticeService;
import com.a206.mychelin.util.RealTimeDataBase;
import com.a206.mychelin.web.dto.NoticeDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    final private RealTimeDataBase realTimeDataBase;
    final private NoticeService noticeService;

    @ApiOperation(value = "해당 계정의 알림 목록을 가져온다.")
    @GetMapping
    public ResponseEntity getNotice(HttpServletRequest httpServletRequest){

        return noticeService.getNotice(httpServletRequest);

    }

    @ApiOperation(value = "해당 계정의 알림 읽음 표시")
    @PutMapping
    public ResponseEntity readNotice(HttpServletRequest httpServletRequest, @RequestBody NoticeDto.NoticeRequest noticeRequest){

        return noticeService.readNotice(httpServletRequest,noticeRequest);

    }

}
