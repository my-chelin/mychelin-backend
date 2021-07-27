package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Place;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.PlaceRepository;
import com.a206.mychelin.domain.repository.PlaceReviewRepository;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.web.dto.CustomResponseEntity;
import com.a206.mychelin.web.dto.MyPlaceReviewResponse;
import com.a206.mychelin.web.dto.PlaceReviewAndUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceReviewService {

    private final PlaceReviewRepository placeReviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    private HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    private int status = 404;
    private String message = null;
    private Object data = null;

    public ResponseEntity getPlaceReviewsByUser(String nickName) {
        CustomResponseEntity result;
        Optional<User> user = userRepository.findUserByNickname(nickName);

        if(!user.isPresent()){
            message=nickName+"는 존재하지 않는 유저입니다.";
        }
        else{
            httpStatus=HttpStatus.OK;
            status=200;
            message=nickName+"의 리뷰 목록 조회에 성공했습니다.";
            List<Object[]> items = placeReviewRepository.getPlaceReviewsById(user.get().getId());
            ArrayList<MyPlaceReviewResponse> arr = new ArrayList<>();
            for (Object[] item : items) {
                arr.add(MyPlaceReviewResponse.builder()
                        .review_id((int)item[0])
                        .star_rate((float) item[1])
                        .content((String)item[2])
                        .user_id((String) item[3])
                        .craete_date((Date)item[4])
                        .place_id((int) item[5])
                        .place_name((String)item[6])
                        .place_image((String) item[7])
                        .build());


            }
            data=arr;
        }
        result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();

        return new ResponseEntity(result,httpStatus);


    }

    public ResponseEntity getPlaceAllReviewsByPlaceId(int placeId) {

        CustomResponseEntity result;
        Optional<Place> place = placeRepository.findPlacesById(placeId);

        if(!place.isPresent()){
            message=placeId+"는 존재하지 않는 맛집입니다.";
        }
        else{
            httpStatus=HttpStatus.OK;
            status=200;
            message=placeId+"의 모든 리뷰 목록 조회에 성공했습니다.";

            List<Object[]> items = placeReviewRepository.getPlaceReviewsByPlaceId(placeId);
            ArrayList<PlaceReviewAndUserResponse> arr = new ArrayList<>();
            for (Object[] item : items) {
                arr.add(PlaceReviewAndUserResponse.builder()
                        .review_id((int)item[0])
                        .star_rate((float) item[1])
                        .content((String)item[2])
                        .user_id((String) item[3])
                        .craete_date((Date)item[4])
                        .user_nickname((String) item[5])
                        .user_profile_image((String)item[6])
                        .build());
            }
            data=arr;
        }
        result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();

        return new ResponseEntity(result,httpStatus);

    }
}
