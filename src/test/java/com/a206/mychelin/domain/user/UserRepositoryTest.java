package com.a206.mychelin.domain.user;

import com.a206.mychelin.domain.entity.User;
import org.junit.*;
import com.a206.mychelin.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @After
    public void cleanup(){
        userRepository.deleteAll();
    }

    @Test
    public void 회원정보_불러오기(){
        String nickname = "테스트 닉네임";
        String id = "테스트이메일@naver.com";
        String phone_number = "010-1111-1111";
        String password = "테스트_비밀번호";
        String bio = "테스트 한 줄 자기 소개";

        userRepository.save(User.builder()
                                .id(id)
                                .password(password)
                                .nickname(nickname)
                                .phoneNumber(phone_number)
                                .build());

        //when
        Optional<User> optionalUser = userRepository.findUserById(id);

        //then
        if(!optionalUser.isPresent()){


        }else {
            User user = optionalUser.get();
            assertThat(user.getId()).isEqualTo(id);
            assertThat(user.getPhoneNumber()).isEqualTo(phone_number);
        }
    }

}
