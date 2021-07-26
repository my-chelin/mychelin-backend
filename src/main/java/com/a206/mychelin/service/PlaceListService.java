package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.PlaceList;
import com.a206.mychelin.domain.entity.PlaceListItem;
import com.a206.mychelin.domain.repository.PlaceListRepository;
import com.a206.mychelin.web.dto.CustomResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceListService {

    final private PlaceListRepository placeListRepository;

    public ResponseEntity createPlaceList(PlaceList placeList){
        CustomResponseEntity result;

        PlaceList insertPlaceList = placeListRepository.save(placeList);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("id",insertPlaceList.getId());

        result = CustomResponseEntity.builder()
                .status(200)
                .message("플레이 리스트 생성에 성공했습니다.")
                .data(hashMap)
                .build();
        return new ResponseEntity(result, HttpStatus.OK);
    }

    public ResponseEntity searchPlaceListById(int id){
        CustomResponseEntity result;

        int status;
        String message;
        Object date=null;
        HttpStatus httpStatus;

        Optional<PlaceList> placeList = placeListRepository.findById(id);

        if(!placeList.isPresent()){
            status=404;
            message=id+"를 가진 플레이리스트를 찾을 수 없습니다.";

        }

        return null;

    }




}
