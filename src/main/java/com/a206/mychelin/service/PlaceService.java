package com.a206.mychelin.service;

import org.springframework.http.ResponseEntity;

public interface PlaceService {

    ResponseEntity getPlaceInfoById(String id);
    ResponseEntity getPlaceInfoByName(String name);
    ResponseEntity getPlaceInfoByLocation(String location);

}
