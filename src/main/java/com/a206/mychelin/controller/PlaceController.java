package com.a206.mychelin.controller;

import com.a206.mychelin.service.PlaceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @ApiOperation(value="id를 이용하여 식당 정보 조회")
    @ApiImplicitParam(name="id", value="식당 고유 id")
    @GetMapping("/{id}")
    public ResponseEntity getPlaceInfoById(@PathVariable String id){
        return placeService.getPlaceInfoById(id);
    }

    @ApiOperation(value="식당 이름을 이용하여 식당 정보 검색")
    @ApiImplicitParam(name="name", value="검색할 식당 이름")
    @GetMapping("search/name/{name}")
    public ResponseEntity getPlaceInfoByName(@PathVariable String name){
        return placeService.getPlaceInfoByName(name);
    }

    @ApiOperation(value="장소를 이용하여 식당 정보 조회")
    @ApiImplicitParam(name="location", value="검색할 식당 위치")
    @GetMapping("search/location/{location}")
    public ResponseEntity getPlaceInfoByLocation(@PathVariable String location){
        return placeService.getPlaceInfoByLocation(location);
    }


}
