package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.*;
import com.a206.mychelin.domain.repository.*;
import com.a206.mychelin.util.RealTimeDataBase;
import com.a206.mychelin.util.TokenToId;
import com.a206.mychelin.util.TimestampToDateString;
import com.a206.mychelin.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    private final NoticePostLikeRepository noticePostLikeRepository;
    private final RealTimeDataBase realTimeDataBase;

    @Transactional
    public ResponseEntity<Response> addPost(@RequestBody PostUploadRequest postRequest, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        postRequest.checkPlaceIdOrPlaceListId();
        Post newPost = Post.builder()
                .userId(userId)
                .content(postRequest.getContent())
                .placeId(postRequest.getPlaceId())
                .placeListId(postRequest.getPlaceListId())
                .build();
        postRepository.save(newPost);

        for (String image : postRequest.getImages()) {
            // 이미지 추가
            PostImage insertPostImage = PostImage.builder()
                    .postId(newPost.getId())
                    .image(image)
                    .build();
            postImageRepository.save(insertPostImage);
        }
        return Response.newResult(HttpStatus.OK, "게시글이 업로드되었습니다.", null);
    }

    @Transactional
    public ResponseEntity<Response> update(@PathVariable int id, @RequestBody PostUpdateRequest postUpdateRequest, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        Optional<Post> tempPost = postRepository.findPostById(id);

        if (!tempPost.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "게시글이 없습니다.", null);
        }
        Post post = tempPost.get();
        if (userId.equals(post.getUserId())) {
            // image Repository 해당 아이디 이미지 데이터 다 지우고
            List<PostImage> oldImages = postImageRepository.findPostImagesByPostId(id);
            for (PostImage item : oldImages) {
                System.out.println(item.getId() + " " + item.getImage());
                postImageRepository.delete(item);
            }
            // 받아온걸로 다시 upload 로직
            post.update(postUpdateRequest.getContent());
            for (String image : postUpdateRequest.getImages()) {
                // 이미지 추가
                PostImage insertPostImage = PostImage.builder()
                        .postId(id)
                        .image(image)
                        .build();
                postImageRepository.save(insertPostImage);
            }

            return Response.newResult(HttpStatus.OK, post.getId() + "번 게시글이 수정되었습니다.", null);
        }
        return Response.newResult(HttpStatus.UNAUTHORIZED, "수정 권한이 없습니다.", null);
    }

    public ResponseEntity<Response> getPostByPostId(@PathVariable int postId, HttpServletRequest httpRequest) {
        List<Object[]> entity = postRepository.findPostInfoByPostId(postId);
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 사용가능합니다.", null);
        }
        if (entity.size() > 0) {
            PostInfoResponse postInfo = extractPosts(entity, userId).get(0);
            return Response.newResult(HttpStatus.OK, postId + "번 게시글을 불러왔습니다.", postInfo);
        }
        return Response.newResult(HttpStatus.BAD_REQUEST, "게시글을 불러오지 못했습니다.", null);
    }

    @Transactional
    public ResponseEntity<Response> delete(@PathVariable int id, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 사용가능합니다.", null);
        }
        Optional<Post> entity = postRepository.findPostById(id);
        if (!entity.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "게시글을 불러오지 못했습니다.", null);
        }
        Post post = entity.get();
        if (userId.equals(post.getUserId())) {
            postRepository.deletePostById(id);
            return Response.newResult(HttpStatus.OK, id + "번 게시글을 삭제했습니다.", null);
        }
        return Response.newResult(HttpStatus.UNAUTHORIZED, "삭제 권한이 없습니다.", null);
    }

    public ResponseEntity<Response> findPostsByUserNickname(@PathVariable String userNickname, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 사용가능합니다.", null);
        }
        List<Object[]> posts = postRepository.findPostsByUserNicknameOrderByCreateDateDesc(userNickname);
        if (posts.size() == 0) {
            return Response.newResult(HttpStatus.OK, "아직 작성한 글이 없습니다", null);
        }
        ArrayList<PostInfoResponse> arr = extractPosts(posts, userId);
        return Response.newResult(HttpStatus.OK, userNickname + "의 게시글을 불러왔습니다.", arr);
    }

    public ResponseEntity<Response> findPostsByFollowingUsersOrderByCreateDateDesc(HttpServletRequest httpRequest, int page, int pageSize) {
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 사용가능합니다.", null);
        }

        long totalPageItemCnt = postRepository.countPostsByFollowingUsers(userId);
        System.out.println(totalPageItemCnt);
        HashMap<String, Object> linkedHashmap = new LinkedHashMap<>();
        linkedHashmap.put("totalPageItemCnt", totalPageItemCnt);
        linkedHashmap.put("totalPage", ((totalPageItemCnt - 1) / pageSize) + 1);
        linkedHashmap.put("nowPage", page);
        linkedHashmap.put("nowPageSize", pageSize);

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        List<Object[]> items = postRepository.findPostsByFollowingUsersOrderByCreateDateDesc(userId, pageRequest);
        ArrayList<PostInfoResponse> arr = extractPosts(items, userId);
        linkedHashmap.put("posts", arr);
        if (items.size() == 0) {
            return Response.newResult(HttpStatus.OK, "팔로우 하는 친구의 소식을 기다려주세요!", null);
        }
        extractPosts(items, userId);
        return Response.newResult(HttpStatus.OK, "팔로우하는 유저의 소식을 불러옵니다.", linkedHashmap);
    }

    private ArrayList<PostInfoResponse> extractPosts(List<Object[]> posts, String userId) {
        ArrayList<PostInfoResponse> arr = new ArrayList<>();

        for (Object[] post : posts) {
            Optional<User> curWriter = userRepository.findUserByNickname((String) post[1]);
            Optional<PostLike> isLiked = postLikeRepository.findPostLikeByPostIdAndUserId((int) post[0], userId);
            String dateDiff = TimestampToDateString.getPassedTime((Timestamp) post[4]);
            List<Object[]> comments = commentRepository.findCommentsLimit2((int) post[0]);
            ArrayList<CommentResponse> commArr = new ArrayList<>();
            int len = comments.size();

            List<String> images = postImageRepository.findPostsByPostIdOrderById((int) post[0]);

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
    public ResponseEntity<Response> likePost(PostLikeRequest postLikeRequest, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 사용가능합니다.", null);
        }
        Optional<Post> post = postRepository.findPostById(postLikeRequest.getPostId());
        if (!post.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글입니다.", null);
        }
        Optional<PostLike> postLike = postLikeRepository.findPostLikeByPostIdAndUserId(postLikeRequest.getPostId(), userId);
        if (!postLike.isPresent()) { // 없으면 좋아요 추가
            PostLike newLike = PostLike.builder()
                    .postId(postLikeRequest.getPostId())
                    .userId(userId)
                    .build();
            postLikeRepository.save(newLike);

            // 좋아요 누르면 해당 알림 추가
            postLikeRepository.findAll();

            NoticePostLike noticePostLike = NoticePostLike.builder()
                    .postId(newLike.getPostId())
                    .userId(newLike.getUserId())
                    .build();

            noticePostLikeRepository.save(noticePostLike);

            User postUser = userRepository.getById(post.get().getUserId());
            System.out.println(postUser);
            realTimeDataBase.setNotice(postUser.getNickname());


            return Response.newResult(HttpStatus.OK, "좋아요가 반영되었습니다.", null);
        }
        // 있으면 좋아요 취소하기.
        PostLike cancelLike = PostLike.builder()
                .postId(postLikeRequest.getPostId())
                .userId(userId)
                .build();
        postLikeRepository.delete(cancelLike);

        // 있으면 좋아요 알림 삭제
        Optional<NoticePostLike> noticePostLike = noticePostLikeRepository.findByPostIdAndUserId(cancelLike.getPostId(), cancelLike.getUserId());

        if (noticePostLike.isPresent()) {
            noticePostLikeRepository.delete(noticePostLike.get());
        }
        return Response.newResult(HttpStatus.OK, "좋아요가 취소되었습니다.", null);
    }

    public ResponseEntity<Response> findAllPosts(HttpServletRequest httpRequest, int page, int pageSize) {
        String userId = TokenToId.check(httpRequest);
        long totalPageItemCnt = postRepository.count();

        HashMap<String, Object> linkedHashmap = new LinkedHashMap<>();
        linkedHashmap.put("totalPageItemCnt", totalPageItemCnt);
        linkedHashmap.put("totalPage", ((totalPageItemCnt - 1) / pageSize) + 1);
        linkedHashmap.put("nowPage", page);
        linkedHashmap.put("nowPageSize", pageSize);

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        List<Object[]> items = postRepository.findAllPost(pageRequest);
        ArrayList<PostInfoResponse> arr = extractPosts(items, userId);
        linkedHashmap.put("posts", arr);
        if (items.size() == 0) {
            return Response.newResult(HttpStatus.OK, "작성된 글이 없습니다.", null);
        }
        extractPosts(items, userId);
        return Response.newResult(HttpStatus.OK, "전체 포스트를 불러옵니다.", linkedHashmap);
    }
}