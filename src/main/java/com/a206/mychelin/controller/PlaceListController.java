package com.a206.mychelin.controller;

import com.a206.mychelin.config.AuthConstants;
import com.a206.mychelin.service.PlaceListService;
import com.a206.mychelin.util.TokenUtils;
import com.a206.mychelin.web.dto.PlaceListCreateRequest;
import com.a206.mychelin.web.dto.PlaceListItemRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/placelist")
@RequiredArgsConstructor
public class PlaceListController {
    final private PlaceListService placeListService;

    @ApiOperation(value = "맛집 리스트 생성")
    @PostMapping
    public ResponseEntity createPlaceList(@RequestBody PlaceListCreateRequest placeListCreateRequest) {
        return placeListService.createPlaceList(placeListCreateRequest);
    }

    @ApiOperation(value = "맛집 리스트 id로 검색 => 리스트의 상세 정보는 안나옵니다!")
    @ApiImplicitParam(name = "id", value = "조회할 맛집 리스트 id")
    @GetMapping("/searchid/{id}")
    public ResponseEntity searchPlaceListById(@PathVariable int id) {
        return placeListService.searchPlaceListById(id);
    }

    @ApiOperation(value = "맛집 리스트 제목으로 검색")
    @ApiImplicitParam(name = "title", value = "검색할 맛집 리스트 제목")
    @GetMapping("/searchtitle/{title}")
    public ResponseEntity searchPlaceListByTitle(@PathVariable String title) {
        return placeListService.searchPlaceListByTitle(title);
    }

    @ApiOperation(value = "맛집 리스트의 상세 맛집 정보")
    @ApiImplicitParam(name = "listId", value = "맛집 리스트의 id")
    @GetMapping("/listitems/{listId}")
    public ResponseEntity getPlaceListItemByTitle(@PathVariable int listId) {
        return placeListService.getPlaceListItemByTitle(listId);
    }

    @ApiOperation(value = "맛집 리스트의 맛집 추가")
    @PostMapping("/listitems/items")
    public ResponseEntity insertPlaceListItem(@RequestHeader(AuthConstants.AUTH_HEADER) String myToken, @RequestBody PlaceListItemRequest placeListItemRequest) {
        String token = TokenUtils.getTokenFromHeader(myToken);
        String userId = TokenUtils.getUserIdFromToken(token);
        return placeListService.insertPlaceListItem(userId, placeListItemRequest.getListId(), placeListItemRequest.getPlaceId());
    }

    @ApiOperation(value = "맛집 리스트의 맛집 삭제")
    @DeleteMapping("/listitems/items")
    public ResponseEntity deletePlaceListItem(@RequestHeader(AuthConstants.AUTH_HEADER) String myToken, @RequestBody PlaceListItemRequest placeListItemRequest) {
        String token = TokenUtils.getTokenFromHeader(myToken);
        String userId = TokenUtils.getUserIdFromToken(token);
        return placeListService.deletePlaceListItem(userId, placeListItemRequest.getListId(), placeListItemRequest.getPlaceId());
    }
}