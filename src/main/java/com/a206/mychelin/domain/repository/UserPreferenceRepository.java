package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Integer> {
    Optional<UserPreference> findUserPreferenceByUserId(String userId);

}
