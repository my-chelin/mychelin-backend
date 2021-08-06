package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Comment;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.CommentRepository;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.util.TimestampToDateString;
import com.a206.mychelin.util.TokenToId;
import com.a206.mychelin.web.dto.CommentInsertRequest;
import com.a206.mychelin.web.dto.CommentResponse;
import com.a206.mychelin.web.dto.CustomResponseEntity;
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
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private User getUser(HttpServletRequest request) {
        String id = TokenToId.check(request);
        Optional<User> user = userRepository.findUserById(id);
        if (!user.isPresent()) {
            return null;
        }
        return user.get();
    }

    //특정 게시글의 모든 댓글 보기
    public ResponseEntity<CustomResponseEntity> findCommentsByPostId(@PathVariable int postId) {
        //포스트 댓글 확인
        List<Object[]> comments = commentRepository.findCommentsByPostId(postId);
        ArrayList<CommentResponse> arr = new ArrayList<>();
        for (Object[] item : comments) {
            String diff = TimestampToDateString.getPassedTime((Timestamp) item[3]);
            arr.add(
                    CommentResponse.builder()
                            .id((int) item[0])
                            .writerId((String) item[1])
                            .message((String) item[2])
                            .createDate(diff)
                            .build()
            );
        }

        CustomResponseEntity customResponse
                = CustomResponseEntity.builder()
                .status(200)
                .message("댓글을 불러왔습니다.")
                .data(arr)
                .build();
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

    // 특정 게시글에 댓글 달기
    @Transactional
    public ResponseEntity<CustomResponseEntity> addComment(@PathVariable int postId, @RequestBody CommentInsertRequest commentRequest, HttpServletRequest request) {
        CustomResponseEntity customResponseEntity;
        User user = getUser(request);
        if (user == null) {
            customResponseEntity = CustomResponseEntity.builder()
                    .status(400)
                    .message("로그인 후 이용해주세요")
                    .build();
            return new ResponseEntity<>(customResponseEntity, HttpStatus.BAD_REQUEST);
        }
        String userId = user.getId();
        commentRequest.setWriterId(userId);
        commentRequest.setPostId(postId);
        Comment newComment = commentRequest.toEntity();
        commentRepository.save(newComment);
        customResponseEntity = CustomResponseEntity.builder()
                .status(200)
                .message("댓글을 달았습니다.")
                .data(newComment)
                .build();
        return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomResponseEntity> deleteComment(@PathVariable int commentId, HttpServletRequest request) {
        CustomResponseEntity customResponseEntity;
        User user = getUser(request);
        if (user == null) {
            customResponseEntity = CustomResponseEntity.builder()
                    .status(400)
                    .message("로그인 후 이용해주세요")
                    .build();
            return new ResponseEntity<>(customResponseEntity, HttpStatus.BAD_REQUEST);
        }
        String userId = user.getId();

        Optional<Comment> comment = commentRepository.findCommentByCommentId(commentId);
        if (!comment.isPresent()) {
            customResponseEntity = CustomResponseEntity.builder()
                    .status(400)
                    .message("작업을 수행할 수 없습니다.")
                    .build();
            return new ResponseEntity<>(customResponseEntity, HttpStatus.BAD_REQUEST);
        }
        if (comment.get().getWriterId().equals(userId)) {
            commentRepository.deleteCommentByCommentId(commentId);
            customResponseEntity = CustomResponseEntity.builder()
                    .status(200)
                    .message("댓글을 삭제했습니다.")
                    .build();
            return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
        }
        customResponseEntity = CustomResponseEntity.builder()
                .status(400)
                .message("댓글 삭제 권한이 없습니다.")
                .build();
        return new ResponseEntity<>(customResponseEntity, HttpStatus.UNAUTHORIZED);
    }
}