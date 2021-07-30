package com.a206.mychelin.service;

import com.a206.mychelin.config.AuthConstants;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.FollowRepository;
import com.a206.mychelin.domain.repository.PostLikeRepository;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.util.TokenUtils;
import com.a206.mychelin.web.dto.*;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final FollowRepository followRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private User getUser(HttpServletRequest request) {
        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        String token = TokenUtils.getTokenFromHeader(header);
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String id = (String) claims.get("id");
        Optional<User> user = userRepository.findUserById(id);
        if (!user.isPresent()) {
            return null;
        }
        return user.get();
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> update(@RequestBody UserUpdateRequest requestDto, HttpServletRequest request) {
        CustomResponseEntity customResponse;
        User user = getUser(request);
        if (user == null) {
            customResponse = CustomResponseEntity.builder().status(400).message("유저가 존재하지 않습니다.").build();
            return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.BAD_REQUEST);
        }
        user.update(user.getId(), requestDto.getNickname(), requestDto.getBio(), requestDto.getPhone_number(), requestDto.getProfile_image());
        customResponse = CustomResponseEntity.builder()
                .status(200)
                .message("개인정보가 수정되었습니다.")
                .build();
        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> changePassword(PasswordChangeRequest passwordChangeRequest, HttpServletRequest request) {
        CustomResponseEntity customResponse;
        User user = getUser(request);
        if (user == null) {
            customResponse = CustomResponseEntity.builder().status(400).message("유저가 존재하지 않습니다.").build();
            return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.BAD_REQUEST);
        }
        if (!passwordEncoder.matches(passwordChangeRequest.getPassword(), user.getPassword())) {
            customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message("비밀번호가 일치하지 않습니다.")
                    .build();
            return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.BAD_REQUEST);
        }
        user.changePassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        customResponse = CustomResponseEntity.builder()
                .status(200)
                .message("비밀번호가 변경되었습니다.")
                .build();
        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity signUp(UserSaveRequest userSaveRequest) {
        CustomResponseEntity customResponse;
        String id = userSaveRequest.getId();
        String nickname = userSaveRequest.getNickname();
        if (userRepository.findUserById(id).isPresent()) {
            customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message("이미 존재하는 이메일입니다.")
                    .build();
            return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findUserByNickname(nickname).isPresent()) {
            customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message("이미 존재하는 닉네임입니다.")
                    .build();
            return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.BAD_REQUEST);
        }
        userSaveRequest.setPassword(passwordEncoder.encode(userSaveRequest.getPassword()));
        User user = User.builder()
                .id(id)
                .password(userSaveRequest.getPassword())
                .nickname(nickname)
                .phoneNumber(userSaveRequest.getPhoneNumber())
                .build();
        userRepository.save(user);
        customResponse = CustomResponseEntity.builder()
                .status(200)
                .message("회원가입이 완료 되었습니다.")
                .build();
        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> deleteUser(HttpServletRequest request) {
        User user = getUser(request);
        userRepository.deleteUsersById(user.getId());
        CustomResponseEntity customResponse
                = CustomResponseEntity.builder()
                .status(200)
                .message("탈퇴가 완료되었습니다")
                .build();
        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> getProfile(String nickname, HttpServletRequest request) {
        CustomResponseEntity customResponseEntity;
        UserProfileResponse userProfileResponse;
        Optional<User> tempUser = userRepository.findUserByNickname(nickname);
        if (!tempUser.isPresent()) {
            customResponseEntity = CustomResponseEntity.builder()
                    .status(400)
                    .message("존재하지 않는 유저입니다.")
                    .build();
            return new ResponseEntity<CustomResponseEntity>(customResponseEntity, HttpStatus.BAD_REQUEST);
        }
        User user = tempUser.get();
        int follow = followRepository.countByUserId(user.getId());
        int follower = followRepository.countByFollowingId(user.getId());
        long like = postLikeRepository.getLikes(user.getId());
        Boolean isFollower = null;
        userProfileResponse = UserProfileResponse.builder()
                .nickname(user.getNickname())
                .bio(user.getBio())
                .profileImage(user.getProfileImage())
                .follow(follow)
                .follower(follower)
                .like(like)
                .isFollowing(isFollower)
                .build();
        User loginUser = getUser(request);
        if (loginUser.getId().equals(user.getId())) {
            customResponseEntity = CustomResponseEntity.builder()
                    .status(200)
                    .data(userProfileResponse)
                    .message("회원정보 출력")
                    .build();
            return new ResponseEntity<CustomResponseEntity>(customResponseEntity, HttpStatus.OK);
        }
        if (followRepository.countByUserIdAndFollowingId(loginUser.getId(), user.getId()) > 0) {
            userProfileResponse.setIsFollower(true);
        } else {
            userProfileResponse.setIsFollower(false);
        }
        customResponseEntity = CustomResponseEntity.builder()
                .status(200)
                .data(userProfileResponse)
                .message("회원정보 출력")
                .build();
        return new ResponseEntity<CustomResponseEntity>(customResponseEntity, HttpStatus.OK);
    }
}