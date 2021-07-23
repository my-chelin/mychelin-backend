package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Place;
import com.a206.mychelin.domain.repository.PlaceRepository;
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
        ResponseEntity<Object> result =null;
        HttpStatus resultHttpStatus;

        HashMap<Object,Object> hm = new HashMap<>();

        if(!placeObject.isPresent()){
            hm.put("status","400");
            hm.put("data",null);

            resultHttpStatus=HttpStatus.BAD_REQUEST;
        }
        else{
            resultHttpStatus=HttpStatus.ACCEPTED;
            hm.put("status","200");
            hm.put("data",placeObject.get());
        }

        return new ResponseEntity<Object>(hm,resultHttpStatus);
    }

    @Override
    public ResponseEntity getPlaceInfoByName(String name) {
        List<Place> placesArrayList = placeRepository.findPlacesByNameContains(name);

        HashMap<Object,Object> hm = new HashMap<>();
        hm.put("status","200");
        hm.put("data",placesArrayList);

        return new ResponseEntity<Object>(hm,HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity getPlaceInfoByLocation(String location) {
        List<Place> placesArrayList = placeRepository.findPlacesByLocationContains(location);

        HashMap<Object,Object> hm = new HashMap<>();
        hm.put("status","200");
        hm.put("data",placesArrayList);

        return new ResponseEntity<Object>(hm,HttpStatus.ACCEPTED);
    }
}
