package com.a206.mychelin.web;

import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.web.dto.UserSaveRequest;
import com.a206.mychelin.web.dto.UserUpdateRequest;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Test
    public void user_회원가입() throws Exception{
        String id = "human@human.com";
        String nickname = "김나무";
        String password = bCryptPasswordEncoder.encode("12341234");
        String phone_number = "010-2222-3333";

        UserSaveRequest requestDto = UserSaveRequest.builder()
                    .id(id)
                    .nickname(nickname)
                    .password(password)
                    .phone_number(phone_number)
                    .build();

        String url = "http://localhost:" + port + "/user/signup";

        //when
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestDto, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains("김나무");


    }

    @Test
    public void user_개인정보_수정() throws Exception{

        //given
        User savedUser = userRepository.save(User.builder()
                        .id("human@human.com")
                        .nickname("김초록")
                        .password(bCryptPasswordEncoder.encode("12341234"))
                        .phoneNumber("02-123-5678")
                        .build());

        String id = savedUser.getId();
        System.out.println(id);
        String newNickname = "김빨강";
        String newPhoneNumber = "010-1234-5678";
        String newBio = "멋진 코드를 짜고 싶다";

        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                                            .id(id)
                                            .bio(newBio)
                                            .nickname(newNickname)
                                            .phone_number(newPhoneNumber)
                                            .build();
        
        String url = "http://localhost:" + port + "/user/changeInfo/" + id;

        HttpEntity<UserUpdateRequest> requestEntity = new HttpEntity<>(updateRequest);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains("김빨강");



    }
}
