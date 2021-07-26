package com.a206.mychelin.service;

import com.a206.mychelin.config.AuthConstants;
import com.a206.mychelin.domain.repository.PostRepository;
import com.a206.mychelin.util.TokenUtils;
import com.a206.mychelin.web.dto.PostUploadRequest;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public ResponseEntity<String> save(@RequestBody PostUploadRequest postUploadRequest, HttpServletRequest request) {
        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        if (header == null) {
            return new ResponseEntity<String>("로그인하지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED);
        }
        String token = TokenUtils.getTokenFromHeader(header);
        if (token.isEmpty()) {
            return new ResponseEntity<String>("EMPTY TOKEN", HttpStatus.UNAUTHORIZED);
        }
        if (!TokenUtils.isValidToken(token)) { // 유효하지 않은 토큰
            return new ResponseEntity<String>("토큰이 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String userId = (String) claims.get("id");
        postUploadRequest.setUserId(userId);

        int id = postRepository.save(postUploadRequest.toEntity()).getId();
        return new ResponseEntity<String>("게시글이 업로드되었습니다", HttpStatus.OK);
    }
}