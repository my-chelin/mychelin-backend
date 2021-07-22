package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.web.dto.UserResponse;
import com.a206.mychelin.web.dto.UserSaveRequest;
import com.a206.mychelin.web.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User update(String id, UserUpdateRequest requestDto){
        User user = userRepository.findUserById(id)
                                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다. id = " + id));
        user.update(requestDto.getNickname(), requestDto.getBio(), requestDto.getPhone_number(), requestDto.getProfile_image());
        return user;
    }

    public UserResponse findUserById(String id) {
        Optional<User> user = userRepository.findUserById(id);
        if(!user.isPresent()){
            throw new IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다. 입력하신 이메일 : " + id);
        }
        return new UserResponse(user.get());
    }

    @Transactional
    public String save (UserSaveRequest requestDto){
        return userRepository.save(requestDto.toEntity()).getId();
    }

}
