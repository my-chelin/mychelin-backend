package com.a206.mychelin.controller;

import com.a206.mychelin.service.PlaceReviewService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/place/review")
@RequiredArgsConstructor
public class PlaceReviewController {
    final private PlaceReviewService placeReviewService;

    @ApiOperation(value = "닉네임을 이용하여 해당 유저의 모든 맛집 리뷰 조회(최신순)")
    @ApiImplicitParam(name = "nickName", value = "유저 고유 닉네임")
    @GetMapping("/user/{nickName}")
    public ResponseEntity getPlaceReviewsByUser(@PathVariable String nickName){
        return placeReviewService.getPlaceReviewsByUser(nickName);
    }

    @ApiOperation(value = "식당 모든 리뷰 가져오기(최신순)")
    @ApiImplicitParam(name = "placeId", value = "식당 고유 번호")
    @GetMapping("{placeId}")
    public ResponseEntity getPlaceReviewsByUser(@PathVariable int placeId){
        return placeReviewService.getPlaceAllReviewsByPlaceId(placeId);
    }

}
