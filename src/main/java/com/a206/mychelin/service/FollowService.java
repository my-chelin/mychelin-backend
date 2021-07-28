package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Follow;
import com.a206.mychelin.domain.entity.FollowPK;
import com.a206.mychelin.domain.repository.FollowRepository;
import com.a206.mychelin.util.TokenToId;
import com.a206.mychelin.web.dto.CustomResponseEntity;
import com.a206.mychelin.web.dto.FollowAcceptRequest;
import com.a206.mychelin.web.dto.FollowAskRequest;
import com.a206.mychelin.web.dto.FollowingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowService {
    private final FollowRepository followRepository;

    //추가하려는 상대방 아이디는 requestbody로 넘기고 나는 httpRequest에서 아이디를 받아온다.
    @Transactional
    public ResponseEntity addFollowingUser(FollowAskRequest followRequest, HttpServletRequest httpRequest) {
        CustomResponseEntity customResponse;
        String userId = TokenToId.check(httpRequest);

        Optional<Follow> findFollow = followRepository.findFollowByUserIdAndFollowingId(followRequest.getUserId()
                , followRequest.getFollowingId());
        System.out.println(findFollow.toString());
        if (!findFollow.isPresent()) { // 두 유저 간의 팔로우 정보가 없다면 새로 요청받을 수 있도록 생성.
            followRepository.save(followRequest.toEntity());

            customResponse = CustomResponseEntity.builder()
                    .status(200)
                    .message("팔로우 신청이 전송됐습니다.")
                    .build();

            /*
            사용자에게 알림 보내는 로직이 필요하다면 여기서 처리할 것.
            */
            return new ResponseEntity(customResponse, HttpStatus.OK);
        } else { // 기존에 요청이 존재함.
            customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message("이미 팔로우 신청이 되었습니다.")
                    .build();

            return new ResponseEntity(customResponse, HttpStatus.BAD_REQUEST);
        }
    }

    //수락한 상대방의 아이디는 requestbody로 넘기고 내 아이디는 httpRequest에서 받아온다.
    @Transactional
    public ResponseEntity acceptFollowing(FollowAcceptRequest followAcceptRequest, HttpServletRequest httpRequest) {
        CustomResponseEntity customResponse;
        String userId = TokenToId.check(httpRequest);

        if (followAcceptRequest.getUserId().equals(userId)) { //현재 사용자와 로그인한 사용자가 같은경우
            Optional<Follow> checkFollow = followRepository.findFollowByUserIdAndFollowingId(followAcceptRequest.getUserId(), followAcceptRequest.getFollowingId());

            if (checkFollow.isPresent()) { //팔로우 신청이 존재
                if (!checkFollow.get().isAccept()) {
                    Follow follow = checkFollow.get();
                    follow.update(userId, followAcceptRequest.getFollowingId());

                    customResponse = CustomResponseEntity.builder()
                            .status(200)
                            .message("팔로우가 수락되었습니다.")
                            .data(follow)
                            .build();
                    return new ResponseEntity(customResponse, HttpStatus.OK);
                }
                customResponse = CustomResponseEntity.builder()
                        .status(400)
                        .message("이미 완료된 작업입니다.")
                        .build();

                return new ResponseEntity(customResponse, HttpStatus.BAD_REQUEST);
            }

            // 팔로우 신청이 없는 경우
            customResponse = CustomResponseEntity.builder()
                    .status(405)
                    .message("팔로우 신청 내역이 없습니다.")
                    .build();
            return new ResponseEntity(customResponse, HttpStatus.NOT_ACCEPTABLE);

        }

        customResponse = CustomResponseEntity.builder()
                .status(400)
                .message("접근 권한이 없습니다.")
                .build();
        return new ResponseEntity(customResponse, HttpStatus.UNAUTHORIZED);
    }

    @Transactional
    public ResponseEntity findFollowingList(String userNickname) {
        CustomResponseEntity customResponse;
//        String userId = TokenToId.check(httpServletRequest);

        List<Object[]> followInfo = followRepository.findFollowsByUserNickname(userNickname);
        ArrayList<FollowingResponse> array = new ArrayList<>();
        for (Object[] followItem : followInfo) {
            array.add(FollowingResponse.builder()
                    .userProfileImage((String) followItem[0])
                    .userNickname((String) followItem[1])
                    .followingId((String) followItem[2])
                    .userBio((String) followItem[3])
                    .build()
            );
        }

        customResponse = CustomResponseEntity.builder()
                .status(200)
                .message("팔로잉 목록을 불러왔습니다.")
                .data(array)
                .build();
        return new ResponseEntity(customResponse, HttpStatus.OK);
    }

    //언팔하기 - 유저가 더 이상 followingId를 팔로우하지 않는다. => userId, followingId, false로 바꾸기

    //
}
