package com.a206.mychelin.controller;

import com.a206.mychelin.exception.PageIndexLessThanZeroException;
import com.a206.mychelin.service.PlaceService;
import com.a206.mychelin.web.dto.Response;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @ApiOperation(value = "id를 이용하여 식당 정보 조회")
    @ApiImplicitParam(name = "id", value = "식당 고유 id")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getPlaceInfoById(@PathVariable String id, HttpServletRequest httpServletRequest) {
        return placeService.getPlaceInfoById(id, httpServletRequest);
    }

    @ApiOperation(value = "식당 이름을 이용하여 식당 정보 검색")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "검색할 식당 이름"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "페이지당 보여주는 데이터 개수", dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping
    public ResponseEntity<Response> getPlaceInfoByName(@RequestParam(defaultValue = "") String name
            , HttpServletRequest httpServletRequest
            , @RequestParam(defaultValue = "") String location
            , @RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int pageSize) throws PageIndexLessThanZeroException {
        if (location.equals("") && !name.equals("")) {
            try {
                return placeService.getPlaceInfoByName(name, page, pageSize, httpServletRequest);
            } catch (ArithmeticException | IllegalArgumentException e) {
                throw new PageIndexLessThanZeroException();
            }
        } else if (!location.equals("") && name.equals("")) {
            try {
                return placeService.getPlaceInfoByLocation(location, page, pageSize, httpServletRequest);
            } catch (ArithmeticException | IllegalArgumentException e) {
                throw new PageIndexLessThanZeroException();
            }
        }
        return Response.newResult(HttpStatus.BAD_REQUEST, "파라미터가 잘못 되었습니다.", null);
    }

    @ApiOperation(value = "위치 정보를 이용하여, 해당 위치 주변 맛집 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lat", value = "위도", required = true),
            @ApiImplicitParam(name = "lng", value = "경도", required = true),
            @ApiImplicitParam(name = "distance", value = "거리(km), 기본 값 : 0.5km", dataType = "float", paramType = "query", defaultValue = "0.5"),
    })
    @GetMapping("/coordinate")
    public ResponseEntity<Response> getPlaceByCoordinate(
            @RequestParam float lat
            , @RequestParam float lng
            , @RequestParam(defaultValue = "0.5", required = false) float distance) {
        return placeService.getPlaceByCoordinate(lat, lng, distance);
    }

    @GetMapping("/recommend")
    public ResponseEntity getPlacesBySimilarUser(HttpServletRequest httpServletRequest) {
        return placeService.getPlacesBySimilarUser(httpServletRequest);
    }
}