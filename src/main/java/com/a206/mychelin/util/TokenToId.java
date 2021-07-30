package com.a206.mychelin.util;

import com.a206.mychelin.config.AuthConstants;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

public class TokenToId {
    public static String check(HttpServletRequest httpRequest){
        String header = httpRequest.getHeader(AuthConstants.AUTH_HEADER);
        if (header == null) {
            return null;
        }
        String token = TokenUtils.getTokenFromHeader(header);
        Claims claims = TokenUtils.getClaimsFormToken(token);
        String userId = (String) claims.get("id");
        return userId;
    }
}