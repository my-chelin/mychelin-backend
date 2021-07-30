package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserById(String id);

//    @Query(value = "select id from user where nickname = :nickname", nativeQuery = true)
    String findUserIdByNickname(@Param("nickname") String nickname);

    Optional<User> findUserByNickname(String nickname);

    void deleteUsersById(String id);
}