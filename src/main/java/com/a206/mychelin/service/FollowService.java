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
    public ResponseEntity<Response> follow(@RequestBody FollowAskRequest followAskRequest, HttpServletRequest request) {
        CustomResponseEntity customResponseEntity;
        String userId = TokenToId.check(request);
        Optional<User> optionalUser = userRepository.findUserByNickname(followAskRequest.getUserNickname()); //허락할 상대 아이디
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다." , null);
        }
        if (optionalUser.get().isWithdraw()) {
            return Response.newResult(HttpStatus.NOT_FOUND, "탈퇴한 유저입니다.", null);
        }
        String getFollowingId = optionalUser.get().getId();
        Optional<Follow> findFollow = followRepository.findFollowByUserIdAndFollowingId(userId, getFollowingId);

        if (!findFollow.isPresent()) { // 두 유저 간의 팔로우 내역이 없다면 새로 요청받을 수 있도록 생성.
            Follow followRequest = Follow.builder().userId(userId).followingId(getFollowingId).build();
            followRepository.save(followRequest);

            /*
            상대방에게 알림 보내는 로직이 필요하다면 여기서 처리할 것.
            */
            return Response.newResult(HttpStatus.OK, "팔로우 신청이 전송되었습니다.", null);
        }
        // 팔로우 내역이 있다면 팔로우 요청 취소를 한다.
        followRepository.deleteAllByUserIdAndFollowingIdAndAccept(userId, getFollowingId, false);
        return Response.newResult(HttpStatus.BAD_REQUEST, "팔로우 요청이 취소 되었습니다.", null);
    }

    @Transactional
    public ResponseEntity<Response> acceptFollowing(@RequestBody FollowAcceptRequest followAcceptRequest, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest); //사용자 ID(허락해주는 사람.)
        Optional<User> user = userRepository.findUserByNickname(followAcceptRequest.getUserNickname()); //허락할 상대 아이디
        if (!user.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다.", null);
        }
        String getFollowerId = user.get().getId();

        if (userId.equals(getFollowerId)) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "잘못된 접근입니다.", null);
        }
        Optional<Follow> checkFollow = followRepository.findFollowByUserIdAndFollowingId(getFollowerId, userId);

        if (checkFollow.isPresent()) { //팔로우 신청이 존재
            if (!checkFollow.get().isAccept()) {
                Follow follow = checkFollow.get();
                follow.update(getFollowerId, userId);

                return Response.newResult(HttpStatus.OK, "팔로우가 수락되었습니다.", follow);
            }
            return Response.newResult(HttpStatus.BAD_REQUEST, "이미 완료된 작업입니다.", null);
        }

        // 팔로우 신청이 없는 경우
        return Response.newResult(HttpStatus.NOT_ACCEPTABLE, "팔로우 신청 내역이 없습니다.", null);
    }

    @Transactional
    public ResponseEntity<Response> findFollowingList(String nickname) {
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
        return Response.newResult(HttpStatus.OK, nickname + "의 팔로잉 목록", list);
    }

    public ResponseEntity<Response> findFollowerList(String nickname) {
        Optional<User> optionalUser = userRepository.findUserByNickname(nickname);
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.", null);
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
        return Response.newResult(HttpStatus.OK, nickname + "의 팔로워 목록", list);
    }

    public ResponseEntity<Response> getFollowRequest(HttpServletRequest request) {
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
        if (requests.size() == 0) {
            return Response.newResult(HttpStatus.OK, "아직은 요청이 없어요.", null);
        }
        return Response.newResult(HttpStatus.OK, user.getNickname() + "의 팔로워 요청 목록" , list);
    }
    public ResponseEntity<Response> unfollow(FollowAskRequest followAskRequest, HttpServletRequest request) {
        System.out.println(followAskRequest);
        System.out.println(followAskRequest.getUserNickname());
        Optional<User> optionalUser = userRepository.findUserByNickname(followAskRequest.getUserNickname());
        if (!optionalUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다." , null);
        }
        String userId = optionalUser.get().getId();
        followRepository.deleteAllByUserIdAndFollowingIdAndAccept(TokenToId.check(request), userId, true);

        return Response.newResult(HttpStatus.OK, "팔로우를 취소하였습니다.", null);
    }
}