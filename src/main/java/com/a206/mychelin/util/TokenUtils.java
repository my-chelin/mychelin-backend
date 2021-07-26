package com.a206.mychelin.util;

import com.a206.mychelin.domain.entity.User;
import io.jsonwebtoken.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtils {
    private static final String secretKey = "ssafy_seoul_a206_jkj_oi";

    // 토큰 최종 생성
    public static String generateJwtToken(User user) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(user.getId())
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .setExpiration(createExpireDateForOneYear())
                .signWith(SignatureAlgorithm.HS256, createSigningKey());
        return builder.compact();
    }

    // 토큰이 유효한지 확인
    public static boolean isValidToken(String token) {
        try {
            Claims claims = getClaimsFormToken(token);
            log.info("expireTime : " + claims.getExpiration());
            log.info("id : " + claims.get("id"));
            return true;
        } catch (ExpiredJwtException exception) {
            log.error("Token Expired");
            return false;
        } catch (JwtException exception) {
            log.error("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is null");
            return false;
        }
    }

    // http 헤더에서 토큰 진짜 내용 가져오기
    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    // 만료 날짜 생성
    private static Date createExpireDateForOneYear() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 30);
        return c.getTime();
    }

    // jwt 기본 헤더 만들기
    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    // jwt payload 생성
    private static Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        return claims;
    }

    // jwt signature 생성
    private static Key createSigningKey() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    // jwt에서 claims(payload) 부분 가져오기
    public static Claims getClaimsFormToken(String token) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token).getBody();
    }

    // Claims에서 user id 가져오기
    public static String getUserIdFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return (String) claims.get("id");
    }
}