package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity getUserEntityById(String id);

    Optional<UserEntity> findUserEntityByIdAndPassword(String id, String password);

    Optional<UserEntity> findUserEntityByPhoneNumber(String phone_number);
}
