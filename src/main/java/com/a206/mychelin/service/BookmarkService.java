package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.BookmarkPlace;
import com.a206.mychelin.domain.entity.BookmarkPlacelist;
import com.a206.mychelin.domain.entity.Place;
import com.a206.mychelin.domain.entity.PlaceList;
import com.a206.mychelin.domain.repository.BookmarkListRepository;
import com.a206.mychelin.domain.repository.BookmarkRepository;
import com.a206.mychelin.domain.repository.PlaceListRepository;
import com.a206.mychelin.domain.repository.PlaceRepository;
import com.a206.mychelin.util.TimestampToDateString;
import com.a206.mychelin.util.TokenToId;
import com.a206.mychelin.web.dto.Bookmark;
import com.a206.mychelin.web.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkListRepository bookmarkListRepository;
    private final PlaceRepository placeRepository;
    private final PlaceListRepository placeListRepository;

    @Transactional
    public ResponseEntity<Response> addBookmarkPlace(@RequestBody Bookmark.PlaceRequest placeRequest, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }
        int placeId = placeRequest.getPlaceId();
        Optional<Place> place = placeRepository.findPlacesById(placeId);
        if (!place.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 장소입니다.", null);
        }

        Optional<BookmarkPlace> item = bookmarkRepository.findBookmarkPlaceByUserIdAndPlaceId(userId, placeId);
        if (!item.isPresent()) {
            BookmarkPlace bookmark = BookmarkPlace.builder()
                    .placeId(placeRequest.getPlaceId())
                    .userId(userId)
                    .build();
            bookmarkRepository.save(bookmark);
            return Response.newResult(HttpStatus.OK, "장소를 북마크로 저장하였습니다.", null);
        }
        BookmarkPlace bookmark = new BookmarkPlace(item.get().getId(), userId, item.get().getPlaceId(), item.get().getAddDate());
        bookmarkRepository.delete(bookmark);
        return Response.newResult(HttpStatus.OK, "북마크를 해체 하였습니다.", null);
    }

    @Transactional
    public ResponseEntity<Response> addBookmarkPlaceList(Bookmark.PlaceListRequest placeListRequest, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }
        Optional<PlaceList> checkPlaceList = placeListRepository.findById(placeListRequest.getPlaceListId());
        if (!checkPlaceList.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 리스트입니다.", null);
        }
        Optional<BookmarkPlacelist> item = bookmarkListRepository.findBookmarkPlacelistByUserIdAndPlacelistId(userId, placeListRequest.getPlaceListId());
        if (!item.isPresent()) {
            BookmarkPlacelist bookmarkPlacelist = BookmarkPlacelist.builder()
                    .userId(userId)
                    .placelistId(placeListRequest.getPlaceListId())
                    .build();

            bookmarkListRepository.save(bookmarkPlacelist);
            return Response.newResult(HttpStatus.OK, "리스트가 저장되었습니다.", null);
        }
        BookmarkPlacelist bookmarkPlacelist = bookmarkListRepository.getById(item.get().getId());
        bookmarkListRepository.delete(bookmarkPlacelist);
        return Response.newResult(HttpStatus.OK, "북마크가 해제되었습니다.", null);
    }

    public ResponseEntity<Response> getPlaceBookmarks(HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }
        List<Object[]> items = bookmarkRepository.findBookmarkPlacesByUserId(userId);
        if (items.size() == 0) {
            return Response.newResult(HttpStatus.OK, "아직 북마크로 찜한 장소가 없네요.", null);
        }
        ArrayList<Bookmark.PlaceResponse> arr = new ArrayList<>();
        for (Object[] item : items) {
            arr.add(Bookmark.PlaceResponse.builder()
                    .placeId((int) item[0])
                    .placeName((String) item[1])
                    .placeDescription((String) item[2])
                    .location((String) item[3])
                    .image((String) item[4])
                    .build());
        }
        return Response.newResult(HttpStatus.OK, "북마크에 저장한 장소들을 불러왔어요.", arr);
    }

    public ResponseEntity<Response> getPlaceListBookmarks(HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }
        List<Object[]> items = bookmarkListRepository.findBookmarkPlacelistsByUserIdOrderByAddDate(userId);
        if (items.size() == 0) {
            return Response.newResult(HttpStatus.OK, "아직 북마크로 찜한 맛집 리스트가 없네요.", null);
        }
        ArrayList<Bookmark.PlaceListResponse> arr = new ArrayList<>();
        for (Object[] item : items) {
            String diff = TimestampToDateString.getPassedTime((Timestamp) item[4]);
            arr.add(Bookmark.PlaceListResponse.builder()
                    .placeListId((int) item[0])
                    .userNickname((String) item[1])
                    .placeListName((String) item[2])
                    .placeCnt(item[3])
                    .addDate(diff)
                    .build());
        }
        return Response.newResult(HttpStatus.OK, "북마크에 저장한 맛집 리스트를 가져왔습니다.", arr);
    }
}