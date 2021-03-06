package com.a206.mychelin.controller;

import com.a206.mychelin.service.BookmarkService;
import com.a206.mychelin.web.dto.Bookmark;
import com.a206.mychelin.web.dto.Response;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @ApiOperation(value = "사용자가 저장한 장소에 대한 북마크")
    @GetMapping("/places")
    public ResponseEntity<Response> getPlaceBookmarks(HttpServletRequest httpServletRequest) {
        return bookmarkService.getPlaceBookmarks(httpServletRequest);
    }

    @ApiOperation(value = "사용자가 저장한 맛집 리스트에 대한 북마크")
    @GetMapping("/lists")
    public ResponseEntity<Response> getPlaceListBookmarks(HttpServletRequest httpServletRequest) {
        return bookmarkService.getPlaceListBookmarks(httpServletRequest);
    }

    @ApiOperation(value = "장소 북마크에 저장하기")
    @ApiImplicitParam(name = "placeId", value = "장소 고유 id")
    @PutMapping("/places")
    public ResponseEntity<Response> addPlaceBookmark(@RequestBody Bookmark.PlaceRequest placeRequest, HttpServletRequest httpServletRequest) {
        return bookmarkService.addBookmarkPlace(placeRequest, httpServletRequest);
    }

    @ApiOperation(value = "맛집 리스트 북마크에 저장하기")
    @ApiImplicitParam(name = "placeListId", value = "맛집 리스트 고유 id")
    @PutMapping("/lists")
    public ResponseEntity<Response> addPlaceListBookmark(@RequestBody Bookmark.PlaceListRequest placeListRequest, HttpServletRequest httpServletRequest) {
        return bookmarkService.addBookmarkPlaceList(placeListRequest, httpServletRequest);
    }
}