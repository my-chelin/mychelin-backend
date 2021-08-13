package com.a206.mychelin.controller;

import com.a206.mychelin.config.AuthConstants;
import com.a206.mychelin.exception.PageIndexLessThanZeroException;
import com.a206.mychelin.service.PlaceListService;
import com.a206.mychelin.util.TokenUtils;
import com.a206.mychelin.web.dto.PlaceListDto;
import com.a206.mychelin.web.dto.PlaceListItemDto;
import com.a206.mychelin.web.dto.Response;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
@RequestMapping("/placelist")
@RequiredArgsConstructor
public class PlaceListController {
    final private PlaceListService placeListService;

    @ApiOperation(value = "맛집 리스트 생성")
    @PostMapping
    public ResponseEntity<Response> createPlaceList(@RequestBody PlaceListDto.PlaceListCreateRequest placeListCreateRequest, HttpServletRequest request) {
        return placeListService.createPlaceList(placeListCreateRequest, request);
    }

    @ApiOperation(value = "맛집 리스트의 상세 맛집 정보")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "listId", value = "맛집 리스트의 id"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping("/{listId}")
    public ResponseEntity<Response> getPlaceListItemByTitle(@PathVariable int listId
            , @RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int pagesize) throws PageIndexLessThanZeroException {
        try {
            return placeListService.getPlaceListItemByTitle(listId, page, pagesize);
        } catch (ArithmeticException | IllegalArgumentException e) {
            throw new PageIndexLessThanZeroException();
        }
    }

    @ApiOperation(value = "맛집 리스트의 맛집 추가")
    @PostMapping("/{listId}")
    public ResponseEntity<Response> insertPlaceListItem(@RequestHeader(AuthConstants.AUTH_HEADER) String myToken, @PathVariable int listId, @RequestBody PlaceListItemDto.PlaceListItemRequest placeListItemRequest) {
        String token = TokenUtils.getTokenFromHeader(myToken);
        String userId = TokenUtils.getUserIdFromToken(token);
        return placeListService.insertPlaceListItem(userId, listId, placeListItemRequest.getPlaceId());
    }

    @ApiOperation(value = "맛집 리스트의 맛집 삭제")
    @DeleteMapping("/{listId}")
    public ResponseEntity<Response> deletePlaceListItem(@RequestHeader(AuthConstants.AUTH_HEADER) String myToken, @PathVariable int listId, @RequestBody PlaceListItemDto.PlaceListItemRequest placeListItemRequest) {
        String token = TokenUtils.getTokenFromHeader(myToken);
        String userId = TokenUtils.getUserIdFromToken(token);
        return placeListService.deletePlaceListItem(userId, listId, placeListItemRequest.getPlaceId());
    }

    @ApiOperation(value = "맛집 리스트 제목, id, 닉네임으로 검색. 한개씩 검색해주세요")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "검색할 맛집 리스트 제목"),
            @ApiImplicitParam(name = "id", value = "검색할 맛집 리스트 id"),
            @ApiImplicitParam(name = "nickname", value = "닉네임으로 맛집 리스트 받아오기"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지 번호", required = false, dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pagesize", value = "페이지당 보여주는 데이터 개수", required = false, dataType = "int", paramType = "query", defaultValue = "10"),
    })
    @GetMapping
    public ResponseEntity<Response> searchPlaceListByTitle(@RequestParam(defaultValue = "") String title
            , @RequestParam(defaultValue = "-1") int id
            , @RequestParam(defaultValue = "") String nickname
            , @RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int pagesize) throws PageIndexLessThanZeroException {
        if (!title.equals("") && id == -1 && nickname.equals("")) {
            try {
                return placeListService.searchPlaceListByTitle(title, page, pagesize);
            } catch (ArithmeticException | IllegalArgumentException e) {
                throw new PageIndexLessThanZeroException();
            }
        } else if (title.equals("") && id != -1 && nickname.equals("")) {
            return placeListService.searchPlaceListById(id);
        } else if (title.equals("") && id == -1 && !nickname.equals("")) {
            try {
                return placeListService.getPlaceListItemByNickname(nickname, page, pagesize);
            } catch (ArithmeticException | IllegalArgumentException e) {
                throw new PageIndexLessThanZeroException();
            }
        }
        return Response.newResult(HttpStatus.BAD_REQUEST, "파라미터가 잘못 되었습니다.", null);
    }
}