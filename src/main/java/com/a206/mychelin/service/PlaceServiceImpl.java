package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Place;
import com.a206.mychelin.domain.repository.PlaceRepository;
import com.a206.mychelin.web.dto.CustomResponseEntity;
import com.a206.mychelin.web.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService{


    private final PlaceRepository placeRepository;

    @Override
    public ResponseEntity getPlaceInfoById(String id) {

        Optional<Place> placeObject =placeRepository.findPlacesById(Integer.parseInt(id));

        // 이 부분 Object는 나중에 객체로 변환
        ResponseEntity<CustomResponseEntity> result =null;
        HttpStatus resultHttpStatus;
        CustomResponseEntity req ;

        if(!placeObject.isPresent()){
            req = CustomResponseEntity.builder()
                    .status(400)
                    .message("식당 id가 존재하지 않습니다.")
                    .build();
            resultHttpStatus=HttpStatus.BAD_REQUEST;
        }
        else{
            resultHttpStatus=HttpStatus.ACCEPTED;
            req = CustomResponseEntity.builder()
                    .status(200)
                    .message("식당을 찾았습니다.")
                    .data(placeObject.get())
                    .build();
        }

        return new ResponseEntity<Object>(req,resultHttpStatus);
    }

    @Override
    public ResponseEntity getPlaceInfoByName(String name) {
        List<Place> placesArrayList = placeRepository.findPlacesByNameContains(name);
        CustomResponseEntity req = CustomResponseEntity.builder()
                .status(200)
                .message("이름으로 검색에 성공했습니다.")
                .data(placesArrayList)
                .build();
        return new ResponseEntity<Object>(req,HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity getPlaceInfoByLocation(String location) {
        List<Place> placesArrayList = placeRepository.findPlacesByLocationContains(location);

        CustomResponseEntity req = CustomResponseEntity.builder()
                .status(200)
                .message("위치로 검색에 성공했습니다.")
                .data(placesArrayList)
                .build();

        return new ResponseEntity<Object>(req,HttpStatus.ACCEPTED);
    }
}
