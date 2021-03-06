package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.BookmarkPlace;
import com.a206.mychelin.domain.entity.Place;
import com.a206.mychelin.domain.repository.BookmarkRepository;
import com.a206.mychelin.domain.repository.PlaceRepository;
import com.a206.mychelin.domain.repository.ReviewRepository;
import com.a206.mychelin.util.TokenToId;
import com.a206.mychelin.web.dto.PlaceDto;
import com.a206.mychelin.web.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ReviewRepository reviewRepository;
    private final UserPreferenceService userPreferenceService;

    public ResponseEntity<Response> getPlaceInfoById(String id, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        Optional<Place> nowPlace = placeRepository.findPlacesById(Integer.parseInt(id));
        if (!nowPlace.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "식당이 존재하지 않습니다.", null);
        }
        int reviewCnt = reviewRepository.countAllByPlaceId(nowPlace.get().getId());
        // 로그인 안 한 상태
        PlaceDto.PlaceResponse.PlaceResponseBuilder placeBuilder = PlaceDto.PlaceResponse.builder()
                .id(nowPlace.get().getId())
                .name(nowPlace.get().getName())
                .description(nowPlace.get().getDescription())
                .latitude(nowPlace.get().getLatitude())
                .longitude(nowPlace.get().getLongitude())
                .phone(nowPlace.get().getPhone())
                .location(nowPlace.get().getLocation())
                .operationHours(nowPlace.get().getOperationHours())
                .categoryId(nowPlace.get().getCategoryId())
                .image(nowPlace.get().getImage())
                .reviewCnt(reviewCnt);

        if (userId != null) {
            Optional<BookmarkPlace> bookmark = bookmarkRepository.findBookmarkPlaceByUserIdAndPlaceId(userId, nowPlace.get().getId());
            if (bookmark.isPresent()) {
                placeBuilder = placeBuilder.isSaved(true);
            }
        }

        Optional<Double> starRate = placeRepository.getStarRateById(String.valueOf(nowPlace.get().getId()));
        if (starRate.isPresent()) {
            placeBuilder = placeBuilder.starRate(starRate.get());
        }
        PlaceDto.PlaceResponse place = placeBuilder.build();
        return Response.newResult(HttpStatus.OK, "식당을 찾았습니다.", place);
    }

    public ResponseEntity<Response> getPlaceInfoByName(String name, int page, int pageSize, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        long totalPageItemCnt = placeRepository.countByNameContains(name);

        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalPageItemCnt);
        linkedHashMap.put("totalPage", ((totalPageItemCnt - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        List<Place> placesArrayList = placeRepository.findPlacesByNameContainsOrderById(name, pageRequest);

        ArrayList<PlaceDto.PlaceResponse> resultList = new ArrayList<>();
        for (Place nowPlace : placesArrayList) {
            int reviewCnt = reviewRepository.countAllByPlaceId(nowPlace.getId());
            PlaceDto.PlaceResponse.PlaceResponseBuilder placeResponseBuilder = PlaceDto.PlaceResponse.builder()
                    .id(nowPlace.getId())
                    .name(nowPlace.getName())
                    .description(nowPlace.getDescription())
                    .latitude(nowPlace.getLatitude())
                    .longitude(nowPlace.getLongitude())
                    .phone(nowPlace.getPhone())
                    .location(nowPlace.getLocation())
                    .operationHours(nowPlace.getOperationHours())
                    .categoryId(nowPlace.getCategoryId())
                    .image(nowPlace.getImage())
                    .reviewCnt(reviewCnt);
            Optional<Double> starRate = placeRepository.getStarRateById(String.valueOf(nowPlace.getId()));
            if (starRate.isPresent()) {
                placeResponseBuilder = placeResponseBuilder.starRate(starRate.get());
            }
            if (userId != null) {
                Optional<BookmarkPlace> bookmark = bookmarkRepository.findBookmarkPlaceByUserIdAndPlaceId(userId, nowPlace.getId());
                if (bookmark.isPresent()) {
                    placeResponseBuilder = placeResponseBuilder.isSaved(true);
                }
            }
            PlaceDto.PlaceResponse place = placeResponseBuilder.build();
            resultList.add(place);
        }
        if (resultList.size() == 0) {
            return Response.newResult(HttpStatus.OK, "검색결과가 없습니다.", null);
        }
        linkedHashMap.put("data", resultList);
        return Response.newResult(HttpStatus.OK, "식당이름으로 검색에 성공했습니다.", linkedHashMap);
    }

    public ResponseEntity<Response> getPlaceInfoByLocation(String location, int page, int pageSize, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        long totalPageItemCnt = placeRepository.countByLocationContains(location);

        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalPageItemCnt);
        linkedHashMap.put("totalPage", ((totalPageItemCnt - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        List<Place> placesArrayList = placeRepository.findPlacesByLocationContainsOrderById(location, pageRequest);
        // 바꾼다면 여기부터 아래까지
        ArrayList<PlaceDto.PlaceResponse> resultList = new ArrayList<>();
        for (Place nowPlace : placesArrayList) {
            PlaceDto.PlaceResponse.PlaceResponseBuilder placeResponseBuilder = PlaceDto.PlaceResponse.builder()
                    .id(nowPlace.getId())
                    .name(nowPlace.getName())
                    .description(nowPlace.getDescription())
                    .latitude(nowPlace.getLatitude())
                    .longitude(nowPlace.getLongitude())
                    .phone(nowPlace.getPhone())
                    .location(nowPlace.getLocation())
                    .operationHours(nowPlace.getOperationHours())
                    .categoryId(nowPlace.getCategoryId())
                    .image(nowPlace.getImage());
            Optional<Double> starRate = placeRepository.getStarRateById(String.valueOf(nowPlace.getId()));
            if (starRate.isPresent()) {
                placeResponseBuilder = placeResponseBuilder.starRate(starRate.get());
            }
            if (userId != null) {
                Optional<BookmarkPlace> bookmark = bookmarkRepository.findBookmarkPlaceByUserIdAndPlaceId(userId, nowPlace.getId());
                if (bookmark.isPresent()) {
                    placeResponseBuilder = placeResponseBuilder.isSaved(true);
                }
            }
            PlaceDto.PlaceResponse place = placeResponseBuilder.build();
            resultList.add(place);
        }
        if (resultList.size() == 0) {
            return Response.newResult(HttpStatus.OK, "검색결과가 없습니다.", null);
        }
        linkedHashMap.put("data", resultList);
        return Response.newResult(HttpStatus.OK, "장소로 검색에 성공했습니다.", linkedHashMap);
    }

    public ResponseEntity<Response> getPlaceByCoordinate(double lat, double lng, float distance) {
        List<Object[]> placeList = placeRepository.getPlaceByCoordinate(lat, lng, distance);
        List<PlaceDto.PlaceAndStarRateByCoordinate> resultList = new ArrayList<>();
        for (Object[] objects : placeList) {
            resultList.add(PlaceDto.PlaceAndStarRateByCoordinate.builder()
                    .distance((double) objects[0])
                    .id((int) objects[1])
                    .name((String) objects[2])
                    .descrption((String) objects[3])
                    .lattitude((double) objects[4])
                    .longitude((double) objects[5])
                    .phone((String) objects[6])
                    .location((String) objects[7])
                    .operationHours((String) objects[8])
                    .categoryId((int) objects[9])
                    .image((String) objects[10])
                    .starRate((Double) objects[11])
                    .build()
            );
        }
        if (resultList.size() == 0) {
            return Response.newResult(HttpStatus.OK, "검색결과가 없습니다.", null);
        }
        return Response.newResult(HttpStatus.OK, "장소로 검색에 성공했습니다.", resultList);
    }

    public ResponseEntity<Response> getPlacesBySimilarUser(HttpServletRequest httpServletRequest) {
        String userId = TokenToId.check(httpServletRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요", null);
        }

        String similarUserId = userPreferenceService.findMostSimilarUserByTaste(userId);
        if (similarUserId == null) {
            return Response.newResult(HttpStatus.NO_CONTENT, "서비스 이용을 위한 데이터를 모으는 중입니다. 조금만 기다려주세요", null);
        }
        if (similarUserId.equals("BAD_REQUEST")) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "취향 설문을 진행해주세요.", null);
        }
        if (similarUserId.equals("curUserNotValid")) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "일치하는 사용자가 없습니다.", null);
        }
        if (similarUserId.equals("NO_CONTENT")) {
            return Response.newResult(HttpStatus.NO_CONTENT, "서비스 이용을 위한 데이터를 모으는 중입니다. 조금만 기다려주세요.", null);
        }
        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("similarTasteUser", similarUserId);
        System.out.println(userId + "와 비슷한 유저 : " + similarUserId);

        List<Object[]> recommendations = placeRepository.findPlacesBySimilarUsersRecommendation(userId, similarUserId);
        ArrayList<PlaceDto.PlaceRecommendationReviewedBySimilarUser> arr = new ArrayList<>();
        for (Object[] item : recommendations) {
            arr.add(PlaceDto.PlaceRecommendationReviewedBySimilarUser.builder()
                    .id((int) item[0])
                    .name((String) item[1])
                    .emoji((String) item[2])
                    .description((String) item[3])
                    .location((String) item[4])
                    .reviewContent((String) item[5])
                    .starRate((float) item[6])
                    .build());
        }
        if (arr.size() > 0) {
//            return Response.newResult(HttpStatus.OK, similarUserId + " 유저가 새로운 장소를 평가하길 기다려주세요.", linkedHashMap);

            Random rand = new Random();
            // 랜덤 5개 추출하기
            ArrayList<PlaceDto.PlaceRecommendationReviewedBySimilarUser> randomArr = new ArrayList<>();
            int randomArrSize = (arr.size() > 5) ? 5 : arr.size();
            for (int i = 0; i < randomArrSize; i++) {
                int randomIdx = rand.nextInt(arr.size());
                while (randomArr.contains(arr.get(randomIdx))) {
                    randomIdx = rand.nextInt(arr.size());
                }
                randomArr.add(arr.get(randomIdx));
            }
            linkedHashMap.put("similarUserVisited", randomArr);
        } else {
            linkedHashMap.put("similarUserVisited", null);
        }

//        linkedHashMap.put("data", randomArr);
        List<Object[]> places = placeRepository.findRecentlyAddedPlaces();
        // p.id, p.name, p.description, p.location, r.starRate
        ArrayList<PlaceDto.RecentlySavedPlaces> recentlySavedPlaces = new ArrayList<>();
        for (Object[] item : places) {
            recentlySavedPlaces.add(PlaceDto.RecentlySavedPlaces.builder()
                    .id((int) item[0])
                    .name((String) item[1])
                    .emoji((String) item[2])
                    .description((String) item[3])
                    .location((String) item[4])
                    .starRate((double) item[5])
                    .build());
        }
        linkedHashMap.put("recentlyAdded", recentlySavedPlaces);

        return Response.newResult(HttpStatus.OK, "유저들이 저장한 평가하고 저장한 식당 정보입니다.", linkedHashMap);
    }

    public ResponseEntity<Response> getRecentlyAddedPlaces() {
        List<Object[]> places = placeRepository.findRecentlyAddedPlaces();
        // p.id, p.name, p.description, p.location, r.starRate
        ArrayList<PlaceDto.RecentlySavedPlaces> recentlySavedPlaces = new ArrayList<>();
        for (Object[] item : places) {
            recentlySavedPlaces.add(PlaceDto.RecentlySavedPlaces.builder()
                    .id((int) item[0])
                    .name((String) item[1])
                    .emoji((String) item[2])
                    .description((String) item[3])
                    .location((String) item[4])
                    .starRate((double) item[5])
                    .build());
        }
        return Response.newResult(HttpStatus.OK, "최근에 사용자들이 저장한 장소입니다", recentlySavedPlaces);
    }
}