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

    @Transactional
    public ResponseEntity<CustomResponseEntity> update(@RequestBody UserUpdateRequest requestDto, HttpServletRequest httpRequest) {
        CustomResponseEntity customResponse;
        String header = httpRequest.getHeader(AuthConstants.AUTH_HEADER);
        String token = TokenUtils.getTokenFromHeader(header);
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String user_id = (String) claims.get("id");

        User user = userRepository.findUserById(user_id).get();
        user.update(user_id, requestDto.getNickname(), requestDto.getBio(), requestDto.getPhone_number(), requestDto.getProfile_image());
        customResponse
                = CustomResponseEntity.builder()
                .status(200)
                .message("개인정보가 수정되었습니다.")
                .build();

        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> changePassword(PasswordChangeRequest passwordChangeRequest, HttpServletRequest request) {
        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        String token = TokenUtils.getTokenFromHeader(header);
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String id = (String) claims.get("id");
        User user = userRepository.findUserById(id).get();
        CustomResponseEntity customResponse;
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
    public ResponseEntity signUp(User user) {
        CustomResponseEntity customResponse;
        String id = user.getId();
        String password = user.getPassword();
        String nickname = user.getNickname();
        String phone_number = user.getPhoneNumber();

        if (userRepository.findUserById(id).isPresent()) {
            customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message("이미 존재하는 이메일입니다.")
                    .build();
            return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.BAD_REQUEST);
        }
        User newUser = User.builder()
                .id(id)
                .nickname(nickname)
                .phoneNumber(phone_number)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(newUser);
        customResponse = CustomResponseEntity.builder()
                .status(200)
                .message("회원가입이 완료 되었습니다.")
                .build();
        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> deleteUser(HttpServletRequest request) {
        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        String token = TokenUtils.getTokenFromHeader(header);
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String id = (String) claims.get("id");
        CustomResponseEntity customResponse
                = CustomResponseEntity.builder()
                .status(200)
                .message("탈퇴가 완료되었습니다")
                .build();
        userRepository.deleteUsersById(id);
        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> getProfile(HttpServletRequest request) {
        CustomResponseEntity customResponseEntity;
        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        String token = TokenUtils.getTokenFromHeader(header);
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String id = (String) claims.get("id");
        Optional<User> tempUser = userRepository.findUserById(id);
        if (tempUser == null) {
            customResponseEntity = CustomResponseEntity.builder()
                    .status(400)
                    .message("존재하지 않는 유저입니다.")
                    .build();
            return new ResponseEntity<CustomResponseEntity>(customResponseEntity, HttpStatus.BAD_REQUEST);
        }
        User user = tempUser.get();
        int follow = followRepository.countByUserId(id);
        long like = postLikeRepository.getLikes(id);
        int follower = followRepository.countByFollowingId(id);
        UserProfileResponse userProfileResponse
                = UserProfileResponse.builder()
                .id(id)
                .nickname(user.getNickname())
                .bio(user.getBio())
                .profileImage(user.getProfileImage())
                .follow(follow)
                .follower(follower)
                .like(like)
                .build();
        customResponseEntity = CustomResponseEntity.builder()
                .status(200)
                .data(userProfileResponse)
                .message("회원정보 출력")
                .build();
        return new ResponseEntity<CustomResponseEntity>(customResponseEntity, HttpStatus.OK);
    }
}