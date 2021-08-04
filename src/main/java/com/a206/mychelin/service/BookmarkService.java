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
import com.a206.mychelin.web.dto.CustomResponseEntity;
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
    public ResponseEntity<CustomResponseEntity> addBookmarkPlace(@RequestBody Bookmark.PlaceRequest placeRequest, HttpServletRequest httpRequest) {
        CustomResponseEntity customResponse;
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message("로그인 후 이용해주세요")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
        }

        int placeId = placeRequest.getPlaceId();
        Optional<Place> place = placeRepository.findPlacesById(placeId);
        if (!place.isPresent()) {
            customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message("존재하지 않는 장소입니다.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
        }

        Optional<BookmarkPlace> item = bookmarkRepository.findBookmarkPlaceByUserIdAndPlaceId(userId, placeId);
        if (!item.isPresent()) {
            BookmarkPlace bookmark = BookmarkPlace.builder()
                    .placeId(placeRequest.getPlaceId())
                    .userId(userId)
                    .build();
            bookmarkRepository.save(bookmark);
            customResponse = CustomResponseEntity.builder()
                    .status(200)
                    .message("장소를 북마크로 저장하였습니다.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
        }
        BookmarkPlace bookmark = new BookmarkPlace(item.get().getId(), userId, item.get().getPlaceId(), item.get().getAddDate());
        bookmarkRepository.delete(bookmark);
        customResponse = CustomResponseEntity.builder()
                .status(200)
                .message("북마크를 해제 하였습니다.")
                .build();
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> addBookmarkPlaceList(Bookmark.PlaceListRequest placeListRequest, HttpServletRequest httpRequest) {
        CustomResponseEntity customResponse;
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message("로그인 후 이용해주세요")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
        }
        Optional<PlaceList> checkPlaceList = placeListRepository.findById(placeListRequest.getPlaceListId());
        if (!checkPlaceList.isPresent()) {
            customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message("존재 하지 않는 리스트입니다.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
        }
        Optional<BookmarkPlacelist> item = bookmarkListRepository.findBookmarkPlacelistByUserIdAndPlacelistId(userId, placeListRequest.getPlaceListId());
        if (!item.isPresent()) {
            BookmarkPlacelist bookmarkPlacelist = BookmarkPlacelist.builder()
                    .userId(userId)
                    .placelistId(placeListRequest.getPlaceListId())
                    .build();

            bookmarkListRepository.save(bookmarkPlacelist);
            customResponse = CustomResponseEntity.builder()
                    .status(200)
                    .message("리스트가 저장되었습니다.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
        }
        BookmarkPlacelist bookmarkPlacelist = bookmarkListRepository.getById(item.get().getId());
        bookmarkListRepository.delete(bookmarkPlacelist);

        customResponse = CustomResponseEntity.builder()
                .status(200)
                .message("북마크가 해제되었습니다.")
                .build();
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

    public ResponseEntity<CustomResponseEntity> getPlaceBookmarks(HttpServletRequest httpRequest) {
        CustomResponseEntity customResponse;
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            customResponse = CustomResponseEntity.builder()
                    .status(401)
                    .message("로그인 후 이용해주세요.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.UNAUTHORIZED);
        }
        List<Object[]> items = bookmarkRepository.findBookmarkPlacesByUserId(userId);
        if (items.size() == 0) {
            customResponse = CustomResponseEntity.builder()
                    .status(200)
                    .message("아직 북마크로 찜한 장소가 없네요.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
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

        customResponse = CustomResponseEntity.builder()
                .status(200)
                .message("북마크에 저장한 장소들을 불러왔어요.")
                .data(arr)
                .build();
        return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<CustomResponseEntity> getPlaceListBookmarks(HttpServletRequest httpRequest) {
        CustomResponseEntity customResponse;
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            customResponse = CustomResponseEntity.builder()
                    .status(401)
                    .message("로그인 후 이용해주세요.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.UNAUTHORIZED);
        }
        List<Object[]> items = bookmarkListRepository.findBookmarkPlacelistsByUserIdOrderByAddDate(userId);
        if (items.size() == 0) {
            customResponse = CustomResponseEntity.builder()
                    .status(200)
                    .message("북마크에 맛집 리스트를 저장해보세요.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
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
        customResponse = CustomResponseEntity.builder()
                .status(200)
                .message("북마크에 저장한 맛집 리스트를 가져왔습니다.")
                .data(arr)
                .build();
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }
}