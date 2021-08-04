package com.a206.mychelin.config;

import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.util.TokenUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    // 로그인 성공하면 해당 유저에 대한 토큰 생성, 헤더에 붙이기
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        final User user = ((MyUserDetails) authentication.getPrincipal()).getUser();
        final String token = TokenUtils.generateJwtToken(user);
        response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().printf("{\"nickname\":\"%s\"}", user.getNickname());
    }
}