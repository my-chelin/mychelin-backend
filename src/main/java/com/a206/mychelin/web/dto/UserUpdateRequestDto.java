package com.a206.mychelin.web.dto;

import com.a206.mychelin.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {
    private String nickname;
    private String phone_number;
    private String bio;
    private String profile_image;

    @Builder
    public UserUpdateRequestDto(String nickname, String phone_number, String bio, String profile_image) {
        this.nickname = nickname;
        this.phone_number = phone_number;
        this.bio = bio;
        this.profile_image = profile_image;
    }

}