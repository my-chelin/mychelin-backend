package com.a206.mychelin.config;

import java.util.ArrayList;
import java.util.List;

public class AuthorizationCheck {
    public static AuthorizationCheck object;
    private List<String> tokenRequiredPathList;

    // 토큰이 필요한 url 경로를 싱글톤 패턴으로 관리
    private AuthorizationCheck() {
        tokenRequiredPathList = new ArrayList<>();
        tokenRequiredPathList.add("/user/profile");
        tokenRequiredPathList.add("/user/changepwd");
        tokenRequiredPathList.add("/user/delete");
        tokenRequiredPathList.add("/placelist");
        tokenRequiredPathList.add("/placelist/listitems/items");
    }

    public static AuthorizationCheck getObject() {
        if (object == null) {
            object = new AuthorizationCheck();
        }
        return object;
    }

    public List<String> getPathList() {
        return tokenRequiredPathList;
    }
}