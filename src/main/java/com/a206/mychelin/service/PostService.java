package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Post;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.PostRepository;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.util.TokenToId;
import com.a206.mychelin.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<CustomResponseEntity> addPost(@RequestBody PostUploadRequest postRequest, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        Post newPost = Post.builder()
                .userId(userId)
                .content(postRequest.getContent())
                .placeId(postRequest.getPlaceId())
                .placelistId(postRequest.getPlacelistId())
                .build();
        System.out.println(newPost.toString());
        postRepository.save(newPost);

        CustomResponseEntity customResponse
                = CustomResponseEntity.builder()
                .message("게시글이 업로드되었습니다")
                .status(200)
                .build();

        return new ResponseEntity(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> update(@PathVariable int id, @RequestBody PostUpdateRequest postUpdateRequest, HttpServletRequest httpRequest) {
        CustomResponseEntity customResponse;
        String userId = TokenToId.check(httpRequest);
        Optional<Post> tempPost = postRepository.findPostById(id);
        if (!tempPost.isPresent()) {
            customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message("게시글이 없습니다.")
                    .build();
            return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.BAD_REQUEST);
        }
        Post post = tempPost.get();
        System.out.println(post.toString());
        if (userId.equals(post.getUserId())) {
            System.out.println("작성자 확인");
            post.update(postUpdateRequest.getContent());
            customResponse = CustomResponseEntity.builder()
                    .status(200)
                    .message(post.getId() + "번 게시글이 수정되었습니다.")
                    .build();
            return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
        }
        customResponse = CustomResponseEntity.builder()
                .status(401)
                .message("수정 권한이 없습니다.")
                .build();
        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity getPost(@PathVariable int id) {
        Optional<Post> entity = postRepository.findPostById(id);
        if (entity.isPresent()) {
            CustomResponseEntity customResponseEntity
                    = CustomResponseEntity.builder()
                    .status(200)
                    .message(id + "번게시글을 불러왔습니다.")
                    .data(entity.get())
                    .build();

            return new ResponseEntity<CustomResponseEntity>(customResponseEntity, HttpStatus.OK);
        }
        CustomResponseEntity customResponseEntity
                = CustomResponseEntity.builder()
                .status(400)
                .message("게시글을 불러오지 못했습니다.")
                .build();
        return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> delete(@PathVariable int id, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        Post post = postRepository.findPostById(id).get();
        if (userId.equals(post.getUserId())) {
            postRepository.deletePostById(id);
            CustomResponseEntity customResponse
                    = CustomResponseEntity.builder()
                    .status(200)
                    .message(id + "번 게시물을 삭제했습니다.")
                    .build();
            return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
        }
        CustomResponseEntity customResponse
                = CustomResponseEntity.builder()
                .status(401)
                .message("권한이 없습니다.")
                .build();
        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity getPostByUserId(@RequestBody String userId) {
        Optional<User> user = userRepository.findUserById(userId);
        if (!user.isPresent()) {
            CustomResponseEntity customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message(userId + "존재하지 않는 유저입니다.")
                    .build();
            return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.BAD_REQUEST);
        }
        List<Post> posts = postRepository.findPostsByUserIdOrderByCreateDateDesc(userId);
        CustomResponseEntity customResponse = CustomResponseEntity.builder()
                .status(200)
                .message(userId + "의 게시글을 불러왔습니다.")
                .data(posts.toArray())
                .build();
        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
    }
}