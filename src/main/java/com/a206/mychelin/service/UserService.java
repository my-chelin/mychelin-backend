package com.a206.mychelin.service;

import com.a206.mychelin.config.AuthConstants;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.util.TokenUtils;
import com.a206.mychelin.web.dto.PasswordChangeRequest;
import com.a206.mychelin.web.dto.UserResponse;
import com.a206.mychelin.web.dto.UserSaveRequest;
import com.a206.mychelin.web.dto.UserUpdateRequest;
import com.sun.org.apache.regexp.internal.RE;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User update(String id, UserUpdateRequest requestDto) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다. id = " + id));
        user.update(requestDto.getNickname(), requestDto.getBio(), requestDto.getPhone_number(), requestDto.getProfile_image());
        return user;
    }

    public UserResponse findUserById(String id) {
        Optional<User> user = userRepository.findUserById(id);
        if (!user.isPresent()) {
            throw new IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다. 입력하신 이메일 : " + id);
        }
        return new UserResponse(user.get());
    }

    @Transactional
    public String save(UserSaveRequest requestDto) {
        return userRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public ResponseEntity<String> changePassword(PasswordChangeRequest passwordChangeRequest, HttpServletRequest request) {
        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        String token = TokenUtils.getTokenFromHeader(header);
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String id = (String) claims.get("id");
        User user = userRepository.findUserById(id).get();
        if (!passwordEncoder.matches(passwordChangeRequest.getPassword(), user.getPassword())) {
            return new ResponseEntity<String>("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        user.changePassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        userRepository.save(user);
        return new ResponseEntity<String>("비밀번호가 변경되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> signUp(User user) {
        String id = user.getId();
        String password = user.getPassword();
        String nickname = user.getNickname();
        String phone_number = user.getPhoneNumber();

        if (userRepository.findUserById(id).isPresent()) {
            return new ResponseEntity<String>("이미 존재하는 이메일 입니다.", HttpStatus.BAD_REQUEST);
        }
        User newUser = User.builder()
                .id(id)
                .nickname(nickname)
                .phoneNumber(phone_number)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(newUser);

        return new ResponseEntity<String>("회원가입이 완료 되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        String token = TokenUtils.getTokenFromHeader(header);
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String id = (String) claims.get("id");
        userRepository.deleteUsersById(id);
        return new ResponseEntity<String>("탈퇴가 완료되었습니다.", HttpStatus.OK);
    }
}