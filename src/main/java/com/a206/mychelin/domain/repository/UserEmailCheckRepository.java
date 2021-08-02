package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.UserEmailCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEmailCheckRepository extends JpaRepository<UserEmailCheck,Integer> {

    Optional<UserEmailCheck> findByUserId(String userId);

}
