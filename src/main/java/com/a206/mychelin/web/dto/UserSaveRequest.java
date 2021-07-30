package com.a206.mychelin.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSaveRequest {
    private String id;
    private String nickname;
    private String password;
    private String phoneNumber;

    @Builder
    public UserSaveRequest(String id, String nickname, String password, String phoneNumber) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}