package com.a206.mychelin.service;

import com.a206.mychelin.config.AuthConstants;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.entity.UserEmailCheck;
import com.a206.mychelin.domain.repository.FollowRepository;
import com.a206.mychelin.domain.repository.PostLikeRepository;
import com.a206.mychelin.domain.repository.UserEmailCheckRepository;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.util.TokenUtils;
import com.a206.mychelin.web.dto.*;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final FollowRepository followRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    // 이메일 보내기 위해 선언, JavaMailSender를 구현한 Bean 객체 주입
    private final JavaMailSender javaMailSender;
    private final UserEmailCheckRepository userEmailCheckRepository;


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
        int follow = followRepository.countByUserIdAndAccept(user.getId(), true);
        int follower = followRepository.countByFollowingIdAndAccept(user.getId(), true);
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
        if (followRepository.countByUserIdAndFollowingIdAndAccept(loginUser.getId(), user.getId(), true) > 0) {
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

    public ResponseEntity checkEmail(EmailRequest emailRequest) {
        Optional<User> user = userRepository.findUserById(emailRequest.getEmail());
        int status=404;
        String message="";
        Object data=null;
        HttpStatus httpStatus=HttpStatus.NOT_FOUND;

        if(user.isPresent()){
            message="이미 가입된 이메일입니다.";
        }
        else{
            SimpleMailMessage emailMessage = new SimpleMailMessage();

            StringBuffer token = new StringBuffer();
            Random rnd = new Random();
            for (int i = 0; i < 10; i++) {
                int rIndex = rnd.nextInt(3);
                switch (rIndex) {
                    case 0:
                        // a-z
                        token.append((char) ((int) (rnd.nextInt(26)) + 97));
                        break;
                    case 1:
                        // A-Z
                        token.append((char) ((int) (rnd.nextInt(26)) + 65));
                        break;
                    case 2:
                        // 0-9
                        token.append((rnd.nextInt(10)));
                        break;
                }
            }

            emailMessage.setTo(emailRequest.getEmail());
            emailMessage.setSubject("가입 인증 메일입니다.");
            emailMessage.setText("안녕하세요 Mychelin 입니다.\n\n" +
                    "가입 인증 토큰 : "+token.toString()+" 입니다.\n\n"+
                    "감사합니다.");

            javaMailSender.send(emailMessage);

            UserEmailCheck userEmailCheck = UserEmailCheck.builder()
                    .userId(emailRequest.getEmail())
                    .token(token.toString())
                    .build();

            userEmailCheckRepository.save(userEmailCheck);

            // 이메일 전송
            status=200;
            message=emailRequest.getEmail()+"로 인증 번호를 전송하였습니다.";
            httpStatus=HttpStatus.OK;
        }

        CustomResponseEntity result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();

        return new ResponseEntity<CustomResponseEntity>(result,httpStatus);


    }

    public ResponseEntity checkEmailToken(EmailTokenRequest emailTokenRequest) {
        Optional<User> user = userRepository.findUserById(emailTokenRequest.getEmail());
        int status=404;
        String message="";
        Object data=null;
        HttpStatus httpStatus=HttpStatus.NOT_FOUND;
        if(user.isPresent()){
            message="이미 가입된 이메일입니다.";
        }
        else{
            /*
                1. token DB에 저장되어 있는지 확인
                2. 없다면, 없다고 리턴
                3. 있다면, 들어온 토큰과 비교
                4. 다르다면, 다르다고 리턴
                5. 같다면 회원 가입 가능한 이메일임을 알려주기
             */

            Optional<UserEmailCheck> userEmailCheck = userEmailCheckRepository.findByUserId(emailTokenRequest.getEmail());

            if(!userEmailCheck.isPresent()){
                message="인증 번호 요청이 되지 않은 이메일입니다.";
            }
            else if(!userEmailCheck.get().getToken().equals(emailTokenRequest.getToken())){
                status=401;
                httpStatus=HttpStatus.UNAUTHORIZED;
                message="인증 번호가 일치하지 않습니다.";
            }
            else{
                status=200;
                httpStatus=HttpStatus.OK;
                message="인증되었습니다.";
            }
        }

        CustomResponseEntity result = CustomResponseEntity.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();

        return new ResponseEntity<CustomResponseEntity>(result,httpStatus);
    }
}