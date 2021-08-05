package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Place;
import com.a206.mychelin.domain.repository.PlaceRepository;
import com.a206.mychelin.web.dto.PlaceAndStarRateByCoordinate;
import com.a206.mychelin.web.dto.PlaceDto;
import com.a206.mychelin.web.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    public ResponseEntity<Response> getPlaceInfoById(String id) {
        Optional<Place> nowPlace = placeRepository.findPlacesById(Integer.parseInt(id));
        if (!nowPlace.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "식당이 존재하지 않습니다.", null);
        }
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
                .image(nowPlace.get().getImage());
        Optional<Double> starRate = placeRepository.getStartRateById(String.valueOf(nowPlace.get().getId()));
        if (starRate.isPresent()) {
            placeBuilder = placeBuilder.starRate(starRate.get());
        }
        PlaceDto.PlaceResponse place = placeBuilder.build();
        return Response.newResult(HttpStatus.OK, "식당을 찾았습니다.", place);
    }

    public ResponseEntity<Response> getPlaceInfoByName(String name, int page, int pageSize) {
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
            Optional<Double> starRate = placeRepository.getStartRateById(String.valueOf(nowPlace.getId()));
            if (starRate.isPresent()) {
                placeResponseBuilder = placeResponseBuilder.starRate(starRate.get());
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

    public ResponseEntity<Response> getPlaceInfoByLocation(String location, int page, int pageSize) {
        long totalPageItemCnt = placeRepository.countByLocationContains(location);

        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalPageItemCnt);
        linkedHashMap.put("totalPage", ((totalPageItemCnt - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        List<Place> placesArrayList = placeRepository.findPlacesByLocationContainsOrderById(location, pageRequest);
        List<HashMap<String, Object>> resultList = new ArrayList<>();

        for (Place nowPlace : placesArrayList) {
            HashMap<String, Object> hashMap = new LinkedHashMap<>();
            if (placeRepository.getStartRateById(String.valueOf(nowPlace.getId())).isPresent()) {
                hashMap.put("starRate", placeRepository.getStartRateById(String.valueOf(nowPlace.getId())).get());
            } else {
                hashMap.put("starRate", null);
            }
            hashMap.put("placeData", nowPlace);
            resultList.add(hashMap);
        }
        if (resultList.size() == 0) {
            return Response.newResult(HttpStatus.OK, "검색결과가 없습니다.", null);
        }
        linkedHashMap.put("data", resultList);
        return Response.newResult(HttpStatus.OK, "장소로 검색에 성공했습니다.", linkedHashMap);
    }

    public ResponseEntity<Response> getPlaceByCoordinate(float lat, float lng, float distance) {
        List<Object[]> placeList = placeRepository.getPlaceByCoordinate(lat, lng, distance);
        List<PlaceAndStarRateByCoordinate> resultList = new ArrayList<>();
        for (Object[] objects : placeList) {
            resultList.add(PlaceAndStarRateByCoordinate.builder()
                    .distance((double) objects[0])
                    .id((int) objects[1])
                    .name((String) objects[2])
                    .descrption((String) objects[3])
                    .lattitude((float) objects[4])
                    .longitude((float) objects[5])
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
}