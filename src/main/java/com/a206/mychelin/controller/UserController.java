package com.a206.mychelin.controller;

import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.util.TokenUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping(value="/signup")
    public ResponseEntity<Object> signUp(@RequestBody User user){
        String id = user.getId();
        String password = user.getPassword();
        String nickname = user.getNickname();
        String phone_number = user.getPhoneNumber();
        Map<String, Object> result = new HashMap<>();

        if(userRepository.findUserById(id).isPresent()){
            result.put("message", "이미 존재하는 이메일입니다.");
            return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
        } else {
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
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        return new ResponseEntity<String>("test", HttpStatus.OK);
    }

    @PostMapping("/loginSuccess")
    public String loginSuccess(){
        return "성공!";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess1(){
        return "성공!12";
    }
}