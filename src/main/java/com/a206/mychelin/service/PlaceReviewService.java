package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Place;
import com.a206.mychelin.domain.entity.Review;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.PlaceRepository;
import com.a206.mychelin.domain.repository.PlaceReviewRepository;
import com.a206.mychelin.domain.repository.UserRepository;
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

    public ResponseEntity<Response> getPlaceReviewsByUser(String nickName, int page, int pageSize) {
        Optional<User> user = userRepository.findUserByNickname(nickName);
        if (!user.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, nickName + "는 존재하지 않는 유저입니다.", null);
        }
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
        return Response.newResult(HttpStatus.OK, nickName + "의 리뷰 목록 조회에 성공했습니다.", hashMap);
    }

    public ResponseEntity<Response> getPlaceAllReviewsByPlaceId(int placeId, int page, int pageSize) {
        Optional<Place> place = placeRepository.findPlacesById(placeId);
        if (!place.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, placeId + "는 존재하지 않는 맛집입니다.", null);
        }
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
        return Response.newResult(HttpStatus.OK, placeId + "의 모든 리뷰 목록 조회에 성공했습니다.", hashMap);
    }

    public ResponseEntity<Response> addPlaceReviews(String userId, ReviewRequest review) {
        Optional<User> user = userRepository.findUserById(userId);
        if (!user.isPresent()) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }
        Review newReview = Review.builder()
                .starRate(review.getStarRate())
                .content(review.getContent())
                .placeId(review.getPlaceId())
                .userId(userId)
                .image(review.getImage())
                .build();
        placeReviewRepository.save(newReview);
        return Response.newResult(HttpStatus.OK, "리뷰 추가에 성공하였습니다.", null);
    }

    public ResponseEntity<Response> editPlaceReviews(String userId, ReviewEditRequest review) {
        Optional<Review> findReview = placeReviewRepository.findById(review.getId());
        Optional<User> user = userRepository.findUserById(userId);

        if (!findReview.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "리뷰가 존재하지 않습니다.", null);
        }
        if (!user.isPresent()) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 사용해주세요", null);
        }
        if (!findReview.get().getUserId().equals(userId)) {
            return Response.newResult(HttpStatus.FORBIDDEN, "자신의 리뷰만 수정 가능합니다.", null);
        }
        if (findReview.get().getPlaceId() != review.getPlaceId()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "맛집 장소가 일치하지 않습니다.", null);
        }
        findReview.get().editReview(review);
        placeReviewRepository.save(findReview.get());
        return Response.newResult(HttpStatus.OK, "리뷰 수정에 성공했습니다.", null);
    }

    public ResponseEntity<Response> deletePlaceReviews(String userId, ReviewDeleteRequest review) {
        Optional<Review> findReview = placeReviewRepository.findById(review.getId());
        Optional<User> user = userRepository.findUserById(userId);

        if (!findReview.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "리뷰가 존재하지 않습니다.", null);
        }
        if (!user.isPresent()) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 사용해주세요", null);
        }
        if (!findReview.get().getUserId().equals(userId)) {
            return Response.newResult(HttpStatus.FORBIDDEN, "자신의 리뷰만 삭제 가능합니다.", null);
        }
        placeReviewRepository.delete(findReview.get());
        return Response.newResult(HttpStatus.OK, "리뷰 삭제에 성공했습니다.", null);
    }

    public ResponseEntity<Response> saveReviewImage(ImageRequest imageRequest, String userId, int reviewId) {
        Optional<Review> findReview = placeReviewRepository.findById(reviewId);
        Optional<User> user = userRepository.findUserById(userId);

        if (!findReview.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "리뷰가 존재하지 않습니다.", null);
        }
        if (!user.isPresent()) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 사용해주세요", null);
        }
        if (!findReview.get().getUserId().equals(userId)) {
            return Response.newResult(HttpStatus.FORBIDDEN, "자신의 리뷰에만 이미지 추가가 가능합니다.", null);
        }
        Review newReview = findReview.get();
        newReview.reviewImageUpdate(imageRequest.getImage());
        placeReviewRepository.save(newReview);
        return Response.newResult(HttpStatus.OK, "리뷰 이미지 추가에 성공하였습니다.", null);
    }
}