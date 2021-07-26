package com.a206.mychelin.web;

import com.a206.mychelin.domain.entity.Post;
import com.a206.mychelin.domain.repository.PostRepository;
import com.a206.mychelin.web.dto.PostUploadRequest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    PostRepository postRepository;

    @After
    public void tearDown() throws Exception {
        postRepository.deleteAll();
    }

    @Test
    public void Post_등록() throws Exception{
        //given
        String title = "오늘 점심 메뉴는";
        String content = "점심은 아무래도 볶음밥을 먹어야겠다";
        PostUploadRequest postUploadRequest = PostUploadRequest.builder()
                                                .title(title)
                                                .content(content)
                                                .userId("hi@naver.com")
                                                .build();

        String url = "http://localhost:" + port + "/post/upload";

        //when
        ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(url, postUploadRequest, Integer.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0);

//        List<Post> allPosts = postRepository.findAll();
//        assertThat(allPosts.get(0).getTitle()).isEqualTo(title);
//        assertThat(allPosts.get(0).getContent()).isEqualTo(content);
    }
}