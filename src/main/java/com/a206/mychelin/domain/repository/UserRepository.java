package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserById(String id);

    Optional<User> findUserByNickname(String nickname);

    void deleteUsersById(String id);
}