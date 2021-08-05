package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Post;
import com.a206.mychelin.domain.entity.PostImage;
import com.a206.mychelin.domain.entity.PostLike;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.*;
import com.a206.mychelin.util.TokenToId;
import com.a206.mychelin.util.TimestampToDateString;
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
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<CustomResponseEntity> addPost(@RequestBody PostUploadRequest postRequest, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);

        postRequest.checkPlaceIdOrPlaceListId();

        Post newPost = Post.builder()
                .userId(userId)
                .content(postRequest.getContent())
                .placeId(postRequest.getPlaceId())
                .placeListId(postRequest.getPlaceListId())
                .build();

        CustomResponseEntity customResponse
                = CustomResponseEntity.builder()
                .message("게시글이 업로드되었습니다")
                .status(200)
                .build();
        postRepository.save(newPost);

        for(String image : postRequest.getImages()){
            System.out.println(image);
            // 이미지 추가
            PostImage insertPostImage = PostImage.builder()
                    .postId(newPost.getId())
                    .image(image)
                    .build();

            postImageRepository.save(insertPostImage);

        }

        return new ResponseEntity<>(customResponse, HttpStatus.OK);
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
            return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
        }
        Post post = tempPost.get();
        if (userId.equals(post.getUserId())) {
            post.update(postUpdateRequest.getContent());
            customResponse = CustomResponseEntity.builder()
                    .status(200)
                    .message(post.getId() + "번 게시글이 수정되었습니다.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
        }
        customResponse = CustomResponseEntity.builder()
                .status(401)
                .message("수정 권한이 없습니다.")
                .build();
        return new ResponseEntity<>(customResponse, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<CustomResponseEntity> getPostByPostId(@PathVariable int postId, HttpServletRequest httpRequest) {
        List<Object[]> entity = postRepository.findPostInfoByPostId(postId);
        String userId = TokenToId.check(httpRequest);
        CustomResponseEntity customResponse;
        if (userId == null) {
            customResponse = CustomResponseEntity.builder()
                    .status(401)
                    .message("로그인 후 사용가능합니다.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
        }

        if (entity.size() > 0) {
            PostInfoResponse postInfo = extractPosts(entity, userId).get(0);
            CustomResponseEntity customResponseEntity
                    = CustomResponseEntity.builder()
                    .status(200)
                    .message(postId + "번 게시글을 불러왔습니다.")
                    .data(postInfo)
                    .build();
            return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
        }
        CustomResponseEntity customResponseEntity
                = CustomResponseEntity.builder()
                .status(400)
                .message("게시글을 불러오지 못했습니다.")
                .build();
        return new ResponseEntity<>(customResponseEntity, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> delete(@PathVariable int id, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            CustomResponseEntity customResponse
                    = CustomResponseEntity.builder()
                    .status(401)
                    .message("로그인 후 이용해주세요.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.UNAUTHORIZED);
        }
        Optional<Post> entity = postRepository.findPostById(id);
        if (!entity.isPresent()) {
            CustomResponseEntity customResponse
                    = CustomResponseEntity.builder()
                    .status(400)
                    .message("게시글이 없습니다.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
        }
        Post post = entity.get();
        if (userId.equals(post.getUserId())) {
            postRepository.deletePostById(id);
            CustomResponseEntity customResponse
                    = CustomResponseEntity.builder()
                    .status(200)
                    .message(id + "번 게시물을 삭제했습니다.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
        }
        CustomResponseEntity customResponse
                = CustomResponseEntity.builder()
                .status(401)
                .message("삭제 권한이 없습니다.")
                .build();
        return new ResponseEntity<>(customResponse, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<CustomResponseEntity> findPostsByUserNickname(@PathVariable String userNickname, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            CustomResponseEntity customResponse = CustomResponseEntity.builder()
                    .status(401)
                    .message("로그인 후 사용해주세요.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.UNAUTHORIZED);
        }
        List<Object[]> posts = postRepository.findPostsByUserNicknameOrderByCreateDateDesc(userNickname);
        ArrayList<PostInfoResponse> arr = extractPosts(posts, userId);

        CustomResponseEntity customResponse = CustomResponseEntity.builder()
                .status(200)
                .message(userNickname + "의 게시글을 불러왔습니다.")
                .data(arr)
                .build();
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

    public ResponseEntity<CustomResponseEntity> findPostsByFollowingUsersOrderByCreateDateDesc(HttpServletRequest httpRequest) {
        CustomResponseEntity customResponseEntity;
        String userId = TokenToId.check(httpRequest);

        List<Object[]> items = postRepository.findPostsByFollowingUsersOrderByCreateDateDesc(userId);
        ArrayList<PostInfoResponse> arr = extractPosts(items, userId);
        if (items.size() == 0) {
            customResponseEntity = CustomResponseEntity.builder()
                    .status(200)
                    .message("팔로우하는 친구의 소식을 기다려주세요!")
                    .build();
            return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
        }

        extractPosts(items, userId);
        customResponseEntity = CustomResponseEntity.builder()
                .status(200)
                .message("포스트를 불러옵니다.")
                .data(arr).build();

        return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
    }

    private ArrayList<PostInfoResponse> extractPosts(List<Object[]> posts, String userId) {
        ArrayList<PostInfoResponse> arr = new ArrayList<>();

        for (Object[] post : posts) {
            Optional<User> curWriter = userRepository.findUserByNickname((String)post[1]);

            Optional<PostLike> isLiked = postLikeRepository.findPostLikeByPostIdAndUserId((int) post[0], userId);
            String dateDiff = TimestampToDateString.getPassedTime((Timestamp) post[4]);
            List<Object[]> comments = commentRepository.findCommentsLimit2((int) post[0]);
            ArrayList<CommentResponse> commArr = new ArrayList<>();
            int len = comments.size();


            List<String> images = postImageRepository.findPostsByPostIdOrderByOrder((int)post[0]);


            for (int i = len - 1; i >= 0; i--) {
                Object[] item = comments.get(i);
                String diff = TimestampToDateString.getPassedTime((Timestamp) item[3]);
                commArr.add(
                        CommentResponse.builder()
                                .id((int) item[0])
                                .writerId((String) item[1])
                                .message((String) item[2])
                                .createDate(diff)
                                .build()
                );
            }
            if (isLiked.isPresent()) {
                arr.add(PostInfoResponse.builder()
                        .postId((int) post[0])
                        .userNickname((String) post[1])
                        .userFollowerCnt(post[2])
                        .content((String) post[3])
                        .createDate(dateDiff)
                        .likeCnt(post[5])
                        .commentCnt(post[6])
                        .placeId(post[7])
                        .placeListId(post[8])
                        .images(images)
                        .comments(commArr)
                        .liked(true)
                        .profileImage(curWriter.get().getProfileImage())
                        .build());
            } else {
                arr.add(PostInfoResponse.builder()
                        .postId((int) post[0])
                        .userNickname((String) post[1])
                        .userFollowerCnt(post[2])
                        .content((String) post[3])
                        .createDate(dateDiff)
                        .likeCnt(post[5])
                        .commentCnt(post[6])
                        .placeId(post[7])
                        .placeListId(post[8])
                        .images(images)
                        .comments(commArr)
                        .profileImage(curWriter.get().getProfileImage())
                        .liked(false)
                        .build());
            }
        }
        return arr;
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> likePost(PostLikeRequest postLikeRequest, HttpServletRequest httpRequest) {
        CustomResponseEntity customResponse;
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            customResponse = CustomResponseEntity.builder()
                    .status(401)
                    .message("로그인 후 이용해주세요.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.UNAUTHORIZED);
        }
        Optional<Post> post = postRepository.findPostById(postLikeRequest.getPostId());
        if (!post.isPresent()) {
            customResponse = CustomResponseEntity.builder()
                    .status(400)
                    .message("존재하지 않는 게시글입니다.")
                    .build();

            return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
        }
        Optional<PostLike> postLike = postLikeRepository.findPostLikeByPostIdAndUserId(postLikeRequest.getPostId(), userId);
        if (!postLike.isPresent()) { // 없으면 좋아요 추가
            PostLike newLike = PostLike.builder()
                    .postId(postLikeRequest.getPostId())
                    .userId(userId)
                    .build();

            postLikeRepository.save(newLike);
            customResponse = CustomResponseEntity.builder()
                    .status(200)
                    .message("좋아요가 반영되었습니다.")
                    .build();
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
        }
        // 있으면 좋아요 취소하기.
        PostLike cancelLike = PostLike.builder()
                .postId(postLikeRequest.getPostId())
                .userId(userId)
                .build();
        postLikeRepository.delete(cancelLike);

        customResponse = CustomResponseEntity.builder()
                .status(200)
                .message("좋아요가 취소되었습니다.")
                .build();
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

    public ResponseEntity<CustomResponseEntity> findAll(HttpServletRequest httpRequest) {
        CustomResponseEntity customResponseEntity;
        String userId = TokenToId.check(httpRequest);

        List<Object[]> items = postRepository.findAllPost();
        ArrayList<PostInfoResponse> arr = extractPosts(items, userId);
        if (items.size() == 0) {
            customResponseEntity = CustomResponseEntity.builder()
                    .status(200)
                    .message("작성된 글이 없습니다.")
                    .build();
            return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
        }

        extractPosts(items, userId);
        customResponseEntity = CustomResponseEntity.builder()
                .status(200)
                .message("전체 포스트를 불러옵니다.")
                .data(arr).build();

        return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
    }
}