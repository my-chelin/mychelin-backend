package com.a206.mychelin.controller;

import com.a206.mychelin.config.AuthConstants;
import com.a206.mychelin.config.CustomAuthenticationProvider;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.util.TokenUtils;
import com.a206.mychelin.web.dto.PasswordChangeRequest;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping(value = "/signup")
    public ResponseEntity<Object> signUp(@RequestBody User user) {
        String id = user.getId();
        String password = user.getPassword();
        String nickname = user.getNickname();
        String phone_number = user.getPhoneNumber();
        Map<String, Object> result = new HashMap<>();

        if (userRepository.findUserById(id).isPresent()) {
            result.put("message", "이미 존재하는 이메일입니다.");
            return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
        }
        User newUser = User.builder()
                .id(id)
                .nickname(nickname)
                .phoneNumber(phone_number)
                .password(passwordEncoder.encode(password))
                .build();
        log.info(">>>>>id check", id);
        userRepository.save(newUser);

        log.info(">>>>>nickname check", newUser.getNickname());
        result.put("nickname", nickname);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/changepwd")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest, HttpServletRequest request) {
        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        if (header == null) {
            return new ResponseEntity<String>("토큰이 없습니다.", HttpStatus.UNAUTHORIZED);
        }
        String token = TokenUtils.getTokenFromHeader(header);
        if (!TokenUtils.isValidToken(token)) {
            return new ResponseEntity<String>("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String id = (String) claims.get("id");
        String password = passwordEncoder.encode(passwordChangeRequest.getPassword());
        Optional<User> nowUser = userRepository.findUserById(id);
        if (!nowUser.isPresent()) {
            return new ResponseEntity<String>("존재하지 않는 유저입니다.", HttpStatus.BAD_REQUEST);
        }
        if (!passwordEncoder.matches(passwordChangeRequest.getPassword(), nowUser.get().getPassword())) {
            return new ResponseEntity<String>("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        User user = nowUser.get();
        user.changePassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        userRepository.save(user);
        return new ResponseEntity<String>("비밀번호가 변경되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        return new ResponseEntity<String>("test", HttpStatus.OK);
    }
}