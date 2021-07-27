package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.web.dto.UserSaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> getUserById(String id);

    Optional<User> findUserById(String id);

    Optional<User> findUserByIdAndPassword(String id, String password);

    Optional<User> findUserByNickname(String nickname);

    String save(UserSaveRequest userSaveRequest);

    void deleteUsersById(String id);
}