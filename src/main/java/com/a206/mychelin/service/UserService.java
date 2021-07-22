package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.web.dto.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User update(String id, UserUpdateRequestDto requestDto){
        User user = userRepository.findUserById(id)
                                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다. id = " + id));
        user.update(requestDto.getNickname(), requestDto.getBio(), requestDto.getPhone_number(), requestDto.getProfile_image());
        return user;
    }

}
