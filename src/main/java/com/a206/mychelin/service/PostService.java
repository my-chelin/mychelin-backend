package com.a206.mychelin.service;

import com.a206.mychelin.config.AuthConstants;
import com.a206.mychelin.domain.entity.Post;
import com.a206.mychelin.domain.repository.CommentRepository;
import com.a206.mychelin.domain.repository.PostRepository;
import com.a206.mychelin.util.TokenUtils;
import com.a206.mychelin.web.dto.*;
import io.jsonwebtoken.Claims;
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
    private final CommentRepository commentRepository;

    @Transactional
    public ResponseEntity<CustomResponseEntity> addPostText(@RequestBody PostUploadRequest postRequest, HttpServletRequest httpRequest) {
        String header = httpRequest.getHeader(AuthConstants.AUTH_HEADER);
        String token = TokenUtils.getTokenFromHeader(header);
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String userId = (String) claims.get("id");
        postRequest.setUserId(userId);

        System.out.println(postRequest.toEntity());

        int id = postRepository.save(postRequest.toEntity()).getId();
//        postRepository.saveText(userId, postRequest.getTitle(), postRequest.getContent());
        CustomResponseEntity customResponse
                = CustomResponseEntity.builder()
                .message("게시글이 업로드되었습니다")
                .status(200)
                .build();

        return new ResponseEntity(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> addPostPlace(@RequestBody PostWPlaceUploadRequest postRequest, HttpServletRequest httpRequest) {
        String header = httpRequest.getHeader(AuthConstants.AUTH_HEADER);
        String token = TokenUtils.getTokenFromHeader(header);
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String userId = (String) claims.get("id");
        postRequest.setUserId(userId);

        System.out.println(postRequest.getPlaceId());

        postRepository.save(postRequest.toEntity());
        CustomResponseEntity customResponse
                = CustomResponseEntity.builder()
                .message("게시글이 업로드되었습니다")
                .status(200)
                .data(postRequest)
                .build();

        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> addPostPlaceList(@RequestBody PostWPlaceListUploadRequest postRequest, HttpServletRequest httpRequest){
        String header = httpRequest.getHeader(AuthConstants.AUTH_HEADER);
        String token = TokenUtils.getTokenFromHeader(header);
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String userId = (String) claims.get("id");
        postRequest.setUserId(userId);
        System.out.println(postRequest.getPlacelistId());

        postRepository.save(postRequest.toEntity());
        CustomResponseEntity customResponse
                = CustomResponseEntity.builder()
                .message("게시글이 업로드되었습니다")
                .status(200)
                .data(postRequest)
                .build();

        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> update(@PathVariable int id, @RequestBody PostUpdateRequest postUpdateRequest, HttpServletRequest httpRequest) {
        CustomResponseEntity customResponse;
        String header = httpRequest.getHeader(AuthConstants.AUTH_HEADER);
        String token = TokenUtils.getTokenFromHeader(header);
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String userId = (String) claims.get("id");
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
            post.update(postUpdateRequest.getTitle(), postUpdateRequest.getContent());
            customResponse = CustomResponseEntity.builder()
                    .status(200)
                    .message(post.getId() + "번 게시글이 수정되었습니다.")
                    .build();
            return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
        }
        customResponse = CustomResponseEntity.builder()
                .status(400)
                .message("수정 권한이 없습니다.")
                .build();
        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity findPostById(@PathVariable int id) {
        Optional<Post> entity = postRepository.findPostById(id);

        if(entity.isPresent()){
            CustomResponseEntity customResponseEntity
                    = CustomResponseEntity.builder()
                    .status(200)
                    .message(id + "번게시글을 불러왔습니다.")
                    .data(entity.get())
                    .build();

            return new ResponseEntity<CustomResponseEntity>(customResponseEntity, HttpStatus.OK);
        }else{
            CustomResponseEntity customResponseEntity
                    = CustomResponseEntity.builder()
                    .status(400)
                    .message("게시글을 불러오지 못했습니다.")
                    .build();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> delete(@PathVariable int id, HttpServletRequest httpRequest) {
        String header = httpRequest.getHeader(AuthConstants.AUTH_HEADER);
        String token = TokenUtils.getTokenFromHeader(header);
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String user_id = (String) claims.get("id");

        Post post = postRepository.findPostById(id).get();
        if (user_id.equals(post.getUserId())) {
            int delete_post_idx = postRepository.deletePostById(id);

            CustomResponseEntity customResponse
                    = CustomResponseEntity.builder()
                    .status(200)
                    .message(delete_post_idx + "번 게시물을 삭제했습니다.")
                    .build();
            return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
        } else {
            CustomResponseEntity customResponse
                    = CustomResponseEntity.builder()
                    .status(400)
                    .message("권한이 없습니다.")
                    .build();
            return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity findPostsByUserId(@RequestBody PostByUserRequest postByUserRequest) {
        String user_id = postByUserRequest.getUser_id();
        List<Post> posts = postRepository.findPostsByUserIdOrderByCreateDateDesc(user_id);

        CustomResponseEntity customResponse = CustomResponseEntity.builder()
                .status(200)
                .message(user_id + "의 게시글을 불러왔습니다.")
                .data(posts.toArray())
                .build();
        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
    }

}
