package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.*;
import com.a206.mychelin.domain.repository.*;
import com.a206.mychelin.util.TokenToId;
import com.a206.mychelin.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PlaceListService {
    final private PlaceListRepository placeListRepository;
    final private PlaceListItemRepository placeListItemRepository;
    final private PlaceRepository placeRepository;
    final private UserRepository userRepository;
    final private ReviewRepository reviewRepository;

    static String message;

    public ResponseEntity<Response> createPlaceList(PlaceListDto.PlaceListCreateRequest placeList, HttpServletRequest request) {
        String userId = TokenToId.check(request);
        User user = userRepository.findUserById(userId).get();
        PlaceList newPlaceList = PlaceList.builder()
                .title(placeList.getTitle())
                .userId(user.getId())
                .build();
        PlaceList insertPlaceList = placeListRepository.save(newPlaceList);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", insertPlaceList.getId());
        return Response.newResult(HttpStatus.OK, "맛집 리스트 생성에 성공했습니다.", hashMap);
    }

    public ResponseEntity<Response> searchPlaceListById(int id) {
        Optional<PlaceList> placeList = placeListRepository.findById(id);
        if (!placeList.isPresent()) {
            return Response.newResult(HttpStatus.OK, id + "번 맛집 리스트가 없습니다.", null);
        }
        return Response.newResult(HttpStatus.OK, id + "번 맛집 리스트 검색 성공.", placeList.get());
    }

    public ResponseEntity<Response> searchPlaceListByTitle(String title, int page, int pageSize) {
        long totalPageItemCnt = placeListRepository.countByTitleContains(title);

        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalPageItemCnt);
        linkedHashMap.put("totalPage", ((totalPageItemCnt - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        List<Object[]> placeListList = placeListRepository.getPlaceListTitleContainsOrderById("%" + title + "%", pageRequest);
        if (placeListList.size() == 0) {
            return Response.newResult(HttpStatus.OK, title + "로 검색한 결과가 없습니다", null);
        }
        List<PlaceListDto.PlaceListByTitle> resultList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

        for (Object[] item : placeListList) {
            List<String> contributorsProfile = userRepository.findContributedUserProfilesByPlaceListId((int) item[0]);
            int contributorCnt = placeListItemRepository.countContributorByPlaceId((int) item[0]);

            String format = formatter.format(item[2]);
            resultList.add(PlaceListDto.PlaceListByTitle.builder()
                    .id((int) item[0])
                    .title((String) item[1])
                    .createDate(format)
                    .userId((String) item[3])
                    .nickname((String) item[4])
                    .totalItemCnt((BigInteger) item[5])
                    .contributorProfiles(contributorsProfile)
                    .contributorCnt(contributorCnt)
                    .build());
        }
        linkedHashMap.put("placeList", resultList);
        return Response.newResult(HttpStatus.OK, "맛집 리스트 제목 " + title + " 검색 성공", linkedHashMap);
    }

    public ResponseEntity<Response> getPlaceListItemByTitle(int listId, int page, int pageSize) {
        Optional<PlaceList> placeList = placeListRepository.findById(listId);
        if (!placeList.isPresent()) {
            return Response.newResult(HttpStatus.OK, listId + "번 맛집 리스트가 없습니다.", null);
        }
        long totalPageItemCnt = placeListRepository.getPlaceListItemsNumById(listId);
        String placeTitle = placeList.get().getTitle();

        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalPageItemCnt);
        linkedHashMap.put("totalPage", ((totalPageItemCnt - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);
        linkedHashMap.put("placeListTitle", placeTitle);

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        List<Object[]> items = placeListRepository.getPlaceListItemsById(listId, pageRequest);

        ArrayList<PlaceListItemDto.PlaceListItemDetail> arr = new ArrayList<>();
        for (Object[] item : items) {
            Optional<Double> starRateOptional = placeRepository.getStartRateById(String.valueOf((int) item[1]));
            Double starRate = null;
            if (starRateOptional.isPresent()) {
                starRate = starRateOptional.get();
            }
            int reviewCnt = reviewRepository.countAllByPlaceId((int) item[1]);

            arr.add(PlaceListItemDto.PlaceListItemDetail.builder()
                    .placeListId((int) item[0])
                    .placeId((int) item[1])
                    .contributorId((String) item[2])
                    .name((String) item[3])
                    .description((String) item[4])
                    .latitude((double) item[5])
                    .longitude((double) item[6])
                    .phone((String) item[7])
                    .location((String) item[8])
                    .opertaionHours((String) item[9])
                    .categoryId((int) item[10])
                    .image((String) item[11])
                    .starRate(starRate)
                    .reviewCnt(reviewCnt)
                    .build()
            );
        }
        linkedHashMap.put("placeListItem", arr);
        return Response.newResult(HttpStatus.OK, listId + "번 맛집 리스트의 맛집을 가져오는데 성공했습니다.", linkedHashMap);
    }

    public boolean checkPlaceListId(int listId) {
        Optional<PlaceList> placeList = placeListRepository.findById(listId);
        if (!placeList.isPresent()) {
            message = listId + "번 맛집 리스트가 존재하지 않습니다.";
            return false;
        }
        return true;
    }

    public boolean checkPlaceId(int placeId) {
        Optional<Place> place = placeRepository.findPlacesById(placeId);
        if (!place.isPresent()) {
            message = placeId + "번 식당이 존재하지 않습니다.";
            return false;
        }
        return true;
    }

    public boolean checkPlaceIntoPlaceList(int listId, int placeId) {
        PlaceListItemPK placeListItemPK = PlaceListItemPK.builder().placeId(placeId).placeListId(listId).build();
        Optional<PlaceListItem> placeListItem = placeListItemRepository.findByPlaceListItemPK(placeListItemPK);
        if (placeListItem.isPresent()) {
            message = "이미 리스트에 존재하는 식당입니다.";
            return true;
        }
        return false;
    }

    public ResponseEntity<Response> insertPlaceListItem(String userId, int listId, int placeId) {
        if (checkPlaceListId(listId) && checkPlaceId(placeId) && !checkPlaceIntoPlaceList(listId, placeId)) {
            // 식당에 추가 가능
            PlaceListItemPK placeListItemPK = PlaceListItemPK.builder().placeId(placeId).placeListId(listId).build();
            PlaceListItem InsertPlaceListItem = PlaceListItem.builder()
                    .placeListItemPK(placeListItemPK)
                    .contributorId(userId)
                    .build();
            placeListItemRepository.save(InsertPlaceListItem);
            return Response.newResult(HttpStatus.OK, "맛집 리스트 " + listId + "에 맛집 " + placeId + " 추가 완료했습니다.", InsertPlaceListItem);
        }
        return Response.newResult(HttpStatus.BAD_REQUEST, message, null);
    }

    public ResponseEntity<Response> deletePlaceListItem(String userId, int listId, int placeId) {
        if (checkPlaceListId(listId) && checkPlaceId(placeId) && checkPlaceIntoPlaceList(listId, placeId)) {
            PlaceListItemPK placeListItemPK = PlaceListItemPK.builder().placeId(placeId).placeListId(listId).build();

            Optional<PlaceListItem> placeListItem = placeListItemRepository.findByPlaceListItemPKAndContributorId(placeListItemPK, userId);
            if (!placeListItem.isPresent()) {
                return Response.newResult(HttpStatus.BAD_REQUEST, "해당 계정으로 삭제할 수 없습니다.(생성한 계정만 삭제할 수 있습니다.)", null);
            }
            PlaceListItem InsertPlaceListItem = PlaceListItem.builder()
                    .placeListItemPK(placeListItemPK)
                    .contributorId(userId)
                    .build();
            placeListItemRepository.delete(InsertPlaceListItem);
            return Response.newResult(HttpStatus.OK, listId + "번 맛집 리스트에서 " + placeId + "번 식당 삭제 완료했습니다.", null);
        }
        return Response.newResult(HttpStatus.BAD_REQUEST, message, null);
    }

    //  닉네임으로 가져오기기
    public ResponseEntity<Response> getPlaceListItemByNickname(String nickname, int page, int pageSize) {
        Optional<User> user = userRepository.findUserByNickname(nickname);
        if (!user.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.", null);
        }
        long totalPageItemCnt = placeListItemRepository.getCountByContributorId(user.get().getId()) + placeListRepository.countPlaceListsByUserId(user.get().getId());

        HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("totalPageItemCnt", totalPageItemCnt);
        linkedHashMap.put("totalPage", ((totalPageItemCnt - 1) / pageSize) + 1);
        linkedHashMap.put("nowPage", page);
        linkedHashMap.put("nowPageSize", pageSize);

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        List<Object[]> list = placeListItemRepository.getMyPlacelistByContributorIdOrderByPlaceId(user.get().getId(), pageRequest);

        List<PlaceListItemDto.PlaceListITemsByNicknameResponse> placeListItemsByNicknameResponses = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

        for (Object[] item : list) {
            String format = formatter.format(item[5]);
            placeListItemsByNicknameResponses.add(PlaceListItemDto.PlaceListITemsByNicknameResponse.builder()
                    .placeListId((int) item[0])
                    .title((String) item[1])
                    .nickname((String) item[2])
                    .contrubuteItemCnt((BigInteger) item[3])
                    .totalItemCnt((BigInteger) item[4])
                    .createDate(format)
                    .build()
            );
        }
        linkedHashMap.put("placeListItem", placeListItemsByNicknameResponses);
        return Response.newResult(HttpStatus.OK, nickname + "의 맛집 정보를 가져오는데 성공했습니다.", linkedHashMap);
    }
}