package com.a206.mychelin.controller;

import com.a206.mychelin.config.AuthConstants;
import com.a206.mychelin.exception.PageIndexLessThanZeroException;
import com.a206.mychelin.service.PlaceListService;
import com.a206.mychelin.util.TokenUtils;
import com.a206.mychelin.web.dto.PlaceListCreateRequest;
import com.a206.mychelin.web.dto.PlaceListItemRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "검색할 맛집 리스트 제목"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", required = false, dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", required = false, dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("/searchtitle/{title}")
    public ResponseEntity searchPlaceListByTitle(@PathVariable String title
            , @RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int pagesize) throws PageIndexLessThanZeroException {
        try {
            return placeListService.searchPlaceListByTitle(title, page, pagesize);
        } catch (ArithmeticException | IllegalArgumentException e) {
            throw new PageIndexLessThanZeroException();
        }
    }

    @ApiOperation(value = "맛집 리스트의 상세 맛집 정보")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "listId", value = "맛집 리스트의 id"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", required = false, dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", required = false, dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("/listitems/{listId}")
    public ResponseEntity getPlaceListItemByTitle(@PathVariable int listId
            , @RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int pagesize) throws PageIndexLessThanZeroException {
        try {
            return placeListService.getPlaceListItemByTitle(listId, page, pagesize);
        } catch (ArithmeticException | IllegalArgumentException e) {
            throw new PageIndexLessThanZeroException();
        }
    }

    @ApiOperation(value = "맛집 리스트 작성한 사람 기준으로 가져오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "맛집 리스트 작성한 닉네임"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", required = false, dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", required = false, dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("/listitems/user/{nickname}")
    public ResponseEntity getPlaceListItemByNickname(@PathVariable String nickname
            , @RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int pagesize) throws PageIndexLessThanZeroException {
        try {
            return placeListService.getPlaceListItemByNickname(nickname, page, pagesize);
        } catch (ArithmeticException | IllegalArgumentException e) {
            throw new PageIndexLessThanZeroException();
        }
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