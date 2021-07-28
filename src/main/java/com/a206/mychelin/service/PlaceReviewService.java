package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Place;
import com.a206.mychelin.domain.entity.Review;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.PlaceRepository;
import com.a206.mychelin.domain.repository.PlaceReviewRepository;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

    private void init(){
        httpStatus = HttpStatus.NOT_FOUND;
        status = 404;
        message = null;
        data = null;
    }

    public ResponseEntity getPlaceReviewsByUser(String nickName) {
        init();
        CustomResponseEntity result;
        Optional<User> user = userRepository.findUserByNickname(nickName);

        if (!user.isPresent()) {
            message = nickName + "는 존재하지 않는 유저입니다.";
        } else {
            httpStatus = HttpStatus.OK;
            status = 200;
            message = nickName + "의 리뷰 목록 조회에 성공했습니다.";
            List<Object[]> items = placeReviewRepository.getPlaceReviewsById(user.get().getId());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

            ArrayList<MyPlaceReviewResponse> arr = new ArrayList<>();
            for (Object[] item : items) {
                String format = formatter.format(item[4]);
                arr.add(MyPlaceReviewResponse.builder()
                        .review_id((int) item[0])
                        .star_rate((float) item[1])
                        .content((String) item[2])
                        .user_id((String) item[3])
                        .craete_date(format)
                        .place_image((String) item[5])
                        .place_id((int) item[6])
                        .place_name((String) item[7])
                        .place_image((String) item[8])
                        .build());
            }
            data = arr;
        }
        result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();

        return new ResponseEntity(result, httpStatus);
    }

    public ResponseEntity getPlaceAllReviewsByPlaceId(int placeId) {
        init();

        CustomResponseEntity result;
        Optional<Place> place = placeRepository.findPlacesById(placeId);

        if (!place.isPresent()) {
            message = placeId + "는 존재하지 않는 맛집입니다.";
        } else {
            httpStatus = HttpStatus.OK;
            status = 200;
            message = placeId + "의 모든 리뷰 목록 조회에 성공했습니다.";

            List<Object[]> items = placeReviewRepository.getPlaceReviewsByPlaceId(placeId);
            ArrayList<PlaceReviewAndUserResponse> arr = new ArrayList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

            for (Object[] item : items) {
                String format = formatter.format(item[4]);
                arr.add(PlaceReviewAndUserResponse.builder()
                        .review_id((int) item[0])
                        .star_rate((float) item[1])
                        .content((String) item[2])
                        .user_id((String) item[3])
                        .craete_date(format)
                        .review_image((String) item[5])
                        .user_nickname((String) item[6])
                        .user_profile_image((String) item[7])
                        .build());
            }
            data = arr;
        }
        result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();

        return new ResponseEntity(result, httpStatus);
    }

    public ResponseEntity addPlaceReviews(String userId, ReviewRequest review) {
        init();

        CustomResponseEntity result;
        Optional<User> user = userRepository.findUserById(userId);
        if(!user.isPresent()){
            message="해당 유저가 존재하지 않습니다.";
        }
        else{
            Review newReview = Review.builder()
                    .star_rate(review.getStar_rate())
                    .content(review.getContent())
                    .placeId(review.getPlace_id())
                    .userId(userId)
                    .image(review.getImage())
                    .build();

            status=200;
            message="리뷰 추가에 성공하였습니다.";
            httpStatus=HttpStatus.OK;
        }

        result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();

        return new ResponseEntity(result,httpStatus);
    }

    public ResponseEntity editPlaceReviews(String userId, ReviewEditRequest review) {
        init();

        CustomResponseEntity result;
        Optional<Review> findReview = placeReviewRepository.findById(review.getId());
        Optional<User> user = userRepository.findUserById(userId);

        if(!findReview.isPresent()){
            message="해당 리뷰가 존재하지 않습니다.";
        }
        else if(!user.isPresent()){
            message="해당 유저가 존재하지 않습니다.";
        }
        else if(!findReview.get().getUserId().equals(userId)){
            message="리뷰 작성한 사람과 수정하려는 사람이 일치하지 않습니다.";
        }
        else if(findReview.get().getPlaceId()!= review.getPlace_id()){
            message="맛집 장소가 일치하지 않습니다.";
        }
        else{
            findReview.get().editReview(review);
            placeReviewRepository.save(findReview.get());

            status=200;
            message="리뷰 수정에 성공하였습니다.";
            httpStatus=HttpStatus.OK;
        }

        result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();

        return new ResponseEntity(result,httpStatus);
    }

    public ResponseEntity deletePlaceReviews(String userId, ReviewDeleteRequest review) {

        init();

        CustomResponseEntity result;
        Optional<Review> findReview = placeReviewRepository.findById(review.getId());
        Optional<User> user = userRepository.findUserById(userId);

        if(!findReview.isPresent()){
            message="해당 리뷰가 존재하지 않습니다.";
        }
        else if(!user.isPresent()){
            message="해당 유저가 존재하지 않습니다.";
        }
        else if(!findReview.get().getUserId().equals(userId)){
            message="리뷰 작성한 사람과 삭제하려는 사람이 일치하지 않습니다.";
        }
        else{
            placeReviewRepository.delete(findReview.get());

            status=200;
            message="리뷰 삭제에 성공하였습니다.";
            httpStatus=HttpStatus.OK;
        }

        result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();

        return new ResponseEntity(result,httpStatus);
    }
}