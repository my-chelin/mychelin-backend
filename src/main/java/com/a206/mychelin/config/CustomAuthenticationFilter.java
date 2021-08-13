package com.a206.mychelin.config;

import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.exception.InputNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public CustomAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    // userid랑 pwd 없을 떄 에러 발생하는지 테스트 => catch에서 null을 잡을까? =>  null 처리 필요
    // body 값이 없는 경우? => catch
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken authRequst;
        try {
            // request에서 값 매핑?
            final User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            if (user.getId() == null || user.getPassword() == null) {
                throw new InputNotFoundException();
            }

            authRequst = new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword());
        } catch (IOException e) {
            throw new InputNotFoundException();
        }

        // authRequst의 setDetails를 통해 detail에 request를 주입
        setDetails(request, authRequst);

        // 알맞는 Provider를 찾아 authenticate로직을 실행하고 성공과 실패 결과에 따른 추가적인 작업은Handler 등록을 통해 작업이 가능하다.
        return this.getAuthenticationManager().authenticate(authRequst);
    }
}