package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Post;
import com.a206.mychelin.domain.repository.CommentRepository;
import com.a206.mychelin.domain.repository.PostRepository;
import com.a206.mychelin.util.TimestampToDateString;
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
import java.sql.Timestamp;
import java.util.*;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public ResponseEntity<CustomResponseEntity> addPostText(@RequestBody PostUploadRequest postRequest, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        postRequest.setUserId(userId);

        postRepository.save(Post.builder()
                .userId(userId)
                .content(postRequest.getContent()).build());

        CustomResponseEntity customResponse
                = CustomResponseEntity.builder()
                .message("게시글이 업로드되었습니다")
                .status(200)
                .build();

        return new ResponseEntity(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> addPostPlace(@RequestBody PostWPlaceUploadRequest postRequest, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        postRequest.setUserId(userId);

        postRepository.save(postRequest.toEntity());
        CustomResponseEntity customResponse
                = CustomResponseEntity.builder()
                .message("게시글이 업로드되었습니다")
                .status(200)
                .data(postRequest)
                .build();

        return new ResponseEntity(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> addPostPlaceList(@RequestBody PostWPlaceListUploadRequest postRequest, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        postRequest.setUserId(userId);

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

    public ResponseEntity findPostByPostId(@PathVariable int postId) {
        List<Object[]> entity = postRepository.findPostInfoByPostId(postId);

        if (entity.size() > 0) {
            Object[] item = entity.get(0);
            String dateDiff = TimestampToDateString.getPassedTime((Timestamp) item[4]);
            int idx = 7;
            PostInfoResponse postInfo
                    = PostInfoResponse.builder()
                    .postId((int) item[0])
                    .userNickname((String) item[1])
                    .userFollowerCnt(item[2])
                    .content((String) item[3])
                    .createDate(dateDiff)
                    .likeCnt(item[5])
                    .commentCnt(item[6])
                    .placeId(item[7])
                    .placelistId(item[8])
                    .image((String) item[9])
                    .build();
            
            CustomResponseEntity customResponseEntity
                    = CustomResponseEntity.builder()
                    .status(200)
                    .message(postId + "번 게시글을 불러왔습니다.")
                    .data(postInfo)
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
        String user_id = TokenToId.check(httpRequest);

        Post post = postRepository.findPostById(id).get();
        if (user_id.equals(post.getUserId())) {
            int delete_post_idx = postRepository.deletePostById(id);

            CustomResponseEntity customResponse
                    = CustomResponseEntity.builder()
                    .status(200)
                    .message(delete_post_idx + "번 게시물을 삭제했습니다.")
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

    public ResponseEntity findPostsByUserNickname(@PathVariable String userNickname) {
        System.out.println(userNickname);
        List<Object[]> posts = postRepository.findPostsByUserNicknameOrderByCreateDateDesc(userNickname);

        ArrayList<PostInfoResponse> arr = extractPosts(posts);


        CustomResponseEntity customResponse = CustomResponseEntity.builder()
                .status(200)
                .message(userNickname + "의 게시글을 불러왔습니다.")
                .data(arr)
                .build();
        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
    }

    public ResponseEntity findPostsByFollowingUsersOrderByCreateDateDesc(HttpServletRequest httpRequest) {
        CustomResponseEntity customResponseEntity;
        String userId = TokenToId.check(httpRequest);

        List<Object[]> items = postRepository.findPostsByFollowingUsersOrderByCreateDateDesc(userId);
        ArrayList<PostInfoResponse> arr = extractPosts(items);
        if (items.size() == 0) {
            customResponseEntity = CustomResponseEntity.builder()
                    .status(200)
                    .message("팔로우하는 친구의 소식을 기다려주세요!")
                    .build();
            return new ResponseEntity(customResponseEntity, HttpStatus.OK);
        }

        extractPosts(items);
        customResponseEntity = CustomResponseEntity.builder()
                .status(200)
                .message("포스트를 불러옵니다.")
                .data(arr).build();

        return new ResponseEntity(customResponseEntity, HttpStatus.OK);
    }

    private ArrayList<PostInfoResponse> extractPosts(List<Object[]> posts) {
        ArrayList<PostInfoResponse> arr = new ArrayList<>();
        for (Object[] post : posts) {
            String dateDiff = TimestampToDateString.getPassedTime((Timestamp) post[4]);
            arr.add(PostInfoResponse.builder()
                    .postId((int) post[0])
                    .userNickname((String) post[1])
                    .userFollowerCnt(post[2])
                    .content((String) post[3])
                    .createDate(dateDiff)
                    .likeCnt(post[5])
                    .commentCnt(post[6])
                    .placeId(post[7])
                    .placelistId(post[8])
                    .image((String) post[9])
                    .build());
        }
        return arr;
    }
}