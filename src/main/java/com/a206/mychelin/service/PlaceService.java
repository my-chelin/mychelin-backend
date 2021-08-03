package com.a206.mychelin.service;

import org.springframework.http.ResponseEntity;

public interface PlaceService {
    ResponseEntity getPlaceInfoById(String id);

    ResponseEntity getPlaceInfoByName(String name, int page, int pageSize);

    ResponseEntity getPlaceInfoByLocation(String location, int page, int pageSize);

    ResponseEntity getPlaceByCoordinate(float lat, float lng, float distance);
}