package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSaveRequest {
    private String id;
    private String nickname;
    private String password;
    private String phone_number;

    @Builder
    public UserSaveRequest(String id, String nickname, String password, String phone_number) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.phone_number = phone_number;
    }

    public User toEntity(){
        return User.builder()
                .id(id)
                .nickname(nickname)
                .phoneNumber(phone_number)
                .password(password)
                .build();
    }
}
