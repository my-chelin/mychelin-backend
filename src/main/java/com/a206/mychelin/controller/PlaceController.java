package com.a206.mychelin.controller;

import com.a206.mychelin.exception.PageIndexLessThanZeroException;
import com.a206.mychelin.service.PlaceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @ApiOperation(value = "id를 이용하여 식당 정보 조회")
    @ApiImplicitParam(name = "id", value = "식당 고유 id")
    @GetMapping("/{id}")
    public ResponseEntity getPlaceInfoById(@PathVariable String id) {
        return placeService.getPlaceInfoById(id);
    }

    @ApiOperation(value = "식당 이름을 이용하여 식당 정보 검색")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "검색할 식당 이름"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", required = false, dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", required = false, dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("search/name/{name}")
    public ResponseEntity getPlaceInfoByName(@PathVariable String name
            , @RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int pagesize) throws PageIndexLessThanZeroException {
        try{
            return placeService.getPlaceInfoByName(name,page ,pagesize);
        }
        catch (ArithmeticException | IllegalArgumentException e){
            throw new PageIndexLessThanZeroException();
        }

    }

    @ApiOperation(value = "장소를 이용하여 식당 정보 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "location", value = "검색할 식당 위치"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", required = false, dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", required = false, dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("search/location/{location}")
    public ResponseEntity getPlaceInfoByLocation(@PathVariable String location
            , @RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int pagesize) throws PageIndexLessThanZeroException {
        try{
            return placeService.getPlaceInfoByLocation(location,page ,pagesize);
        }
        catch (ArithmeticException | IllegalArgumentException e){
            throw new PageIndexLessThanZeroException();
        }

    }

    @ApiOperation(value = "위치 정보를 이용하여, 해당 위치 주변 맛집 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lat", value = "위도",required = true),
            @ApiImplicitParam(name = "lng", value = "경도",required = true),
            @ApiImplicitParam(name = "distance", value = "거리(km), 기본 값 : 0.5km", required = false, dataType = "float", paramType = "query", defaultValue = "0.5"),
    })
    @GetMapping("coordinate")
    public ResponseEntity getPlaceByCoordinate(
            @RequestParam(required = true) float lat
            , @RequestParam(required = true) float lng
    ,@RequestParam(defaultValue = "0.5", required = false) float distance) {
        return placeService.getPlaceByCoordinate(lat,lng,distance);
    }
}