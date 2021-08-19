package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.NoticeComment;
import com.a206.mychelin.domain.entity.NoticeFollow;
import com.a206.mychelin.domain.entity.NoticePostLike;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.NoticeCommentRepository;
import com.a206.mychelin.domain.repository.NoticeFollowRepository;
import com.a206.mychelin.domain.repository.NoticePostLikeRepository;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.util.TokenToId;
import com.a206.mychelin.web.dto.NoticeDto;
import com.a206.mychelin.web.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class NoticeService {
    final private NoticePostLikeRepository noticePostLikeRepository;
    final private NoticeFollowRepository noticeFollowRepository;
    final private NoticeCommentRepository noticeCommentRepository;
    final private UserRepository userRepository;

    public ResponseEntity<Response> getNotice(HttpServletRequest httpServletRequest) {
        String userId = TokenToId.check(httpServletRequest);
        Optional<User> user = userRepository.findUserById(userId);
        if (!user.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.", null);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        TreeMap<Long, Object> map = new TreeMap<>(Collections.reverseOrder());

        getLike(userId, formatter, map);
        getPost(userId, formatter, map);
        getFollow(userId, formatter, map);

        List<Object> result = new ArrayList<>();

        // 최종 넣기
        for (Object item : map.values()) {
            result.add(item);
        }
        return Response.newResult(HttpStatus.OK, "알림 전송", result);
    }

    private void getLike(String userId, SimpleDateFormat formatter, TreeMap<Long, Object> map) {
        List<Object[]> items = noticePostLikeRepository.getPostLikeByUserId(userId);
        // 좋아요 항목 가져오기기
        for (Object[] item : items) {
            String format = formatter.format(item[8]);

            NoticeDto.NoticePostLike noticeComment = NoticeDto.NoticePostLike.builder()
                    .type("POSTLIKE")
                    .id((int) item[0])
                    .postId((int) item[1])
                    .postContent((String) item[2])
                    .placeId((Integer) item[3])
                    .placeListId((Integer) item[4])
                    .postLikeUserId((String) item[5])
                    .postLikeUserNickname((String) item[6])
                    .isRead((boolean) item[7])
                    .addTime(format)
                    .build();

            map.put(((Date) item[8]).getTime(), noticeComment);
        }
    }

    private void getPost(String userId, SimpleDateFormat formatter, TreeMap<Long, Object> map) {
        List<Object[]> items = noticeCommentRepository.getCommentByUserId(userId);
        // 댓글 항목 가져오기기
        for (Object[] item : items) {
            String format = formatter.format(item[8]);

            NoticeDto.NoticeComment noticeComment = NoticeDto.NoticeComment.builder()
                    .type("COMMENT")
                    .id((int) item[0])
                    .commentId((int) item[1])
                    .commentMessage((String) item[2])
                    .writerId((String) item[3])
                    .writerNickname((String) item[4])
                    .postId((int) item[5])
                    .postContent((String) item[6])
                    .isRead((boolean) item[7])
                    .addTime(format)
                    .build();

            map.put(((Date) item[8]).getTime(), noticeComment);
        }
    }

    private void getFollow(String userId, SimpleDateFormat formatter, TreeMap<Long, Object> map) {
        List<Object[]> items = noticeFollowRepository.getNoticeFollowById(userId);
        // 댓글 항목 가져오기기
        for (Object[] item : items) {
            String format = formatter.format(item[4]);

            NoticeDto.NoticeFollow noticeComment = NoticeDto.NoticeFollow.builder()
                    .type("FOLLOW")
                    .id((int) item[0])
                    .userId((String) item[1])
                    .userNickname((String) item[2])
                    .isRead((boolean) item[3])
                    .addTime(format)
                    .build();

            map.put(((Date) item[4]).getTime(), noticeComment);
        }
    }

    @Transactional
    public ResponseEntity<Response> readNotice(HttpServletRequest httpServletRequest, NoticeDto.NoticeRequest noticeRequest) {
        String userId = TokenToId.check(httpServletRequest);
        Optional<User> user = userRepository.findUserById(userId);

        if (!user.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.", null);
        }
        switch (noticeRequest.getType()) {
            case "FOLLOW":
                Optional<NoticeFollow> noticeFollow = noticeFollowRepository.findById(noticeRequest.getId());
                if (noticeFollow.isPresent()) {
                    noticeFollow.get().readNotice();
                    return Response.newResult(HttpStatus.OK, "팔로우 알림을 읽었습니다.", null);
                }
                break;
            case "POSTLIKE":
                Optional<NoticePostLike> noticePostLike = noticePostLikeRepository.findById(noticeRequest.getId());
                if (noticePostLike.isPresent()) {
                    noticePostLike.get().readNotice();
                    return Response.newResult(HttpStatus.OK, "게시글 좋아요 알림을 읽었습니다.", null);
                }
                break;
            case "COMMENT":
                Optional<NoticeComment> noticeComment = noticeCommentRepository.findById(noticeRequest.getId());
                if (noticeComment.isPresent()) {
                    noticeComment.get().readNotice();
                    return Response.newResult(HttpStatus.OK, "댓글 알림을 읽었습니다.", null);
                }
                break;
        }
        return Response.newResult(HttpStatus.BAD_REQUEST, "해당 알림을 찾을 수 없습니다.", null);
    }
}