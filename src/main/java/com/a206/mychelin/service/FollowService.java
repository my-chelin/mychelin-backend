package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Follow;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.FollowRepository;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.util.TokenToId;
import com.a206.mychelin.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<CustomResponseEntity> follow(@RequestBody FollowAskRequest followAskRequest, HttpServletRequest request) {
        CustomResponseEntity customResponseEntity;
        String userId = TokenToId.check(request);
        Optional<User> optionalUser = userRepository.findUserByNickname(followAskRequest.getUserNickname()); //허락할 상대 아이디
        if (!optionalUser.isPresent()) {
            customResponseEntity = CustomResponseEntity.builder()
                    .status(400)
                    .message("존재하지 않는 유저입니다.")
                    .build();
            return new ResponseEntity<>(customResponseEntity, HttpStatus.BAD_REQUEST);
        }
        String getFollowingId = optionalUser.get().getId();
        Optional<Follow> findFollow = followRepository.findFollowByUserIdAndFollowingId(userId, getFollowingId);

        if (!findFollow.isPresent()) { // 두 유저 간의 팔로우 내역이 없다면 새로 요청받을 수 있도록 생성.
            Follow followRequest = Follow.builder().userId(userId).followingId(getFollowingId).build();
            followRepository.save(followRequest);
            customResponseEntity = CustomResponseEntity.builder()
                    .status(200)
                    .message("팔로우 신청이 전송됐습니다.")
                    .build();

            /*
            상대방에게 알림 보내는 로직이 필요하다면 여기서 처리할 것.
            */
            return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
        }
        // 팔로우 내역이 있다면 팔로우 요청 취소를 한다.
        followRepository.deleteAllByUserIdAndFollowingIdAndAccept(userId, getFollowingId, false);
        customResponseEntity = CustomResponseEntity.builder()
                .status(200)
                .message("팔로우 요청이 취소되었습니다.")
                .build();
        return new ResponseEntity<>(customResponseEntity, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> acceptFollowing(@RequestBody FollowAcceptRequest followAcceptRequest, HttpServletRequest httpRequest) {
        CustomResponseEntity customResponse;
        String userId = TokenToId.check(httpRequest); //사용자 ID(허락해주는 사람.)
        Optional<User> user = userRepository.findUserByNickname(followAcceptRequest.getUserNickname()); //허락할 상대 아이디
        if (!user.isPresent()) {
            customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message("존재하지 않는 사용자입니다.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
        }
        String getFollowerId = user.get().getId();

        if (userId.equals(getFollowerId)) {
            customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message("잘못된 접근입니다.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
        }
        Optional<Follow> checkFollow = followRepository.findFollowByUserIdAndFollowingId(getFollowerId, userId);

        if (checkFollow.isPresent()) { //팔로우 신청이 존재
            if (!checkFollow.get().isAccept()) {
                Follow follow = checkFollow.get();
                follow.update(getFollowerId, userId);

                customResponse = CustomResponseEntity.builder()
                        .status(200)
                        .message("팔로우가 수락되었습니다.")
                        .data(follow)
                        .build();
                return new ResponseEntity<>(customResponse, HttpStatus.OK);
            }
            customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message("이미 완료된 작업입니다.")
                    .build();

            return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
        }

        // 팔로우 신청이 없는 경우
        customResponse = CustomResponseEntity.builder()
                .status(405)
                .message("팔로우 신청 내역이 없습니다.")
                .build();
        return new ResponseEntity<>(customResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> findFollowingList(String nickname) {
        CustomResponseEntity customResponse;
        List<Object[]> following = followRepository.findFollowingByUserNickname(nickname);
        ArrayList<FollowListResponse> list = new ArrayList<>();
        for (Object[] followItem : following) {
            list.add(FollowListResponse.builder()
                    .profileImage((String) followItem[0])
                    .nickname((String) followItem[1])
                    .bio((String) followItem[2])
                    .build()
            );
        }

        customResponse = CustomResponseEntity.builder()
                .status(200)
                .message(nickname + "의 팔로잉 목록")
                .data(list)
                .build();
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

    public ResponseEntity<CustomResponseEntity> findFollowerList(String nickname) {
        CustomResponseEntity customResponseEntity;
        Optional<User> optionalUser = userRepository.findUserByNickname(nickname);
        if (!optionalUser.isPresent()) {
            customResponseEntity = CustomResponseEntity.builder()
                    .status(400)
                    .message("존재하지 않는 유저입니다.")
                    .build();
            return new ResponseEntity<>(customResponseEntity, HttpStatus.BAD_REQUEST);
        }
        List<Object[]> follower = followRepository.findFollowerByUserNickname(nickname);
        ArrayList<FollowListResponse> list = new ArrayList<>();
        for (Object[] followItem : follower) {
            list.add(FollowListResponse.builder()
                    .profileImage((String) followItem[0])
                    .nickname((String) followItem[1])
                    .bio((String) followItem[2])
                    .build()
            );
        }
        customResponseEntity = CustomResponseEntity.builder()
                .status(200)
                .message(nickname + "의 팔로워 목록")
                .data(list)
                .build();
        return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
    }

    public ResponseEntity<CustomResponseEntity> getFollowRequest(HttpServletRequest request) {
        CustomResponseEntity customResponseEntity;
        User user = userRepository.findUserById(TokenToId.check(request)).get();
        List<String[]> requests = followRepository.findUserIdByUserId(user.getId());
        ArrayList<FollowRequestResponse> list = new ArrayList<>();
        for (String[] item : requests) {
            System.out.println(item[0] + " " + item[1]);
            list.add(FollowRequestResponse.builder()
                    .nickname(item[0])
                    .profileImage(item[1])
                    .build()
            );
        }
        customResponseEntity = CustomResponseEntity.builder()
                .status(200)
                .message(user.getNickname() + "의 팔로워 요청 목록")
                .data(list)
                .build();
        return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
    }

    public ResponseEntity<CustomResponseEntity> unfollow(FollowAskRequest followAskRequest, HttpServletRequest request) {
        System.out.println(followAskRequest);
        System.out.println(followAskRequest.getUserNickname());
        CustomResponseEntity customResponseEntity;
        Optional<User> optionalUser = userRepository.findUserByNickname(followAskRequest.getUserNickname());
        if (!optionalUser.isPresent()) {
            customResponseEntity = CustomResponseEntity.builder()
                    .status(400)
                    .message("존재하지 않는 유저입니다.")
                    .build();
            return new ResponseEntity<>(customResponseEntity, HttpStatus.BAD_REQUEST);
        }
        String userId = optionalUser.get().getId();
        followRepository.deleteAllByUserIdAndFollowingIdAndAccept(TokenToId.check(request), userId, true);
        customResponseEntity = CustomResponseEntity.builder()
                .status(200)
                .message("언팔로우 되었습니다.")
                .build();
        return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
    }
}