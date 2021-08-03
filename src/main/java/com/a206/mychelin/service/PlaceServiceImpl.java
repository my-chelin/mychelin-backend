package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Place;
import com.a206.mychelin.domain.repository.PlaceRepository;
import com.a206.mychelin.web.dto.CustomResponseEntity;
import com.a206.mychelin.web.dto.PlaceAndStarRateByCoordinate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;

    @Override
    public ResponseEntity getPlaceInfoById(String id) {
        Optional<Place> placeObject = placeRepository.findPlacesById(Integer.parseInt(id));

        // 이 부분 Object는 나중에 객체로 변환
        ResponseEntity<CustomResponseEntity> result = null;
        HttpStatus resultHttpStatus;
        CustomResponseEntity req;

        if (!placeObject.isPresent()) {
            req = CustomResponseEntity.builder()
                    .status(400)
                    .message("식당 id가 존재하지 않습니다.")
                    .build();
            resultHttpStatus = HttpStatus.BAD_REQUEST;
        } else {
            resultHttpStatus = HttpStatus.ACCEPTED;
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("placeData", placeObject.get());

            if (placeRepository.getStartRateById(id).isPresent()) {
                hashMap.put("star_rate", placeRepository.getStartRateById(id).get());
            } else {
                hashMap.put("star_rate", null);
            }
            req = CustomResponseEntity.builder()
                    .status(200)
                    .message("식당을 찾았습니다.")
                    .data(hashMap)
                    .build();
        }
        return new ResponseEntity<Object>(req, resultHttpStatus);
    }

    @Override
    public ResponseEntity getPlaceInfoByName(String name, int page, int pageSize) {
        long totalPageItemCnt = placeRepository.countByNameContains(name);

        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalPageItemCnt);
        linkedHashMap.put("totalPage", ((totalPageItemCnt - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        List<Place> placesArrayList = placeRepository.findPlacesByNameContainsOrderById(name, pageRequest);
        List<HashMap<String, Object>> resultList = new ArrayList<>();

        for (Place nowPlace : placesArrayList) {
            HashMap<String, Object> hashMap = new LinkedHashMap<>();

            if (placeRepository.getStartRateById(String.valueOf(nowPlace.getId())).isPresent()) {
                hashMap.put("star_rate", placeRepository.getStartRateById(String.valueOf(nowPlace.getId())).get());
            } else {
                hashMap.put("star_rate", null);
            }
            hashMap.put("placeData", nowPlace);
            resultList.add(hashMap);
        }
        linkedHashMap.put("data", resultList);
        CustomResponseEntity req = CustomResponseEntity.builder()
                .status(200)
                .message("이름으로 검색에 성공했습니다.")
                .data(linkedHashMap)
                .build();
        return new ResponseEntity<Object>(req, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity getPlaceInfoByLocation(String location, int page, int pageSize) {
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
                hashMap.put("star_rate", placeRepository.getStartRateById(String.valueOf(nowPlace.getId())).get());
            } else {
                hashMap.put("star_rate", null);
            }
            hashMap.put("placeData", nowPlace);
            resultList.add(hashMap);
        }
        linkedHashMap.put("data", resultList);

        CustomResponseEntity req = CustomResponseEntity.builder()
                .status(200)
                .message("장소로 검색에 성공했습니다.")
                .data(linkedHashMap)
                .build();
        return new ResponseEntity<Object>(req, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity getPlaceByCoordinate(float lat, float lng, float distance) {
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
                    .operation_hours((String) objects[8])
                    .category_id((int) objects[9])
                    .image((String) objects[10])
                    .star_rate((Double) objects[11])
                    .build()
            );
        }
        CustomResponseEntity req = CustomResponseEntity.builder()
                .status(200)
                .message("위치 기반 맛집 검색에 성공했습니다.")
                .data(resultList)
                .build();
        return new ResponseEntity<CustomResponseEntity>(req, HttpStatus.OK);
    }
}