package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Comment;
import com.a206.mychelin.domain.entity.NoticeComment;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.CommentRepository;
import com.a206.mychelin.domain.repository.NoticeCommentRepository;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.util.RealTimeDataBase;
import com.a206.mychelin.util.TimestampToDateString;
import com.a206.mychelin.util.TokenToId;
import com.a206.mychelin.web.dto.CommentDto;
import com.a206.mychelin.web.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final RealTimeDataBase realTimeDataBase;
    private final NoticeCommentRepository noticeCommentRepository;

    private User getUser(HttpServletRequest request) {
        String id = TokenToId.check(request);
        System.out.println(id);
        Optional<User> user = userRepository.findUserById(id);
        if (!user.isPresent()) {
            return null;
        }
        return user.get();
    }

    //특정 게시글의 모든 댓글 보기
    public ResponseEntity<Response> findCommentsByPostId(@PathVariable int postId) {
        //포스트 댓글 확인
        List<Object[]> comments = commentRepository.findCommentsByPostId(postId);
        ArrayList<CommentDto.CommentResponse> arr = new ArrayList<>();
        for (Object[] item : comments) {
            String diff = TimestampToDateString.getPassedTime((Timestamp) item[3]);
            arr.add(
                    CommentDto.CommentResponse.builder()
                            .id((int) item[0])
                            .writerId((String) item[1])
                            .message((String) item[2])
                            .createDate(diff)
                            .build()
            );
        }
        return Response.newResult(HttpStatus.OK, "댓글을 불러왔습니다.", arr);
    }

    // 특정 게시글에 댓글 달기
    @Transactional
    public ResponseEntity<Response> addComment(@PathVariable int postId, @RequestBody CommentDto.CommentInsertRequest commentRequest, HttpServletRequest request) {
        User user = getUser(request);
        if (user == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }
        String userId = user.getId();
        commentRequest.setWriterId(userId);
        commentRequest.setPostId(postId);
        Comment newComment = commentRequest.toEntity();
        commentRepository.save(newComment);

        commentRepository.findAll();

        NoticeComment noticeComment = NoticeComment.builder()
                .commentId(newComment.getCommentId())
                .build();

        noticeCommentRepository.save(noticeComment);
        // post 쓴 사람 이름으로 저장 필요

        String nickName = noticeCommentRepository.getPostWriterNicknameByCommentId(newComment.getCommentId());

        realTimeDataBase.setNotice(nickName);


        return Response.newResult(HttpStatus.OK, "댓글을 달았습니다.", newComment);

    }

    @Transactional
    public ResponseEntity<Response> deleteComment(@PathVariable int commentId, HttpServletRequest request) {
        User user = getUser(request);
        if (user == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }
        String userId = user.getId();
        Optional<Comment> comment = commentRepository.findCommentByCommentId(commentId);
        if (!comment.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "작업을 수행할 수 없습니다.", null);
        }

        if (comment.get().getWriterId().equals(userId)) {
            commentRepository.deleteCommentByCommentId(comment.get().getCommentId());
            return Response.newResult(HttpStatus.OK, "댓글을 삭제했습니다.", null);
        }
        return Response.newResult(HttpStatus.UNAUTHORIZED, "댓글 삭제 권한이 없습니다.", null);
    }
}