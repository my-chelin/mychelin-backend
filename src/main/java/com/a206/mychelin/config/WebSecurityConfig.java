package com.a206.mychelin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // csrf().disable() => csrf 공격을 막기 위한 부분 제거
        // 해당 작업 수행하지 않으면 에러 발생하기 때문에 disabled

        // authorizeRequests => 인증을 처리하는 부분을 설정할 떄 사용용
        http.csrf().disable().authorizeRequests()
                // 토큰을 활용하는 경우 모든 요청에 대해 접근이 가능하도록 한다.
                .anyRequest().permitAll()
                .and()
                // 토큰을 활용하면 세션이 필요 없으므로 세션 사용x
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // form 쓰지 않음 => JSON으로만 주세요!
                .formLogin()
                .disable()
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/user/login");
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    @Bean
    public CustomLoginSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService, bCryptPasswordEncoder());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider());
    }
}