package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Place;
import com.a206.mychelin.domain.entity.Review;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.PlaceRepository;
import com.a206.mychelin.domain.repository.PlaceReviewRepository;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.util.ImageServer;
import com.a206.mychelin.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PlaceReviewService {
    private final PlaceReviewRepository placeReviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private int status = 400;
    private String message = null;
    private Object data = null;

    // 이미지 저장을 위해 선언
    private final ImageServer s3Service;

    private void init() {
        httpStatus = HttpStatus.BAD_REQUEST;
        status = 400;
        message = null;
        data = null;
    }

    public ResponseEntity<CustomResponseEntity> getPlaceReviewsByUser(String nickName, int page, int pageSize) {
        init();
        CustomResponseEntity result;
        Optional<User> user = userRepository.findUserByNickname(nickName);

        if (!user.isPresent()) {
            message = nickName + "는 존재하지 않는 유저입니다.";
        } else {
            httpStatus = HttpStatus.OK;
            status = 200;
            message = nickName + "의 리뷰 목록 조회에 성공했습니다.";
            PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

            int totalPageItemCnt = placeReviewRepository.getPlaceReviewsNumById(user.get().getId());

            HashMap<String, Object> hashMap = new LinkedHashMap<>();
            hashMap.put("totalPageItemCnt", totalPageItemCnt);
            hashMap.put("totalPage", ((totalPageItemCnt - 1) / pageSize) + 1);
            hashMap.put("nowPage", page);
            hashMap.put("nowPageSize", pageSize);

            List<Object[]> items = placeReviewRepository.getPlaceReviewsById(user.get().getId(), pageRequest);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

            ArrayList<MyPlaceReviewResponse> arr = new ArrayList<>();
            for (Object[] item : items) {
                String format = formatter.format(item[4]);
                arr.add(MyPlaceReviewResponse.builder()
                        .reviewId((int) item[0])
                        .starRate((float) item[1])
                        .content((String) item[2])
                        .userId((String) item[3])
                        .craeteDate(format)
                        .reviewImage((String) item[5])
                        .placeId((int) item[6])
                        .placeName((String) item[7])
                        .placeImage((String) item[8])
                        .build());
            }
            hashMap.put("reviews", arr);
            data = hashMap;
        }
        result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();

        return new ResponseEntity<>(result, httpStatus);
    }

    public ResponseEntity<CustomResponseEntity> getPlaceAllReviewsByPlaceId(int placeId, int page, int pageSize) {
        init();
        CustomResponseEntity result;
        Optional<Place> place = placeRepository.findPlacesById(placeId);

        if (!place.isPresent()) {
            message = placeId + "는 존재하지 않는 맛집입니다.";
        } else {
            httpStatus = HttpStatus.OK;
            status = 200;
            message = placeId + "의 모든 리뷰 목록 조회에 성공했습니다.";

            int totalPageItemCnt = placeReviewRepository.getPlaceReviewsNumByPlaceId(placeId);

            HashMap<String, Object> hashMap = new LinkedHashMap<>();
            hashMap.put("totalPageItemCnt", totalPageItemCnt);
            hashMap.put("totalPage", ((totalPageItemCnt - 1) / pageSize) + 1);
            hashMap.put("nowPage", page);
            hashMap.put("nowPageSize", pageSize);

            Optional<Float> totalStarRate = placeReviewRepository.getPlaceReviewsAVGByPlaceId(placeId);
            if (totalStarRate.isPresent()) {
                hashMap.put("totalStarRate", totalStarRate.get());
            } else {
                hashMap.put("totalStarRate", null);
            }

            PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
            List<Object[]> items = placeReviewRepository.getPlaceReviewsByPlaceId(placeId, pageRequest);
            ArrayList<PlaceReviewAndUserResponse> arr = new ArrayList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

            for (Object[] item : items) {
                String format = formatter.format(item[4]);
                arr.add(PlaceReviewAndUserResponse.builder()
                        .reviewId((int) item[0])
                        .starRate((float) item[1])
                        .content((String) item[2])
                        .userId((String) item[3])
                        .craeteDate(format)
                        .reviewImage((String) item[5])
                        .userNickname((String) item[6])
                        .userProfileImage((String) item[7])
                        .build());
            }
            hashMap.put("reviews", arr);
            data = hashMap;
        }
        result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
        return new ResponseEntity<>(result, httpStatus);
    }

    public ResponseEntity<CustomResponseEntity> addPlaceReviews(String userId, ReviewRequest review) {
        init();
        CustomResponseEntity result;
        Optional<User> user = userRepository.findUserById(userId);
        if (!user.isPresent()) {
            message = "해당 유저가 존재하지 않습니다.";
        } else {
            Review newReview = Review.builder()
                    .starRate(review.getStarRate())
                    .content(review.getContent())
                    .placeId(review.getPlaceId())
                    .userId(userId)
                    .image(review.getImage())
                    .build();
            placeReviewRepository.save(newReview);
            status = 200;
            message = "리뷰 추가에 성공하였습니다.";
            httpStatus = HttpStatus.OK;
        }

        result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
        return new ResponseEntity<>(result, httpStatus);
    }

    public ResponseEntity<CustomResponseEntity> editPlaceReviews(String userId, ReviewEditRequest review) {
        init();
        CustomResponseEntity result;
        Optional<Review> findReview = placeReviewRepository.findById(review.getId());
        Optional<User> user = userRepository.findUserById(userId);

        if (!findReview.isPresent()) {
            message = "해당 리뷰가 존재하지 않습니다.";
        } else if (!user.isPresent()) {
            message = "해당 유저가 존재하지 않습니다.";
        } else if (!findReview.get().getUserId().equals(userId)) {
            message = "리뷰 작성한 사람과 수정하려는 사람이 일치하지 않습니다.";
        } else if (findReview.get().getPlaceId() != review.getPlaceId()) {
            message = "맛집 장소가 일치하지 않습니다.";
        } else {
            findReview.get().editReview(review);
            placeReviewRepository.save(findReview.get());
            status = 200;
            message = "리뷰 수정에 성공하였습니다.";
            httpStatus = HttpStatus.OK;
        }

        result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
        return new ResponseEntity<>(result, httpStatus);
    }

    public ResponseEntity<CustomResponseEntity> deletePlaceReviews(String userId, ReviewDeleteRequest review) {
        init();
        CustomResponseEntity result;
        Optional<Review> findReview = placeReviewRepository.findById(review.getId());
        Optional<User> user = userRepository.findUserById(userId);

        if (!findReview.isPresent()) {
            message = "해당 리뷰가 존재하지 않습니다.";
        } else if (!user.isPresent()) {
            message = "해당 유저가 존재하지 않습니다.";
        } else if (!findReview.get().getUserId().equals(userId)) {
            message = "리뷰 작성한 사람과 삭제하려는 사람이 일치하지 않습니다.";
        } else {
            placeReviewRepository.delete(findReview.get());
            status = 200;
            message = "리뷰 삭제에 성공하였습니다.";
            httpStatus = HttpStatus.OK;
        }

        result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
        return new ResponseEntity<>(result, httpStatus);
    }

    public ResponseEntity<CustomResponseEntity> saveReviewImage(ImageRequest imageRequest, String userId, int reviewId) {
        init();
        CustomResponseEntity result;
        Optional<Review> findReview = placeReviewRepository.findById(reviewId);
        Optional<User> user = userRepository.findUserById(userId);

        if (!findReview.isPresent()) {
            message = "해당 리뷰가 존재하지 않습니다.";
        } else if (!user.isPresent()) {
            message = "해당 유저가 존재하지 않습니다.";
        } else if (!findReview.get().getUserId().equals(userId)) {
            message = "리뷰 작성한 사람과 이미지 추가하려는 사람이 일치하지 않습니다.";
        } else {
            Review newReview = findReview.get();
            newReview.reviewImageUpdate(imageRequest.getImage());
            placeReviewRepository.save(newReview);
            status = 200;
            message = "리뷰 이미지 추가에 성공하였습니다.";
            httpStatus = HttpStatus.OK;
        }

        result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
        return new ResponseEntity<>(result, httpStatus);
    }
}