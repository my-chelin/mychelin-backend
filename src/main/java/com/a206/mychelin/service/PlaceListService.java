package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Place;
import com.a206.mychelin.domain.entity.PlaceList;
import com.a206.mychelin.domain.entity.PlaceListItem;
import com.a206.mychelin.domain.entity.PlaceListItemPK;
import com.a206.mychelin.domain.repository.PlaceListItemRepository;
import com.a206.mychelin.domain.repository.PlaceListRepository;
import com.a206.mychelin.domain.repository.PlaceRepository;
import com.a206.mychelin.web.dto.CustomResponseEntity;
import com.a206.mychelin.web.dto.PlaceListItemDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceListService {
    final private PlaceListRepository placeListRepository;
    final private PlaceListItemRepository placeListItemRepository;
    final private PlaceRepository placeRepository;

    private HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    private int status = 0;
    private String message = null;
    private Object data = null;

    public ResponseEntity createPlaceList(PlaceList placeList) {
        CustomResponseEntity result;
        placeList.clear();
        PlaceList insertPlaceList = placeListRepository.save(placeList);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", insertPlaceList.getId());

        result = CustomResponseEntity.builder()
                .status(200)
                .message("플레이 리스트 생성에 성공했습니다.")
                .data(hashMap)
                .build();
        return new ResponseEntity(result, HttpStatus.OK);
    }

    public ResponseEntity searchPlaceListById(int id) {
        CustomResponseEntity result;
        HttpStatus httpStatus;
        Optional<PlaceList> placeList = placeListRepository.findById(id);

        if (!placeList.isPresent()) {
            result = CustomResponseEntity.builder()
                    .status(404)
                    .message("플레이리스트 번호 " + id + " 검색 실패.")
                    .data(null)
                    .build();
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            result = CustomResponseEntity.builder()
                    .status(200)
                    .message("플레이리스트 번호 " + id + " 검색 성공.")
                    .data(placeList.get())
                    .build();
            httpStatus = HttpStatus.OK;
        }
        return new ResponseEntity(result, httpStatus);
    }

    public ResponseEntity searchPlaceListByTitle(String title) {
        CustomResponseEntity result;
        HttpStatus httpStatus;
        List<PlaceList> placeListList = placeListRepository.findByTitleContains(title);
        result = CustomResponseEntity.builder()
                .status(200)
                .message("맛집 리스트 제목 " + title + " 검색 성공.")
                .data(placeListList)
                .build();
        httpStatus = HttpStatus.OK;
        return new ResponseEntity(result, httpStatus);
    }

    public ResponseEntity getPlaceListItemByTitle(int listId) {
        CustomResponseEntity result;
        Optional<PlaceList> placeList = placeListRepository.findById(listId);
        if (!placeList.isPresent()) {
            result = CustomResponseEntity.builder()
                    .status(404)
                    .message("맛집 리스트 " + listId + "는 존재하지 않습니다.")
                    .data(null)
                    .build();
            return new ResponseEntity(result, HttpStatus.NOT_FOUND);
        }

        List<Object[]> items = placeListRepository.getPlaceListItemsById(listId);
        System.out.println(items.size());
        ArrayList<PlaceListItemDetail> arr = new ArrayList<>();
        for (Object[] item : items) {
            arr.add(PlaceListItemDetail.builder()
                    .placelistId((int) item[0])
                    .placeId((int) item[1])
                    .contributorId((String) item[2])
                    .name((String) item[3])
                    .description((String) item[4])
                    .latitude((float) item[5])
                    .longitude((float) item[6])
                    .phone((String) item[7])
                    .location((String) item[8])
                    .opertaionHours((String) item[9])
                    .category_id((int) item[10])
                    .image((String) item[11])
                    .build()
            );
        }
        result = CustomResponseEntity.builder()
                .status(200)
                .message(listId + "의 맛집 정보를 가져오는데 성공했습니다.")
                .data(arr)
                .build();
        return new ResponseEntity(result, HttpStatus.OK);
    }

    public boolean checkPlaceListId(int listId) {
        Optional<PlaceList> placeList = placeListRepository.findById(listId);
        if (!placeList.isPresent()) {
            status = 404;
            message = "맛집 리스트 " + listId + "가 존재하지 않습니다.";
            return false;
            // 리스트가 존재하지 않음.
        }
        return true;
    }

    public boolean checkPlaceId(int placeId) {
        Optional<Place> place = placeRepository.findPlacesById(placeId);

        if (!place.isPresent()) {
            status = 404;
            message = "식당 " + placeId + "가 존재하지 않습니다.";
            return false;
        }
        return true;
    }

    public boolean checkPlaceIntoPlaceList(int listId, int placeId) {
        PlaceListItemPK placeListItemPK = PlaceListItemPK.builder().placeId(placeId).placelistId(listId).build();
        Optional<PlaceListItem> placeListItem = placeListItemRepository.findByPlaceListItemPK(placeListItemPK);

        if (placeListItem.isPresent()) {
            status = 404;
            message = "이미 리스트에 존재하는 식당입니다.";
            // 이미 리스트에 해당 맛집이 존재.
            return true;
        }
        status = 404;
        message = "리스트에 존재하지 않는 식당입니다.";
        return false;
    }

    public ResponseEntity insertPlaceListItem(String userId, int listId, int placeId) {
        if (checkPlaceListId(listId) && checkPlaceId(placeId) && !checkPlaceIntoPlaceList(listId, placeId)) {
            PlaceListItemPK placeListItemPK = PlaceListItemPK.builder().placeId(placeId).placelistId(listId).build();
            // 식당에 추가 가능
            PlaceListItem InsertPlaceListItem = PlaceListItem.builder()
                    .placeListItemPK(placeListItemPK)
                    .contributorId(userId)
                    .build();
            placeListItemRepository.save(InsertPlaceListItem);
            data = InsertPlaceListItem;
            status = 200;
            message = "맛집 리스트 " + listId + "에 맛집 " + placeId + " 추가 완료했습니다.";
            httpStatus = HttpStatus.OK;
        }
        CustomResponseEntity result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
        return new ResponseEntity(result, httpStatus);
    }

    public ResponseEntity deletePlaceListItem(String userId, int listId, int placeId) {
        if (checkPlaceListId(listId) && checkPlaceId(placeId) && checkPlaceIntoPlaceList(listId, placeId)) {
            PlaceListItemPK placeListItemPK = PlaceListItemPK.builder().placeId(placeId).placelistId(listId).build();

            Optional<PlaceListItem> placeListItem = placeListItemRepository.findByPlaceListItemPKAndContributorId(placeListItemPK, userId);
            if (!placeListItem.isPresent()) {
                message = "해당 계정으로는 삭제할 수 없습니다.(생성한 계정만 삭제할 수 있습니다.)";
            } else {
                PlaceListItem InsertPlaceListItem = PlaceListItem.builder()
                        .placeListItemPK(placeListItemPK)
                        .contributorId(userId)
                        .build();
                placeListItemRepository.delete(InsertPlaceListItem);
                status = 200;
                message = "맛집 리스트 " + listId + "에서 맛집 " + placeId + " 삭제 완료했습니다.";
                httpStatus = HttpStatus.OK;
            }
        }

        CustomResponseEntity result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
        return new ResponseEntity(result, httpStatus);
    }
}