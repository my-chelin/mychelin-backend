package com.a206.mychelin.service;

import com.a206.mychelin.config.AuthConstants;
import com.a206.mychelin.domain.entity.Comment;
import com.a206.mychelin.domain.repository.CommentRepository;
import com.a206.mychelin.domain.repository.PostRepository;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.util.TimestampToDateString;
import com.a206.mychelin.util.TokenToId;
import com.a206.mychelin.util.TokenUtils;
import com.a206.mychelin.web.dto.CommentInsertRequest;
import com.a206.mychelin.web.dto.CommentResponse;
import com.a206.mychelin.web.dto.CustomResponseEntity;
import io.jsonwebtoken.Claims;
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

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    //특정 게시글의 모든 댓글 보기
    public ResponseEntity findCommentsByPostId(@PathVariable int id, HttpServletRequest httpRequest) {
//        String writer_id = postRepository.findPostById(id).get().getUserId();
        /*
         * 비공개계정이라면 현재 사용자가 글쓴이 팔로우 중인지 확인하는 부분
         */

        //팔로우 상태 확인
        //followRepository.findFollowingIdBy

        //포스트 댓글 확인
        List<Object[]> comments = commentRepository.findCommentsByPostId(id);
        ArrayList<CommentResponse> arr = new ArrayList<>();
        for (Object[] item : comments) {
            String diff = TimestampToDateString.getPassedTime((Timestamp) item[3]);
            arr.add(
                    CommentResponse.builder()
                            .id((int) item[0])
                            .writerNickname((String) item[1])
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
        return new ResponseEntity(customResponse, HttpStatus.OK);
    }

    // 특정 게시글에 댓글 달기
    @Transactional
    public ResponseEntity addComment(@PathVariable int id, @RequestBody CommentInsertRequest commentRequest, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);

        commentRequest.setWriterId(userId);
        commentRequest.setPostId(id);

        String writerId = userRepository.getUserById(userId).get().getNickname();
        Comment newComment = commentRequest.toEntity();
        int comment_id = commentRepository.save(commentRequest.toEntity()).getCommentId();

        CustomResponseEntity customResponse
                = CustomResponseEntity.builder()
                .status(200)
                .message("댓글을 달았습니다.")
                .data(newComment)
                .build();
        return new ResponseEntity<CustomResponseEntity>(customResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity deleteComment(@PathVariable int commentId, HttpServletRequest httpRequest) {
        String userId = TokenToId.check(httpRequest);

        Optional<Comment> comment = commentRepository.findCommentByCommentId(commentId);
        if (comment.isPresent()) {
            if (comment.get().getWriterId().equals(userId)) {
                commentRepository.deleteCommentByCommentId(commentId);

                CustomResponseEntity customResponseEntity
                        = CustomResponseEntity.builder()
                        .status(200)
                        .message("댓글을 삭제했습니다.")
                        .build();
                return new ResponseEntity(customResponseEntity, HttpStatus.OK);
            }

            CustomResponseEntity customResponseEntity
                    = CustomResponseEntity.builder()
                    .status(400)
                    .message("댓글 삭제 권한이 없습니다.")
                    .build();
            return new ResponseEntity(customResponseEntity, HttpStatus.UNAUTHORIZED);
        }
        CustomResponseEntity customResponseEntity
                = CustomResponseEntity.builder()
                .status(400)
                .message("작업을 수행할 수 없습니다.")
                .build();
        return new ResponseEntity(customResponseEntity, HttpStatus.BAD_REQUEST);
    }
}