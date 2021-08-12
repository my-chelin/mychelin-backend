package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.NoticeCommentRepository;
import com.a206.mychelin.domain.repository.NoticeFollowRepository;
import com.a206.mychelin.domain.repository.NoticePostLikeRepository;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.web.dto.NoticeDto;
import com.a206.mychelin.web.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class NoticeService {

    final private NoticePostLikeRepository noticePostLikeRepository;
    final private NoticeFollowRepository noticeFollowRepository;
    final private NoticeCommentRepository noticeCommentRepository;
    final private UserRepository userRepository;


    public ResponseEntity<Response> getNotice(String nickname) {

        Optional<User> user = userRepository.findUserByNickname(nickname);

        HttpStatus status=HttpStatus.BAD_REQUEST;
        String message=nickname+"에 맞는 유저를 찾을 수 없습니다.";
        Object data=null;

        // 유저가 존재하면.
        if(user.isPresent()){
            status=HttpStatus.OK;
            message="알림 전송";

            List<Object[]> items = noticePostLikeRepository.getPostLikeByUserId(user.get().getId());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

            TreeMap<Long,Object> map = new TreeMap<>();

            for (Object[] item : items) {
                String format = formatter.format(item[7]);

                NoticeDto.NoticePostLike noticeComment = NoticeDto.NoticePostLike.builder()
                        .type("POSTLIKE")
                        .id((int)item[0])
                        .postId((int)item[1])
                        .postContent((String)item[2])
                        .placeId((int)item[3])
                        .placeListId((int)item[4])
                        .postLikeUserId((String) item[5])
                        .isRead((boolean)item[6])
                        .addTime(format)
                        .build();

                map.put((Long)item[8],noticeComment);
            }

            data = map;



        }

        return Response.newResult(status,message,data);

    }
}
