package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> getUserById(String id);

    Optional<User> findUserById(String id);

    // 로그인
    Optional<User> findUserByIdAndPassword(String id, String password);

    Optional<User> findUserByPhoneNumber(String phone_number);
}