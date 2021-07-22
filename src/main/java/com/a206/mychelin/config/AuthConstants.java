package com.a206.mychelin.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConstants {
    // 키
    public static final String AUTH_HEADER = "Authorization";

    // 토큰 식별자
    public static final String TOKEN_TYPE = "BEARER";
}